package exam_easv_belman.BLL;

import exam_easv_belman.BE.Product;
import exam_easv_belman.DAL.IProductDataAccess;
import exam_easv_belman.DAL.ProductDAO; // Add this import
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class ProductManager {
    private IProductDataAccess productDataAccess;
    
    public ProductManager() throws Exception {
        this.productDataAccess = new ProductDAO(); // Initialize in constructor
    }

    public List<Product> getAvailableProducts() throws SQLException {
        return productDataAccess.getAllProducts();
    }

    public ObservableList<Product> getProductsForOrder(String orderNumber) throws SQLException {
    return productDataAccess.getProductsForOrder(orderNumber);
    }
}