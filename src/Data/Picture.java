package Data;

import GUI.MainFrame;
import GUI.PictureLabel;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
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
    private transient PictureLabel pictureLabel = null;

    public Picture(File pictureFile) {

        this.imageFile = pictureFile;
        metadata = new Tag();
        createPictureLabel();
        getDate();
    }

    private void getDate() {
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

        Date pictureTakenDate;
        // get original date and time picture was taken and add to metadata
        ExifSubIFDDirectory directory = originalPictureMetadata.getDirectory(ExifSubIFDDirectory.class);
        if (directory != null) {
            pictureTakenDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            metadata.setDate(pictureTakenDate);
        }
        else { // use the date the file was created
            pictureTakenDate = new Date(attr.creationTime().toMillis());
            dateIsWrong();
            metadata.setDate(null);
        }
    }
    public boolean dateIsWrong(){
        return true;
    }

    private void createPictureLabel() {
        if (MainFrame.getMainFrames().size() > 0 && imageFile.exists()) {
            if (pictureLabel == null) {
                pictureLabel = new PictureLabel(this, MainFrame.getMainFrames().get(0).getPicturesPanel());
            }
        }
    }
    public PictureLabel getPictureLabel() {
        createPictureLabel();
        return pictureLabel;
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
