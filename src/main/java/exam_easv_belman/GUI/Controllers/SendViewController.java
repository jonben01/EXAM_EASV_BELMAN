package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BLL.QCReportManager;
import exam_easv_belman.BLL.util.Gmailer;
import exam_easv_belman.BLL.util.PdfGeneratorUtil;
import exam_easv_belman.BLL.util.PdfPreviewUtil;
import exam_easv_belman.GUI.Models.OrderModel;
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
import java.sql.SQLException;
import java.util.List;
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
    private OrderModel orderModel;

    private Gmailer gMailer;
    @FXML
    private Button btnLog;

    public void setOrderNumber(String orderNumber) throws Exception {
        if (orderModel == null) {
            orderModel = new OrderModel();
        }
        SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
        txtOrderNumber.setText(orderNumber);
        String email = orderModel.getEmailForOrder(orderNumber);
        txtEmail.setText(email);
        txtComment.setText(orderModel.getCommentForOrder(txtOrderNumber.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            orderModel = new OrderModel();
        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Failed to load SendView", Alert.AlertType.ERROR);
        }
        try {
            gMailer = new Gmailer();
        } catch (GeneralSecurityException e) {
            //todo fix exception handling
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image img = new Image(getClass().getResourceAsStream("/images/icon-back.png"));
        ImageView imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);
        img = new Image(getClass().getResourceAsStream("/images/icon-log.png"));
        imgView = new ImageView(img);
        btnLog.setGraphic(imgView);
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
                    try {
                        ((QCController) controller).setOrderNumber(orderNumber);
                    } catch (Exception e) {
                        AlertHelper.showAlert("Error", "Failed to load QCView (SendViewController.java)", Alert.AlertType.ERROR);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load QCView", Alert.AlertType.ERROR);
        }

    }

    public void handlePreview(ActionEvent actionEvent) throws Exception {
        /*
        File pdfFile = new File("src/main/resources/Images/" + txtOrderNumber.getText() + ".pdf");
        Stage stage = (Stage) txtOrderNumber.getScene().getWindow(); // Get current stage
        PdfGeneratorUtil.generatePdf(pdfFile.getAbsolutePath(), txtEmail.getText(), txtComment.getText(), txtOrderNumber.getText(), true, stage);
         */
        try {
            // Define the file path for the PDF
            String filePath = "src/main/resources/Images/" + txtOrderNumber.getText() + "_Preview.pdf";
            File pdfFile = new File(filePath);

            // Generate the PDF using QCReportManager
            QCReportManager qcReportManager = new QCReportManager();
            qcReportManager.generateQCReportPDF(
                    filePath,
                    txtEmail.getText(),
                    txtComment.getText(),
                    (Stage) txtOrderNumber.getScene().getWindow()
            );

            // Open the preview using PdfPreviewUtil
            PdfPreviewUtil.showPdfPreview(pdfFile, (Stage) txtOrderNumber.getScene().getWindow());

        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Failed to preview the PDF: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }


    public void handleSend(ActionEvent actionEvent) throws Exception {
        /*
        String filePath = "src/main/resources/Images/" + txtOrderNumber.getText() + ".pdf";
        PdfGeneratorUtil.generatePdf(filePath, txtEmail.getText(), txtComment.getText(), txtOrderNumber.getText(), false);
        File generatedPDF = new File(filePath);
        gMailer.sendMail(txtOrderNumber.getText(), "This email contains a quality control report as per request by the client.\nThis Quality Control report is centered around the order: " + txtOrderNumber.getText(), txtEmail.getText(), generatedPDF);
        System.out.println(generatedPDF.delete());
        */

        String filePath = "src/main/resources/Images/" + txtOrderNumber.getText() + ".pdf";
        File generatedPDF = new File(filePath);

        QCReportManager qcReportManager = new QCReportManager();
        qcReportManager.generateQCReportPDF(
                filePath,
                txtEmail.getText(),
                txtComment.getText(),
                (Stage) txtOrderNumber.getScene().getWindow()
        );


        // Send the mail with the generated PDF attached
        gMailer.sendMail(
                txtOrderNumber.getText(),
                "This email contains a quality control report as per request by the client.\nThis Quality Control report is centered around the order: "
                        + txtOrderNumber.getText(),
                txtEmail.getText(),
                generatedPDF
        );

        // Delete the generated PDF after sending the email
        boolean deleted = generatedPDF.delete();
        System.out.println("File deleted: " + deleted);
    }

    @FXML
    private void handleLog(ActionEvent actionEvent) {
        try {
            AlertHelper.showConfirmationAlert("Log out?", "Are you sure you wish to log out?", () -> {
                Navigator.getInstance().goTo(View.LOGIN);
                SessionManager.getInstance().logout();
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load LoginView", Alert.AlertType.ERROR);
        }
    }
}