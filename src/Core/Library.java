package Core;

import Data.Area;
import Data.Child;
import Data.Picture;
import GUI.MainFrame;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class Library implements Serializable {

    private static ArrayList<Picture> pictureLibrary = new ArrayList<Picture>();
    private static ArrayList<Taggable> taggableComponents = new ArrayList<Taggable>();
    //private static ArrayList<Taggable> areaList = new ArrayList<Taggable>();

    public static synchronized ArrayList<Taggable> getTaggableComponentsList() {
        return taggableComponents;
    }

    public static Vector<String> getTaggableComponentNamesVector() {
        Vector<String> taggableComponentNames = new Vector<String>();
        for (Taggable t : taggableComponents) {
            taggableComponentNames.add(t.getName());
        }
        return taggableComponentNames;
    }
    
    /*public static synchronized ArrayList<Taggable> getAreaList() {
        return areaList;
    }

    public static Vector<String> getAreaNamesVector() {
        Vector<String> areaNames = new Vector<String>();
        for (Taggable t : areaList) {
            areaNames.add(t.getName());
        }
        return areaNames;
    }*/

    /*public static Vector<String> getAllNamesVector() {
        Vector<String> allNames = getChildrenNamesVector();
        allNames.add("All Pictures");
        //for (int i = 0; i < getAreaNamesVector().size(); ++i) {
        //    allNames.add(getAreaNamesVector().get(i));
        //}
        return allNames;

    }*/

    public static synchronized ArrayList<Picture> getPictureLibrary() {
        return pictureLibrary;
    }

    public static void importPicture(final File[] importedPictures) {
        //specify no. of threads not including thread for leftover pictures
        int noOfThreads = 10;
        //size of array
        int importSize = importedPictures.length;
        //leftover calculated by size of array % no. of threads
        int leftover = 0;
        while (importSize % noOfThreads != 0 && importSize > noOfThreads) {
            ++leftover;
            --importSize;
        }
        //if there are more pictures than threads to import pictures
        if (importedPictures.length > noOfThreads) {
            //find out how many pictures will go in each thread and import
            int chunkSize = importSize / noOfThreads;
            for (int i = 0; i < importSize; i += chunkSize) {
                File[] partition = new File[chunkSize];
                System.arraycopy(importedPictures, i, partition, 0, chunkSize);
                new PictureImportThread(partition).start();
            }
            //import leftover pictures
            if (leftover > 0) {
                File[] partialPartition = new File[leftover];
                System.arraycopy(importedPictures, importedPictures.length - leftover, partialPartition, 0, leftover);
                new PictureImportThread(partialPartition).start();
            }
        }
        //if there are less pictures than threads to import pictures, import all pictures on 1 thread :))
        else {
            new PictureImportThread(importedPictures).start();
        }
    }

	public static void importFolder(String folderPath) {
		// TODO for every picture in folder do importPicture()
	}

	public static void rotate(ArrayList<Picture> picturesToRotate,
			boolean direction) {
		// TODO (true = clockwise, false = anticlockwise)
	}

	public static void print(ArrayList<Picture> picturesToPrint) {
		// TODO 
	}

	public static void export(ArrayList<Picture> picturesToExport) {
		// TODO
	}

	public static void delete(ArrayList<Picture> picturesToDelete) {
		// TODO
	}

	public static void addChild(Child child) {
		taggableComponents.add(child);
	}

	public static void removeChild(Child child) {
		taggableComponents.remove(child);
	}

	public static void addArea(Area area) {
		taggableComponents.add(area);
	}

	public static void removeArea(Area area) {
		taggableComponents.remove(area);
	}

	public static synchronized void addPictureToLibrary(Picture picture) {
		pictureLibrary.add(picture);
	}
	
	public static void deletePictureLibrary()
	{
		pictureLibrary = null;
	}

	public static Date getDate(String s) {
		int date = Integer.parseInt(s.substring(0, 2));
		int month = Integer.parseInt(s.substring(3, 5));
		int year = Integer.parseInt(s.substring(6, 10));
		if (date > 0 && date < 32 && month > -1 && month < 12 && year > -1)
			return new Date(year - 1900, month - 1, date);
		return null;
	}

	public static String getFormattedDate(Date date) {
		String d = "" + date.getDate();
		if (d.length() == 1)
			d = "0" + d;
		String m = "" + (date.getMonth() + 1);
		if (m.length() == 1)
			m = "0" + m;
		String y = "" + (date.getYear() + 1900);
		return "" + d + "/" + m + "/" + y;
	}

}
