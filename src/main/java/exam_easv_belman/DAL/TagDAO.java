package exam_easv_belman.DAL;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Tag;
import io.opencensus.tags.Tags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TagDAO implements ITagDataAccess {

    private DBConnector dbConnector;

    public TagDAO() throws Exception {
        dbConnector = new DBConnector();
    }

    public Tag createTag(Tag tag) throws Exception{
        String sql = "INSERT INTO Tags (name) VALUES (?)";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tag.getName());

            statement.executeUpdate();

            return tag;

        } catch (Exception e) {
            throw new Exception(e);
        }

    }


    public List<Tag> getAllTags() throws SQLException {
        String sql = "SELECT * FROM Tags";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            ObservableList<Tag> tags = FXCollections.observableArrayList();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                tags.add(new Tag(id, name));
            }
            return tags;
        }catch (SQLException e){
            throw new SQLException(e);
        }

    }

    @Override
    public void addTagToPhoto(Photo photo, Tag tag) throws SQLException {
        String sql = "INSERT INTO PhotoTagJunction (photo_id, tag_id) VALUES (?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, photo.getId());
            statement.setInt(2, tag.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to add tag to photo",e);
        }
    }


    public List<Tag> getTagsForPhoto(Photo photo) throws SQLException{
        String sql = "SELECT t.id, t.name " +
                "FROM Tags t " +
                "JOIN PhotoTagJunction ptj ON t.id = ptj.tag_id " +
                "WHERE ptj.photo_id = ?";

        List<Tag> tags = FXCollections.observableArrayList();
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, photo.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                tags.add(new Tag(id, name));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to get tags due to database error",e);
            }
        return tags;
    }
}
