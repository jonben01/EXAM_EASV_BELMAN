package exam_easv_belman.BLL;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Tag;
import exam_easv_belman.DAL.TagDAO;

import java.sql.SQLException;
import java.util.List;

public class TagManager {

    private TagDAO tagDao;

    public TagManager() throws Exception {
        tagDao = new TagDAO();
    }

    public Tag createTag(Tag tag) throws Exception {
        return tagDao.createTag(tag);
    }

    public List<Tag> getAllTags() throws Exception {
        return tagDao.getAllTags();
    }

    public List<Tag> getTagsForPhoto(Photo photo) throws Exception {
        return tagDao.getTagsForPhoto(photo); // DAL call
    }

    public void addTagToPhoto(Photo photo, Tag tag) throws Exception {
        tagDao.addTagToPhoto(photo, tag); // DAL call
    }


    public void removeTagFromPhoto(Photo photo, Tag tag) throws Exception {
        tagDao.removeTagFromPhoto(photo, tag);
    }
}
