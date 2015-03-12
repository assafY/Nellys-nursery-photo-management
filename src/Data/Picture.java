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
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class Picture implements Serializable{

    private static final long serialVersionUID = -690711084688757476L;
    private Tag metadata;
    private File imageFile;

    private transient Object imageKey;

    public Picture(File pictureFile) {

        this.imageFile = pictureFile;
        metadata = new Tag();

          getDateAndKey();
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
        //imageKey = attr.fileKey();
        Date pictureTakenDate;
        // get original date and time picture was taken and add to metadata
        ExifSubIFDDirectory directory = originalPictureMetadata.getDirectory(ExifSubIFDDirectory.class);
        if (directory != null) {
            pictureTakenDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            metadata.setDate(pictureTakenDate);
        }
        else { // use the date the file was created
            pictureTakenDate = new Date(attr.creationTime().toMillis());
            metadata.setDate(pictureTakenDate);
        }
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
    public Tag getTag() {
    	return metadata;
    }
    
    public void setTag(Tag tag) {
    	metadata = tag;
    }
}
