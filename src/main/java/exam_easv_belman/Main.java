package exam_easv_belman;

import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Navigator.getInstance().init(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //implemented due to openCV keeping the JVM alive even when closing the application.
        System.exit(0);
    }
}