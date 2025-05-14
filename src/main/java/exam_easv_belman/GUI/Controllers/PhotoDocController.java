package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Objects;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.Node;

public class PhotoDocController {
    @FXML
    public Button btnOpenCamera;
    @FXML
    private Text txtOrderNumber; // THIS CAN ALSO HOLD THE PRODUCT NUMBER, IF USER CHOOSES SO
    @FXML
    private Button btnPrev;

    private String orderNumber;


    private boolean isProduct;
    @FXML
    private GridPane gridPhoto;

    private PhotoModel photoModel;
    @FXML
    private Button btnQC;

    private static final int MAX_PHOTOS = 6;

    private ObservableList<Photo> imagesFromDatabase;
    @FXML
    private Pagination pagination;
    @FXML
    private StackPane photoGridContainer;
    @FXML
    private MenuButton btnProduct;


    @FXML
    private void initialize() throws Exception {
        orderNumber = SessionManager.getInstance().getCurrentOrderNumber();
        photoModel = new PhotoModel();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon-log.png")));
        ImageView imgView = new ImageView(img);
        btnPrev.setGraphic(imgView);

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            btnQC.setVisible(true);
        } else {
            btnQC.setVisible(false);
        }
    }

private Node fillPhotoGrid(int pageIndex) {
    gridPhoto.getChildren().clear();
    if (!isProduct) {
        Label noImagesLabel = new Label("Switch to a product to see images");
        noImagesLabel.getStylesheets().add("/css/general.css");
        noImagesLabel.getStyleClass().add("label-image");
        noImagesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666666;");

        // Center the label in the GridPane
        GridPane.setHalignment(noImagesLabel, HPos.CENTER);
        GridPane.setValignment(noImagesLabel, VPos.CENTER);
        GridPane.setColumnSpan(noImagesLabel, 2);
        GridPane.setRowSpan(noImagesLabel, 3);

        gridPhoto.add(noImagesLabel, 0, 0);
        return photoGridContainer;
    }
    if(imagesFromDatabase.size() == 0)
    {
        Label noImagesLabel = new Label("This product has no images yet");
        noImagesLabel.getStylesheets().add("/css/general.css");
        noImagesLabel.getStyleClass().add("label-image");
        noImagesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666666;");

        // Center the label in the GridPane
        GridPane.setHalignment(noImagesLabel, HPos.CENTER);
        GridPane.setValignment(noImagesLabel, VPos.CENTER);
        GridPane.setColumnSpan(noImagesLabel, 2);
        GridPane.setRowSpan(noImagesLabel, 3);

        gridPhoto.add(noImagesLabel, 0, 0);
        return photoGridContainer;
    }

    int startIndex = pageIndex * MAX_PHOTOS;
    int endIndex = Math.min(startIndex + MAX_PHOTOS, imagesFromDatabase.size());
    int column = 0;
    int row = 0;

    gridPhoto.widthProperty().addListener((obs, oldVal, newVal) -> updateImageSizes());
    gridPhoto.heightProperty().addListener((obs, oldVal, newVal) -> updateImageSizes());
    for (int i = startIndex; i < endIndex && i < imagesFromDatabase.size(); i++) {
        Photo photo = imagesFromDatabase.get(i);
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
    return photoGridContainer;
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
        System.out.println("handleImageClick triggered with photo: " + photo);
        Navigator.getInstance().setRoot(View.IMG_VIEW, controller -> {
            System.out.println(controller);
            if (controller instanceof ImageController) {
                ImageController imageController = (ImageController) controller;

                System.out.println("Controller is an instance of ImageController.");
                imageController.setImage(photo);  // Ensure this method works
                imageController.setPhoto(photo); // Set additional photo data
            }
        });
    }
    catch (Exception e) {
        e.printStackTrace();
        AlertHelper.showAlert("Error", "Failed to load PhotoDocView", Alert.AlertType.ERROR);
    }
    }

    public void setOrderNumber(String orderNumber) throws Exception {
        SessionManager.getInstance().setCurrentOrderNumber(orderNumber);
        txtOrderNumber.setText(orderNumber + "-");
        this.orderNumber = orderNumber;
        isProduct = SessionManager.getInstance().getIsProduct();
        if(isProduct)
        {
            String productNumber = SessionManager.getInstance().getCurrentProductNumber();
            String productIdentifier = productNumber.substring(productNumber.lastIndexOf("-")+1);
            btnProduct.setText(productIdentifier);
            imagesFromDatabase = photoModel.getImagesForProduct(SessionManager.getInstance().getCurrentProductNumber());
            int pageCount = (int) Math.ceil((double) imagesFromDatabase.size() / MAX_PHOTOS);
            pagination.setPageCount(pageCount);
            pagination.setPageFactory(this::fillPhotoGrid);
        }
        else
        {
            btnOpenCamera.setDisable(true);
            int pageCount = 1;
            pagination.setPageCount(pageCount);
            pagination.setPageFactory(this::fillPhotoGrid);
        }

    }

    public void handleCamera(ActionEvent actionEvent) {
        Navigator.getInstance().goTo(View.CAMERA);
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
                    try {
                        ((SendViewController) controller).setOrderNumber(orderNumber);
                    } catch (SQLException e) {
                        AlertHelper.showAlert("Error", "Failed to load OrderView", Alert.AlertType.ERROR);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load OrderView", Alert.AlertType.ERROR);
        }


    }

    public void handleQC(ActionEvent actionEvent){
        try {
            Navigator.getInstance().goTo(View.QCView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}