package test;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by shengshoubo on 2015/12/23.
 */
public class findkey {
    private static final String DATABASE_NAME="commlib";
    private static final String COLLECTION_NAME="paramTable";
    private static final String HOST_ADDRESS="127.0.0.1";
    private static final int HOST_PORT=27017;
    private static final String DATABASE_USERNAME="taiziye";
    private static final String DATABASE_PASSWORD="123";
    private static Mongo mongo=null;
    private static DB db=null;

    public static void main(String[] args){
        getConnection();
        /**
         * http://detail.zol.com.cn/3d_printer/index355001.shtml
         http://detail.zol.com.cn/large_format/index264501.shtml
         http://detail.zol.com.cn/3d_printer/index363271.shtml
         http://detail.zol.com.cn/3d_printer/index354994.shtml
         http://detail.zol.com.cn/ink_box/index285781.shtml
         http://detail.zol.com.cn/ink_box/index285783.shtml
         http://detail.zol.com.cn/ink_box/index285784.shtml
         http://detail.zol.com.cn/ink_box/index285785.shtml
         http://detail.zol.com.cn/3d_printer/index363249.shtml
         http://detail.zol.com.cn/3d_printer/index363262.shtml
         */
        search("http://detail.zol.com.cn/3d_printer/index355001.shtml");
        search("http://detail.zol.com.cn/large_format/index264501.shtml");
        search("http://detail.zol.com.cn/3d_printer/index363271.shtml");
        search("http://detail.zol.com.cn/3d_printer/index354994.shtml");
        search("http://detail.zol.com.cn/ink_box/index285781.shtml");
        search("http://detail.zol.com.cn/ink_box/index285783.shtml");
        search("http://detail.zol.com.cn/ink_box/index285784.shtml");
        search("http://detail.zol.com.cn/ink_box/index285785.shtml");
        search("http://detail.zol.com.cn/3d_printer/index363249.shtml");
        search("http://detail.zol.com.cn/3d_printer/index363262.shtml");
    }

    private static void search(String thingURL){
        DBCollection collection=db.getCollection(COLLECTION_NAME);
        BasicDBObject condition=new BasicDBObject();
        condition.put("refUrl", thingURL);
        BasicDBObject key=new BasicDBObject();
        key.append("img_url", 1);
        key.append("_id", 0);
        DBCursor cursor=collection.find(condition, key);
        if(cursor.hasNext()){
            BasicDBObject object= (BasicDBObject) cursor.next();
            List<String> images_small= (List<String>) object.get("img_url");
            System.out.println(images_small.get(0));
        }
    }
    private static void getConnection(){
        try {
            //Mongo(p1, p2):p1=>IP��ַ     p2=>�˿�
            mongo = new Mongo(HOST_ADDRESS, HOST_PORT);
            //����mongodb���ݿ�����ƻ�ȡmongodb����
            db = mongo.getDB(DATABASE_NAME);
            //У���û������Ƿ���ȷ
//            if (!db.authenticate(DATABASE_USERNAME, DATABASE_PASSWORD.toCharArray())){
//                System.out.println("����MongoDB���ݿ�,У��ʧ�ܣ�");
//            }else{
//                System.out.println("����MongoDB���ݿ�,У��ɹ���");
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
                // ����Mongo���ݿ����������
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
