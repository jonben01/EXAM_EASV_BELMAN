package exam_easv_belman.GUI.Models;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.Tag;
import exam_easv_belman.BE.User;
import exam_easv_belman.BLL.PhotoManager;
import exam_easv_belman.BLL.TagManager;
import javafx.collections.ObservableList;

import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.List;

public class PhotoModel {
    private PhotoManager photoManager;
    private TagManager tagManager;


    public PhotoModel() {
        try {
            photoManager = new PhotoManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveImageAndPath(List<BufferedImage> images,
                                    List<String> fileNames,
                                    User uploader,
                                    String productNumber) throws Exception {
        return photoManager.saveImageAndPath(images, fileNames, uploader, productNumber);
    }
    public ObservableList<Photo> getImagesForOrder(String orderNumber) throws Exception {
        return photoManager.getImagesForOrder(orderNumber);
    }
    public ObservableList<Photo> getImagesForProduct(String productNumber) throws Exception {
        return photoManager.getImagesForProduct(productNumber);
    }

    public void deleteImage(Photo photo) throws SQLException {
        photoManager.deleteImage(photo);
    }

    public void addCommentToPhoto(String comment, Photo photo)
    {
        photoManager.addCommentToPhoto(comment, photo);
    }

    public List<Tag> getTagsForPhoto(Photo photo) throws Exception {
        return tagManager.getTagsForPhoto(photo);
    }

    public void addTagToPhoto(Photo photo, Tag tag) throws Exception {
        tagManager.addTagToPhoto(photo, tag);
    }
}
