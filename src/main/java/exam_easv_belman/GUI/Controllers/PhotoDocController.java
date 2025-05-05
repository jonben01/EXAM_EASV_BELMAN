package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.GUI.Models.PhotoModel;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.Node;

public class PhotoDocController {
    @FXML
    public Button btnOpenCamera;
    @FXML
    private Text txtOrderNumber;
    @FXML
    private Button btnPrev;

    private String orderNumber;
    @FXML
    private GridPane gridPhoto;
    
    private PhotoModel photoModel;


    @FXML
    private void initialize() throws Exception {
        orderNumber = SessionManager.getInstance().getCurrentOrderNumber();
        photoModel = new PhotoModel();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-log.png")));
        ImageView imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);
        ObservableList<Photo> imagesFromDatabase = photoModel.getImagesFromDatabase(orderNumber);
        fillPhotoGrid(imagesFromDatabase);
        System.out.println("has gotten " + imagesFromDatabase.size() + " images from database" );
    }

private void fillPhotoGrid(ObservableList<Photo> imagesFromDatabase) {
    gridPhoto.getChildren().clear();

    int column = 0;
    int row = 0;
    int counter = 0;

    gridPhoto.widthProperty().addListener((obs, oldVal, newVal) -> updateImageSizes());
    gridPhoto.heightProperty().addListener((obs, oldVal, newVal) -> updateImageSizes());
    for (Photo photo : imagesFromDatabase) {
        counter++;
        //TODO Temporary fix. Need to add multiple pages.
        if(counter <= 6) {
            try {
                ImageView imageView = new ImageView();
                if (Files.exists(Path.of(photo.getFilepath()))) {
                    Image image = new Image(new File(photo.getFilepath()).toURI().toString());
                    imageView.setImage(image);

                    imageView.fitWidthProperty().bind(gridPhoto.widthProperty().divide(2.2)); // 2.2 to account for padding
                    imageView.fitHeightProperty().bind(gridPhoto.heightProperty().divide(3.2)); // 3.2 to account for padding
                    imageView.setPreserveRatio(true);

                    GridPane.setMargin(imageView, new Insets(5));

                    imageView.setOnMouseClicked(event -> handleImageClick(photo));

                    GridPane.setHalignment(imageView, HPos.CENTER);
                    GridPane.setValignment(imageView, VPos.CENTER);
                    GridPane.setFillHeight(imageView, true);
                    GridPane.setFillWidth(imageView, true);

                    gridPhoto.add(imageView, column, row);
                } else {
                    Label tempLabel = new Label("Image not found");
                    tempLabel.getStylesheets().add("/css/general.css");
                    tempLabel.getStyleClass().add("label-image");

                    GridPane.setHalignment(tempLabel, HPos.CENTER);
                    GridPane.setValignment(tempLabel, VPos.CENTER);
                    GridPane.setFillHeight(tempLabel, true);
                    GridPane.setFillWidth(tempLabel, true);

                    gridPhoto.add(tempLabel, column, row);
                }

                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

private void updateImageSizes() {
    for (Node node : gridPhoto.getChildren()) {
        if (node instanceof ImageView) {
            ImageView imageView = (ImageView) node;
            imageView.fitWidthProperty().bind(gridPhoto.widthProperty().divide(2.2));
            imageView.fitHeightProperty().bind(gridPhoto.heightProperty().divide(3.2));
        }
    }
}
private void handleImageClick(Photo photo) {
    try {
        System.out.println("You have clicked on an image.");
        //TODO implement this.
        //Navigator.getInstance().goTo(View.IMG_VIEW, photo);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void setOrderNumber(String orderNumber) {
        SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
        txtOrderNumber.setText(orderNumber);
        this.orderNumber = orderNumber;
    }

    public void handleCamera(ActionEvent actionEvent) {
        String orderNumber = txtOrderNumber.getText();

        Navigator.getInstance().goTo(View.CAMERA, controller -> {
            if (controller instanceof CameraController) {
                ((CameraController) controller).setOrderNumber(orderNumber);
            }
        });

    }

    public void handleReturn(ActionEvent actionEvent) {
        String orderNumber = SessionManager.getInstance().getCurrentOrderNumber();
        if (orderNumber == null || orderNumber.isEmpty()) {
            AlertHelper.showAlert("Error", "No order number available", Alert.AlertType.ERROR);
            return;
        }
        try {
            Navigator.getInstance().goTo(View.ORDER, controller -> {
                if (controller instanceof SendViewController) {
                    ((SendViewController) controller).setOrderNumber(orderNumber);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load OrderView", Alert.AlertType.ERROR);
        }


    }

}