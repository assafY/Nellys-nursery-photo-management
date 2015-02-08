package Data;

import Core.Library;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Picture {

    public static Tag METADATA;
    private static final int DEFAULT_SIZE = 25;

    private ImageIcon pictureIcon;
    private ImageIcon thumbnail;

    public Picture(String filePath) {

        pictureIcon = new ImageIcon(filePath);
        createThumbnail(DEFAULT_SIZE);

        METADATA = new Tag();
        Library.addPictureToLibrary(this);
    }

    public void createThumbnail(int resizeNum) {
        int newHeight = pictureIcon.getIconHeight() / resizeNum;
        int newWidth = pictureIcon.getIconWidth() / resizeNum;
        Image resizedImage = pictureIcon.getImage().getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
        thumbnail = new ImageIcon(resizedImage);
    }

    public ImageIcon getPictureIcon() {
        return pictureIcon;
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }


}
