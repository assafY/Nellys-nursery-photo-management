package Core;

import Data.Picture;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class PictureImportThread extends Thread {

    private File[] importedPictures;
    private ArrayList<Picture> picturesToDisplay = new ArrayList<Picture>();

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
                        break;
                    }
                }
                // if it doesn't exist in library
                if (!exists) {
                    // add picture to library
                    Picture currentPicture = new Picture(importedPictures[i]);
                    picturesToDisplay.add(currentPicture);
                }

            }
        } finally {
            Runnable displayPictures = new Runnable() {

                public void run() {
                    if (picturesToDisplay.size() > 0) {
                        ArrayList<Picture> picturesInCurrentFolder = new ArrayList<Picture>();
                        for (int i = 0; i < picturesToDisplay.size(); ++i) {
                            Library.addPictureToLibrary(picturesToDisplay.get(i));
                            picturesInCurrentFolder.add(picturesToDisplay.get(i));
                            System.out.println("added: " + picturesToDisplay.get(i).getImagePath());
                        }

                        String parentDirPath = picturesToDisplay.get(0).getImagePath();
                        File parentDir = new File(parentDirPath.substring(0, parentDirPath.lastIndexOf(File.separator)));
                        ArrayList<Picture> existingPictureList = new ArrayList<Picture>();
                        if (Library.getDirectoryPictureMap().containsKey(parentDir)) {
                            existingPictureList = Library.getDirectoryPictureMap().get(parentDir);
                        }
                        for (Picture p : picturesToDisplay) {
                            if (!existingPictureList.contains(p)) {
                                existingPictureList.add(p);
                            }
                        }
                        Library.getDirectoryPictureMap().put(parentDir, existingPictureList);
                    }

                    importedPictures = null;
                    picturesToDisplay = null;
                }
            };
            SwingUtilities.invokeLater(displayPictures);
        }
    }
}
