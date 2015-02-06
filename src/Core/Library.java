package Core;

import Data.Picture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by ivaylo on 06/02/15.
 */
public class Library implements Serializable {
    private static ArrayList<String> childNameList = new ArrayList<String>();
    private static Iterator childnameListIterator = childNameList.iterator();
    private static ArrayList<Picture> pictureLibrary = new ArrayList<Picture>();
    private static Iterator pictureLibraryIterator = pictureLibrary.iterator();
    private static ArrayList<String> possibleArea = new ArrayList<String>();
    private static Iterator possibleAreaIterator = possibleArea.iterator();
    private static ArrayList<Date> possibleDate = new ArrayList<Date>();
    private static Iterator possibleDateIterator = possibleDate.iterator();

    public static ArrayList<String> getChildrenList(){
        return childNameList;
    }

    public static ArrayList<Picture> getPictureLibrary(){
        return pictureLibrary;
    }

    public static void tagDate(ArrayList<Picture> picturesToTag, Date dateToTag){
        //TO DO
    }

    public static void tagArea(ArrayList<Picture> picturesToTag, String Area){
        //TO DO
    }

    public static void tagChildren(ArrayList<Picture> picturesToTag, ArrayList<String> childreninPicture){
        //TO DO
    }

    public static void importPicture(String filePath){
        //TO DO
    }

    public static void importFolder(String folderPath){
        //TO DO
    }

    public static ArrayList<Picture> searchByChild(String childName){
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TO DO
        return result;
    }

    public static ArrayList<Picture> searchByDate(Date date){
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TO DO
        return result;
    }

    public static ArrayList<Picture> searchByArea(String area){
        ArrayList<Picture> result = new ArrayList<Picture>();
        //TO DO
        return result;
    }

    public static void rotate(ArrayList<Picture> picturesToRotate, boolean direction){
        //TO DO (true = clockwise, false = anticlockwise)

    }

    public static void print(ArrayList<Picture> picturesToPrint){
        //TO DO
    }

    public static void export(ArrayList<Picture> picturesToExport){
        //TO DO
    }

    public static void addChild(String childName){
        childNameList.add(childName);
    }

    public static void rmChild(String childName){
        childNameList.remove(childName);
    }

}
