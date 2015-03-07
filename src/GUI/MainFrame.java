package GUI;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Core.Library;
import Core.Settings;
import Core.Taggable;
import Data.Area;
import Data.Child;
import Data.Picture;
import ch.rakudave.suggest.JSuggestField;

public class MainFrame extends JFrame {
	// menu bar component declaration
	private MenuBar menuBar = new MenuBar();
	Menu fileMenu;
	Menu editMenu;
	Menu toolsMenu;
	Menu helpMenu;
	MenuItem impMenuItem;
	MenuItem expMenuItem;
	MenuItem backupMenuItem;
	MenuItem exitMenuItem;
	MenuItem rotateMenuItem;
	MenuItem resizeMenuItem;
	MenuItem cropMenuItem;
	MenuItem selMenuItem;
	MenuItem tagMenuItem;
	MenuItem deleteMenuItem;
	MenuItem printMenuItem;

	// root panel declaration
	private JPanel mainPanel;

	// north component declaration
	private JPanel northPanel;
	private JPanel searchPanel;
    private JPanel searchLabelPanel;
	private JSuggestField searchField;
    private ArrayList<Taggable> currentSearchTags;
	private JCheckBox taggedCheckBox;
	private JCheckBox unTaggedCheckBox;
	private JCheckBox incompleteCheckBox;
	private JCheckBox allCheckBox;
	private JPanel sortByPanel;
	private JLabel sortByLabel;

	// west component declaration
	private JPanel westPanel;
	private JPanel buttonPanel;
	private JButton importButton;
	private JButton exportButton;
	private JButton backupButton;
	private JButton rotateButton;
	private JButton deleteButton;
	private JButton printButton;

	// center component declaration
	private JPanel centerPanel;
	private JPanel innerCenterPanel;
	private PicturesFrame picturePanel;
	private JScrollPane picturePanelScrollPane;
	private JPanel scrollPanel;
	private JSlider zoomSlider;

	// selection stuff
	private boolean shiftIsPressed = false;
	private boolean controlIsPressed = false;

	// east component declaration
	private JPanel eastPanel;
	private JPanel tagPanel;
	private JPanel descriptionPanel;
	private JPanel donePanel;
	public TagPanel storedTagsPanel;
	private JSuggestField tagField;
	private JButton doneButton;
	private JButton resetButton;

	private Listeners.ThumbnailClickListener tcl;
	private static ArrayList<MainFrame> frames = new ArrayList<MainFrame>();

	private File[] savedFiles;

