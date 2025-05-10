package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BLL.util.Gmailer;
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

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
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

    private Gmailer gMailer;

    public void setOrderNumber(String orderNumber) {
        SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
        txtOrderNumber.setText(orderNumber);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            gMailer = new Gmailer();
        } catch (GeneralSecurityException e) {
            //todo fix exception handling
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        File pdfFile = new File("src/main/resources/Images/" + txtOrderNumber.getText() + ".pdf");
        Stage stage = (Stage) txtOrderNumber.getScene().getWindow(); // Get current stage
        PdfGeneratorUtil.generatePdf(pdfFile.getAbsolutePath(), txtEmail.getText(), txtComment.getText(), txtOrderNumber.getText(), true, stage);
    }


    public void handleSend(ActionEvent actionEvent) throws Exception {
        //Todo send report using gmailer class like previous project
        /*
        String filePath = "src/main/resources/Images/" + txtOrderNumber.getText() + ".pdf";
        PdfGeneratorUtil.generatePdf(filePath, txtEmail.getText(), txtComment.getText(), txtOrderNumber.getText(), false);
        File generatedPDF = new File(filePath);
        gMailer.sendMail(txtOrderNumber.getText(), "This email contains a quality control report as per request by the client.\nThis Quality Control report is centered around the order: " + txtOrderNumber.getText(), txtEmail.getText(), generatedPDF);
        System.out.println(generatedPDF.delete());
        */

        String filePath = "src/main/resources/Images/" + txtOrderNumber.getText() + ".pdf";
        File generatedPDF = new File(filePath);

        Stage stage = (Stage) txtOrderNumber.getScene().getWindow();

        PdfGeneratorUtil.generatePdf(filePath, txtEmail.getText(), txtComment.getText(), txtOrderNumber.getText(), false, stage);

        gMailer.sendMail(txtOrderNumber.getText(), "This email contains a quality control report as per request by the client.\nThis Quality Control report is centered around the order: " + txtOrderNumber.getText(),txtEmail.getText(), generatedPDF);

        boolean deleted = generatedPDF.delete();
        System.out.println("File deleted: " + deleted);


    }
}
