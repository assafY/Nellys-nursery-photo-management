package Core;

import Data.Picture;
import GUI.MainFrame;
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

    public void addThumbnail(Picture picture, int size) {
        loadedThumbnailList.add(new Thumbnail(picture, size));
    }

    /*public void resizeThumbnail(final Picture picture, final int size) {
        try {
            //Thread resizeThread = new Thread() {
                //public void run() {


                    //if (newThumb != null) {
            /*if (thumbnail.getHeight() > thumbnail.getWidth()) {
                switch (size) {
                    case 109:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 27);
                    case 119:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 30);
                    case 132:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 33);
                    case 148:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 37);
                    case 169:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 42);
                    case 196:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 49);
                    case 233:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 58);
                    case 288:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 72);
                    case 320:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 82);
                    case 377:
                        return Scalr.resize(thumbnail, Scalr.Method.SPEED, size - 94);
                }
                        //} else {
                        picture.getPictureLabel().setIcon(new ImageIcon(Scalr.resize(getThumbnail(picture), Scalr.Method.SPEED, size)));

                        //}
                    //}
                    //return null;
                //}
            //};
            //resizeThread.start();
        } finally {
            System.gc();
        }
    }*/

    public void removeThumbnail(Picture picture) {
        for (Thumbnail t: loadedThumbnailList) {
            if (t.getPicture().equals(picture)) {
                loadedThumbnailList.remove(t);
                break;
            }
        }
    }

    public void removeAllThumbnails() {
        for (Thumbnail t: loadedThumbnailList) {
                t.getPicture().getPictureLabel().setIcon(null);
        }
        loadedThumbnailList.clear();
    }



    private class Thumbnail {

        private Picture picture;

        public Thumbnail(Picture picture, int size) {
            this.picture = picture;
            createThumbnail(picture, size);
        }

        private void createThumbnail(Picture picture, int size) {
            BufferedImage newThumbnail = null;
            Settings.LOADED_THUMBNAILS_COUNT++;
            try {
                newThumbnail = ImageIO.read(picture.getImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (newThumbnail != null) {
                picture.getPictureLabel().setIcon(new ImageIcon(Scalr.resize(newThumbnail, Scalr.Method.SPEED, Settings.THUMBNAIL_SIZES[4])));
            }

            newThumbnail.flush();
            newThumbnail = null;
            System.gc();
        }

        public Picture getPicture() {
            return picture;
        }
    }
}
