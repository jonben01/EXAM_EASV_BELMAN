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
    private List<String> products = Arrays.asList("ORD-1001-001", "ORD-1001-002");
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
            AlertHelper.showAlert("Error", "Please enter an order/product number", Alert.AlertType.ERROR);
            return;
        }

        if (!orders.contains(inputOrderNumber) && !products.contains(inputOrderNumber)) {

            AlertHelper.showAlert("Error", "The order/product number entered does not exist", Alert.AlertType.ERROR);
            return;
        }

        int dashCount = inputOrderNumber.length() - inputOrderNumber.replace("-", "").length();

        if(dashCount == 1) {
            SessionManager.getInstance().setCurrentOrderNumber(inputOrderNumber);

            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null && currentUser.getRole() == Role.QC) {
                GoToQCView(event, inputOrderNumber, false);
            } else {
                GoToPhotoDocView(event, inputOrderNumber, false);
            }
        }
        if(dashCount == 2) {
            String orderNumber = inputOrderNumber.substring(0, inputOrderNumber.lastIndexOf("-"));
            SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null && currentUser.getRole() == Role.QC) {
                GoToQCView(event, inputOrderNumber,true);
            } else {
                GoToPhotoDocView(event, inputOrderNumber, true);
            }
        }

    }

    public void handleLogOut(ActionEvent actionEvent) {
        SessionManager.getInstance().logout();
        Navigator.getInstance().goTo(View.LOGIN);

    }

    private void GoToPhotoDocView(ActionEvent event, String orderNumber, boolean IsProduct ){
        try {
            Navigator.getInstance().setRoot(View.PHOTO_DOC, controller -> {
                System.out.println(controller);
                if (controller instanceof PhotoDocController) {
                    if(!IsProduct) {
                        try {
                            ((PhotoDocController) controller).setOrderNumber(orderNumber);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(IsProduct)
                    {
                        try {
                            ((PhotoDocController) controller).setProductNumber(orderNumber);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load PhotoDocView", Alert.AlertType.ERROR);
        }

    }

    private void GoToQCView(ActionEvent event, String orderNumber, boolean IsProduct){
        try {
            Navigator.getInstance().setRoot(View.QCView, controller -> {
                if (controller instanceof QCController) {
                    if(!IsProduct) {
                        ((QCController) controller).setOrderNumber(orderNumber);

                    }
                    else if(IsProduct) {
                        ((QCController) controller).setProductNumber(orderNumber);
                    }
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

