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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.element.Paragraph;

import javafx.stage.Stage;
import java.io.IOException;

import java.awt.Desktop;
import java.io.File;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PdfGeneratorUtil {

    public static void generatePdf(String filePath, String email, String comment, String orderNumber, Boolean delete, Stage mainStage) throws Exception {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Fonts
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Logo and Event Details
        Table topSectionTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        topSectionTable.setMarginTop(10);

        File logoFile = new File("src/main/resources/images/BELMAN_Logo_264pxl.png");
        if (logoFile.exists()) {
            ImageData logoImageData = ImageDataFactory.create(logoFile.getAbsolutePath());
            Image logoImage = new Image(logoImageData).setWidth(264).setHeight(264);
            topSectionTable.addCell(new Cell().add(logoImage).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));

            Cell textCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
            textCell.add(new Paragraph(email).setFont(normal).setTextAlignment(TextAlignment.LEFT));
            textCell.add(new Paragraph(orderNumber).setFont(normal).setTextAlignment(TextAlignment.LEFT));
            textCell.add(new Paragraph(comment).setFont(normal).setTextAlignment(TextAlignment.LEFT));

            topSectionTable.addCell(textCell);
            
            document.add(topSectionTable);
            document.close();


            javafx.application.Platform.runLater(() -> {
                try {
                    PdfPreviewUtil.showPdfPreview(file, mainStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

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
}