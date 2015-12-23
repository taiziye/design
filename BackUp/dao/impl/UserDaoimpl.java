package dao.impl;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import org.vertx.java.core.json.impl.Json;
import util.CloseRes;
import dao.UserDao;
import entity.User;
public class UserDaoimpl implements UserDao {

    CloseRes close=new CloseRes();
    @Override
    public String save(Connection conn,String email,String name,String password)throws SQLException{
        // TODO Auto-generated method stub
        JsonObject jsonObject=new JsonObject();
        String checksql="select * from tbl_user where email=?";
        PreparedStatement checkps=conn.prepareStatement(checksql);
        checkps.setString(1,email);
        ResultSet ckeckrs=checkps.executeQuery();
        if(ckeckrs.next()){
            jsonObject.addProperty("status","fail");
        }
        else{
            String sql="insert into tbl_user(email,name,password)values(?,?,?)";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.execute();
            jsonObject.addProperty("status", "success");
            close.closeRes(ps, conn);
        }
        close.closeRes(ckeckrs,checkps,conn);
        return jsonObject.toString();
    }

    @Override
    public void update(Connection conn, Long id, User user) throws SQLException {
        // TODO Auto-generated method stub
        String sql="update tbl_user set name=?,password=?,email=?where id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getEmail());
        ps.setLong(4, id);
        ps.execute();
        close.closeRes(ps,conn);
    }

    @Override
    public void delete(Connection conn, User user) throws SQLException {
        String sql="delete from tbl_user where id=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        //ps.setLong(1, user.getid());
        ps.setString(1, user.getid());
        ps.execute();
        close.closeRes(ps,conn);
    }

    @Override
    public String get(Connection conn, String email,String password) throws SQLException {
        JsonObject jsonObject=new JsonObject();
        String sql="select name from tbl_user where email=? and password=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            jsonObject.addProperty("status","success");
            jsonObject.addProperty("name",rs.getString("name"));
        }
        else{
            jsonObject.addProperty("status","fail");
        }
        close.closeRes(ps,conn);
        return jsonObject.toString();
        }
}
