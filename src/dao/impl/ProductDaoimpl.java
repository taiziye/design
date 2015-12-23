package dao.impl;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.*;
import dao.ProductDao;
import entity.Product;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.CloseRes;

public class ProductDaoimpl implements ProductDao {

    CloseRes close=new CloseRes();
    public static int random=new Random().nextInt(1000);
    @Override
    public void save(Connection conn, Product product) throws SQLException {
        String sql="insert into product (name,src,title1,title2,sescription,officialSite,likes,owners,reviews,feelings) "
                + "values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1, product.getName());
        ps.setString(2, product.getSrc());
        ps.setString(3, product.getTitle1());
        ps.setString(4, product.getTitle2());
        ps.setString(5,product.getDescription());
        ps.setString(6, product.getOfficialSite());
        ps.setInt(7, product.getLikes());
        ps.setInt(8, product.getOwners());
        ps.setInt(9, product.getReviews());
        ps.setInt(10, product.getFeelings());
        ps.execute();
        close.closeRes(ps,conn);
    }

    @Override
    public void update(Connection conn, String name, Product product)
            throws SQLException {
        String sql="update product set product.src=?,product.title1=?,product.title2=?,product.description=?,product.officialSite=?,product.likes=?,product.owners=?,product.reviews=?,roduct.feelings=? where name=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1, product.getSrc());
        ps.setString(2, product.getTitle1());
        ps.setString(3, product.getTitle2());
        ps.setString(4, product.getDescription());
        ps.setString(5, product.getOfficialSite());
        ps.setInt(6, product.getLikes());
        ps.setInt(7, product.getOwners());
        ps.setInt(8, product.getReviews());
        ps.setInt(9, product.getFeelings());
        ps.setString(10, name);
        ps.execute();
        close.closeRes(ps,conn);
    }

    @Override
    public void delete(Connection conn, Product product) throws SQLException {
        String sql="delete from product where name=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1, product.getName());
        ps.execute();
        close.closeRes(ps,conn);
    }

