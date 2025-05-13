package exam_easv_belman.GUI.Models;

import exam_easv_belman.BE.Product;
import exam_easv_belman.BLL.ProductManager;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ProductModel {

    private ProductManager productManager;

    public ProductModel() throws Exception {
        productManager = new ProductManager();
    }

    public List<Product> getAvailableProducts() throws SQLException {
        return productManager.getAvailableProducts();
    }
}
