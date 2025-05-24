package exam_easv_belman.BLL.util;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.Paragraph;

import javafx.stage.Stage;

import java.util.List;

public class PdfGeneratorUtil {

    public static void generatePdf(String filePath,
                                   String email,
                                   String comment,
                                   String orderNumber,
                                   Boolean delete,
                                   Stage mainStage,
                                   List<String> headers,
                                   List<String> photoPaths,
                                   String qcName,
                                   String qcSignaturePath,
                                   String opName,
                                   List<String> imageComments
    ) throws Exception {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PageSize.A4);
        document.setMargins(20, 20, 20, 20);

        // Add Header Section (QC and OP Details)
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Title
        Paragraph title = new Paragraph("Quality Control Report")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);
        // Spacer
        document.add(new Paragraph(" "));

        // OrderNumber
        document.add(new Paragraph("Order Number: " + orderNumber)
                .setFont(font)
                .setFontSize(12));

        // QC Name
        document.add(new Paragraph("Prepared by (QC): " + qcName)
                .setFont(font)
                .setFontSize(12));

        // OP Name
        document.add(new Paragraph("Operator Name: " + opName)
                .setFont(font)
                .setFontSize(12));

        // Add QC Signature
        if (qcSignaturePath != null && !qcSignaturePath.isEmpty()) {
            ImageData signatureImageData = ImageDataFactory.create(qcSignaturePath);
            Image signatureImage = new Image(signatureImageData)
                    .scaleToFit(100, 50) // Resizing the signature
                    .setMarginTop(10)
                    .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.LEFT);
            document.add(signatureImage);
        }
        // Spacer
        document.add(new Paragraph(" "));

        //Add Comment Section
        document.add(new Paragraph("General Comments: " + comment)
                .setFont(font)
                .setFontSize(12)
                .setItalic());
        // Spacer
        document.add(new Paragraph(" "));

       // Add Photos and Comments
        document.add(new Paragraph("Images and Comments:")
                .setFont(boldFont)
                .setFontSize(14)
                .setUnderline()
                .setMarginBottom(10));

        if (photoPaths != null && !photoPaths.isEmpty()) {
            for (int i = 0; i < photoPaths.size(); i++) {
                // Add Photo
                ImageData imgData = ImageDataFactory.create(photoPaths.get(i));
                Image img = new Image(imgData)
                        .scaleToFit(400, 300) // Adjust photo size
                        .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
                document.add(img);

                // Add Comment for this Image
                String imageComment = (imageComments != null && i < imageComments.size() && imageComments.get(i) != null && !imageComments.get(i).isEmpty())
                        ? imageComments.get(i)
                        : "No added comments.";
                Paragraph commentParagraph = new Paragraph("Comment: " + imageComment)
                        .setFont(font)
                        .setFontSize(10)
                        .setMarginBottom(20);
                document.add(commentParagraph);
            }
        } else {
            document.add(new Paragraph("No valid photos were provided for this report.")
                    .setFont(font)
                    .setFontSize(12)
                    .setItalic());
        }


        document.add(new Paragraph("Report generated for: " + email)
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(20));


        document.close();


        /*
        // Display the generated PDF in a preview
        javafx.application.Platform.runLater(() -> {
            try {
                PdfPreviewUtil.showPdfPreview(file, mainStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

         */


            /*
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);

                if (delete) {
                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.schedule(() -> {
                        try {
                            if (file.exists()) {
                                boolean deleted = file.delete();
                                System.out.println("File deleted: " + deleted);
                                if (!deleted) {
                                    System.out.println("Failed to delete the file: " + filePath);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error deleting file: " + e.getMessage());
                        } finally {
                            executorService.shutdown();
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }


        } else {
            throw new Exception("Logo file not found");
        }
        */
        }
    }
