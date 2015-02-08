package Data;

import javax.swing.*;
import java.awt.*;

public class PictureLabel extends JLabel {

    private static final int DEFAULT_SIZE = 25;

    private Picture picture;

    public PictureLabel(Picture picture) {
        this.picture = picture;
        createThumbnail(DEFAULT_SIZE);
    }

    public void createThumbnail(int size) {

        ImageIcon origin = picture.getPictureIcon();

        int adjustment = 3 * (size - DEFAULT_SIZE);
        int newHeight = origin.getIconHeight() / (size + adjustment);
        int newWidth = origin.getIconWidth() / (size + adjustment);
        Image resizedImage = origin.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
        setIcon(new ImageIcon(resizedImage));

    }
}
