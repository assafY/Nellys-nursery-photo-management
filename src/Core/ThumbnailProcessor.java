package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class ThumbnailProcessor {

    private ArrayList<Thumbnail> loadedThumbnailList;

    public ThumbnailProcessor() {
        loadedThumbnailList = new ArrayList<Thumbnail>();
    }

    public void addThumbnail(final Picture picture, final int size) {
                    loadedThumbnailList.add(new Thumbnail(picture, size));
    }

    public void removeThumbnail(Picture picture) {
        for (Thumbnail t: loadedThumbnailList) {
            if (t.getPicture().equals(picture)) {
                loadedThumbnailList.remove(t);
                break;
            }
        }
    }

    public void setThumbnail(PictureLabel p, int size) {

        for (Thumbnail t: loadedThumbnailList) {
            if (t.getPicture().equals(p.getPicture())) {
                t.createThumbnail(size);
                break;
            }
        }
    }

    public void removeAllThumbnails() {
        for (Thumbnail t: loadedThumbnailList) {
            if (t != null) {
                t.getPicture().getPictureLabel().setIcon(null);
            }
        }
        loadedThumbnailList.clear();
        System.gc();
    }



    private class Thumbnail {

        private Picture picture;

        public Thumbnail(Picture picture, int size) {
            this.picture = picture;
            createThumbnail(size);
        }

        public void createThumbnail(int size) {
            try {

                if (ImageIO.read(picture.getImageFile()) != null) {
                    picture.getPictureLabel().setIcon(new ImageIcon(Scalr.resize(ImageIO.read(picture.getImageFile()), Scalr.Method.SPEED, Settings.THUMBNAIL_SIZES[size])));
                }
                ImageIO.read(picture.getImageFile()).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }

        public Picture getPicture() {
            return picture;
        }
    }
}
