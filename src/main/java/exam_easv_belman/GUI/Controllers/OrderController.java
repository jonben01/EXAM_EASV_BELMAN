package exam_easv_belman.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class OrderController {

    @FXML
    private TextField OrderNumber;

    //TODO take orders from db not just these hardcoded values
    private final List<String> orders = Arrays.asList("1001", "1002", "1003");


    @FXML
    private void handleSearch(ActionEvent event) {
        String inputOrderNumber = OrderNumber.getText();

        if (inputOrderNumber.isEmpty()) {
            showAlert("Error", "Please enter an order number", Alert.AlertType.ERROR);
        }

        if (!orders.contains(inputOrderNumber)) {
            showAlert("Error", "The Order Number Entered Does Not Exist", Alert.AlertType.ERROR);
        } else {
            GoToPhotoDocView(event, inputOrderNumber);
        }
    }

    private void GoToPhotoDocView(ActionEvent event, String orderNumber){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PhotoDocView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());

            PhotoDocController controller = loader.getController();
            controller.setOrderNumber(orderNumber);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            showAlert("Error", "Failed to load PhotoDocView", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType alerttype){
        Alert alert = new Alert(alerttype);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
