package Core;

import Data.Area;
import Data.Child;
import Data.Picture;
import Data.Tag;
import GUI.MainFrame;
import GUI.PictureLabel;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class Library implements Serializable {

	private static ArrayList<Child> childrenList = new ArrayList<Child>();
	private static Iterator childrenListIterator = childrenList.iterator();
	private static ArrayList<Picture> pictureLibrary = new ArrayList<Picture>();
	private static Iterator pictureLibraryIterator = pictureLibrary.iterator();
	private static ArrayList<Area> areaList = new ArrayList<Area>();
	private static Iterator areasListIterator = areaList.iterator();
/**/	private static ArrayList<Date> possibleDate = new ArrayList<Date>();
/**/	private static Iterator possibleDateIterator = possibleDate.iterator();
	private static ArrayList<PictureLabel> thumbsOnDisplay = new ArrayList<PictureLabel>();
	private static ArrayList<PictureLabel> selectedThumbs = new ArrayList<PictureLabel>();

	public static synchronized ArrayList<Child> getChildrenList() {
		return childrenList;
	}

    public static synchronized ArrayList<Area> getAreaList() {
        return areaList;
    }

	public static Vector<String> getChildrenNamesVector() {
		Vector<String> childrenNames = new Vector<String>();
		for (Child c : childrenList) {
			childrenNames.add(c.getName());
		}
		return childrenNames;
	}

	public static Vector<String> getAreaNamesVector() {
		Vector<String> areaNames = new Vector<String>();
        for (Area a : areaList) {
            areaNames.add(a.getName());
        }
        return areaNames;
	}

    public static Vector<String> getAllNamesVector() {
        Vector<String> allNames = getChildrenNamesVector();
        allNames.add("All Pictures");
        for (int i = 0; i < getAreaNamesVector().size(); ++i) {
            allNames.add(getAreaNamesVector().get(i));
        }
        return allNames;

    }

	public static synchronized ArrayList<Picture> getPictureLibrary() {
		return pictureLibrary;
	}

	/* comment section */{
		// public static void tagDate(ArrayList<Picture> picturesToTag, Date
		// dateToTag) {
		// //TO DO
		// }
		//
		// public static void tagArea(ArrayList<Picture> picturesToTag, String
		// Area) {
		// //TO DO
		// }
		//
		// public static void tagChildren(ArrayList<Picture> picturesToTag,
		// ArrayList<String> childreninPicture) {
		// //TO DO
		// }
	}

	public static void importPicture(final File[] importedPictures) {

		Thread newPictureImport = new Thread() {

			ArrayList<Picture> picturesToDisplay = new ArrayList<Picture>();

			public void run() {
				try {
					// for evey file path sent from importing in GUI
					for (int i = 0; i < importedPictures.length; ++i) {

						// check if it is already imported into library
						boolean exists = false;
						for (int j = 0; j < pictureLibrary.size(); ++j) {
							if (importedPictures[i].getPath().equals(
									pictureLibrary.get(j).getImagePath())) {
								exists = true;
							}
						}
						System.out.println(exists);
						// if it doesn't exist in library
						if (!exists) {
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
		// System.out.println("USED MEMORY: " +
		// Runtime.getRuntime().totalMemory() + "FREE MEMORY: " +
		// Runtime.getRuntime().freeMemory());
	}

	public static void importFolder(String folderPath) {
		// TODO for every picture in folder do importPicture()
	}

	public static ArrayList<Picture> searchByChild(String childName) {
		ArrayList<Picture> result = new ArrayList<Picture>();
		// TODO
		return result;
	}

	/* comment section */{

		// public static ArrayList<Picture> searchByDate(Date date) {
		// ArrayList<Picture> result = new ArrayList<Picture>();
		// //TO DO
		// return result;
		// }
		//
		// public static ArrayList<Picture> searchByArea(String area) {
		// ArrayList<Picture> result = new ArrayList<Picture>();
		// //TO DO
		// return result;
		// }
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
		childrenList.add(child);
	}

	public static void removeChild(Child child) {
		childrenList.remove(child);
	}

	public static void addArea(Area area) {
		areaList.add(area);
	}

	public static void removeArea(Area area) {
		areaList.remove(area);
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

    public static void removeAllThumbsFromDisplay() {
        thumbsOnDisplay = new ArrayList<PictureLabel>();
    }

	public static ArrayList<PictureLabel> getSelectedThumbs() {
		return selectedThumbs;
	}

	public static ArrayList<Picture> getSelectedPictures() {
		ArrayList<Picture> selectedPictures = new ArrayList<Picture>();
		for (PictureLabel p : selectedThumbs) {
			selectedPictures.add(p.getPicture());
		}
		return selectedPictures;
	}

	public static void addSelectedThumb(PictureLabel selectedThumb) {
		selectedThumbs.add(selectedThumb);
	}

	public static void removeSelectedThumb(PictureLabel selectedThumb) {
		selectedThumbs.remove(selectedThumb);
	}
	
	public static void deletePictureLibrary()
	{
		pictureLibrary = null;
	}
	
	public static void removeAllSelectedThumbs() {
		for (PictureLabel p : selectedThumbs) {
			p.toggleSelection();
		}
		selectedThumbs.clear();
	}

	public static void getCurrentMetadata() {
		if (selectedThumbs.size() <= 1) {
			// TODO: Show metadata on main frame
		} else {
			// TODO: compare metadata of all selected photos and show common
			// tags
		}
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
