package Data;

import Core.Settings;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureLabel extends JLabel {

    private Picture picture;
    BufferedImage thumbnail;

    public PictureLabel(Picture picture) {
        this.picture = picture;
        thumbnail = null;

        try {
            thumbnail = ImageIO.read(new File(picture.getImagePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (thumbnail != null) {
            thumbnail = Scalr.resize(thumbnail, Settings.THUMBNAIL_SIZES[0]);
        }
    }

    public void createThumbnail(int size) {
        this.removeAll();
        setIcon(new ImageIcon(Scalr.resize(thumbnail, size)));
    }
}
