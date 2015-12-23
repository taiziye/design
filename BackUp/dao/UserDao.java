package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.User;

public interface UserDao {
    public String save(Connection conn,String email,String name,String password)throws SQLException;

    public void update(Connection conn, Long id, User user)throws SQLException;

    public void delete(Connection conn, User user)throws SQLException;

    public String get(Connection conn,String email,String password)throws SQLException;


}
