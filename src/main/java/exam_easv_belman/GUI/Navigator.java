package exam_easv_belman.GUI;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.GUI.Controllers.ImageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * The Navigator class is responsible for managing navigation between different views in the application.
 * It implements the Singleton design pattern to ensure only one instance is created and used throughout
 * the application lifecycle. This class initializes the application's primary stage and provides methods
 * to navigate to specific views defined by the View enum.
 */
public class Navigator {

    private static Navigator instance;
    private Stage stage;
    private Object currentController;


    private Navigator() {
    }

    public static Navigator getInstance() {
        //no need for double-checked locking due to only being accessed by javafx application thread
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    /**
     * Initializes the primary stage for the application and navigates to the initial view.
     * This method is responsible for setting up the primary stage and displaying it.
     *
     * @param primaryStage the primary stage of the application, provided by the JavaFX framework
     */
    public void init(Stage primaryStage) {
        stage = primaryStage;
        goTo(View.LOGIN);
        stage.show();
    }

    /**
     * Navigates to the specified view based on the provided View enum constant.
     * This method loads the corresponding FXML file for the view, sets it as the current scene
     * on the primary stage, and ensures the application's navigation flow transitions smoothly.
     *
     * @param view the target view to navigate to, specified as a View enum constant
     */
    public void goTo(View view) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Navigator.class.getResource(view.getFXML())));
            Parent root = loader.load();

            stage.setScene(new Scene(root));

            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO AlertClass.alert SOMETHING or rather send the exception up as an Exception or custom one
        }
    }
    public void goTo(View view, Photo photo) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Navigator.class.getResource(view.getFXML())));
            Parent root = loader.load();
            currentController = loader.getController();

            if(currentController instanceof ImageController){
                ((ImageController) currentController).setImage(photo);
            }

            stage.setMinHeight(460);
            stage.setMinWidth(754);
            stage.setScene(new Scene(root));

            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO AlertClass.alert SOMETHING or rather send the exception up as an Exception or custom one
        }
    }

    /**
     * Navigates to the specified view and allows setting the controller with custom data.
     *
     * @param view               The target view to navigate to.
     * @param controllerConsumer A function that will be applied to the controller, allowing parameters to be set.
     */
    public void goTo(View view, Consumer<Object> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(view.getFXML())));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();

            // Get the controller and apply the provided configuration
            currentController = loader.getController();
            if (controllerConsumer != null && currentController != null) {
                controllerConsumer.accept(currentController);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setRoot(View view, Consumer<Object> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Navigator.class.getResource(view.getFXML())));
            Parent root = loader.load();

            if (stage.getScene() == null) {
                stage.setScene(new Scene(root));
            } else {
                stage.getScene().setRoot(root);
            }

            currentController = loader.getController();
            if (controllerConsumer != null && currentController != null) {
                controllerConsumer.accept(currentController);
            }

            stage.centerOnScreen();
        } catch (IOException e) {
            //TODO exception or alert
        }
    }

    public Object showModal(View view) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Navigator.class.getResource(view.getFXML())));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("User Creation");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            //TODO AlertClass.alert SOMETHING or rather send the exception up as an Exception or custom one
            return null;
        }
    }

    public Object getCurrentController() {
        return currentController;
    }
}