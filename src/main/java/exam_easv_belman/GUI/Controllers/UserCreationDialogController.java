package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserCreationDialogController implements Initializable {


    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<Role> cmbRole;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnCloseWindow;

    private User result;



    //TODO make window draggable.

    public User getResult() {
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbRole.getItems().addAll(Role.values());
    }

    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        result = null;
        close();
    }

    @FXML
    private void handleCreate(ActionEvent actionEvent) {

        //TODO input validation

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        Role role = cmbRole.getValue();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        result = new User(username, password, role, firstName, lastName, email, phone);
        close();

    }

    //TODO implement this.
    //private boolean validateFields()

    @FXML
    private void handleCloseWindow(ActionEvent actionEvent) {
        result = null;
        close();
    }

    private void close() {
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }
}
