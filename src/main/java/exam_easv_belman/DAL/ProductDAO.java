package exam_easv_belman.DAL;

import exam_easv_belman.BE.Order;
import exam_easv_belman.BE.Product;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductDAO implements IProductDataAccess {
    private DBConnector dbConnector;

    public ProductDAO() throws Exception {
        dbConnector = new DBConnector();
    }

    @Override
    public ObservableList<Product> getAllProducts() throws SQLException {
        ObservableList<Product> products = FXCollections.observableArrayList();
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

    @Override
    public ObservableList<Product> getProductsForOrder(String orderNumber) throws SQLException {
        Order order = getOrderForNumber(orderNumber);
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM dbo.Products WHERE order_id = ?";
        try(Connection conn = dbConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, order.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Product tempProduct = new Product();
                tempProduct.setId(rs.getInt("id"));
                tempProduct.setOrder_id(rs.getInt("order_id"));
                tempProduct.setProduct_number(rs.getString("products_order_number"));
                productsList.add(tempProduct);
            }
        }
    return productsList;
    }

    @Override
    public Order getOrderForNumber(String orderNumber) throws SQLException {
        Order tempOrder = new Order();
       String sql = "SELECT * FROM dbo.Orders WHERE order_number = ?";
        try(Connection conn = dbConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, orderNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                tempOrder.setId(rs.getInt("id"));
                tempOrder.setOrderNumber(rs.getString("order_number"));
                tempOrder.setCustomerEmail(rs.getString("customer_email"));
                tempOrder.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                tempOrder.setComment(rs.getString("Comment"));
            }
        }
        return tempOrder;
        }


}
