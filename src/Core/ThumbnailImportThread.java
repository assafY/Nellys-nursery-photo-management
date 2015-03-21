package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;

import javax.swing.*;
import java.util.ArrayList;

public class ThumbnailImportThread extends Thread {

    PictureLabel pictureToDisplay;
    MainFrame mainFrame = MainFrame.getMainFrames().get(0);

    public ThumbnailImportThread(PictureLabel pictureToDisplay){
        this.pictureToDisplay = pictureToDisplay;
    }

    public void run() {
        try {
            Library.addRunningThread(this);

            if (!isInterrupted() && pictureToDisplay != null) {
                if (pictureToDisplay.getThumbnail() == null) {
                    pictureToDisplay.createThumbnail();
                }
                mainFrame.getPicturesPanel().addThumbnailToView(pictureToDisplay, mainFrame.getZoomValue());
            }
        }  finally {
                Library.removeRunningThread(this);
        }
    }
}