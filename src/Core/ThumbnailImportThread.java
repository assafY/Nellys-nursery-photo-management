package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;

import javax.swing.*;
import java.util.ArrayList;

public class ThumbnailImportThread extends Thread {

    ArrayList<PictureLabel> picturesToDisplay;

    public ThumbnailImportThread(ArrayList<PictureLabel> picturesToDisplay){
        this.picturesToDisplay = picturesToDisplay;
    }

    public void run() {
        try {
            Library.addRunningThread(this);
            int pictureCounter = 0;
            if (!isInterrupted()) {
                for (PictureLabel p: picturesToDisplay) {
                    if (isInterrupted()) {
                        break;
                    }
                    if (p.getIcon() == null) {
                        p.createThumbnail(MainFrame.getMainFrames().get(0).getZoomValue());
                    }
                    //mainFrame.getPicturesPanel().addThumbnailToView(p, mainFrame.getZoomValue());
                    ++pictureCounter;
                }
            }
        }  finally {
                /*for (PictureLabel p: picturesToDisplay) {
                    if (p.getThumbnail() != null)
                        p.getThumbnail().flush();
                }*/
            picturesToDisplay.clear();
            Library.removeRunningThread(this);
            System.gc();
        }
    }
}