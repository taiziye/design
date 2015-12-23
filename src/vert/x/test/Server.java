package vert.x.test;
import dao.UserDao;
import dao.impl.ProductDaoimpl;
import dao.impl.UserDaoimpl;
import org.vertx.java.core.http.HttpServer;
import util.ConnectionFactory;
import dao.ProductDao;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;
import java.sql.*;
import java.util.List;

/**
 * Created by Sheng Shoubo on 4/5/15.
 */

public class Server extends Verticle  {
    @Override
    public void start() {
        final Logger logger = container.logger();
        HttpServer server = vertx.createHttpServer();
        RouteMatcher routeMatcher = new RouteMatcher();

        //获取该目录下的产品:get_catalog
        routeMatcher.get("/product/all", new RequestCatalogHandler(logger));

        //获取产品详情：get_product
        routeMatcher.get("/product/detail", new RequestProductHandler(logger));

        //获取三级目录：get_menu
        routeMatcher.get("/product/index", new RequestMenuHandler(logger));

        //搜索：search
        routeMatcher.get("/product/search", new RequestSearchHandler(logger));

        //注册:save
        routeMatcher.get("/user/join", new RequestSaveHandler(logger));

        //登录:get
        routeMatcher.get("/user/checkLogin", new RequestCheckLoginHandler(logger));

        //无匹配路径
        routeMatcher.noMatch(new RequestNoMatchHandler(logger));

        //服务器监听8080端口
        server.requestHandler(routeMatcher).listen(8080, "localhost");
    }

    private static class RequestCatalogHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestCatalogHandler(Logger logger) {
            this.logger = logger;
        }

        public void handle(HttpServerRequest req) {
            logger.info(req.params().get("productcatalog"));
            try {
                Connection conn= ConnectionFactory.getInstance().makeConnection();
                ProductDao productDao=new ProductDaoimpl();
                String productcatalog=req.params().get("productcatalog");
                String sheetnum=req.params().get("sheetnum");
                String result;
                if(productcatalog.isEmpty()){
                    result=productDao.get_index(conn, Integer.parseInt(sheetnum));
                } else {
                    result=productDao.get_catalog(conn,productcatalog);
                }
                req.response().end(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RequestProductHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestProductHandler(Logger logger) {
            this.logger = logger;
        }

        public void handle(HttpServerRequest req) {
            logger.info(req.params().get("productid"));
            try {
                String productid = req.params().get("productid");
                Connection conn = ConnectionFactory.getInstance().makeConnection();
                ProductDao productDao = new ProductDaoimpl();
                String result = productDao.get_product(conn, productid);
                req.response().end(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RequestMenuHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestMenuHandler(Logger logger) {
            this.logger = logger;
        }

        public void handle(HttpServerRequest req) {
            logger.info(req.params().get("id"));
            try {
                Connection conn = ConnectionFactory.getInstance().makeConnection();
                ProductDao productDao = new ProductDaoimpl();
                String result = productDao.get_menu(conn);
                req.response().end(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RequestSearchHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestSearchHandler(Logger logger) {
            this.logger = logger;
        }

        public void handle(HttpServerRequest req) {

            logger.info(req.params().get("searchproduct"));
            try {
                Connection conn = ConnectionFactory.getInstance().makeConnection();
                ProductDao productDao = new ProductDaoimpl();
                String keyword = req.params().get("searchproduct");
                String result = productDao.search(conn, keyword);
                req.response().end(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RequestSaveHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestSaveHandler(Logger logger) {
            this.logger = logger;
        }

        public void handle(HttpServerRequest req) {
            logger.info(req.params().get("email"));
            logger.info(req.params().get("name"));
            logger.info(req.params().get("password"));
            try {
                Connection conn = ConnectionFactory.getInstance().makeConnection();
                UserDao userDao = new UserDaoimpl();
                String email = req.params().get("email");
                String name = req.params().get("name");
                String password = req.params().get("password");
                String result = userDao.save(conn, email, name, password);
                req.response().end(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RequestCheckLoginHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestCheckLoginHandler(Logger logger) {
            this.logger = logger;
        }

        public void handle(HttpServerRequest req) {
            logger.info(req.params().get("email"));
            logger.info(req.params().get("password"));
            try {
                Connection conn = ConnectionFactory.getInstance().makeConnection();
                UserDao userDao = new UserDaoimpl();
                String email = req.params().get("email");
                String password = req.params().get("password");
                String result = userDao.get(conn, email, password);
                req.response().end(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class RequestNoMatchHandler implements Handler<HttpServerRequest> {
        private final Logger logger;

        public RequestNoMatchHandler(Logger logger) {
            this.logger = logger;
        }

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
    }
}
