package exam_easv_belman.BLL;

import exam_easv_belman.BLL.exceptions.CameraNotFoundException;
import javafx.scene.image.Image;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayInputStream;

public class OpenCVStrategy implements PhotoStrategy {

    private VideoCapture camera;
    private int width = 1280;
    private int height = 720;

    @Override
    public void start() throws CameraNotFoundException {
        OpenCV.loadLocally();

        camera = new VideoCapture(0);
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, width);
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, height);

        if (!camera.isOpened()) {
            throw new CameraNotFoundException();
            //TODO custom exception and alert the user in the gui layer, say CAMERA NOT FOUND or smth
        }
    }

    @Override
    public void stop(){
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    public Image takePhoto() throws Exception {
        Mat frame = new Mat();
        camera.read(frame);
        if (frame.empty()) {
            throw new Exception("Frame empty");
            //TODO custom exception and alert the user in the gui layer, say BLANK FRAME or smth
        }

        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Mat grabRawMat() {
        Mat frame = new Mat();
        camera.read(frame);
        return frame;
    }

    @Override
    public Image convertToImage(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }
}
