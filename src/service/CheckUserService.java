package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dao.UserDao;
import dao.impl.UserDaoimpl;
import entity.User;
import util.CloseRes;
import util.ConnectionFactory;

public class CheckUserService {
	public boolean checkLogin(User user){
		Connection conn;
		CloseRes close=new CloseRes();

		try {
			conn=ConnectionFactory.getInstance().makeConnection();
			String sql="select * from tbl_user where email=? and password=?";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,user.getEmail());
			ps.setString(2,user.getPassword());
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				close.closeRes(rs,ps,conn);
				return true;
			}
			else{
				close.closeRes(rs,ps,conn);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
