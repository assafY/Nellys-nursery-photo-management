package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;

import javax.swing.*;
import java.util.ArrayList;

public class ThumbnailImportThread extends Thread {

    ArrayList<PictureLabel> picturesToDisplay;
    MainFrame mainFrame = MainFrame.getMainFrames().get(0);

    public ThumbnailImportThread(ArrayList<PictureLabel> picturesToDisplay){
        this.picturesToDisplay = picturesToDisplay;
    }

    public void run() {
        try {
            Library.addRunningThread(this);
            int pictureCounter = 0;
            //if (!isInterrupted()) {
            for (PictureLabel p: picturesToDisplay) {
                /*if (pictureCounter % 25 == 0) {
                    for (int i = 0; i < pictureCounter; ++i) {
                        if (picturesToDisplay.get(i).getThumbnail() != null)
                            picturesToDisplay.get(i).getThumbnail().flush();
                    }
                }*/
                if (isInterrupted()) {
                    break;
                }
                if (p.getIcon() == null) {
                    p.createThumbnail(mainFrame.getZoomValue());
                }
                //mainFrame.getPicturesPanel().addThumbnailToView(p, mainFrame.getZoomValue());
                ++pictureCounter;
            }
            //}
        }  finally {
                /*for (PictureLabel p: picturesToDisplay) {
                    if (p.getThumbnail() != null)
                        p.getThumbnail().flush();
                }*/
            picturesToDisplay.clear();
            mainFrame = null;
            Library.removeRunningThread(this);
            System.gc();
        }
    }
}