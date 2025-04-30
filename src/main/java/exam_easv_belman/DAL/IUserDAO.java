package exam_easv_belman.DAL;

import exam_easv_belman.BE.User;

import java.util.List;

public interface IUserDAO {

    User findByUsername(String username);

    User createUser(User user);

    void deleteUser(User user);

    List<User> getAllUsers();
}
