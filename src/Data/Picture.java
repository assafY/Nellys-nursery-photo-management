package Data;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Picture {

    public static Tag METADATA;

    private ImageIcon pictureIcon;
    private ImageIcon thumbnail;

    public Picture(String filePath) {
        METADATA = new Tag();

        pictureIcon = new ImageIcon(filePath);
        createThumbnail();
    }

    private void createThumbnail() {
        int newHeight = pictureIcon.getIconHeight() / 20;
        int newWidth = pictureIcon.getIconWidth() / 20;
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
