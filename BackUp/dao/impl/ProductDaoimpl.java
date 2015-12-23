package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.ProductDao;
import entity.Product;
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
        ps.setString(6,product.getOfficialSite());
        ps.setInt(7, product.getLikes());
        ps.setInt(8, product.getOwners());
        ps.setInt(9, product.getReviews());
        ps.setInt(10,product.getFeelings());
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
        ps.setString(1,product.getName());
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
        ps.setString(2,catalog);
        ps.setString(3,catalog);
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
            jsonObject1.add("submenu",jsonArray2);
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
            newjsonObject.addProperty("img",rs.getString("path"));
            jsonArray.add(newjsonObject);
        }
        jsonObject.add("product",jsonArray);
        close.closeRes(rs,ps,conn);
        return jsonObject.toString();
    }
}
