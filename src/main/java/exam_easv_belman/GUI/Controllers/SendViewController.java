package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.View;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class SendViewController {

    public void handleReturn(ActionEvent actionEvent) {
        try{
            Navigator.getInstance().goTo(View.QCView);
        }catch(Exception e){
            e.printStackTrace();
            AlertHelper.showAlert("Error", "Failed to load OrderView", Alert.AlertType.ERROR);
        }
    }

    public void handlePreview(ActionEvent actionEvent) {
        //Todo preview report

    }

    public void handleSend(ActionEvent actionEvent) {
        //Todo send report using gmailer class like previous project
    }
}
