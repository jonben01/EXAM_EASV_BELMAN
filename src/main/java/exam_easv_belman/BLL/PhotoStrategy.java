package exam_easv_belman.BLL;

import javafx.scene.image.Image;
import org.opencv.core.Mat;

public interface PhotoStrategy {

    void start() throws Exception;

    void stop() throws Exception;

    Image takePhoto() throws Exception;

    void setSize(int width, int height);

    Mat grabRawMat();

    Image convertToImage(Mat frame);
}
