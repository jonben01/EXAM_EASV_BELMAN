package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Objects;

public class PhotoDocController {
    @FXML
    private Text txtOrderNumber;
    @FXML
    private Button btnPrev;


    @FXML
    private void initialize() {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-log.png")));
        ImageView imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);
    }
    public void setOrderNumber(String orderNumber) {
        SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
        txtOrderNumber.setText(orderNumber);

    }

    public void handleReturn(ActionEvent actionEvent) {
        String orderNumber = SessionManager.getInstance().getCurrentOrderNumber();
        if (orderNumber == null || orderNumber.isEmpty()) {
            AlertHelper.showAlert("Error", "No order number available", Alert.AlertType.ERROR);
            return;
        }
        try {
            Navigator.getInstance().goTo(View.ORDER, controller -> {
                if (controller instanceof SendViewController) {
                    ((SendViewController) controller).setOrderNumber(orderNumber);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load OrderView", Alert.AlertType.ERROR);
        }


    }

}
