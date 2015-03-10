package GUI;

import static java.awt.Color.WHITE;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import Core.Library;
import Core.Settings;
import Data.Picture;

public class PicturesFrame extends JPanel {

	private GridLayout picturePanelLayout;
	private MainFrame mainFrame;
	public PictureLabel[][] thumbsOnDisplay2DArray;

	private PictureLabel mostRecentSelection = null;
	private ArrayList<PictureLabel> thumbsOnDisplay = new ArrayList<PictureLabel>();
	private ArrayList<PictureLabel> selectedThumbs = new ArrayList<PictureLabel>();

	private int currentColumnCount = 0;
	private boolean picturePanelBiggerThanFrame = false;

	private int currentRow = 0;
	private int currentColumn = 0;
	private boolean shiftIsPressed;

	public PicturesFrame(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		picturePanelLayout = new GridLayout(0, 1, 2, 2);
		this.setLayout(picturePanelLayout);
		this.setBackground(WHITE);
        addListeners();
	}

	private void addListeners() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setFocusable(true);
				requestFocus();
			}
		});

	}

	public void setCurrentPosition(int row, int col) {
		currentRow = row;
		currentColumn = col;
	}

	public void adjustColumnCount(int zoomValue) {

		/*
		 * if(picturePanelBiggerThanFrame) {
		 * 
		 * //picturePanelBiggerThanFrame = false; } else { currentPanelSize =
		 * (int) Math.round(getSize().getWidth()); }
		 */

		int newColumnCount;
		int currentPanelSize = ((int) Math
				.round(mainFrame.getSize().getWidth())) - 460;

		newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[zoomValue];

		if (picturePanelLayout.getColumns() != newColumnCount
				&& newColumnCount != 0) {
			picturePanelLayout.setColumns(newColumnCount);

			picturePanelLayout.setHgap(2);
			picturePanelLayout.setVgap(2);

			this.revalidate();
			this.repaint();
			createThumbnailArray();
		}

	}

	public PictureLabel getMostRecentSelection() {
		return mostRecentSelection;
	}

	public void setMostRecentSelection(PictureLabel selection) {
		mostRecentSelection = selection;
	}

	public synchronized ArrayList<PictureLabel> getThumbsOnDisplay() {
		return thumbsOnDisplay;
	}

    public synchronized ArrayList<Picture> getPicturesOnDisplay() {
        ArrayList<Picture> allPicturesOnDisplay = new ArrayList<Picture>();
        for (PictureLabel p: thumbsOnDisplay) {
            allPicturesOnDisplay.add(p.getPicture());
        }
        return allPicturesOnDisplay;
    }

	public synchronized void addThumbToDisplay(PictureLabel thumb) {
		thumbsOnDisplay.add(thumb);
	}

	public void removeThumbFromDisplay(PictureLabel thumb) {
		thumbsOnDisplay.remove(thumb);
	}

	public void removeAllThumbsFromDisplay() {
		thumbsOnDisplay = new ArrayList<PictureLabel>();
	}

	public ArrayList<PictureLabel> getSelectedThumbs() {
		return selectedThumbs;
	}

	public ArrayList<Picture> getSelectedPictures() {
		ArrayList<Picture> selectedPictures = new ArrayList<Picture>();
		for (PictureLabel p : selectedThumbs) {
			selectedPictures.add(p.getPicture());
		}
		return selectedPictures;
	}

	public void addSelectedThumb(PictureLabel selectedThumb) {
		selectedThumbs.add(selectedThumb);
	}

	public void removeSelectedThumb(PictureLabel selectedThumb) {
		selectedThumbs.remove(selectedThumb);
	}

	public void removeAllSelectedThumbs() {
		for (PictureLabel p : selectedThumbs) {
			p.toggleSelection();
		}
		selectedThumbs.clear();
	}

	/**
	 * Creates a 2D array of currently shown thumbnails. used in the key
	 * listener for selection using keyboard
	 */
	public void createThumbnailArray() {
		int columnCount = picturePanelLayout.getColumns();
		int rowCount = getThumbsOnDisplay().size() / columnCount;
		if (getThumbsOnDisplay().size() % columnCount != 0) {
			++rowCount;
		}
		System.out.println(rowCount);
		int thumbnailCounter = 0;

		thumbsOnDisplay2DArray = new PictureLabel[rowCount][columnCount];
		for (int i = 0; i < rowCount; ++i) {
			for (int j = 0; j < columnCount; ++j) {
				if (thumbnailCounter != getThumbsOnDisplay().size()) {
					thumbsOnDisplay2DArray[i][j] = getThumbsOnDisplay().get(
							thumbnailCounter++);
				}
			}
		}
		refresh();
	}

	public void refresh() {
		if (getMostRecentSelection() != null) {
			for (int i = 0; i < thumbsOnDisplay2DArray.length; ++i) {
				for (int j = 0; j < thumbsOnDisplay2DArray[i].length; ++j) {
					if (thumbsOnDisplay2DArray[i][j] != null
							&& thumbsOnDisplay2DArray[i][j]
									.equals(getMostRecentSelection())) {
						setCurrentPosition(i, j);
						break;
					}
				}
			}
		}
	}

	public void addThumbnailsToView(ArrayList<PictureLabel> picturesToDisplay,
			int zoomSize) {

            for (PictureLabel currentThumb: picturesToDisplay) {
                this.add(currentThumb);
                currentThumb.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSize]);
            }
		createThumbnailArray();
	}

	public void keyAction(KeyEvent e, boolean shiftIsPressed) {
		this.shiftIsPressed = shiftIsPressed;
		if (Library.getPictureLibrary().size() > 0) {
			switch (getSelectedThumbs().size()) {
			case 0:
				setMostRecentSelection(thumbsOnDisplay2DArray[currentRow][currentColumn]);
				getMostRecentSelection().toggleSelection();
				addSelectedThumb(getMostRecentSelection());
				break;
			case 1:
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					moveSingle(0, -1);
					break;
				case KeyEvent.VK_RIGHT:
					moveSingle(0, 1);
					break;
				case KeyEvent.VK_UP:
					moveSingle(-1, 0);
					break;
				case KeyEvent.VK_DOWN:
					moveSingle(1, 0);
					break;
				}
				break;
			default:
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					moveMultiple(0, -1);
					break;
				case KeyEvent.VK_RIGHT:
					moveMultiple(0, 1);
					break;
				case KeyEvent.VK_UP:
					moveMultiple(-1, 0);
					break;
				case KeyEvent.VK_DOWN:
					moveMultiple(1, 0);
					break;

				}
				break;

			}
			mainFrame.createTagLabels();
		}
	}

	private void moveSingle(int row, int col) {
		if (row == -1) {
			if (currentRow > 0) {
				moveSingleInner(-1, 0);
			}
		}
		if (row == 1) {
			if (currentRow < thumbsOnDisplay2DArray.length - 1) {
				moveSingleInner(1, 0);
			}
		}
		if (col == -1) {
			if (currentColumn > 0) {
				moveSingleInner(0, -1);
			}
		}
		if (col == 1) {
			if (currentColumn < thumbsOnDisplay2DArray[currentRow].length - 1) {
				moveSingleInner(0, 1);
			}
		}
	}

	private void moveSingleInner(int row, int col) {
		if (thumbsOnDisplay2DArray[currentRow + row][currentColumn + col] != null) {
			if (!shiftIsPressed) {
				getMostRecentSelection().toggleSelection();
				removeSelectedThumb(getMostRecentSelection());
			}
			currentRow += row;
			currentColumn += col;
			setMostRecentSelection(thumbsOnDisplay2DArray[currentRow][currentColumn]);
			getMostRecentSelection().toggleSelection();
			addSelectedThumb(getMostRecentSelection());
		}
	}

	private void moveMultiple(int row, int col) {
		if (row == -1) {
			if (currentRow > 0) {
				moveMultipleInner(-1, 0);
			} else {
				if (!shiftIsPressed) {
					removeAllSelectedThumbs();
					getMostRecentSelection().toggleSelection();
					addSelectedThumb(getMostRecentSelection());
				}
			}
		}
		if (row == 1) {
			if (currentRow < thumbsOnDisplay2DArray.length - 1) {
				moveSingleInner(1, 0);
			} else {
				if (!shiftIsPressed) {
					removeAllSelectedThumbs();
					getMostRecentSelection().toggleSelection();
					addSelectedThumb(getMostRecentSelection());
				}
			}
		}
		if (col == -1) {
			if (currentColumn > 0) {
				moveMultipleInner(0, -1);
			} else {
				if (!shiftIsPressed) {
					removeAllSelectedThumbs();
					getMostRecentSelection().toggleSelection();
					addSelectedThumb(getMostRecentSelection());
				}
			}
		}
		if (col == 1) {
			if (currentColumn < thumbsOnDisplay2DArray[currentRow].length - 1) {
				moveMultipleInner(0, 1);
			} else {
				if (!shiftIsPressed) {
					removeAllSelectedThumbs();
					getMostRecentSelection().toggleSelection();
					addSelectedThumb(getMostRecentSelection());
				}
			}
		}
	}

	private void moveMultipleInner(int row, int col) {

		if (thumbsOnDisplay2DArray[currentRow + row][currentColumn + col] != null) {
			currentRow += row;
			currentColumn += col;
			setMostRecentSelection(thumbsOnDisplay2DArray[currentRow][currentColumn]);
			if (!shiftIsPressed) {
				removeAllSelectedThumbs();
				getMostRecentSelection().toggleSelection();
				addSelectedThumb(getMostRecentSelection());
			} else {
				if (!getMostRecentSelection().isSelected()) {
					getMostRecentSelection().toggleSelection();
					addSelectedThumb(getMostRecentSelection());
				}
			}
		} else {
			if (!shiftIsPressed) {
				removeAllSelectedThumbs();
				getMostRecentSelection().toggleSelection();
				addSelectedThumb(getMostRecentSelection());
			}
		}

	}

	public boolean isShiftPressed() {
		return mainFrame.isShiftPressed();
	}

	public void createTagLabels() {
		mainFrame.createTagLabels();
	}
	
	public JPanel getCenterPanel() {
		return mainFrame.getCenterPanel();
	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