	/**
	 * Constructor for the application
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public MainFrame() throws IOException, ClassNotFoundException {

		// root panel assignment
		mainPanel = new JPanel(new BorderLayout());

        // addSavedData();
        startUpChecks();
        loadTaggableComponents();
		createMenuBar();
		createPanels();
		addListeners();
        if (Settings.PICTURE_HOME_DIR != null) {
            Library.importFolder(Settings.PICTURE_HOME_DIR);
        }

		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(mainPanel);

		setTitle("Photo Management Software");
		// setMinimumSize(new Dimension(1333, 766));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		frames.add(this);
	}

	// for csv file, location and picture library, if any has not
	// been initialised, prompt the user on startup
	private void startUpChecks() {
		// if the site of the machine was not set, prompt user to set it.
		if (Settings.NURSERY_LOCATION == null) {

			String selectedSite = null;
			while (selectedSite == null) {
				selectedSite = (String) JOptionPane.showInputDialog(this,
						"In which nursery site is this computer?",
						"Select Site", JOptionPane.PLAIN_MESSAGE, null,
						Library.getNurserySites(), "Rosendale");

				if ((selectedSite != null) && (selectedSite.length() > 0)) {
					Settings.NURSERY_LOCATION = selectedSite;
					break;
				}
			}
		}

		if (Settings.CSV_PATH == null) {
			final JFileChooser csvFileChooser = new JFileChooser();
			csvFileChooser.setDialogTitle("Select children list CSV file");
			csvFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"CSV File", "csv"));
			csvFileChooser.setAcceptAllFileFilterUsed(false);
			int wasFileSelected = csvFileChooser.showOpenDialog(this);

			if (wasFileSelected == JFileChooser.APPROVE_OPTION) {
				Settings.CSV_PATH = csvFileChooser.getSelectedFile().getPath();
			} else {
				JOptionPane.showMessageDialog(this,
						"Without importing a CSV file, it is not possible to tag pictures.\n"
								+ "You can import a CSV from the file menu.");
			}
		}

		if (Settings.PICTURE_HOME_DIR == null) {

			new JFXPanel();
			final CountDownLatch latch = new CountDownLatch(1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					DirectoryChooser directoryChooser = new DirectoryChooser();
					directoryChooser
							.setTitle("Select root directory of all pictures");
					Settings.PICTURE_HOME_DIR = directoryChooser
							.showDialog(new Stage());
					latch.countDown();
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadTaggableComponents() {

		BufferedReader br = null;
		String currentLine = "";

		if (Settings.CSV_PATH != null) {

			if (Library.getAreaList().size() == 0) {
				for (int i = 0; i < Settings.AREA_NAMES.length; ++i) {
					new Area(Settings.AREA_NAMES[i]);
				}
			}

			try {

				br = new BufferedReader(new FileReader(Settings.CSV_PATH));
				boolean firstLine = true;
				int firstNameColumn = -1, lastNameColumn = -1, roomNameColumn = -1;
				while ((currentLine = br.readLine()) != null) {

					// use comma as separator
					String[] columns = currentLine.split(",");

					if (firstLine) {
						firstLine = false;
						for (int i = 0; i < columns.length; ++i) {
							if (columns[i].equals("FirstName")) {
								firstNameColumn = i;
							} else if (columns[i].equals("Last Name")) {
								lastNameColumn = i;
							} else if (columns[i].equals("RoomName")) {
								roomNameColumn = i;
							}
						}
					} else {

						String wordsInColumn[] = columns[roomNameColumn].split(
								" ", 2);
						String siteName = wordsInColumn[0];

						String childName = columns[firstNameColumn] + " "
								+ columns[lastNameColumn];

						if (siteName.equals(Settings.NURSERY_LOCATION)) {

							boolean childExists = false;
							for (Taggable t : Library
									.getTaggableComponentsList()) {
								System.out.println(t.getName());
								if (t.getName().equals(childName)) {
									childExists = true;
									break;
								}
							}
							if (!childExists) {

								for (Taggable area : Library.getAreaList()) {
									if (area.getName().startsWith(
											wordsInColumn[1])) {
										new Child(childName, area);
									}
								}
							}
						}
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}





	private void createMenuBar() {

		// menu bar component assignment
		fileMenu = new Menu("File");
		editMenu = new Menu("Edit");
		toolsMenu = new Menu("Tools");
		helpMenu = new Menu("Help");
		impMenuItem = new MenuItem("Import");
		expMenuItem = new MenuItem("Export");
		backupMenuItem = new MenuItem("Backup");
		exitMenuItem = new MenuItem("Exit");
		rotateMenuItem = new MenuItem("Rotate");
		resizeMenuItem = new MenuItem("Resize");
		cropMenuItem = new MenuItem("Crop");
		selMenuItem = new MenuItem("Select");
		tagMenuItem = new MenuItem("Tag");
		deleteMenuItem = new MenuItem("Delete");
		printMenuItem = new MenuItem("Print");

		menuBar.add(fileMenu);
		fileMenu.add(impMenuItem);
		fileMenu.add(backupMenuItem);
		fileMenu.add(expMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		menuBar.add(editMenu);
		editMenu.add(rotateMenuItem);
		editMenu.add(resizeMenuItem);
		editMenu.add(cropMenuItem);

		menuBar.add(toolsMenu);
		toolsMenu.add(selMenuItem);
		toolsMenu.add(tagMenuItem);
		toolsMenu.add(deleteMenuItem);
		toolsMenu.add(printMenuItem);

		menuBar.add(helpMenu);

		setMenuBar(menuBar);

	}

	private void createPanels() {

		/* private void createNorthPanel() */{

			// north panel component assignment
			northPanel = new JPanel(new BorderLayout());
			searchPanel = new JPanel();
            searchLabelPanel = new JPanel();
            searchLabelPanel.setName("Search");
            currentSearchTags = new ArrayList<Taggable>();
			searchField = new JSuggestField(MainFrame.this,
					Library.getTaggableComponentNamesVector(true));
			searchField.setPreferredSize(new Dimension(210, 30));
			taggedCheckBox = new JCheckBox("Tagged");
			unTaggedCheckBox = new JCheckBox("Untagged");
			incompleteCheckBox = new JCheckBox("Incomplete");
			allCheckBox = new JCheckBox("All");
			sortByPanel = new JPanel();
			sortByLabel = new JLabel("Filter: ");

			taggedCheckBox.setMnemonic(KeyEvent.VK_T);
			taggedCheckBox.setSelected(false);
			unTaggedCheckBox.setMnemonic(KeyEvent.VK_T);
			unTaggedCheckBox.setSelected(false);
			incompleteCheckBox.setMnemonic(KeyEvent.VK_T);
			incompleteCheckBox.setSelected(false);
			allCheckBox.setMnemonic(KeyEvent.VK_T);
			allCheckBox.setSelected(true);
			searchPanel.add(sortByLabel);
			searchPanel.add(searchField);
			sortByPanel.add(taggedCheckBox);
			sortByPanel.add(unTaggedCheckBox);
			sortByPanel.add(incompleteCheckBox);
			sortByPanel.add(allCheckBox);

			mainPanel.add(northPanel, BorderLayout.NORTH);
			northPanel.add(searchPanel, BorderLayout.WEST);
			northPanel.add(sortByPanel, BorderLayout.EAST);
            northPanel.add(searchLabelPanel, BorderLayout.CENTER);

			TitledBorder titledBorder = new TitledBorder("Search: ");
			EmptyBorder emptyBorder = new EmptyBorder(3, 3, 3, 3);
			CompoundBorder compoundBorder = new CompoundBorder(emptyBorder,
					titledBorder);
			northPanel.setBorder(compoundBorder);

		}

