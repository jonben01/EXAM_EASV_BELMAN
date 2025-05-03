package exam_easv_belman.GUI.util;

import javafx.scene.control.Alert;

public class AlertHelper {

    private AlertHelper(){

    }

    public static void showAlert(String title, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
