package Data;

import Core.Library;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Picture {

    public static Tag METADATA;

    private String imagePath;

    public Picture(File pictureFile) {

        imagePath = pictureFile.getPath();
        METADATA = new Tag();

        Metadata originalPictureMetadata = null;

        try {
            originalPictureMetadata = ImageMetadataReader.readMetadata(pictureFile);
        } catch (IOException e) {
            //TODO: Handle exception
        } catch (ImageProcessingException e1) {
            //TODO: Handle exception
        }

        // get original date and time picture was taken and add to metadata
        ExifSubIFDDirectory directory = originalPictureMetadata.getDirectory(ExifSubIFDDirectory.class);
        Date PictureTakenDate = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        METADATA.setDate(PictureTakenDate);

        Library.addPictureToLibrary(this);
    }

    public String getImagePath() {
        return imagePath;
    }


}