    @Override
    public String get_index(Connection conn, int sheetnum) throws SQLException {
        ResultSet rs;
        //String sql="select * from detail";
        String sql="SELECT detail.thingname,detail.title1,mainimg.path FROM detail ,mainimg WHERE detail.thingname = mainimg.thingname LIMIT 24 OFFSET "+ ((sheetnum-1)*12);
        PreparedStatement ps=conn.prepareStatement(sql);
        rs=ps.executeQuery();
        JsonObject object=new JsonObject();
        JsonArray array=new JsonArray();
        while(rs.next()){
            JsonObject newObject=new JsonObject();
            newObject.addProperty("id", rs.getString("thingname"));
            newObject.addProperty("title", rs.getString("title1"));
            //String sql1="select * from slickimg where thingname=?";
            //PreparedStatement ps1=conn.prepareStatement(sql1);
            //ps1.setString(1,rs.getString("name"));
            //ResultSet rs1;
            //rs1=ps1.executeQuery();
            //rs1.next();
            //newObject.addProperty("img", rs1.getString("path"));
            newObject.addProperty("img",rs.getString("path"));
            array.add(newObject);
        }
        object.add("product", array);
        close.closeRes(rs,ps,conn);
        return object.toString();
    }
    public String get_catalog(Connection conn,String catalog) throws SQLException {
        ResultSet rs;
        String sql="select detail.thingname,detail.title1,mainimg.path from detail,mainimg where detail.thingname=mainimg.thingname and (detail.content1=? or detail.content2=? or detail.content3=?) limit 12";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1,catalog);
        ps.setString(2, catalog);
        ps.setString(3, catalog);
        rs=ps.executeQuery();
        JsonObject object=new JsonObject();
        JsonArray array=new JsonArray();
        while(rs.next()){
            JsonObject newObject=new JsonObject();
            newObject.addProperty("id", rs.getString("thingname"));
            newObject.addProperty("title", rs.getString("title1"));
            newObject.addProperty("img", rs.getString("path"));
            array.add(newObject);
        }
        object.add("product", array);
        close.closeRes(rs,ps,conn);
        return object.toString();
    }
    @Override
    public String get_product(Connection conn, String id) throws SQLException {
        String sql1="select * from detail where detail.thingname=?";
        PreparedStatement ps=conn.prepareStatement(sql1);
        ps.setString(1, id);
        ResultSet rs;
        rs=ps.executeQuery();
        rs.next();
        JsonObject object=new JsonObject();
        object.addProperty("id",rs.getString("thingname"));
        object.addProperty("title", rs.getString("title1"));
        object.addProperty("source", rs.getString("src"));
        String regularDescription=rs.getString("description")
                .replace("来源网站", "")
                .replace("*#*","</br></br>")
                .replace("!review", "")
                .replace("http", "<img src=\"http")
                .replace("_.webp", "")
                .replace("jpg", "jpg\"/>")
                .replace("png","png\"/>")
                .trim();
        object.addProperty("description",regularDescription);

        //这里需要规范数据库中到底需要怎样去存那些同一个产品的多个URL
        String sql2="select * from slickimg where slickimg.thingname=?";
        ps=conn.prepareStatement(sql2);
        ps.setString(1, id);
        rs=null;
        rs=ps.executeQuery();
        JsonArray array=new JsonArray();
        while(rs.next()){
            JsonObject imag_object=new JsonObject();
            imag_object.addProperty("url", rs.getString("path"));
            array.add(imag_object);
        }
        object.add("images", array);

        String sql3="select * from review where review.ProductID=?";
        ps=conn.prepareStatement(sql3);
        ps.setString(1, id);
        rs=null;
        rs=ps.executeQuery();
        JsonArray array2=new JsonArray();
        while(rs.next()){
            JsonObject comm_object=new JsonObject();
            comm_object.addProperty("content", rs.getString("Content"));
            comm_object.addProperty("time", rs.getTime("Time").toString());
            comm_object.addProperty("name", rs.getString("Name"));
            array2.add(comm_object);
        }
        object.add("comment", array2);
        //以下要单独写出推荐算法
        String sql4="select * from detail limit 4";
        ps=conn.prepareStatement(sql4);
        rs=null;
        rs=ps.executeQuery();
        JsonArray array3=new JsonArray();
        while(rs.next()){
            JsonObject guess_object=new JsonObject();
            guess_object.addProperty("id", rs.getString("thingname"));
            guess_object.addProperty("title", rs.getString("title1"));
            String newsql1="select path from slickimg where thingname=?";
            PreparedStatement newps1=conn.prepareStatement(newsql1);
            newps1.setString(1,rs.getString("thingname"));
            ResultSet newrs1=null;
            newrs1=newps1.executeQuery();
            newrs1.next();
            guess_object.addProperty("img", newrs1.getString("path"));
            array3.add(guess_object);
        }
        object.add("guess", array3);
        close.closeRes(rs,ps,conn);
        return object.toString();
    }
    @Override
    public String get_menu(Connection conn)throws SQLException{
        String sql1="select distinct content1 from content";
        PreparedStatement ps1=conn.prepareStatement(sql1);
        ResultSet rs1;
        rs1=ps1.executeQuery();
        JsonObject jsonObject=new JsonObject();
        JsonArray jsonArray1=new JsonArray();
        while(rs1.next()){
            JsonObject jsonObject1=new JsonObject();
            String content1=rs1.getString("content1");
            jsonObject1.addProperty("name", content1);
            String sql2="select distinct content2 from content where content1=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, content1);
            ResultSet rs2;
            rs2=ps2.executeQuery();
            JsonArray jsonArray2=new JsonArray();
            while(rs2.next()){
                JsonObject jsonObject2=new JsonObject();
                String content2=rs2.getString("content2");
                jsonObject2.addProperty("name", content2);
                String sql3="select distinct content3 from content where content1=? and content2=?";
                PreparedStatement ps3=conn.prepareStatement(sql3);
                ps3.setString(1,content1);
                ps3.setString(2, content2);
                ResultSet rs3;
                rs3=ps3.executeQuery();
                JsonArray jsonArray3=new JsonArray();
                while (rs3.next()){
                    JsonObject jsonObject3=new JsonObject();
                    jsonObject3.addProperty("name",rs3.getString("content3"));
                    jsonArray3.add(jsonObject3);
                }
                jsonObject2.add("submenu",jsonArray3);
                jsonArray2.add(jsonObject2);
            }
            jsonObject1.add("submenu", jsonArray2);
            jsonArray1.add(jsonObject1);
        }
        jsonObject.add("menu",jsonArray1);
        close.closeRes(ps1, conn);
        return jsonObject.toString();
    }

    public  String search(Connection conn,String keyword)throws SQLException{
        JsonObject jsonObject=new JsonObject();
        //采用正则表达式找出包含搜索关键字的titile 
        String sql="select detail.thingname,detail.title1,mainimg.path from detail,mainimg WHERE (detail.title1 like '%"+keyword+"%' or detail.title2 like '%"+keyword+"%') and detail.thingname=mainimg.thingname limit 12";
        PreparedStatement ps=conn.prepareStatement(sql);
        //ps.setString(1,keyword);
        //ps.setString(2,keyword);
        ResultSet rs=ps.executeQuery();
        JsonArray jsonArray=new JsonArray();
        while(rs.next()){
            JsonObject newjsonObject=new JsonObject();
            newjsonObject.addProperty("id",rs.getString("thingname"));
            newjsonObject.addProperty("title",rs.getString("title1"));
            newjsonObject.addProperty("img", rs.getString("path"));
            jsonArray.add(newjsonObject);
        }
        jsonObject.add("product",jsonArray);
        close.closeRes(rs,ps,conn);
        return jsonObject.toString();
    }

    //获取中关村的数据
    public JsonObject getProductData(Connection conn)throws SQLException{
        JsonObject jsonObject=new JsonObject();
        String sql="SELECT * from thinglist LEFT JOIN brandlist on thinglist.brandhref = brandlist.brandhref LEFT JOIN content on brandlist.content3href = content.content3href";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        JsonArray jsonArray=new JsonArray();
        while(rs.next()){
            JsonObject newJsonObject=new JsonObject();
            if(rs.getString("pricetime")!="-1"&&rs.getString("releasetime")!=null&&rs.getString("price")!=null){
                String price=rs.getString("price");
                if(price.contains("万")||price.matches("[0-9]+")){
                    if(price.contains("万")){
                        price=price.substring(0,price.length()-1);
                        price=(int)(Float.valueOf(price)*10000)+"";
                    }
                    newJsonObject.addProperty("thingname",rs.getString("thingname"));
                    newJsonObject.addProperty("price",price);
                    //newJsonObject.addProperty("releasetime",rs.getString("releasetime"));
                    newJsonObject.addProperty("content3",rs.getString("content3"));
                    newJsonObject.addProperty("content2",rs.getString("content2"));
                    newJsonObject.addProperty("releasetime",rs.getString("releasetime"));
                    newJsonObject.addProperty("thinghref",rs.getString("thinghref"));
                    jsonArray.add(newJsonObject);
                }
            }
        }
        jsonObject.add("product", jsonArray);
        close.closeRes(rs,ps,conn);
        return jsonObject;
    }

    @Override
    public JSONObject search_key(Connection connection,String key) throws SQLException {

        JSONObject jsonObject=new JSONObject();

        String sql1="SELECT * from things WHERE things.thingname like '%"+key+"%'";
        PreparedStatement ps1=connection.prepareStatement(sql1);
        ResultSet rs1=ps1.executeQuery();
        rs1.last();
        jsonObject.put("totalnum",rs1.getRow());

        String sql2="SELECT content2,count(*) as num from things WHERE things.thingname LIKE '%"+key+"%' GROUP BY content2";
        PreparedStatement  ps2=connection.prepareStatement(sql2);
        ResultSet rs2=ps2.executeQuery();
        JSONArray product=new JSONArray();
        while(rs2.next()){
            JSONObject object=new JSONObject();
            object.put("type", rs2.getString("content2"));
            object.put("count", rs2.getInt("num"));
            product.add(object);
        }
        jsonObject.put("product", product);
        //String sql3="SELECT count(*) as num FROM things where price >=100 AND price <= 500 ;";
        List<String> pricerange=new ArrayList<>();
        List<Integer> pricedata=new ArrayList<>();
        pricerange.add("0-100");
        pricerange.add("100-500");
        pricerange.add("500-1000");
        pricerange.add("1000-5000");
        pricerange.add("5000-10000");
        pricerange.add("10000以上");
        jsonObject.put("pricerange",pricerange);
        String[] sql3=new String[]{
                "SELECT count(*) as num FROM things where price >0 AND price <= 100 ;",
                "SELECT count(*) as num FROM things where price >100 AND price <= 500 ;",
                "SELECT count(*) as num FROM things where price >500 AND price <= 1000 ;",
                "SELECT count(*) as num FROM things where price >1000 AND price <= 5000 ;",
                "SELECT count(*) as num FROM things where price >5000 AND price <= 10000 ;",
                "SELECT count(*) as num FROM things where price >10000 ;"
        };
        for(int i=0;i<sql3.length;i++){
            PreparedStatement ps3=connection.prepareStatement(sql3[i]);
            ResultSet rs3=ps3.executeQuery();
            rs3.next();
            pricedata.add(rs3.getInt("num"));
        }
        jsonObject.put("pricedata",pricedata);

        List<String> timerange=new ArrayList<>();
        List<Integer> timecount=new ArrayList<>();
        String sql4="SELECT SUBSTRING_INDEX(releasetime,'年',1) as nian, COUNT(*) as num FROM things WHERE SUBSTRING_INDEX(releasetime,'年',1) is not null and LENGTH(SUBSTRING_INDEX(releasetime,'年',1)) =4 GROUP BY SUBSTRING_INDEX(releasetime,'年',1)";
        PreparedStatement ps4=connection.prepareStatement(sql4);
        ResultSet rs4=ps4.executeQuery();
        while(rs4.next()){
            timerange.add(rs4.getString("nian"));
            timecount.add(rs4.getInt("num"));
        }
        jsonObject.put("timerange", timerange);
        jsonObject.put("timecount",timecount);

        List<String> topproducts=new ArrayList<>();
        List<Float> stars=new ArrayList<>();
        String sql5="SELECT thingname,thinghref FROM things where thingname LIKE '%"+key+"%' ORDER BY reviews desc LIMIT 10";
        PreparedStatement ps5=connection.prepareStatement(sql5);
        ResultSet rs5=ps5.executeQuery();
        getConnection();
        while(rs5.next()){
            topproducts.add(rs5.getString("thingname"));
            stars.add(readTotalScore(rs5.getString("thinghref")));
        }
        jsonObject.put("topproducts", topproducts);
        jsonObject.put("stars", stars);

        List<String> images_small=new ArrayList<>();
        List<String> images_big=new ArrayList<>();
        String sql7="SELECT thinghref FROM things where thingname LIKE '%"+key+"%' ORDER BY reviews desc LIMIT 10";
        PreparedStatement ps7=connection.prepareStatement(sql7);
        ResultSet rs7=ps7.executeQuery();
        while(rs7.next()){
            String small_ImageUrl=readSmallImage(rs7.getString("thinghref"));
            images_small.add(small_ImageUrl);
            String big_ImageUrl=small_ImageUrl.replaceAll("_120x90","");
            images_big.add(big_ImageUrl);
        }
        jsonObject.put("images_small",images_small);
        jsonObject.put("images_big", images_big);

        List<String> topimages_small=new ArrayList<>();
        List<String> topimages_big=new ArrayList<>();
        String sql8="SELECT thinghref FROM things where thingname LIKE '%"+key+"%' ORDER BY reviews desc LIMIT 1";
        PreparedStatement ps8=connection.prepareStatement(sql8);
        ResultSet rs8=ps8.executeQuery();
        if(rs8.next()){
            topimages_small=readTopSmallImage(rs8.getString("thinghref"));
            for(int i=0;i<topimages_small.size();i++){
                topimages_big.add(topimages_small.get(i).replaceAll("_120x90",""));
            }
        }
        jsonObject.put("topimages_small",topimages_small);
        jsonObject.put("topimages_big",topimages_big);

        JSONArray comment=new JSONArray();
        String sql6="SELECT thingname,reviews FROM things where thingname LIKE '%"+key+"%' ORDER BY reviews desc LIMIT 10";
        PreparedStatement ps6=connection.prepareStatement(sql6);
        ResultSet rs6=ps6.executeQuery();
        while(rs6.next()){
            JSONObject object=new JSONObject();
            object.put("name", rs6.getString("thingname"));
            object.put("commentnum", rs6.getString("reviews"));
            comment.add(object);
        }
        jsonObject.put("comment", comment);


        List<Integer> trendprice=new ArrayList<>();
        List<String> trendrange=new ArrayList<>();
        jsonObject.put("trendrange",trendrange);
        jsonObject.put("trendprice",trendprice);
        String sql9="SELECT thinghref FROM things where thingname LIKE '%"+key+"%' ORDER BY reviews desc LIMIT 1";
        PreparedStatement ps9=connection.prepareStatement(sql9);
        ResultSet rs9=ps9.executeQuery();
        if (rs9.next()){
            trendprice=readPricedata(rs9.getString("thinghref"));
            trendrange=readPricedate(rs9.getString("thinghref"));
        }
        jsonObject.put("trendrange",trendrange);
        jsonObject.put("trendprice",trendprice);

        return jsonObject;
    }

    private static List<String> readTopSmallImage(String thingURL){
        DBCollection collection=db.getCollection("paramTable");
        BasicDBObject condition=new BasicDBObject();
        condition.put("refUrl", thingURL);
        BasicDBObject key=new BasicDBObject();
        key.append("img_url",1);
        key.append("_id", 0);
        List<String> images_small;
        DBCursor cursor=collection.find(condition, key);
        if(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            images_small= (List<String>) object.get("img_url");
            return images_small;
        }
        else{
            images_small=new ArrayList<>();
            images_small.add("http://2f.zol-img.com.cn/product/55_120x90/179/ceCVtbijeyteM.jpg");
            return  images_small;
        }
    }
    private static List<Integer> readPricedata(String thingURL){
        DBCollection collection=db.getCollection("paramTable");
        BasicDBObject condition=new BasicDBObject();
        condition.put("refUrl", thingURL);
        BasicDBObject key=new BasicDBObject();
        key.append("price",1);
        key.append("_id", 0);
        List<Integer> data;
        DBCursor cursor=collection.find(condition, key);
        if(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            data= (List<Integer>) object.get("data");
            return data;
        }else{
            data=new ArrayList<>();
            data.add(0);
            return data;
        }
    }
    private static List<String> readPricedate(String thingURL){
        DBCollection collection=db.getCollection("paramTable");
        BasicDBObject condition=new BasicDBObject();
        condition.put("refUrl", thingURL);
        BasicDBObject key=new BasicDBObject();
        key.append("price",1);
        key.append("_id", 0);
        List<String> date;
        DBCursor cursor=collection.find(condition, key);
        if(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            date= (List<String>) object.get("time");
            return date;
        }else{
            date=new ArrayList<>();
            date.add("0000-00-00");
            return date;
        }
    }

    private static String readSmallImage(String thingURL){
        DBCollection collection=db.getCollection("paramTable");
        BasicDBObject condition=new BasicDBObject();
        condition.put("refUrl", thingURL);
        BasicDBObject key=new BasicDBObject();
        key.append("img_url",1);
        key.append("_id", 0);
        DBCursor cursor=collection.find(condition, key);
        if(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            List<String> images_small= (List<String>) object.get("img_url");
            return images_small.get(0);
        }
        else return "http://2f.zol-img.com.cn/product/55_120x90/179/ceCVtbijeyteM.jpg";
    }
    private static float readTotalScore(String thingURL){
        DBCollection collection=db.getCollection("review");

        BasicDBObject query=new BasicDBObject("thingUrl",thingURL);
        BasicDBObject key=new BasicDBObject();
        key.put("totalScore", 1);
        key.put("_id", 0);
        DBCursor cursor=collection.find(query, key);
        if(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            return Float.valueOf(object.getString("totalScore"));
        }else{
            return 0f;
        }
    }

    private static final String DATABASE_NAME="commlib";
    private static final String HOST_ADDRESS="127.0.0.1";
    private static final int HOST_PORT=27017;
    private static Mongo mongo=null;
    private static DB db=null;
    private static void getConnection(){
        try {
            //Mongo(p1, p2):p1=>IP地址     p2=>端口
            mongo = new Mongo(HOST_ADDRESS, HOST_PORT);
            //根据mongodb数据库的名称获取mongodb对象
            db = mongo.getDB(DATABASE_NAME);
            //校验用户密码是否正确
//            if (!db.authenticate(DATABASE_USERNAME, DATABASE_PASSWORD.toCharArray())){
//                System.out.println("连接MongoDB数据库,校验失败！");
//            }else{
//                System.out.println("连接MongoDB数据库,校验成功！");
//            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
    private static void stopMongoDBConn(){
        if (null != mongo) {
            if (null != db) {
                // 结束Mongo数据库的事务请求
                try {
                    db.requestDone();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            try
            {
                mongo.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
            mongo = null;
            db = null;
        }
    }

}
