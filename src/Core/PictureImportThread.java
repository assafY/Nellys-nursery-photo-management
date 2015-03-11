package Core;

import Data.Picture;

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
            while (Settings.IMPORT_THREAD_COUNT > 10) {
                sleep(500);
            }
            ++Settings.IMPORT_THREAD_COUNT;
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
                // if it doesn't exist in library
                if (!exists ) {
                    // add picture to library
                    Picture currentPicture = new Picture(
                            importedPictures[i]);
                    picturesToDisplay.add(currentPicture);
                }

            }
        } catch (InterruptedException e) {

        } finally {
            Runnable displayPictures = new Runnable() {

                public void run() {
                    for (int i = 0; i < picturesToDisplay.size(); ++i) {
                        Library.addPictureToLibrary(picturesToDisplay.get(i));
                        System.out.println("added: " + picturesToDisplay.get(i).getImagePath());
                    }
                    --Settings.IMPORT_THREAD_COUNT;
                }
            };
            SwingUtilities.invokeLater(displayPictures);
        }
    }
}
