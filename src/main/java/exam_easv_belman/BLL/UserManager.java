package exam_easv_belman.BLL;

import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import exam_easv_belman.BLL.util.PBKDF2PasswordUtil;
import exam_easv_belman.DAL.UserDAO;
import exam_easv_belman.GUI.util.AlertHelper;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class UserManager {

    private UserDAO userDAO;

    public UserManager() throws Exception {
        userDAO = new UserDAO();
    }

    public User authenticateUser(String username, String rawPassword) throws Exception {

        //1. get the user from DAO, throw an exception if user doesnt exist
        //TODO implement the called method
        User user = userDAO.findByUsername(username);
        if (user == null || !PBKDF2PasswordUtil.verifyPassword(rawPassword, user.getPassword())) {
            //TODO make a custom InvalidCredentialException, that ideally comes with a default message,
            // so i dont have to write a message everytime its used(which is exactly once....)
            // then pass that exception up the stack. to alert the user.
            throw new Exception();
        }

        return user;
    }

    public User authenticateUser(String QRKey) throws Exception {
        List<User> allUsers = userDAO.getAllUsers();
        for(User user : allUsers) {
            if(user.getQrKey() != null) {
                if (PBKDF2PasswordUtil.verifyPassword(QRKey, user.getQrKey())) {
                    return user;
                }
            }
        }
        AlertHelper.showAlert("Error", "Invalid QR Key", Alert.AlertType.ERROR);
        return null;
    }

    public User createUser(User user) throws Exception {
        String rawPwd = user.getPassword();
        String hashedPwd = PBKDF2PasswordUtil.hashPassword(rawPwd);
        user.setPassword(hashedPwd);

        String rawKey = user.getQrKey();
        String hashedKey = PBKDF2PasswordUtil.hashPassword(rawKey);
        user.setQrKey(hashedKey);
        return userDAO.createUser(user);
    }

    public ObservableList<User> getAllUsers() throws Exception {
         return userDAO.getAllUsers();
    }

    public void deleteUser(User user) {
        userDAO.deleteUser(user);
    }
}
