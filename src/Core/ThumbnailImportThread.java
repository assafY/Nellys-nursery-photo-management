package Core;

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
            if (!isInterrupted()) {
                Settings.IMPORT_IN_PROGRESS = true;
                for (PictureLabel p: picturesToDisplay) {
                    if (isInterrupted()) {
                        break;
                    }
                    if (p.getIcon() == null) {
                        p.createThumbnail(mainFrame.getZoomValue());
                    }
                }
                Settings.IMPORT_IN_PROGRESS = false;
            }
        }  finally {
            picturesToDisplay.clear();
            picturesToDisplay = null;
            mainFrame = null;
            Library.removeRunningThread(this);
            System.gc();
        }
    }
}