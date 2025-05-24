package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.GUI.Models.PhotoModel;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CommentController implements Initializable {

    private PhotoModel photoModel;
    @FXML
    private TextArea txtComment;
    private Photo photo;
    @FXML
    private Label countLabel;

    public CommentController() {
        try {
            photoModel = new PhotoModel();
        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Could not create photo model.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countLabel.setText("0/300");
        txtComment.setTextFormatter(new TextFormatter<String>(change -> (change.getControlNewText().length() <= 300 ? change : null)));
        txtComment.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                countLabel.setText(newValue.length() + "/300");
            }
        });
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        if(photo.getComment() != null)
        {
            txtComment.setText(photo.getComment());
        }
    }

    @FXML
    private void handleConfirm(ActionEvent actionEvent) {
        AlertHelper.showConfirmationAlert("Confirmation", "Are you sure you wish to add this comment to this photo?", () -> {
            try {
                photoModel.addCommentToPhoto(txtComment.getText(), photo);
            } catch (SQLException e) {
                AlertHelper.showAlert("Error", "Failed to add comment to photo.", Alert.AlertType.ERROR);
            }
        });
    }
}