		/* private void createWestPanel() */{

			// west component assignment
			westPanel = new JPanel(new BorderLayout());
			buttonPanel = new JPanel();
			importButton = new JButton("Import");
			exportButton = new JButton("Export");
			backupButton = new JButton("Backup");
			rotateButton = new JButton("Rotate");
			deleteButton = new JButton("Delete");
			printButton = new JButton("Print");

			mainPanel.add(westPanel, BorderLayout.WEST);
			westPanel.add(buttonPanel, BorderLayout.NORTH);
			buttonPanel.setLayout(new GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(4, 4, 4, 4);
			c.gridx = 0;
			c.gridy = 0;
			buttonPanel.add(importButton, c);
			c.gridy = 1;
			buttonPanel.add(exportButton, c);
			c.gridy = 2;
			buttonPanel.add(backupButton, c);
			c.gridy = 3;
			buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
			c.gridy = 6;
			buttonPanel.add(rotateButton, c);
			c.gridy = 9;
			buttonPanel.add(deleteButton, c);
			c.gridy = 10;
			buttonPanel.add(printButton, c);

			TitledBorder titledBorder = new TitledBorder("Tools: ");
			westPanel.setBorder(titledBorder);

		}

		/* private void createCenterPanel() */{

			// center component assignment
			picturePanel = new PicturesFrame(this);

			picturePanelScrollPane = new JScrollPane(picturePanel);
			picturePanelScrollPane
					.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
			// picturePanelScrollPane.setBackground(WHITE);

			zoomSlider = new JSlider(Adjustable.HORIZONTAL, 0, 9, 4);
			scrollPanel = new JPanel();
			scrollPanel.add(zoomSlider);

			TitledBorder titledBorder = new TitledBorder("Pictures: ");
			EmptyBorder emptyBorder = new EmptyBorder(7, 7, 1, 7);
			CompoundBorder compoundBorder = new CompoundBorder(emptyBorder,
					titledBorder);

			innerCenterPanel = new JPanel(new BorderLayout());
			innerCenterPanel.setBorder(compoundBorder);
			innerCenterPanel.add(picturePanelScrollPane, BorderLayout.CENTER);
			innerCenterPanel.add(scrollPanel, BorderLayout.SOUTH);

			centerPanel = new JPanel(new BorderLayout());
			centerPanel.add(innerCenterPanel, BorderLayout.CENTER);

			mainPanel.add(centerPanel, BorderLayout.CENTER);

		}

		/* private void createEastPanel() */{

			// east component assignment

			/**
			 * for testing purposes our names and some mock areas are added to
			 * the list
			 */

			tagField = new JSuggestField(this,
					Library.getTaggableComponentNamesVector(false));
			tagField.setSize(210, 30);

			storedTagsPanel = new TagPanel(this);

			tagPanel = new JPanel(new BorderLayout());
			tagPanel.add(storedTagsPanel, BorderLayout.CENTER);
			tagPanel.add(tagField, BorderLayout.NORTH);

			doneButton = new JButton("Done");
			resetButton = new JButton("Reset");

			donePanel = new JPanel();
			donePanel.add(resetButton);
			donePanel.add(doneButton);

			descriptionPanel = new JPanel(new BorderLayout());
			descriptionPanel.setBorder(new TitledBorder("Add desription: "));
			descriptionPanel.add(new JScrollPane(new JTextArea(7, 21)),
					BorderLayout.NORTH);
			descriptionPanel.add(donePanel, BorderLayout.SOUTH);

			eastPanel = new JPanel(new BorderLayout());
			eastPanel.setBorder(new TitledBorder("Add Tag: "));
			eastPanel.add(descriptionPanel, BorderLayout.SOUTH);
			eastPanel.add(tagPanel, BorderLayout.NORTH);

			mainPanel.add(eastPanel, BorderLayout.EAST);

		}
	}

