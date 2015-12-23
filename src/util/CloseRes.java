package util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 * Created by shengshoubo on 2015/4/4.
 */
public class CloseRes {

    public static void closeRes(ResultSet rs,PreparedStatement ps,Connection conn){
        try {
            if(rs!=null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(conn!=null){
                conn.close();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public static void closeRes(PreparedStatement ps,Connection conn){
        try {
            if(ps!=null){
                ps.close();
            }
            if(conn!=null){
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
