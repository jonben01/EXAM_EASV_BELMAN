package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

public class PhotoDocController {
    @FXML
    private Text txtOrderNumber;

    public void setOrderNumber(String orderNumber) {
        txtOrderNumber.setText(orderNumber);
    }

    public void handleReturn(ActionEvent actionEvent) {

        try{
            Navigator.getInstance().goTo(View.ORDER);
        }catch(Exception e){
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load OrderView", Alert.AlertType.ERROR);
        }

    }

}
