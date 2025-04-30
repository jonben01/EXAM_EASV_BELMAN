package exam_easv_belman.GUI.Models;

import exam_easv_belman.BE.User;
import exam_easv_belman.BLL.UserManager;

public class UserModel {

    private UserManager userManager;

    public UserModel() {
        userManager = new UserManager();
    }

    public User authenticateUser(String username, char[] rawPassword) throws Exception {
        return userManager.authenticateUser(username, rawPassword);
    }
}
