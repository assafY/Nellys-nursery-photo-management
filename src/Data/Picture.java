package Data;

import Core.Settings;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class Picture {

    public static Tag METADATA;

    private BufferedImage thumbnail;
    private File imageFile;
    private Object imageKey;

    public Picture(File pictureFile) {

        this.imageFile = pictureFile;
        METADATA = new Tag();

        createThumbnail();
        getDateAndKey();
    }

    private void createThumbnail() {
        thumbnail = null;

        try {
            thumbnail = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (thumbnail != null) {
            thumbnail = Scalr.resize(thumbnail, Settings.THUMBNAIL_SIZES[1]);

        }
    }

    //TODO what if there is no date and key? boolean maybe?
    private void getDateAndKey() {
        BasicFileAttributes attr = null;
        Metadata originalPictureMetadata = null;

        try {
            attr = Files.readAttributes(imageFile.toPath(), BasicFileAttributes.class);
            originalPictureMetadata = ImageMetadataReader.readMetadata(imageFile);
        } catch (IOException e) {
            //TODO: Handle exception
        } catch (ImageProcessingException e1) {
            //TODO: Handle exception
        }

        // store unique file key
        imageKey = attr.fileKey();
        // get original date and time picture was taken and add to metadata
        ExifSubIFDDirectory directory = originalPictureMetadata.getDirectory(ExifSubIFDDirectory.class);
        if (directory != null) {
            Date pictureTakenDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            METADATA.setDate(pictureTakenDate);
        }
    }

    // prints the number of bytes taken by a thumbnail
    private void checkSizeOnDisk() {
        DataBuffer buff = thumbnail.getRaster().getDataBuffer();
        int bytes = buff.getSize() * DataBuffer.getDataTypeSize(buff.getDataType()) / 8;
        System.out.println(bytes);
    }

    public Object getImageKey() {
        return imageKey;
    }
    public String getImagePath() {
        return imageFile.getPath();
    }
    public File getImageFile() {
        return imageFile;
    }
    public BufferedImage getThumbnail() {
        return thumbnail;
    }
}
