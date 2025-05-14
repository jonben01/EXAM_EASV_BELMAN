package exam_easv_belman.GUI.Models;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Tag;
import exam_easv_belman.BLL.TagManager;
import exam_easv_belman.BLL.UserManager;

import java.sql.SQLException;
import java.util.List;

public class TagModel {

    private TagManager tagManager;

    public TagModel() throws Exception {
        tagManager = new TagManager();
    }

    public List<Tag> getTagsForPhoto(Photo photo) throws Exception {
        return tagManager.getTagsForPhoto(photo);
    }


    public List<Tag> getAllTags() throws Exception {
        return tagManager.getAllTags();
    }

    public void removeTagFromPhoto(Photo photo, Tag tag) throws Exception {
        tagManager.removeTagFromPhoto(photo, tag);

    }
}
