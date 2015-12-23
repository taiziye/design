package dao;
import java.sql.Connection;
import java.sql.SQLException;
import entity.Product;
import util.ConnectionFactory;

public interface ProductDao {
    public void save(Connection conn, Product product)throws SQLException;

    public void update(Connection conn, String id, Product product)throws SQLException;

    public void delete(Connection conn, Product product)throws SQLException;

    public String get_index(Connection conn, int sheetnum)throws SQLException;

    public String get_product(Connection conn, String id)throws SQLException;

    public String get_catalog(Connection conn, String catalog)throws SQLException;

    public String get_menu(Connection conn)throws SQLException;

    public String search(Connection conn,String keyword)throws SQLException;
}
