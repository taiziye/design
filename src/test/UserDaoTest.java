package test;

import java.sql.Connection;

import dao.UserDao;
import dao.impl.UserDaoimpl;
import entity.User;
import util.ConnectionFactory;


public class UserDaoTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Connection conn=null;
        try {
            conn=ConnectionFactory.getInstance().makeConnection();
            UserDao userDao=new UserDaoimpl();
            String email="yatoum@gmail.com";
            String name="123";
            String password="1234567";
            System.out.println(userDao.save(conn,email,name,password));
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

}
