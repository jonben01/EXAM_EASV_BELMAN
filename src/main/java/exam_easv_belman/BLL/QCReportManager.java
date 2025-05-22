package exam_easv_belman.BLL;

import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.User;
import exam_easv_belman.BLL.util.PdfGeneratorUtil;
import exam_easv_belman.GUI.SessionManager;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QCReportManager {
    private PhotoManager photoManager;

    public QCReportManager() throws Exception {
        this.photoManager = new PhotoManager();
    }

    /**
     * Generates a QC Report PDF based on the session details (current user, order, and product).
     *
     * @param outputFilePath The file path where the PDF will be saved.
     * @param email          The email to include in the PDF metadata or to send the report.
     * @param comment        Additional comments to include in the report.
     * @param mainStage      The main application stage, used for displaying dialogs or file previews.
     * @throws Exception If an error occurs during the generation process.
     */
    public void generateQCReportPDF(String outputFilePath, String email, String comment, Stage mainStage) throws Exception {
        // Fetch session details
        SessionManager sessionManager = SessionManager.getInstance();
        String orderNumber = sessionManager.getCurrentOrderNumber();
        boolean isProduct = sessionManager.getIsProduct();
        String productNumber = sessionManager.getCurrentProductNumber();

        // Gather QC details
        User qcUser = sessionManager.getCurrentUser();
        String qcName = qcUser.getFirstName() + " " + qcUser.getLastName();
        String qcSignaturePath = qcUser.getSignaturePath();

        // Retrieve photos and group by product number
        ObservableList<Photo> photos = isProduct
                ? photoManager.getImagesForProduct(productNumber)
                : photoManager.getImagesForOrder(orderNumber);
        Map<String, List<Photo>> groupedPhotos = photos.stream()
                .collect(Collectors.groupingBy(photo -> extractProductNumber(photo.getOrderNumber())));

        // Prepare headers, photo paths, and comments
        List<String> headers = new ArrayList<>();
        List<String> photoPaths = new ArrayList<>();
        List<String> imageComments = new ArrayList<>();

        for (Map.Entry<String, List<Photo>> entry : groupedPhotos.entrySet()) {
            String productNumberHeader = entry.getKey(); // Header like "001", "002"
            List<Photo> productPhotos = entry.getValue();

            headers.add("Product: " + productNumberHeader); // Add product header
            photoPaths.addAll(productPhotos.stream().map(Photo::getFilepath).collect(Collectors.toList()));
            imageComments.addAll(productPhotos.stream().map(Photo::getComment).collect(Collectors.toList()));
        }

        // Resolve operator names
        UserManager userManager = new UserManager();
        List<String> operatorNames = photos.stream()
                .map(photo -> {
                    try {
                        return userManager.getAllUsers().stream()
                                .filter(user -> user.getId() == photo.getUploadedBy())
                                .findFirst()
                                .map(uploader -> uploader.getFirstName() + " " + uploader.getLastName())
                                .orElse("Unknown Operator");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Unknown Operator";
                    }
                })
                .distinct()
                .collect(Collectors.toList());
        String opName = String.join(", ", operatorNames);

        // Generate the PDF with headers
        PdfGeneratorUtil.generatePdf(
                outputFilePath,
                email,
                comment,
                isProduct ? productNumber : orderNumber,
                false,              // Do not delete after preview
                mainStage,          // Stage instance
                headers,            // Product headers
                photoPaths,         // Photo paths
                qcName,             // QC name
                qcSignaturePath,    // QC signature path
                opName,             // Operator names
                imageComments       // Image comments
        );


    }
    private String extractProductNumber(String orderNumber) {
        try {
            String[] parts = orderNumber.split("-");
            return parts[parts.length - 1]; // Product number is the final part
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }


}

