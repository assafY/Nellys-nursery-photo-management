package Data;

import Core.Library;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class Picture {

    public static Tag METADATA;

    private String imagePath;
    private Object imageKey;

    public Picture(File pictureFile) {

        imagePath = pictureFile.getPath();
        METADATA = new Tag();

        BasicFileAttributes attr = null;
        Metadata originalPictureMetadata = null;

        try {
            attr = Files.readAttributes(pictureFile.toPath(), BasicFileAttributes.class);
            originalPictureMetadata = ImageMetadataReader.readMetadata(pictureFile);
        } catch (IOException e) {
            //TODO: Handle exception
        } catch (ImageProcessingException e1) {
            //TODO: Handle exception
        }

        // store unique file key
        imageKey = attr.fileKey();
        // get original date and time picture was taken and add to metadata
        ExifSubIFDDirectory directory = originalPictureMetadata.getDirectory(ExifSubIFDDirectory.class);
        Date PictureTakenDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        METADATA.setDate(PictureTakenDate);

        Library.addPictureToLibrary(this);
    }

    public Object getImageKey() {
        return imageKey;
    }
    public String getImagePath() {
        return imagePath;
    }


}
