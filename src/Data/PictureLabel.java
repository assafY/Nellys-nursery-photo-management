package Data;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureLabel extends JLabel {

    private static final int DEFAULT_SIZE = 25;
    private Picture picture;

    public PictureLabel(Picture picture) {
        this.picture = picture;
    }

    public void createThumbnail(int size) {

        ImageIcon origin = new ImageIcon(picture.getImagePath());

        int adjustment = 3 * (size - DEFAULT_SIZE);
        int newHeight = origin.getIconHeight() / (size + adjustment);
        int newWidth = origin.getIconWidth() / (size + adjustment);
        Image resizedImage = origin.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
        setIcon(new ImageIcon(resizedImage));
        origin.getImage().flush();
        origin = null;
    }

    /*public void createThumbnail() {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(picture.getImagePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage thumbnail = Scalr.resize(img, 150);
        setIcon(new ImageIcon(thumbnail));
        img.flush();
        img = null;
    }*/
}
