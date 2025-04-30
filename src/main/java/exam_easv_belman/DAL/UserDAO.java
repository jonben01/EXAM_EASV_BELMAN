package exam_easv_belman.DAL;

import exam_easv_belman.BE.User;

import java.util.List;

public class UserDAO implements IUserDAO {
    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }
}
