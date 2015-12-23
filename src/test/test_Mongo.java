package test;

import com.google.gson.JsonObject;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengshoubo on 2015/12/19.
 */
public class test_Mongo {
    private static final String DATABASE_NAME="commlib";
    private static final String COLLECTION_NAME="image";
    private static final String HOST_ADDRESS="127.0.0.1";
    private static final int HOST_PORT=27017;
    private static final String DATABASE_USERNAME="taiziye";
    private static final String DATABASE_PASSWORD="123";
    private static Mongo mongo=null;
    private static DB db=null;

    public static void main(String[] args){
        getConnection();
        readData();
        stopMongoDBConn();
    }

    private static void readData(){
//        DBCollection collection=db.getCollection(COLLECTION_NAME);
//        DBCursor cursor=collection.find();
//        int i=0;
//        while(cursor.hasNext()&&i<10){
//            BasicDBObject object= (BasicDBObject) cursor.next();
//            if(object!=null){
//                System.out.println("url:"+object.getString("url"));
//                System.out.println("path:"+object.getString("path"));
//                System.out.println("checksum:"+object.getString("checksum"));
//            }
//            i++;
//        }
        Map<String,BasicDBObject> map=new HashMap<>();
        DBCollection thinglist=db.getCollection("thinglist");
        BasicDBObject key1=new BasicDBObject();
        key1.put("thingname",1);
        key1.put("price", 1);
        key1.put("pricetime", 1);
        key1.put("brandhref",1);
        key1.put("_id", 0);
        DBCursor cursor=thinglist.find(null, key1);
        System.out.println(cursor.count());
        int overlap=0;
        while(cursor.hasNext()){
            BasicDBObject object1= (BasicDBObject) cursor.next();
            if(map.get(object1.getString("brandhref"))!=null){
                overlap++;
            }
            map.put(object1.getString("brandhref"), object1);
            //System.out.println(map.get(object1.getString("brandhref")));
        }
        System.out.println(overlap);
        DBCollection brandlist=db.getCollection("brandlist");
        BasicDBObject key2=new BasicDBObject();
        key2.put("brandhref",1);
        key2.put("brandname",1);
        key2.put("content3href",1);
        key2.put("_id",0);
        DBCursor cursor2=brandlist.find(null,key2);
        while(cursor2.hasNext()){
            BasicDBObject object2= (BasicDBObject) cursor2.next();
//            System.out.println(map.get(object2.getString("brandhref")).toString());
            if(object2.getString("brandhref")!=null&&object2.getString("content3href")!=null&&map.get(object2.getString("brandhref"))!=null){
                BasicDBObject tmp=map.get(object2.getString("brandhref"));
                tmp.append("content3href", object2.getString("content3href"));
                tmp.append("brandname", object2.getString("brandname"));
                map.remove(object2.getString("brandhref"));
                map.put(object2.getString("content3href"), tmp);
            }
        }

        DBCollection content=db.getCollection("content");
        BasicDBObject key3=new BasicDBObject();
        key3.put("content3",1);
        key3.put("content2",1);
        key3.put("content3href",1);
        key3.put("_id",0);
        DBCursor cursor3=content.find(null,key3);
        while(cursor3.hasNext()){
            BasicDBObject object3= (BasicDBObject) cursor3.next();
            if(object3.getString("content3href")!=null&&object3.getString("content3")!=null&&object3.getString("content2")!=null&&map.get(object3.getString("content3href"))!=null){
                map.get(object3.getString("content3href")).put("content3",object3.getString("content3"));
                map.get(object3.getString("content3href")).put("content2", object3.getString("content2"));
                //System.out.println(map.get(object3.getString("content3href")).toString());
            }
        }
        System.out.println(map.keySet().size());
        for(String key:map.keySet()){
            map.get(key).remove("brandhref");
            map.get(key).remove("content3href");
            //System.out.println(map.get(key).toString());
        }
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
