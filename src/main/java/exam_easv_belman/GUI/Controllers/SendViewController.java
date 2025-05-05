package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BLL.util.PdfGeneratorUtil;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SendViewController implements Initializable {

    @FXML
    private Text txtOrderNumber;
    @FXML
    private Button btnPrev;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtComment;

    public void setOrderNumber(String orderNumber) {
        SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
        txtOrderNumber.setText(orderNumber);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResourceAsStream("/images/icon-log.png"));
        ImageView imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);
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

    public void handlePreview(ActionEvent actionEvent) throws Exception {
        PdfGeneratorUtil.generatePdf("src/main/resources/Images/generatedPDF.pdf", txtEmail.getText(), txtComment.getText(), txtOrderNumber.getText());
    }

    public void handleSend(ActionEvent actionEvent) {
        //Todo send report using gmailer class like previous project
    }
}
