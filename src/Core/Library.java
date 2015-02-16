package Core;

import Data.Child;
import Data.Picture;
import Data.Tag;
import GUI.MainFrame;
import GUI.PictureLabel;
//import com.sun.deploy.util.SystemUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Library implements Serializable {

    private static ArrayList<Child> childrenList = new ArrayList<Child>();
    private static Iterator childrenListIterator = childrenList.iterator();
    private static ArrayList<Picture> pictureLibrary = new ArrayList<Picture>();
    private static Iterator pictureLibraryIterator = pictureLibrary.iterator();
    private static ArrayList<String> possibleArea = new ArrayList<String>();
    private static Iterator possibleAreaIterator = possibleArea.iterator();
    private static ArrayList<Date> possibleDate = new ArrayList<Date>();
    private static Iterator possibleDateIterator = possibleDate.iterator();
    private static ArrayList<PictureLabel> thumbsOnDisplay = new ArrayList<PictureLabel>();
    private static ArrayList<PictureLabel> selectedThumbs = new ArrayList<PictureLabel>();

    public static synchronized ArrayList<Child> getChildrenList() {
        return childrenList;
    }

    public static synchronized ArrayList<Picture> getPictureLibrary() {
        return pictureLibrary;
    }

    public static void tagPicture(Picture picture, Tag tag) {
    	picture.setTag(tag);
    }

    /* comment section */ {
//    public static void tagDate(ArrayList<Picture> picturesToTag, Date dateToTag) {
//        //TO DO
//    }
//
//    public static void tagArea(ArrayList<Picture> picturesToTag, String Area) {
//        //TO DO
//    }
//
//    public static void tagChildren(ArrayList<Picture> picturesToTag, ArrayList<String> childreninPicture) {
//        //TO DO
//    }
    }

    public static void importPicture(final File[] importedPictures) {

        Thread newPictureImport = new Thread() {

            ArrayList<Picture> picturesToDisplay = new ArrayList<Picture>();

            public void run() {
                try {
                    //for evey file path sent from importing in GUI
                    for (int i = 0; i < importedPictures.length; ++i) {

                        //check if it is already imported into library
                        boolean exists = false;
                        for (int j = 0; j < pictureLibrary.size(); ++j) {
                            if (importedPictures[i].getPath().equals(pictureLibrary.get(j).getImagePath())) {
                                exists = true;
                            }
                        }
                        System.out.println(exists);
                        //if it doesn't exist in library
                        if (!exists) {
                            //add picture to library
                            Picture currentPicture = new Picture(importedPictures[i]);
                            picturesToDisplay.add(currentPicture);
                            System.out.println("Added: " + currentPicture.getImagePath());
                        }

                    }
                } finally {
                    Runnable displayPictures = new Runnable() {

                        public void run() {
                            System.out.println("Import Complete.");
                            MainFrame.addThumbnailsToView(picturesToDisplay);
                            for (int i = 0; i < picturesToDisplay.size(); ++i) {
                                addPictureToLibrary(picturesToDisplay.get(i));
                            }
                        }
                    };
                    SwingUtilities.invokeLater(displayPictures);
                }
            }
        };

        newPictureImport.start();
        // System.out.println("USED MEMORY: " + Runtime.getRuntime().totalMemory() + "FREE MEMORY: " + Runtime.getRuntime().freeMemory());
    }

    public static void importFolder(String folderPath) {
        //TODO for every picture in folder do importPicture()
    }

    public static ArrayList<Picture> searchByChild(String childName) {
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TODO
        return result;
    }

    /* comment section */ {
//    public static ArrayList<Picture> searchByChild(String childName) {
//        ArrayList<Picture> result = new ArrayList<Picture>();
//        //TO DO
//        return result;
//    }
//
//    public static ArrayList<Picture> searchByDate(Date date) {
//        ArrayList<Picture> result = new ArrayList<Picture>();
//        //TO DO
//        return result;
//    }
//
//    public static ArrayList<Picture> searchByArea(String area) {
//        ArrayList<Picture> result = new ArrayList<Picture>();
//        //TO DO
//        return result;
//    }
    }

    public static void rotate(ArrayList<Picture> picturesToRotate, boolean direction) {
        //TODO (true = clockwise, false = anticlockwise)
    }

    public static void print(ArrayList<Picture> picturesToPrint) {
        //TODO here? or in GUI?
    }

    public static void export(ArrayList<Picture> picturesToExport) {
        //TODO
    }

    public static void delete(ArrayList<Picture> picturesToDelete) {
        //TODO
    }

    public static void addChild(Child child) {
        childrenList.add(child);
    }

    public static void removeChild(Child child) {
        childrenList.remove(child);
    }

    public static synchronized void addPictureToLibrary(Picture picture) {
        pictureLibrary.add(picture);
    }

    public static synchronized ArrayList<PictureLabel> getThumbsOnDisplay() {
        return thumbsOnDisplay;
    }

    public static synchronized void addThumbToDisplay(PictureLabel thumb) {
        thumbsOnDisplay.add(thumb);
    }

    public static void removeThumbFromDisplay(PictureLabel thumb) {
        thumbsOnDisplay.remove(thumb);
    }

    public static ArrayList<PictureLabel> getSelectedThumbs() {
        return selectedThumbs;
    }

    public static void addSelectedThumb(PictureLabel selectedThumb) {
        selectedThumbs.add(selectedThumb);
    }

    public static void removeSelectedThumb(PictureLabel selectedThumb) {
        selectedThumbs.remove(selectedThumb);
    }

    public static void removeAllSelectedThumbs() {
        for (PictureLabel p: selectedThumbs) {
            p.toggleSelection();
        }
        selectedThumbs.clear();
    }

}
