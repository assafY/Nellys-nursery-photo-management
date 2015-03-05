package Core;

import Data.Picture;
import GUI.MainFrame;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class PictureImportThread extends Thread {
    File[] importedPictures;
    ArrayList<Picture> picturesToDisplay = new ArrayList<Picture>();
    public PictureImportThread(File[] importedPictures){
        this.importedPictures = importedPictures;
    }
    public void run(){
        try {
            // for evey file path sent from importing in GUI
            for (int i = 0; i < importedPictures.length; ++i) {

                // check if it is already imported into library
                boolean exists = false;
                for (int j = 0; j < Library.getPictureLibrary().size(); ++j) {
                    if (importedPictures[i].getPath().equals(
                            Library.getPictureLibrary().get(j).getImagePath())) {
                        exists = true;
                    }
                }
                System.out.println(exists);
                // if it doesn't exist in library
                if (!exists ) {
                    // add picture to library
                    Picture currentPicture = new Picture(
                            importedPictures[i]);
                    picturesToDisplay.add(currentPicture);
                    System.out.println("Added: "
                            + currentPicture.getImagePath());
                }

            }
        } finally {
            Runnable displayPictures = new Runnable() {

                public void run() {
                    System.out.println("Import Complete.");
                    for (MainFrame mainFrame : MainFrame.getMainFrames()) {
                    mainFrame.getPicturesPanel().addThumbnailsToView(picturesToDisplay, mainFrame.getZoomValue());
                    }
                    for (int i = 0; i < picturesToDisplay.size(); ++i) {
                        Library.addPictureToLibrary(picturesToDisplay.get(i));
                    }
                }
            };
            SwingUtilities.invokeLater(displayPictures);
        }
    }
}
