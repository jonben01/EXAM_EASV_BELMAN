package exam_easv_belman.BLL;

import exam_easv_belman.BE.User;
import exam_easv_belman.DAL.UserDAO;

public class UserManager {

    private UserDAO userDAO;

    public UserManager() {
        userDAO = new UserDAO();
    }

    public User authenticateUser(String username, char[] rawPassword) throws Exception {

        //1. get the user from DAO, throw an exception if user doesnt exist
        //TODO implement the called method
        User user = userDAO.findByUsername(username);
        if (user == null) {
            //TODO make a custom InvalidCredentialException, that ideally comes with a default message,
            // so i dont have to write a message everytime its used(which is exactly once....)
            throw new Exception();
        }


        //TODO implement this
        //2. verify the password using pbkdf2 util class

        //TODO write: return user;
        //3. return the user now that its been found and the password has been verified.

        return new User(); //TODO delete this
    }
}
