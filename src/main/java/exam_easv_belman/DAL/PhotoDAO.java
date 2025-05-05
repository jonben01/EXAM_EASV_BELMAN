package exam_easv_belman.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import exam_easv_belman.BE.Photo;
import exam_easv_belman.BE.User;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class PhotoDAO implements IPhotoDataAccess{

    private static final DateTimeFormatter FileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    private DBConnector dbConnector;
    private final Path baseRelativePath = Paths.get("QC_Images");

    public PhotoDAO() throws Exception {
        dbConnector = new DBConnector();

    }


    public boolean saveImageAndPath(List<BufferedImage> photos,
                                    List<String> fileNames,
                                    User uploader,
                                    String orderID) throws Exception {

        //check if the lists are of the same size, if not throw an exception.
        if (photos.size() != fileNames.size()) {
            throw new IllegalArgumentException("Photos and paths must be of same size");
        }
        Connection connection = null;
        List<Path> persistedPaths = new ArrayList<>();

        try {
            connection = dbConnector.getConnection();
            connection.setAutoCommit(false);

            Path orderFolderPath = baseRelativePath.resolve(orderID + "_Images");
            persistedPaths = saveImages(photos, fileNames, orderFolderPath);

            insertImagePathToDatabase(connection, persistedPaths, uploader, orderID);

            connection.commit();
            return true;

        } catch (Exception e) {
            if (connection != null) {
                try {
                    //roll back the transaction if ANYTHING goes wrong.
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    //TODO exception handling
                }
            }
            deleteFiles(persistedPaths);
            throw e;
            //TODO exception handling
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    //TODO exception handling
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Path> saveImages(List<BufferedImage> photos,
                                  List<String> fileNames,
                                  Path orderFolderPath) throws IOException {
       //if the dir doesn't exist, then make it. if it does, do nothing(IDEMPOTENT).
       Files.createDirectories(orderFolderPath);

       Path tempDir = Files.createTempDirectory(orderFolderPath, "temp_images_");
       List<Path> tempFilePaths = new ArrayList<>(photos.size());
       List<Path> movedFilePaths = new ArrayList<>(photos.size());

       try {
           LocalDateTime now = LocalDateTime.now();
           for (int i = 0; i < photos.size(); i++) {
               //give the files unique names using DateTime + index + file extension.
               String uniqueFileName = fileNames.get(i) + "_" + FileNameFormatter.format(now) + "_" + i + ".png" ;
               Path tempFilePath = tempDir.resolve(uniqueFileName);
               ImageIO.write(photos.get(i), "png", tempFilePath.toFile());
               tempFilePaths.add(tempFilePath);
           }

           for (Path temp : tempFilePaths) {
               Path dest = orderFolderPath.resolve(temp.getFileName());
               if (Files.exists(dest)) {
                   throw new IOException("File already exists" + dest.toString());
               }
               Files.move(temp, dest, StandardCopyOption.ATOMIC_MOVE);
               movedFilePaths.add(dest);
           }
           Files.deleteIfExists(tempDir);
           return movedFilePaths;
       } catch (IOException e) {
           deleteFiles(movedFilePaths);
           deleteRecursively(tempDir);
           throw e;
       }
    }

    private void deleteRecursively(Path tempDir) {
        if (tempDir == null || Files.notExists(tempDir)) {
            return;
        }
        //auto close stream after use.
        try (Stream<Path> walk = Files.walk(tempDir)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignoredException) {
                    //Deletion failure is what it is
                    //ideally its logged or flagged so it can be manually deleted later.
                }
            });
        } catch (IOException ignoredException) {
            //same as previous catch
        }
    }

    private void deleteFiles(List<Path> movedFilePaths) {
        for (Path path : movedFilePaths) {
             try {
                 Files.deleteIfExists(path);
             } catch (IOException ignoredException) {
                 //Deletion failure is what it is
                 //ideally its logged or flagged so it can be manually deleted later.
             }
        }
    }


    @Override
    public void insertImagePathToDatabase(Connection connection,
                                          List<Path> filePaths,
                                          User uploader,
                                          String orderID) throws SQLException {

        String sql = "INSERT INTO Photos (order_number, file_path, uploaded_by, uploaded_at) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Path path : filePaths) {
                statement.setString(1, orderID);
                statement.setString(2, path.toString());
                statement.setInt(3, uploader.getId());
                statement.setObject(4, LocalDateTime.now());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    @Override
    public ObservableList<Photo> getImagesFromDatabase(String orderNumber) throws SQLException {
        ObservableList<Photo> photos = javafx.collections.FXCollections.observableArrayList();
        String sql = "SELECT * FROM Photos WHERE order_number = ?";

        try(Connection conn = dbConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, orderNumber);

            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Photo tempImg = new Photo();
                tempImg.setId(rs.getInt("id"));
                tempImg.setOrderNumber(rs.getString("order_Number"));
                tempImg.setFilepath(rs.getString("file_path"));
                tempImg.setUploadedBy(rs.getInt("uploaded_by"));
                tempImg.setUploadTime(rs.getObject("uploaded_at", LocalDateTime.class));
                photos.add(tempImg);
            }
            System.out.println("length:" + photos.size());
            return photos;
        }
    }

    @Override
    public void deleteImageFromDatabase() {

    }
}
