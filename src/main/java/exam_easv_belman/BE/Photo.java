package exam_easv_belman.BE;

//class is primarily used to store filepath and metadata

import java.time.LocalDateTime;

public class Photo {
    int id;
    String orderNumber;
    String filepath;
    int uploadedBy;
    LocalDateTime uploadTime;

    public Photo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString()
    {
        return "Photo [id=" + id + ", orderNumber=" + orderNumber + ", filepath=" + filepath + ", uploadedBy=" + uploadedBy + ", uploadTime=" + uploadTime + "]";
    }
}
