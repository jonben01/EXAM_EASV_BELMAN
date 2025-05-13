package exam_easv_belman.GUI.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CommentController {
    @FXML
    private TextField txtComment;

    @FXML
    private void handleConfirm(ActionEvent actionEvent) {
        System.out.println("bro really said: " + txtComment.getText());
    }
}
