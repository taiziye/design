package test;

import dao.ProductDao;
import dao.impl.ProductDaoimpl;
import util.ConnectionFactory;

import java.sql.Connection;

/**
 * Created by shengshoubo on 2015/4/16.
 */
public class search_test {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn=null;
        try {
            conn= ConnectionFactory.getInstance().makeConnection();
            String keyword="手表";
            ProductDao productDao=new ProductDaoimpl();
            String result=productDao.search(conn,keyword);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
