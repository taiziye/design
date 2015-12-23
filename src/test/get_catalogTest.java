package test;

import dao.ProductDao;
import dao.impl.ProductDaoimpl;
import util.ConnectionFactory;

import java.sql.Connection;

/**
 * Created by shengshoubo on 2015/4/17.
 */
public class get_catalogTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn;
        try {
            conn= ConnectionFactory.getInstance().makeConnection();
            String catalog="音乐";
            ProductDao productDao=new ProductDaoimpl();
            String result=productDao.get_catalog(conn,catalog);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
