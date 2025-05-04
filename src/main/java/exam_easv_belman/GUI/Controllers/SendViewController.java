package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

public class SendViewController {

    @FXML
    private Text txtOrderNumber;

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
            Navigator.getInstance().goTo(View.QCView, controller -> {
                if (controller instanceof QCController) {
                    ((QCController) controller).setOrderNumber(orderNumber);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load QCView", Alert.AlertType.ERROR);
        }

    }

    public void handlePreview(ActionEvent actionEvent) {
        //Todo preview report

    }

    public void handleSend(ActionEvent actionEvent) {
        //Todo send report using gmailer class like previous project
    }
}
