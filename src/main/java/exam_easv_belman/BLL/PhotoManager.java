package exam_easv_belman.BLL;

import exam_easv_belman.BE.User;
import exam_easv_belman.DAL.IPhotoDataAccess;
import exam_easv_belman.DAL.PhotoDAO;

import java.awt.image.BufferedImage;
import java.util.List;

public class PhotoManager {
    private IPhotoDataAccess photoDataAccess;

    public PhotoManager() throws Exception {
        try {
            photoDataAccess = new PhotoDAO();
        } catch (Exception e) {
            throw new Exception();
            //TODO exception handling
        }
    }

    public boolean saveImageAndPath(List<BufferedImage> images,
                                    List<String> fileNames,
                                    User uploader,
                                    String orderID) throws Exception {
        return photoDataAccess.saveImageAndPath(images, fileNames, uploader, orderID);
    }
}
