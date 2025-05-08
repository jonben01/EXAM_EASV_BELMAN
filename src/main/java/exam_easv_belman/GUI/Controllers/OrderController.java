package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class OrderController {

    @FXML
    private TextField OrderNumber;

    //TODO take orders from db not just these hardcoded values
    private final List<String> orders = Arrays.asList("1001", "1002", "1003", "ORD-1001");
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnUser;


    @FXML
    private void initialize() {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-search.png")));
        ImageView imgView = new ImageView(img);
        btnSearch.setGraphic(imgView);
        img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-log.png")));
        imgView = new ImageView(img);
        btnLogOut.setGraphic(imgView);

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            btnUser.setVisible(true);
        } else {
            btnUser.setVisible(false);
        }


    }


    @FXML
    private void handleSearch(ActionEvent event) {
        String inputOrderNumber = OrderNumber.getText();

        if (inputOrderNumber.isEmpty()) {
            AlertHelper.showAlert("Error", "Please enter an order number", Alert.AlertType.ERROR);
            return;
        }

        if (!orders.contains(inputOrderNumber)) {
            AlertHelper.showAlert("Error", "The Order Number Entered Does Not Exist", Alert.AlertType.ERROR);
            return;
        }

        SessionManager.getInstance().setCurrentOrderNumber(inputOrderNumber);

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.getRole() == Role.QC){
            GoToQCView(event, inputOrderNumber);
        } else {
            GoToPhotoDocView(event, inputOrderNumber);
        }

    }

    public void handleLogOut(ActionEvent actionEvent) {
        SessionManager.getInstance().logout();
        Navigator.getInstance().goTo(View.LOGIN);

    }

    private void GoToPhotoDocView(ActionEvent event, String orderNumber){
        try {
            Navigator.getInstance().setRoot(View.PHOTO_DOC, controller -> {
                if (controller instanceof PhotoDocController) {
                    ((PhotoDocController) controller).setOrderNumber(orderNumber);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load PhotoDocView", Alert.AlertType.ERROR);
        }

    }

    private void GoToQCView(ActionEvent event, String orderNumber){
        try {
            Navigator.getInstance().setRoot(View.QCView, controller -> {
                if (controller instanceof QCController) {
                    ((QCController) controller).setOrderNumber(orderNumber);
                }
            });
            } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load QCView", Alert.AlertType.ERROR);

        }

    }

    public void handleUserManagement(ActionEvent actionEvent) {
        try {
            Navigator.getInstance().goTo(View.ADMIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

