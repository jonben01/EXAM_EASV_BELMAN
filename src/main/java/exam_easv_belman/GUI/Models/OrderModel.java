package exam_easv_belman.GUI.Models;

import exam_easv_belman.BLL.OrderManager;

import java.sql.SQLException;

public class OrderModel {
    private OrderManager orderManager;

    public OrderModel() throws Exception {
        orderManager = new OrderManager();
    }

    public String getEmailForOrder(String orderNumber) throws SQLException {
        return orderManager.getEmailForOrder(orderNumber);
    }

    public void addCommentToOrder(String comment, String orderNumber) throws SQLException {
        orderManager.addCommentToOrder(comment, orderNumber);
    }

    public String getCommentForOrder(String orderNumber) throws SQLException {
        return orderManager.getCommentForOrder(orderNumber);
    }
}
