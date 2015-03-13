package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;
import GUI.PicturesFrame;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {

    private static ArrayList<Thread> RUNNING_THREADS = new ArrayList<Thread>();
    private static ArrayList<Picture> PICTURE_LIBRARY = new ArrayList<Picture>();
    private static ArrayList<PictureLabel> ALL_PICTURE_LABELS = new ArrayList<PictureLabel>();
    private static Map<File, ArrayList<Picture>> directoryPictureMap = new HashMap<File, ArrayList<Picture>>();
    private static ArrayList<Taggable> taggableComponents = new ArrayList<Taggable>();
    private static ArrayList<Taggable> areaList = new ArrayList<Taggable>();
    private static final Object[] nurserySites = {"Dulwich", "Lancaster", "Rosendale", "Turney"};
    public static boolean LAST_THREAD = false;

    public static synchronized ArrayList<Taggable> getTaggableComponentsList() {
        return taggableComponents;
    }

    public static Vector<String> getTaggableComponentNamesVector(boolean forSearchField) {
        Vector<String> taggableComponentNames = new Vector<String>();
        if (forSearchField) {
            taggableComponentNames.add("View All");
        }
        for (Taggable t : taggableComponents) {
            taggableComponentNames.add(t.getName());
        }
        return taggableComponentNames;
    }

    public static ArrayList<Taggable> getAreaList() {
        return areaList;
    }

    public static Object[] getNurserySites() {
        return nurserySites;
    }

    public static synchronized ArrayList<Picture> getPictureLibrary() {
        return PICTURE_LIBRARY;
    }

    public static Map<File, ArrayList<Picture>> getDirectoryPictureMap() {
        return directoryPictureMap;
    }

    public static ArrayList<PictureLabel> getAllPicturesLabels() {
        return ALL_PICTURE_LABELS;
    }

    public static void setDirectoryPictureMap(Map<File, ArrayList<Picture>> newMap) {
        directoryPictureMap = newMap;
    }

    public static ArrayList<Thread> getRunningThreads() {
        return RUNNING_THREADS;
    }

    public static void addRunningThread(Thread t) {
        if (!RUNNING_THREADS.contains(t)) {
            RUNNING_THREADS.add(t);
        }
    }

    public static void removeRunningThread(Thread t) {
        RUNNING_THREADS.remove(t);
    }

    /**
     *
     *
     * @param importedPictures File array for all pictures being imported in this batch
     */
    public static void importPicture(final ArrayList<Picture> importedPictures) {

        Settings.IMPORT_IN_PROGRESS = true;

        final PicturesFrame picturesPanel = MainFrame.getMainFrames().get(0).getPicturesPanel();
        // master list of all thumbnails to be shown
        // thumbnails are added to it by all threads
        picturesPanel.removeAll();
        //picturesPanel.removeAllThumbsFromDisplay();
        picturesPanel.revalidate();

        if (importedPictures.size() > 0) {

            //specify no. of threads not including thread for leftover pictures
            int noOfThreads = 5;
            //size of array
            int importSize = importedPictures.size();
            //leftover calculated by size of array % no. of threads
            int leftover = 0;

            while (importSize % noOfThreads != 0 && importSize > noOfThreads) {
                ++leftover;
                --importSize;
            }

            //if there are more pictures than threads to import pictures
            if (importedPictures.size() > noOfThreads) {
                //find out how many pictures will go in each thread and import
                int chunkSize = importSize / noOfThreads;
                for (int i = 0; i < importSize; i += chunkSize) {
                    ArrayList<PictureLabel> thumbsToImport = new ArrayList<PictureLabel>();
                    for (int j = i; j < i + chunkSize; ++j) {
                        thumbsToImport.add(importedPictures.get(j).getPictureLabel());
                    }
                    new ThumbnailImportThread(thumbsToImport).start();
                }
                //import leftover pictures
                if (leftover > 0) {
                    ArrayList<PictureLabel> thumbsToImport = new ArrayList<PictureLabel>();
                    for (int i = 0; i < leftover; ++i) {
                        thumbsToImport.add(importedPictures.get(importSize - i - 1).getPictureLabel());
                    }

                    try {
                        ThumbnailImportThread leftoverThread = new ThumbnailImportThread(thumbsToImport);
                        leftoverThread.start();
                    } finally {
                        picturesPanel.createThumbnailArray();
                        Settings.IMPORT_IN_PROGRESS = false;
                    }
                }
            }
            //if there are less pictures than threads to import pictures, import all pictures on 1 thread :))
            else {
                ArrayList<PictureLabel> thumbsToImport = new ArrayList<PictureLabel>();
                for (Picture currentPic : importedPictures) {
                    thumbsToImport.add(currentPic.getPictureLabel());

                }
                try {
                ThumbnailImportThread singleThread = new ThumbnailImportThread(thumbsToImport);
                singleThread.start();

                } finally {
                    picturesPanel.createThumbnailArray();
                    Settings.IMPORT_IN_PROGRESS = false;
                }
            }
        }
    }

    /**
     * Recursive method looking at all files inside a folder. Any
     * folders found call the method on themselves. All files found are added
     * to a list. After all folders are opened the list of all files is sent to importPicture().
     *
     * @param importDirectory root directory to be imported
     */
	public static void importFolder(final File importDirectory) {
		// TODO for every picture in folder do importPicture()
        ArrayList<File> nestedPictures = new ArrayList<File>();
        File[] nestedItems = importDirectory.listFiles();

        for(File file: nestedItems) {
            if(file.isFile() && (FilenameUtils.getExtension(file.getPath()).equalsIgnoreCase("jpg") ||
                    FilenameUtils.getExtension(file.getPath()).equalsIgnoreCase("jpeg"))){
                nestedPictures.add(file);
            }
            else if(file.isDirectory()){
                importFolder(file);
            }
        }

        File[] toProcess = new File[0];
        System.out.println(nestedPictures.size() + " lqlq");
        if(nestedPictures.size() > 0){
            new PictureImportThread(nestedPictures.toArray(toProcess)).start();
        }

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

	public static void addTaggableComponent(Taggable t) {
		taggableComponents.add(t);
        if (t.getType() == Settings.AREA_TAG) {
            areaList.add(t);
        }
	}

	public static void removeTaggableComponent(Taggable t) {
		taggableComponents.remove(t);
	}

	public static synchronized void addPictureToLibrary(Picture picture) {
		PICTURE_LIBRARY.add(picture);
	}
	
	public static void deletePictureLibrary()
	{
		PICTURE_LIBRARY = null;
	}
	
	public static void deleteTaggableComponentsList() {
		taggableComponents = null;
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
