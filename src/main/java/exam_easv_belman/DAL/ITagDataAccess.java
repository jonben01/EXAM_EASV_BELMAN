package exam_easv_belman.DAL;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Tag;
import java.sql.SQLException;


public interface ITagDataAccess {

    void addTagToPhoto(Photo photo, Tag tag) throws SQLException;

}
