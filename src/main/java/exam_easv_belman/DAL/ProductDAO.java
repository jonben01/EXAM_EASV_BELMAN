package exam_easv_belman.DAL;

import exam_easv_belman.BE.Product;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO implements IProductDataAccess {
    private DBConnector dbConnector;

    public ProductDAO() throws Exception {
        dbConnector = new DBConnector();
    }

    @Override
    public ObservableList<Product> getAllProducts() throws SQLException {
        ObservableList<Product> products = javafx.collections.FXCollections.observableArrayList();
        String sql = "SELECT * FROM dbo.Products";
        try(Connection conn = dbConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Product tempProduct = new Product();
                tempProduct.setId(rs.getInt("id"));
                tempProduct.setOrder_id(rs.getInt("order_id"));
                tempProduct.setProduct_number(rs.getString("products_order_number"));
                products.add(tempProduct);
            }
        }
        catch(SQLException e){
            AlertHelper.showAlert("DB error", "Could not get products from DB", Alert.AlertType.ERROR);
        }
        return products;
    }
}
