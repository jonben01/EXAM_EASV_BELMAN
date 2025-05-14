package exam_easv_belman.DAL;

import exam_easv_belman.BE.Order;
import exam_easv_belman.BE.Product;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IProductDataAccess {

    public ObservableList<Product> getAllProducts() throws SQLException;

    ObservableList<Product> getProductsForOrder(String orderNumber) throws SQLException;

    Order getOrderForNumber(String orderNumber) throws SQLException;
}