	private void addListeners() {

		Listeners l = new Listeners();

		tcl = l.new ThumbnailClickListener();

		// add key listener for thumbnail selection using keyboard
		this.addKeyListener(l.new KeysListener());
		this.addKeyListener(tcl);

		searchField.addSelectionListener(l.new SearchListener());
		tagField.addSelectionListener(l.new TagListener());
		importButton.addActionListener(l.new ImportButtonListener());

		// exit menu item listener
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to quit?", "Warning!",
						JOptionPane.YES_NO_OPTION);
				if (dialogButton == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		// change picture thumbnail size when slider is used
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				final ArrayList<PictureLabel> thumbs = picturePanel
						.getThumbsOnDisplay();

				Thread sliderChangeThread = new Thread() {
					public void run() {

						try {
							for (PictureLabel currentThumbnail : thumbs) {
								currentThumbnail
										.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider
												.getValue()]);
							}
						} finally {
							picturePanel.adjustColumnCount(zoomSlider
									.getValue());
						}
					}
				};
				sliderChangeThread.start();
			}
		});

		// adjust number of columns when window size changes
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {

				picturePanel.adjustColumnCount(zoomSlider.getValue());
				// TODO: Fix this method !
				/*
				 * int currentPanelSize = (int)
				 * Math.round(picturePanel.getSize().getWidth()); int
				 * currentWindowSize = (int)
				 * Math.round(e.getComponent().getSize().getWidth()); int
				 * framePanelGap = currentWindowSize - currentPanelSize;
				 * 
				 * if (framePanelGap < 450) { picturePanelBiggerThanFrame =
				 * true;
				 * 
				 * 
				 * } else { picturePanelBiggerThanFrame = false;
				 * picturePanel.adjustColumnCount(zoomSlider.getValue()); }
				 */
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		/*
		 * Whenever the scroll pane is scrolled, generates thumbnails coming
		 * into view and deletes thumbnails exiting view.
		 */
		picturePanelScrollPane.getViewport().addChangeListener(
				new ChangeListener() {

					public void stateChanged(ChangeEvent e) {
						Rectangle currentView = picturePanel.getVisibleRect();
						for (PictureLabel currentThumbnail : picturePanel
								.getThumbsOnDisplay()) {
							if (currentThumbnail.isHorizontal()) {
								if (isInView(currentThumbnail, currentView)) {
									if (currentThumbnail.getIcon() == null) {
										currentThumbnail
												.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider
														.getValue()]);
									}
								} else {
									currentThumbnail.hideThumbnail();
								}
							}
						}
					}
				});

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO: probably remove reset button
			}
		});

		doneButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: see if we need done button
			}
		});
	}

	private class Listeners {
		public class KeysListener implements KeyListener {

			public KeysListener() {
				super();
				shiftIsPressed = false;
				controlIsPressed = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				if (k == KeyEvent.VK_SHIFT) {
					shiftIsPressed = true;
				} else if (k == KeyEvent.VK_CONTROL) {
					controlIsPressed = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					shiftIsPressed = false;
				} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					controlIsPressed = false;
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

		}

		private class ImportButtonListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				// open system file manager to ask user for pictures to import
				FileDialog importDialog = new FileDialog(MainFrame.this,
						"Choose picture(s) to import", FileDialog.LOAD);
				importDialog.setFile("*.jpg");
				importDialog.setMultipleMode(true);
				importDialog.setVisible(true);
				if (Library.getPictureLibrary().size() == 0) {
					setFocusable(true);
					requestFocus();
				}
				// import pictures into library
				//Library.importPicture(importDialog.getFiles());
			}
		}

		/**
		 * method to action the tag field
		 */
		private class TagListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Picture> picturesToTag = picturePanel
						.getSelectedPictures();
				if (picturesToTag.size() == 0) {
					// TODO: no thumnails selected, either reset texfield or
					// simply disable them until picture/s selected
				} else {
					for (Taggable t : Library.getTaggableComponentsList()) {
						if (tagField.getText().toLowerCase()
								.equals(t.getName().toLowerCase())) {
							for (Picture p : picturePanel.getSelectedPictures()) {
								if (!p.getTag().getTaggedComponents()
										.contains(t)) {
									// if this is an area tag only tag if
									// picture has no area tag
									if (!(t.getType() == Settings.AREA_TAG && p
											.getTag().isAreaSet())) {
										p.getTag().addTag(t);
										t.addTaggedPicture(p);
										createTagLabels();
									}
								}
							}
						}
					}
				}
				tagField.setText("");
			}
		}

		/**
		 * method to action the search
		 */
		private class SearchListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (searchField.getText().equals("View All")) {
                    searchLabelPanel.removeAll();
                    currentSearchTags.clear();
                    refreshSearch();
				} else {
                    ArrayList<Taggable> allTaggableComponents = Library
                            .getTaggableComponentsList();

                    // gets text from GUI to a string
                    String searchString = searchField.getText().toLowerCase();

                    // loops to the end of tagged children
                    for (int i = 0; i < allTaggableComponents.size(); ++i) {
                        if (searchString.equalsIgnoreCase(allTaggableComponents
                                .get(i).getName())) {
                            if (!currentSearchTags.contains(allTaggableComponents.get(i))) {
                                addSearchTag(allTaggableComponents.get(i));
                                searchLabelPanel.add(new TagPanel.TagTextLabel(true, allTaggableComponents.get(i), searchLabelPanel, MainFrame.this));
                                refreshSearch();
                                validate();
                            }
                        }
                    }
				}
			}
		}

		public class ThumbnailClickListener implements KeyListener {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

				picturePanel.keyAction(e, shiftIsPressed);
			}
		}
	}

    public void refreshSearch() {
        ArrayList<Picture> allPictureSet = new ArrayList<Picture>();
        searchLabelPanel.repaint();

        if (currentSearchTags.size() == 0) {
            allPictureSet = Library.getPictureLibrary();
        }
        else if (currentSearchTags.size() > 1) {
            for (int i = 0; i < currentSearchTags.size() - 1; ++i) {
                ArrayList<Picture> currentPictureSet = currentSearchTags.get(i).getTaggedPictures();
                for (Picture p : currentPictureSet) {
                    if (currentSearchTags.get(i + 1).getTaggedPictures().contains(p)) {
                        allPictureSet.add(p);
                    }
                }
            }
        }
        else {
            allPictureSet = currentSearchTags.get(0).getTaggedPictures();
        }

        picturePanel.removeAll();
        picturePanel.repaint();
        picturePanel.removeAllThumbsFromDisplay();
        picturePanel.addThumbnailsToView(allPictureSet, zoomSlider.getValue());
        picturePanel.createThumbnailArray();
    }

	/**
	 * Call this method when the display needs to be updated
	 */
	public static void updateViews() {
		for (MainFrame mainFrame : frames) {
			mainFrame.updateView();
		}
	}

	/**
	 * This method is being called when refreshing the display. Put here all
	 * reconstructors.
	 */
	private void updateView() {
		// TODO maybe have multiple methods for pics, tags etc that run in
		// separate threads
	}

    public void addSearchTag(Taggable t) {
        if (!currentSearchTags.contains(t)) {
            currentSearchTags.add(t);
        }
    }

    public void removeSearchTag(Taggable t) {
            currentSearchTags.remove(t);
    }

	/**
	 * Gets the MainFrame objects. In general a list with one window - the app
	 * 
	 * @return
	 */
	public static ArrayList<MainFrame> getMainFrames() {
		return frames;
	}

	public boolean isShiftPressed() {
		return shiftIsPressed;
	}

	public boolean isControlPressed() {
		return controlIsPressed;
	}

	public void createTagLabels() {
		storedTagsPanel.resetTagLabels();
	}

	/**
	 * Automatically saving pictures when the application is closed.
	 */
	private void saveData() {
		this.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				try {
					FileOutputStream savedFile = new FileOutputStream(
							"savedLibrary.ser");
					ObjectOutputStream savedObject = new ObjectOutputStream(
							savedFile);
					savedObject.writeObject(Library.getPictureLibrary());
					savedObject.close();
					System.out.println("Saved");
				} catch (FileNotFoundException ex1) {
					ex1.printStackTrace();
				} catch (IOException ex2) {
					ex2.printStackTrace();
				}
				Library.deletePictureLibrary();
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});
	}

	/*
	 * Automatically adding pictures that have been imported and saved before
	 * thus a *.ser file is created. The latter was also pushed into the
	 * 'Displaying and saving images' branch
	 */
	private void addSavedData() throws IOException, ClassNotFoundException {
		FileInputStream savedFile = new FileInputStream("savedLibrary.ser");
		ObjectInputStream restoredObject = new ObjectInputStream(savedFile);
		try {
			ArrayList<Picture> savedData = (ArrayList<Picture>) restoredObject
					.readObject();
			for (int i = 0; i < savedData.size(); i++) {
				Picture recreatedPicture = new Picture(new File(savedData
						.get(i).getImagePath()));
				recreatedPicture.setTag(savedData.get(i).getTag());
				ArrayList<Picture> savedPictures = new ArrayList<Picture>();
				savedPictures.add(recreatedPicture);
				picturePanel.addThumbnailsToView(savedPictures,
						zoomSlider.getValue());
				Library.getPictureLibrary().add(recreatedPicture);
			}
			restoredObject.close();
		} catch (EOFException exception) {
			exception.printStackTrace();
		}
	}

	// returns CenterPanel
	public JPanel getCenterPanel() {
		return centerPanel;
	}

	// returns innerCenterPanel
	public JPanel getInnerCenterPanel() {
		return innerCenterPanel;
	}

	public PicturesFrame getPicturesPanel() {
		return picturePanel;
	}

	/* returns true if a pictureLabel is in view in the scroll pane */
	private static boolean isInView(PictureLabel thumbnail,
			Rectangle currentView) {

		if (thumbnail.getBounds().intersects(currentView)) {
			return true;
		} else {
			return false;
		}
	}

	// {
	/**
	 * If a new thumbnail is selected and only part of the thumbnail is visible
	 * in the scrollpane, the scrollbar goes up or down depending on direction.
	 *
	 */
	/*
	 * public static void scrollVertical(String direction) { Rectangle
	 * currentView = picturePanel.getVisibleRect(); JScrollBar jsb =
	 * picturePanelPane.getVerticalScrollBar(); if
	 * (ThumbnailClickListener.picturePanel.getMostRecentSelection() != null) {
	 * if (tcl.picturePanel.getMostRecentSelection().getVisibleRect().isEmpty())
	 * { if (direction == "UP") { jsb.setValue(jsb.getValue() - 1); } else if
	 * (direction == "DOWN") { jsb.setValue(jsb.getValue() + 1); } } else { if
	 * (direction == "UP") { jsb.setValue(jsb.getValue() - 150); } else if
	 * (direction == "DOWN") { jsb.setValue(jsb.getValue() + 150); } } } }
	 */
	// }

	/**
	 * Divides the current size of the picture panel by current thumbnail size
	 * to determine required number of columns in GridLayout.
	 */

	public int getZoomValue() {
		return zoomSlider.getValue();
	}

	public static void main(String[] args) {

		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			new MainFrame();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// IMPORT PARENT DIR
		// Library.importFolder(Settings.PICTURE_HOME_DIR);
	}

}
