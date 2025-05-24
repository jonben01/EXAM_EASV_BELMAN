package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Tag;
import exam_easv_belman.BLL.TagManager;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;

public class CustomTagViewController {

    public javafx.scene.control.Button btnCancel;
    public TextField txtTagName;
    public Button btnComplete;


    public void HandleCancel(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void HandleComplete(ActionEvent actionEvent) {
        String tagName = txtTagName.getText().trim();

        if (tagName.isEmpty()) {
            AlertHelper.showAlert("Error", "Please provide a name for the tag.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Opret et nyt tag og indsæt i databasen
            Tag newTag = new Tag(tagName);
            Tag createdTag = new TagManager().createTag(newTag);

            AlertHelper.showAlert("Success", "Tag created successfully with name: " + createdTag.getName(), Alert.AlertType.INFORMATION);

            // Luk vinduet
            ((Button) actionEvent.getSource()).getScene().getWindow().hide();
        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Could not create tag. Please try again.", Alert.AlertType.ERROR);
        }

    }
}
