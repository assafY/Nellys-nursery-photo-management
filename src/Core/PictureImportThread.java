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

    public void run() {
        try {
            // for evey file path sent from importing in GUI
            for (File f: importedPictures) {

                // check if it is already imported into library
                boolean exists = false;
                for (int j = 0; j < Library.getPictureLibrary().size(); ++j) {
                    if (f.getPath().equals(
                            Library.getPictureLibrary().get(j).getImagePath())) {
                        exists = true;
                        break;
                    }
                }
                // if it doesn't exist in library
                if (!exists) {
                    // add picture to library
                    Picture currentPicture = new Picture(f);
                    picturesToDisplay.add(currentPicture);
                }

            }
        } finally {
            Runnable displayPictures = new Runnable() {

                public void run() {
                    if (picturesToDisplay.size() > 0) {
                        for (Picture p: picturesToDisplay) {
                            Library.addPictureToLibrary(p);
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
                    picturesToDisplay.clear();
                }
            };
            SwingUtilities.invokeLater(displayPictures);
        }
    }
}
