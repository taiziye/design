package dao.impl;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

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
            String sql="insert into tbl_user(email,name,password,email_checked)values(?,?,?,?)";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setBoolean(4, false);
            ps.execute();
            jsonObject.addProperty("status", "success");
            close.closeRes(ps, conn);
            String to = email;
            String from = "noreply@shejiqingdan.com";
            String host = "smtp.ym.163.com";
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.auth", "true");
            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("noreply@shejiqingdan.com", "shejiqingdan");
                }
            });
            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));
                message.setSubject("请激活您的设计清单网站账号");
                String htmlpage = "<div>\n" +
                        "    <table width=\"640\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                        "    bgcolor=\"#ffffff\">\n" +
                        "        <tbody>\n" +
                        "            <tr>\n" +
                        "                <td>\n" +
                        "                    <table width=\"800\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                        "                    bgcolor=\"#00a1cc\" height=\"48\">\n" +
                        "                    </table>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "            <tr>\n" +
                        "                <td>\n" +
                        "                    <table width=\"800\" border=\"0\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                        "                    style=\" border:1px solid #edecec; border-top:none; border-bottom:none; padding:0 20px;font-size:14px;color:#333333;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"760\" height=\"56\" border=\"0\" align=\"left\" colspan=\"2\" style=\"font-family:'Microsoft YaHei'; font-size:16px;vertical-align:bottom;\">\n" +
                        "                                    尊敬的\n" +
                        "                                    <span>" +name +
                        "                                        <wbr>" +
                        "                                    </span>" +
                        "                                    ：\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"760\" height=\"30\" border=\"0\" align=\"left\" colspan=\"2\">\n" +
                        "                                    &nbsp;\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" border=\"0\" align=\"left\" valign=\"middle\" style=\"padding-left:40px; width:720px; text-align:left;vertical-align:middle;line-height:32px;font-family:'Microsoft YaHei';\">\n" +
                        "                                    请激活您的设计清单网站账号。\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" colspan=\"2\" style=\"padding-left:40px;font-family:'Microsoft YaHei';\">\n" +
                        "                                    您已经在设计清单网站申请注册，登录帐号为：" +
                        "                                    <span>" +name +
                        "                                        <wbr>" +
                        "                                    </span>" +
                        "                                    ,密码为：" +
                        "                                    <span>\n" +password +
                        "                                        <wbr>\n" +
                        "                                    </span>\n" +
                        "                                    ，为了确认您的电子邮件地址并激活您的帐号，请立即\n" +
                        "                                    <a href=\"#\"\n" +
                        "                                    target=\"_blank\">\n" +
                        "                                        点击这里激活账户\n" +
                        "                                    </a>\n" +
                        "                                    。\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" colspan=\"2\" style=\"padding-left:40px;\">\n" +
                        "                                    &nbsp;\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" colspan=\"2\" style=\"padding-left:40px;font-family:'Microsoft YaHei';\">\n" +
                        "                                    如果以上链接不能打开，请将下面地址复制到您的浏览器(如IE)的地址栏，打开页面后同样可以完成帐户激活。（该链接在 12小时 内有效， 如超时请登录\n" +
                        "                                    <a target=\"_blank\" href=\"#\">\n" +
                        "                                        设计清单网站\n" +
                        "                                    </a>\n" +
                        "                                    重新发送验证邮件）\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" colspan=\"2\" style=\"padding-left:40px;\">\n" +
                        "                                    &nbsp;\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" colspan=\"2\" style=\"padding-left:40px;font-family:'Microsoft YaHei';\">\n" +
                        "                                    <a style=\"color: #0088cc; text-decoration: underline;word-break: break-all;word-wrap: break-word;display: block;width: 640px;font-size:12px;\"\n" +
                        "                                    href=\"#\"\n" +
                        "                                    target=\"_blank\">http://www.shejiqingdan.com/active.html\n" +
                        "                                    </a>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"32\" colspan=\"2\" style=\"padding-left:40px;font-family:'Microsoft YaHei';\">\n" +
                        "                                    如果您没有注册设计清单网站，请忽略此邮件。\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td width=\"720\" height=\"14\" colspan=\"2\" style=\"padding:8px 0 28px;color:#999999; font-size:12px;font-family:'Microsoft YaHei';\">\n" +
                        "                                    此为系统邮件请勿回复\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "                </td>\n" +
                        "            </tr>\n" +
                        "        </tbody>\n" +
                        "    </table>\n" +
                        "    </div>";
                message.setContent(htmlpage, "text/html; charset=utf-8");
//                message.setText("This is actual message");
                Transport.send(message);
                System.out.println("Sent message successfully....");
            }catch (MessagingException mex) {
                mex.printStackTrace();
            }
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
