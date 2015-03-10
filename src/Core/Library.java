package Core;

import Data.Picture;
import GUI.MainFrame;
import GUI.PictureLabel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Library implements Serializable {

    private static ArrayList<Picture> pictureLibrary = new ArrayList<Picture>();
    private static ArrayList<Taggable> taggableComponents = new ArrayList<Taggable>();
    private static ArrayList<Taggable> areaList = new ArrayList<Taggable>();
    private static final Object[] nurserySites = {"Dulwich", "Lancaster", "Rosendale", "Turney"};

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
        return pictureLibrary;
    }

    /*public static void loadPictures(File currentDir) {
        if (currentDir != null) {
            ArrayList<File> currentDirectoryFiles = new ArrayList<File>();
            for (File currentFile : currentDir.listFiles()) {
                if (currentFile.isDirectory()) {
                loadPictures(currentFile);
                }
                else {
                    if (FilenameUtils.getExtension(currentFile.getPath()).equalsIgnoreCase("jpg") ||
                            FilenameUtils.getExtension(currentFile.getPath()).equalsIgnoreCase("jpeg")) {
                        currentDirectoryFiles.add(currentFile);
                    }
                }
            }
            File picturesToImport[] = new File[currentDirectoryFiles.size()];
            for (int i = 0; i < picturesToImport.length; ++i) {
                picturesToImport[i] = currentDirectoryFiles.get(i);
            }
            Library.importPicture(picturesToImport);
        }
    }*/

    /**
     *
     *
     * @param importedPictures File array for all pictures being imported in this batch
     */
    public static void importPicture(ArrayList<Picture> importedPictures) {

        //specify no. of threads not including thread for leftover pictures
        int noOfThreads = 10;
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
                    PictureLabel currentThumb = new PictureLabel(importedPictures.get(j), MainFrame.getMainFrames().get(0).getPicturesPanel());
                    thumbsToImport.add(currentThumb);
                    MainFrame.getMainFrames().get(0).getPicturesPanel().addThumbToDisplay(currentThumb);
                }
                //new ThumbnailImportThread(thumbsToImport).start();

            }
            //import leftover pictures
            if (leftover > 0) {
                ArrayList<PictureLabel> thumbsToImport = new ArrayList<PictureLabel>();
                for (int i = 0; i < leftover; ++i) {
                    PictureLabel currentThumb = new PictureLabel(importedPictures.get(importSize - i - 1), MainFrame.getMainFrames().get(0).getPicturesPanel());
                    thumbsToImport.add(currentThumb);
                    MainFrame.getMainFrames().get(0).getPicturesPanel().addThumbToDisplay(currentThumb);
                }

                //new ThumbnailImportThread(thumbsToImport).start();
            }
        }
        //if there are less pictures than threads to import pictures, import all pictures on 1 thread :))
        else {
            ArrayList<PictureLabel> thumbsToImport = new ArrayList<PictureLabel>();
            for (Picture currentPic: importedPictures) {
                PictureLabel currentThumb = new PictureLabel(currentPic, MainFrame.getMainFrames().get(0).getPicturesPanel());
                thumbsToImport.add(currentThumb);
                MainFrame.getMainFrames().get(0).getPicturesPanel().addThumbToDisplay(currentThumb);
            }
            //new ThumbnailImportThread(thumbsToImport).start();
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
		pictureLibrary.add(picture);
	}
	
	public static void deletePictureLibrary()
	{
		pictureLibrary = null;
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
