package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.*;
import dao.ProductDao;
import dao.impl.ProductDaoimpl;
import net.sf.json.JSONObject;
import org.vertx.java.core.json.impl.Json;
import util.ConnectionFactory;

import java.io.*;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengshoubo on 2015/12/23.
 */
public class test_zhongguancun {
    private static final String DATABASE_NAME="commlib";
    private static final String COLLECTION_NAME="paramTable";
    private static final String HOST_ADDRESS="127.0.0.1";
    private static final int HOST_PORT=27017;
    private static final String DATABASE_USERNAME="taiziye";
    private static final String DATABASE_PASSWORD="123";
    private static Mongo mongo=null;
    private static DB db=null;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn;
        try {
            conn= ConnectionFactory.getInstance().makeConnection();
            ProductDao productDao=new ProductDaoimpl();
            JSONObject result=productDao.search_key(conn, "打印机");
            System.out.println(result.toString());
//            JsonArray array= (JsonArray) result.get("product");
//            FileOutputStream fos=new FileOutputStream("data.txt");
//
//            OutputStreamWriter osw=new OutputStreamWriter(fos,"UTF-8");
//                PrintWriter pw=new PrintWriter(osw,true);
//            for(JsonElement object:array){
//                System.out.println(object.toString());
//                pw.write(object.toString() + "\n");
//            }
//            pw.close();
//            osw.close();
//            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getConnection();
//        readData();
    }
    private static void readData(){
        Map<String,BasicDBObject> map=new HashMap<>();
        DBCollection thinglist=db.getCollection(COLLECTION_NAME);
        BasicDBObject queryCondition=new BasicDBObject();
        BasicDBList condition=new BasicDBList();
        condition.add(new BasicDBObject("paramTable.基本参数.曝光日期",new BasicDBObject(QueryOperators.EXISTS,true)));
        condition.add(new BasicDBObject("paramTable.基本参数.上市日期",new BasicDBObject(QueryOperators.EXISTS,true)));
        condition.add(new BasicDBObject("paramTable.基本参数.上市时间",new BasicDBObject(QueryOperators.EXISTS,true)));
        condition.add(new BasicDBObject("paramTable.基本参数.发布时间",new BasicDBObject(QueryOperators.EXISTS,true)));
        condition.add(new BasicDBObject("paramTable.主要参数.上市时间",new BasicDBObject(QueryOperators.EXISTS,true)));
        condition.add(new BasicDBObject("paramTable.主要参数.发布日期",new BasicDBObject(QueryOperators.EXISTS,true)));
        condition.add(new BasicDBObject("paramTable.基本性能.发布日期", new BasicDBObject(QueryOperators.EXISTS, true)));
        condition.add(new BasicDBObject("paramTable.主要性能.发布日期", new BasicDBObject(QueryOperators.EXISTS, true)));

        BasicDBObject key=new BasicDBObject();
        key.append("refUrl",1);
        key.append("paramTable.基本参数.曝光日期",1);
        key.append("paramTable.基本参数.上市日期",1);
        key.append("paramTable.基本参数.上市时间",1);
        key.append("paramTable.基本参数.发布时间",1);
        key.append("paramTable.主要参数.上市时间",1);
        key.append("paramTable.主要参数.发布日期",1);
        key.append("paramTable.基本性能.发布日期",1);
        key.append("paramTable.主要性能.发布日期",1);
        key.append("_id",0);

        queryCondition.put("$or",condition);

        DBCursor cursor=thinglist.find(queryCondition,null);
        System.out.println(cursor.count());
        while(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            System.out.println(object.toString());
        }
        stopMongoDBConn();
    }
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
