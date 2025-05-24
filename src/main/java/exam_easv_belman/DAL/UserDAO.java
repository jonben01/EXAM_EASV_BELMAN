package exam_easv_belman.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import exam_easv_belman.BE.Role;
import exam_easv_belman.BE.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDataAccess {

    private DBConnector dbConnector;

    public UserDAO() throws Exception {
        dbConnector = new DBConnector();
    }

    @Override
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM dbo.Users WHERE username = ?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password_hash"));
                user.setRole(resultSet.getInt("role_id") == 1 ? Role.ADMIN : resultSet.getInt("role_id") == 2 ? Role.OPERATOR : Role.QC);
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phone"));

                return user;
            }

        } catch (SQLException e) {
            throw new Exception();
        }
        return null;
    }

    @Override
    public User createUser(User user) throws Exception {

        String sql = "INSERT INTO Users (username, password_hash, role_id, first_name, last_name, email, phone) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            //ROLE IDS ARE ADMIN = 1, OPERATOR = 2, QC = 3, this is hardcoded.
            //ternary operator used instead of if statements, could alternatively be done with switch statements.
            int roleId = user.getRole() == Role.ADMIN ? 1 : user.getRole() == Role.OPERATOR ? 2 : 3;
            statement.setInt(3, roleId);

            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getPhoneNumber());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }

            return user;

        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public ObservableList<User> getAllUsers() throws Exception {

        String sql = "SELECT * FROM dbo.Users";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            ObservableList<User> users = FXCollections.observableArrayList();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password_hash"));

                //ROLE IDS ARE ADMIN = 1, OPERATOR = 2, QC = 3, this is hardcoded.
                //ternary operator used instead of if statements, could alternatively be done with switch statements.
                int roleId = resultSet.getInt("role_id");
                user.setRole(roleId == 1 ? Role.ADMIN : roleId == 2 ? Role.OPERATOR : Role.QC);

                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phone"));
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    @Override
    public String getPassword() {
        return "";
    }
}
