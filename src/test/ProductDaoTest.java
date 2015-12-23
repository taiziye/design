package test;

import java.sql.Connection;
import java.util.Random;

import dao.ProductDao;
import dao.impl.ProductDaoimpl;
import util.ConnectionFactory;

public class ProductDaoTest {

    public static void main(String[] args) {
        //int random=new Random().nextInt(100);
        //System.out.println(random);
        Connection conn=null;
        try {
            conn=ConnectionFactory.getInstance().makeConnection();
            conn.setAutoCommit(false);

            ProductDao productdao= new ProductDaoimpl();
            String id="apple-watch";
            conn.commit();
            String object=productdao.get_product(conn,id);
            //String object=productdao.get_index(conn);
            System.out.println(object);
//            int random=new Random().nextInt(100);
//            System.out.print(random);
        } catch (Exception e) {
            try {
                conn.rollback();
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

}
