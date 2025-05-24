package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import exam_easv_belman.GUI.Models.UserModel;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private UserModel userModel;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogin;

    public LoginController() {
        try {
            userModel = new UserModel();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO alert
        }
    }


    @FXML
    private void handleLogin(ActionEvent actionEvent) {
        login();
    }


    private void login() {
        String username = txtUsername.getText().trim();
        String rawPassword = txtPassword.getText().trim();

        try {
            User user = userModel.authenticateUser(username, rawPassword);
            SessionManager.getInstance().setCurrentUser(user);

            //Navigator.getInstance().goTo(user.getRole() == Role.ADMIN ? View.ADMIN : View.ORDER);
            Navigator.getInstance().goTo(View.ORDER);

            //TODO implement some sort of visual indicator when something fails, red outline or maybe a text (not popup

        } catch (Exception e) {
            e.printStackTrace();
            //TODO exception handling
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enterKeyListeners();
    }

    private void enterKeyListeners() {
        txtUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        txtPassword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        //TODO remember this when password field is added
        /*
        txtPasswordVisible.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                login();
            }
        });

         */
    }

}
