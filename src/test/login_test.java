package test;

import dao.UserDao;
import dao.impl.UserDaoimpl;
import util.ConnectionFactory;

import java.sql.Connection;

/**
 * Created by shengshoubo on 2015/4/16.
 */
public class login_test {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn;
        try {
            conn= ConnectionFactory.getInstance().makeConnection();
            String email="tom@gmail.com";
            String password="123456";
            UserDao userDao=new UserDaoimpl();
            String result=userDao.get(conn,email,password);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
