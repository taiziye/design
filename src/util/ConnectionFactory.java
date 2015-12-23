package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {
    private static String driver;
    private static String dburl;
    private static String user;
    private static String password;
    private static final ConnectionFactory factory=new ConnectionFactory();
    //静态代码块，用于初始化类，可以为类的属性进行复赋值

    private Connection conn;
    static{
        Properties prop=new Properties();
        try {
            InputStream in=ConnectionFactory.class.getClassLoader()
                    .getResourceAsStream("dbconfig.properties");
            prop.load(in);
        } catch (Exception e) {
            System.out.println("=========配置文件读取错误=========");
        }

        driver=prop.getProperty("driver");
        dburl=prop.getProperty("dburl");
        user=prop.getProperty("user");
        password=prop.getProperty("password");
    }
    private ConnectionFactory(){

    }
    //这里采用的是单例模式，保证程序运行期间只有一个ConnectionFactory实例存在
    public static ConnectionFactory getInstance(){
        return factory;
    }
    public Connection makeConnection() {
        // TODO Auto-generated method stub
        try {
            Class.forName(driver);
            conn=DriverManager.getConnection(dburl,user,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
