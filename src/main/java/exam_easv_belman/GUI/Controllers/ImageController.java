package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import exam_easv_belman.GUI.Models.PhotoModel;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Objects;

public class ImageController implements Initializable {

    private Photo photo;
    @FXML
    private HBox rootHBox;
    @FXML
    private ImageView imageView;
    @FXML
    private VBox sidebarVbox;

    private double sidebarSize = 0.1;
    private double buttonSize = 0.7;
    private boolean isProduct;
    @FXML
    private Button btnPrev;
    @FXML
    private Text txtOrderNumber;
    @FXML
    private Button btnLog;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnConfirm;
    private PhotoModel photoModel;
    @FXML
    private Button btnQC;
    public boolean isQC = false;

    public void setImage(Photo photo) {
        this.photo = photo;
        if (Files.exists(Path.of(photo.getFilepath()))) {
            Image image = new Image(new File(photo.getFilepath()).toURI().toString());
            imageView.setImage(image);
        }
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        photoModel = new PhotoModel();
        sidebarVbox.prefWidthProperty().bind(rootHBox.widthProperty().multiply(sidebarSize));
        imageView.fitWidthProperty().bind(rootHBox.widthProperty().multiply(1-sidebarSize));
        imageView.fitHeightProperty().bind(rootHBox.heightProperty());

        btnDelete.prefWidthProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnDelete.prefHeightProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnConfirm.prefWidthProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnConfirm.prefHeightProperty().bind(rootHBox.heightProperty().multiply(buttonSize));

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-log.png")));
        ImageView imgView = new ImageView(img);
        btnLog.setGraphic(imgView);
        img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-back.png")));
        imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);

        setButtonGraphic(btnDelete, "/images/icon-trash.png");
        setButtonGraphic(btnConfirm, "/images/icon-check.png");

    }

    @FXML
    private void handleReturn(ActionEvent actionEvent) {
        View path;
        if(isQC)
        {
            path = View.QCView;
        }
        else
        {
            path = View.PHOTO_DOC;
        }
            try {
                Navigator.getInstance().setRoot(path, controller -> {
                    if (controller instanceof PhotoDocController) {
                        try {
                            if (!isProduct) {
                                ((PhotoDocController) controller).setOrderNumber(txtOrderNumber.getText());
                            } else {
                                ((PhotoDocController) controller).setProductNumber(txtOrderNumber.getText());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (controller instanceof QCController) {
                        try {
                            if(!isProduct)
                            ((QCController) controller).setOrderNumber(txtOrderNumber.getText());
                            else
                                ((QCController) controller).setProductNumber(txtOrderNumber.getText());
                        } catch (Exception e) {
                            AlertHelper.showAlert("Error", "Failed to load QCView", Alert.AlertType.ERROR);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                AlertHelper.showAlert("Error", "Failed to load PhotoDocView", Alert.AlertType.ERROR);
            }
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

    @FXML
    private void handleDelete(ActionEvent actionEvent) {
        AlertHelper.showConfirmationAlert("Delete this photo?", "Are you sure you wish to delete this photo?", () -> {
            try {
                photoModel.deleteImage(photo);
                handleReturn(actionEvent);
            } catch (Exception e) {
                e.printStackTrace();
                AlertHelper.showAlert("Error", "Failed to delete photo", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void handleConfirm(ActionEvent actionEvent) {
        handleReturn(actionEvent);
    }

    private void setButtonGraphic(Button button, String imagePath) {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imgView = new ImageView(img);
        button.setMaxSize(60,60);

        imgView.fitWidthProperty().bind(button.widthProperty().multiply(0.6));
        imgView.fitHeightProperty().bind(button.heightProperty().multiply(0.6));
        imgView.setPreserveRatio(true);

        button.setGraphic(imgView);
    }


    public void setOrderNumber(String orderNumber, boolean isProduct) throws Exception {
        txtOrderNumber.setText(orderNumber);
        this.isProduct = isProduct;
    }
}