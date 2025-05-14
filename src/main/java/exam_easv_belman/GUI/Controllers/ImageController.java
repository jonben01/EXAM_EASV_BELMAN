package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.Tag;
import exam_easv_belman.BE.User;
import exam_easv_belman.BLL.TagManager;
import exam_easv_belman.GUI.Models.PhotoModel;
import exam_easv_belman.GUI.Models.TagModel;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ImageController implements Initializable {

    private Photo photo;
    private TagModel tagModel;
    private PhotoModel photoModel;
    private TagManager tagManager;

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

    @FXML
    private ComboBox<Tag> cbmBox;
    @FXML
    private ListView<Tag> photoTagsListView;
    @FXML
    private Button btnComment;

    public void setImage(Photo photo) {
        this.photo = photo;
        if (Files.exists(Path.of(photo.getFilepath()))) {
            Image image = new Image(new File(photo.getFilepath()).toURI().toString());
            imageView.setImage(image);
        }
    }

    public void setPhoto(Photo photo){
        this.photo = photo;
        loadPhotoTags();
    }


    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        txtOrderNumber.setText(SessionManager.getInstance().getCurrentProductNumber());
        isProduct = SessionManager.getInstance().getIsProduct();
        photoModel = new PhotoModel();
        sidebarVbox.prefWidthProperty().bind(rootHBox.widthProperty().multiply(sidebarSize));
        imageView.fitWidthProperty().bind(rootHBox.widthProperty().multiply(1-sidebarSize));
        imageView.fitHeightProperty().bind(rootHBox.heightProperty());

        //todo sæt i sin egen metode. wtf.
        btnDelete.prefWidthProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnDelete.prefHeightProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnConfirm.prefWidthProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnConfirm.prefHeightProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnComment.prefWidthProperty().bind(rootHBox.heightProperty().multiply(buttonSize));
        btnComment.prefHeightProperty().bind(rootHBox.heightProperty().multiply(buttonSize));

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-log.png")));
        ImageView imgView = new ImageView(img);
        btnLog.setGraphic(imgView);
        img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-back.png")));
        imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);

        setButtonGraphic(btnDelete, "/images/icon-trash.png");
        setButtonGraphic(btnConfirm, "/images/icon-check.png");
        setButtonGraphic(btnComment, "/images/icon-note.png");
        if(SessionManager.getInstance().getCurrentUser().getRole() == Role.OPERATOR)
        {
            btnComment.setVisible(false);
        }
        try {
            tagModel = new TagModel(); // Initialize with BLL layer
            photoModel = new PhotoModel(); // Initialize with BLL layer
            loadAvailableTags();
            applyQCRoleRestrictions();

        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Failed to initialize TagModel", Alert.AlertType.ERROR);
        }
    }

    private void loadAvailableTags() throws Exception {
        try {
            cbmBox.getItems().setAll(tagModel.getAllTags());
        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Failed to load available tags", Alert.AlertType.ERROR);
        }
    }

    private void loadPhotoTags() {
        try {
            // Hent alle tags for dette billede fra databasen
            List<Tag> allPhotoTags = tagModel.getTagsForPhoto(photo);

            // Opdater photoTagsListView
            photoTagsListView.getItems().setAll(allPhotoTags);
        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Could not load tags for this photo.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleReturn(ActionEvent actionEvent) {
        View path;
        if(SessionManager.getInstance().getCurrentUser().getRole() == Role.QC)
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
                                ((PhotoDocController) controller).setOrderNumber(SessionManager.getInstance().getCurrentOrderNumber());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if (controller instanceof QCController) {
                        try {
                            ((QCController) controller).setOrderNumber(SessionManager.getInstance().getCurrentOrderNumber());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
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


    /*
     Checks if there’s a tag selected in the ComboBox.
    Verifies that a valid photo (photo) is selected.
    Calls addTagToPhoto via PhotoModel to add the tag to the PhotoTags table.
    Updates the photoTagsListView with the new tag.

     */

    @FXML
    private void handleAddTag(ActionEvent actionEvent) {
        try {
            // Get the selected tag from the ComboBox
            Tag selectedTag = cbmBox.getSelectionModel().getSelectedItem();
            if (selectedTag == null) {
                // No tag selected, provide user feedback
                System.out.println("No tag selected!");
                return;
            }

            // Check if the tag is already assigned to the photo
            List<Tag> currentTags = tagModel.getTagsForPhoto(photo);
            boolean tagExists = currentTags.stream()
                    .anyMatch(tag -> tag.getId() == selectedTag.getId());

            if (tagExists) {
                // Provide feedback to the user and return
                System.out.println("Tag already exists for this photo!");
                return;
            }

            // Add the tag to the selected photo
            photoModel.addTagToPhoto(photo, selectedTag);

            // Update the ListView to reflect the newly-added tag
            loadPhotoTags();

            // Clear the ComboBox selection
            cbmBox.getSelectionModel().clearSelection();

            // Provide successful feedback
            System.out.println("Tag added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add tag: " + e.getMessage());
        }

    }


    @FXML
    private void onHandleComment(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CommentView.fxml"));
            Parent root = fxmlLoader.load();
            CommentController controller = fxmlLoader.getController();
            controller.setPhoto(photo);

            Stage stage = new Stage();
            stage.setTitle("Add comment");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnComment.getScene().getWindow());
            stage.showAndWait();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load TicketManagementView", Alert.AlertType.ERROR);
        }
    }

    private void applyQCRoleRestrictions(){
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if(currentUser != null && currentUser.getRole() == Role.QC)
        {
            cbmBox.setDisable(true);
        }

    }

    @FXML
    public void handleRemoveTag(ActionEvent actionEvent) {
        Tag selectedTag = photoTagsListView.getSelectionModel().getSelectedItem();

        if (selectedTag == null) {
            AlertHelper.showAlert("Warning", "No tag selected. Please select a tag to remove.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Fjern tag fra databasen
            tagModel.removeTagFromPhoto(photo, selectedTag);

            // Opdater ListView med de resterende tags
            loadPhotoTags();

            AlertHelper.showAlert("Success", "Tag removed successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Failed to remove tag from photo. Please try again.", Alert.AlertType.ERROR);
        }


    }

    @FXML
    public void handleCreate(ActionEvent actionEvent) {
        try {
        Navigator.getInstance().showModal(View.CUSTOM_TAG);

        } catch (Exception e) {
            AlertHelper.showAlert("Error", "Failed to load CustomTagView", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }

}