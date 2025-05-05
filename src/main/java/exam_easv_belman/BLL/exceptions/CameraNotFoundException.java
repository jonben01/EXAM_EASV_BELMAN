package exam_easv_belman.BLL.exceptions;

public class CameraNotFoundException extends Exception {
    public CameraNotFoundException() {
        super("No camera found");
    }
}
