package exam_easv_belman.BLL;

import javafx.scene.image.Image;

public interface PhotoStrategy {

    void start() throws Exception;

    void stop() throws Exception;

    Image takePhoto() throws Exception;
}
