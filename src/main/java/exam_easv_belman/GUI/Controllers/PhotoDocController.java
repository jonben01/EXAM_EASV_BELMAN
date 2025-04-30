package exam_easv_belman.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PhotoDocController {
    @FXML
    private Text txtOrderNumber;

    public void setOrderNumber(String orderNumber) {
        txtOrderNumber.setText(orderNumber);
    }
}
