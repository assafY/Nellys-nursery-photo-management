package Core;

import Data.Child;
import Data.Picture;
import Data.PictureLabel;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Library implements Serializable {

    private static ArrayList<Child> childrenList = new ArrayList<Child>();
    private static Iterator childrenListIterator = childrenList.iterator();
    private static ArrayList<Picture> pictureLibrary = new ArrayList<Picture>();
    private static Iterator pictureLibraryIterator = pictureLibrary.iterator();
    private static ArrayList<PictureLabel> pictureLabels = new ArrayList<PictureLabel>();
    private static ArrayList<String> possibleArea = new ArrayList<String>();
    private static Iterator possibleAreaIterator = possibleArea.iterator();
    private static ArrayList<Date> possibleDate = new ArrayList<Date>();
    private static Iterator possibleDateIterator = possibleDate.iterator();

    public static ArrayList<Child> getChildrenList() {
        return childrenList;
    }

    public static void addPictureToLibrary(Picture picture) {
        pictureLibrary.add(picture);
    }

    public static void addPictureLabel(PictureLabel picLabel) {
        pictureLabels.add(picLabel);
    }

    public static ArrayList<PictureLabel> getPictureLabels() {
        return pictureLabels;
    }

    public static ArrayList<Picture> getPictureLibrary() {
        return pictureLibrary;
    }

    public static void tagDate(ArrayList<Picture> picturesToTag, Date dateToTag) {
        //TO DO
    }

    public static void tagArea(ArrayList<Picture> picturesToTag, String Area) {
        //TO DO
    }

    public static void tagChildren(ArrayList<Picture> picturesToTag, ArrayList<String> childreninPicture) {
        //TO DO
    }

    public static void importPicture(String filePath) {
        //TO DO
    }

    public static void importFolder(String folderPath) {
        //TO DO
    }

    public static ArrayList<Picture> searchByChild(String childName) {
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TO DO
        return result;
    }

    public static ArrayList<Picture> searchByDate(Date date) {
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TO DO
        return result;
    }

    public static ArrayList<Picture> searchByArea(String area) {
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TO DO
        return result;
    }

    public static void rotate(ArrayList<Picture> picturesToRotate, boolean direction) {
        //TO DO (true = clockwise, false = anticlockwise)

    }

    public static void print(ArrayList<Picture> picturesToPrint) {
        //TO DO
    }

    public static void export(ArrayList<Picture> picturesToExport) {
        //TO DO
    }

    public static void addChild(Child child) {
        childrenList.add(child);
    }

    public static void removeChild(Child child) {
        childrenList.remove(child);
    }

}
