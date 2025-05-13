package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CommentController {
    @FXML
    private TextField txtComment;
    private Photo photo;

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @FXML
    private void handleConfirm(ActionEvent actionEvent) {

    }
}
