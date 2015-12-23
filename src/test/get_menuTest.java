package test;
import java.sql.Connection;
import dao.ProductDao;
import dao.impl.ProductDaoimpl;
import util.ConnectionFactory;
/**
 * Created by shengshoubo on 2015/4/13.
 */
public class get_menuTest {
    public static void main(String[] args) {
        Connection conn=null;
        try {
            conn=ConnectionFactory.getInstance().makeConnection();
            conn.setAutoCommit(false);
            ProductDao productdao= new ProductDaoimpl();
            conn.commit();
            String object=productdao.get_menu(conn);
            System.out.println(object);
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
