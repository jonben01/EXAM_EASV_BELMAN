package exam_easv_belman.GUI.Models;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.User;
import exam_easv_belman.BLL.PhotoManager;
import javafx.collections.ObservableList;

import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.List;

public class PhotoModel {
    private PhotoManager photoManager;

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
                                    String orderID) throws Exception {
        return photoManager.saveImageAndPath(images, fileNames, uploader, orderID);
    }
    public ObservableList<Photo> getImagesFromDatabase(String orderNumber) throws Exception {
        return photoManager.getImagesFromDatabase(orderNumber);
    }

    public void deleteImage(Photo photo) throws SQLException {
        photoManager.deleteImage(photo);
    }
}
