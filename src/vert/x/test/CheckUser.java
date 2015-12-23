package vert.x.test;
import dao.UserDao;
import dao.impl.ProductDaoimpl;
import dao.impl.UserDaoimpl;
import entity.User;
import org.vertx.java.core.http.HttpServer;
import service.CheckUserService;
import util.ConnectionFactory;
import dao.ProductDao;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;
import java.sql.*;
/**
 * Created by Sheng Shoubo on 4/5/15.
 */

public class CheckUser extends Verticle  {
    @Override
    public void start() {
        final Logger logger = container.logger();
        HttpServer server = vertx.createHttpServer();
        RouteMatcher routeMatcher = new RouteMatcher();
        routeMatcher.get("/CheckUser/all", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                logger.info(req.params().get("UserId"));
                logger.info(req.params().get("PassWord"));
                try {
                    User user = new User();
                    String userid = req.params().get("UserId");
                    String password = req.params().get("PassWord");
                    user.setName(userid);
                    user.setPassword(password);
                    CheckUserService cku = new CheckUserService();
                    boolean check = cku.checkLogin(user);
                    System.out.println(check);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).noMatch(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                logger.debug(req.path());
                String file = "";
                if (req.path().equals("/")) {
                    file = "index.html";
                } else if (!req.path().contains("..")) {
                    file = req.path();
                }
                req.response().sendFile("public/" + file);
            }
        });
        server.requestHandler(routeMatcher).listen(8080, "localhost");
    }
}
