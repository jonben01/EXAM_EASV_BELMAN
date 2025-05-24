package exam_easv_belman.DAL;

import java.sql.SQLException;

public interface IOrderDataAccess {

     String getEmailForOrder(String orderNumber) throws SQLException;

     void addCommentToOrder(String comment, String orderNumber) throws SQLException;

     String getCommentForOrder(String orderNumber) throws SQLException;


}
