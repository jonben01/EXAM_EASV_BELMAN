package exam_easv_belman.GUI.Controllers;

import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import exam_easv_belman.GUI.Models.UserModel;
import exam_easv_belman.GUI.Navigator;
import exam_easv_belman.GUI.SessionManager;
import exam_easv_belman.GUI.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    public Button btnLogout;
    @FXML
    public Button btnCreateUser;
    @FXML
    public Button btnDeleteUser;
    @FXML
    public ListView<User> lstUsers;
    @FXML
    public TextField txtUsername;
    @FXML
    public TextField txtPassword;
    @FXML
    public TextField txtSearch;
    @FXML
    public ComboBox<Role> cmbRole;
    @FXML
    public TextField txtFirstName;
    @FXML
    public TextField txtLastName;
    @FXML
    public TextField txtEmail;
    @FXML
    public TextField txtPhone;
    @FXML
    public Label lblCurrentUser;

    private UserModel userModel;

    public AdminController() {
        try {
            userModel = new UserModel();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO alert
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateUserList();
        //lblCurrentUser.setText(SessionManager.getInstance().getCurrentUser().getUsername());
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        Navigator.getInstance().goTo(View.LOGIN);
        SessionManager.getInstance().logout();
    }

    @FXML
    private void handleCreateUser(ActionEvent actionEvent) {
        Object controllerObj = Navigator.getInstance().showModal(View.USER_CREATION_MODAL);

        if (controllerObj instanceof UserCreationDialogController controller) {
            User newUser = controller.getResult();
            if (newUser != null) {
                try {
                    //making a new User object to make sure parity between the persisted data and what will be displayed.
                    User user = userModel.createUser(newUser);
                    lstUsers.getItems().add(user);
                    lstUsers.getSelectionModel().select(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO alert
                }
            }

        }
    }



    @FXML
    public void handleDeleteUser(ActionEvent actionEvent) {
    }



    private void populateUserList() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {

            users = userModel.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO alert
        }

        if (users == null) {
            Label lblNoUsers = new Label("No users found");
            lstUsers.setPlaceholder(lblNoUsers);
        } else {
            users.sort(Comparator.comparing(user -> user.getFirstName().toLowerCase()));
            lstUsers.setItems(users);
            setupCellFactory();
        }
    }

    private void setupCellFactory() {
        lstUsers.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                setText(null);
                setGraphic(null);
                if (empty || item == null) {
                    return;
                }

                //TODO assign style classes in here

                Label nameLabel = new Label(item.getFirstName() + " " + item.getLastName());
                Label roleLabel;
                if (item.getRole() != null) {
                    roleLabel = new Label(item.getRole().toString().toLowerCase());
                } else {
                    roleLabel = new Label("null");
                }

                VBox vBox = new VBox(nameLabel, roleLabel);
                setGraphic(vBox);

            }
        });
    }
}
