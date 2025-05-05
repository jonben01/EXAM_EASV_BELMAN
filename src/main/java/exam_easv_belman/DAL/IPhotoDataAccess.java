package exam_easv_belman.DAL;

import exam_easv_belman.BE.User;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IPhotoDataAccess {

    //TODO add the mandatory methods that have been implemented in PhotoDAO


    void insertImagePathToDatabase(Connection connection, List<Path> filePath, User uploader, String orderID) throws SQLException;

    ObservableList<Image> getImagesFromDatabase();

    void deleteImageFromDatabase();

    boolean saveImageAndPath(List<BufferedImage> photos, List<String> fileNames, User uploader, String orderID) throws Exception;

}
