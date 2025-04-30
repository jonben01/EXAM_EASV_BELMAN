package exam_easv_belman.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The Navigator class is responsible for managing navigation between different views in the application.
 * It implements the Singleton design pattern to ensure only one instance is created and used throughout
 * the application lifecycle. This class initializes the application's primary stage and provides methods
 * to navigate to specific views defined by the View enum.
 */
public class Navigator {

    private static Navigator instance;
    private Stage stage;

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(Navigator.class.getResource(view.getFXML())));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            //TODO AlertClass.alert SOMETHING or rather send the exception up as an Exception or custom one
        }
    }

}
