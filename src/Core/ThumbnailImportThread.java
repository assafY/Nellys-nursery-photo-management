package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;

import javax.swing.*;
import java.util.ArrayList;

public class ThumbnailImportThread extends Thread {

    ArrayList<PictureLabel> picturesToDisplay = new ArrayList<PictureLabel>();
    MainFrame mainFrame = MainFrame.getMainFrames().get(0);

    public ThumbnailImportThread(ArrayList<PictureLabel> picturesToDisplay){
        this.picturesToDisplay = picturesToDisplay;
    }

    public void run() {
        try {
            while (Settings.IMPORT_THREAD_COUNT > 5) {
                sleep(500);
            }
            ++Settings.IMPORT_THREAD_COUNT;
            // for evey thumbnail requesting view in GUI
            if (!Settings.IMPORT_INTERRUPTED) {
                for (PictureLabel p : picturesToDisplay) {
                   // if (!mainFrame.getPicturesPanel().getThumbsOnDisplay().contains(p)) {
                        p.createThumbnail();
                        mainFrame.getPicturesPanel().addThumbnailToView(p, mainFrame.getZoomValue());
                        System.out.println("added: " + p.getPicture().getImagePath());
                   /* } else {
                        if (p.getThumbnail() != null) {
                            mainFrame.getPicturesPanel().addThumbnailToView(p, mainFrame.getZoomValue());
                            System.out.println("showing existing thumb: " + p.getPicture().getImagePath());
                        }
                    }*/
                }
            }

        } catch (InterruptedException e) {

        } finally {
            /*Runnable displayPictures = new Runnable() {

                public void run() {*/
                    //mainFrame.getPicturesPanel().addThumbnailsToView(picturesToDisplay, MainFrame.getMainFrames().get(0).getZoomValue());
                    --Settings.IMPORT_THREAD_COUNT;
                /*}
            };
            SwingUtilities.invokeLater(displayPictures);*/

        }
    }
}