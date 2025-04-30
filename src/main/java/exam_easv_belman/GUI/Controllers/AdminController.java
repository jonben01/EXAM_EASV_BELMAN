package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class AdminController {
    @FXML
    public Button btnLogout;
    @FXML
    public Button btnCreateUser;
    @FXML
    public Button btnDeleteUser;
    @FXML
    public ListView<User> lstUsers;

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        //send user to login page
    }

    @FXML
    public void handleCreateUser(ActionEvent actionEvent) {
    }

    @FXML
    public void handleDeleteUser(ActionEvent actionEvent) {
    }
}
