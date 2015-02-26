package GUI;

import Core.Library;
import Core.Settings;
import Data.*;
import ch.rakudave.suggest.JSuggestField;

import org.apache.commons.lang3.text.WordUtils;

import Data.Child;
import Data.Picture;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import static java.awt.Color.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

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
	private JSuggestField searchField;
	private JCheckBox taggedCheckBox;
	private JCheckBox unTaggedCheckBox;
	private JCheckBox incompleteCheckBox;
	private JCheckBox allCheckBox;
	private JPanel sortByPanel;
	private JLabel sortByLabel;
	private JCheckBox nameAZCheckBox;
	private JCheckBox nameZACheckBox;

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
	private static GridLayout picturePanelLayout;
	private static JPanel centerPanel;
	private static JPanel innerCenterPanel;
	private static JPanel picturePanel;
	public static PictureLabel[][] thumbsOnDisplay2DArray;
	private static JScrollPane picturePanelScrollPane;
	private JPanel scrollPanel;
	private static JSlider zoomSlider;

	// selection stuff
	private PictureLabel mostRecentSelection = null;
	private boolean shiftIsPressed = false;
	private boolean controlIsPressed = false;

	private static ArrayList<PictureLabel> thumbsOnDisplay = new ArrayList<PictureLabel>();
	private static ArrayList<PictureLabel> selectedThumbs = new ArrayList<PictureLabel>();

	// east component declaration
	private JPanel eastPanel;
	private JPanel tagPanel;
	private JPanel tagsLabelsPanel;
	private JPanel tagsFieldsPanel;
	private JPanel descriptionPanel;
	private JPanel donePanel;
	public static JPanel storedTagsPanel;
	private static int tagCounter;
	private static JPanel currentTagPanel;
	private static JTextField dateField;
	private static JSuggestField areaField;
	private JSuggestField childField;
	private JLabel areaLabel;
	private JLabel dateLabel;
	private JButton doneButton;
	private JButton resetButton;

	private int currentColumnCount = 0;
	private boolean picturePanelBiggerThanFrame = false;
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

		createMenuBar();
		createPanels();
		addListeners();
		// saveData();
		// addSavedData();

		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(mainPanel);

		setTitle("Photo Management Software");
		// setMinimumSize(new Dimension(1333, 766));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		frames.add(this);
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
			northPanel = new JPanel(new GridLayout(1, 2));
			searchPanel = new JPanel();
			searchField = new JSuggestField(MainFrame.this,
					Library.getAllNamesVector());
			searchField.setPreferredSize(new Dimension(210, 30));
			taggedCheckBox = new JCheckBox("Tagged");
			unTaggedCheckBox = new JCheckBox("Untagged");
			incompleteCheckBox = new JCheckBox("Incomplete");
			allCheckBox = new JCheckBox("All");
			sortByPanel = new JPanel();
			sortByLabel = new JLabel("Filter: ");
			// nameAZ = new JCheckBox("name A-Z");
			// nameZA = new JCheckBox("name Z-A");

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
			northPanel.add(searchPanel);
			northPanel.add(sortByPanel);

			// nameAZ.setSelected(false);
			// nameZA.setSelected(false);
			// sortByPanel.add(labelSortby);
			// sortByPanel.add(nameAZ);
			// sortByPanel.add(nameZA);

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
			picturePanelLayout = new GridLayout(0, 1, 2, 2);
			picturePanel = new JPanel(picturePanelLayout);
			picturePanel.setBackground(WHITE);

			picturePanelScrollPane = new JScrollPane(picturePanel);
			picturePanelScrollPane
					.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
			picturePanelScrollPane.setBackground(WHITE);

			zoomSlider = new JSlider(Adjustable.HORIZONTAL, 0, 9, 4);
			scrollPanel = new JPanel();
			scrollPanel.add(zoomSlider);

			TitledBorder titledBorder = new TitledBorder("Pictures: ");
			EmptyBorder emptyBorder = new EmptyBorder(7, 7, 1, 7);
			CompoundBorder compoundBorder = new CompoundBorder(emptyBorder,
					titledBorder);

			/**
			 * centerPanel = new JPanel(new BorderLayout());
			 * centerPanel.setBorder(compoundBorder);
			 * centerPanel.add(picturePanelPane, BorderLayout.CENTER);
			 * centerPanel.add(scrollPanel, BorderLayout.SOUTH);
			 * 
			 * mainPanel.add(centerPanel, BorderLayout.CENTER);
			 */

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
			areaLabel = new JLabel("Area");
			dateLabel = new JLabel("Date");

			tagsLabelsPanel = new JPanel(new GridLayout(0, 1));
			tagsLabelsPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
			tagsLabelsPanel.add(new JLabel("Child name"));
			tagsLabelsPanel.add(areaLabel);
			tagsLabelsPanel.add(dateLabel);

			childField = new JSuggestField(this,
					Library.getChildrenNamesVector());
			areaField = new JSuggestField(this, Library.getAreaNamesVector());
			dateField = new JFormattedTextField();
			dateField.setColumns(12);

			tagsFieldsPanel = new JPanel(new GridLayout(0, 1));
			tagsFieldsPanel.setBorder(new EmptyBorder(17, 17, 17, 17));
			tagsFieldsPanel.add(childField);
			tagsFieldsPanel.add(areaField);
			tagsFieldsPanel.add(dateField);

			TitledBorder titledBorder = new TitledBorder(" Existing Tags ");
			EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
			CompoundBorder compoundBorder = new CompoundBorder(titledBorder,
					emptyBorder);

			storedTagsPanel = new JPanel();
			storedTagsPanel.setLayout(new BoxLayout(storedTagsPanel,
					BoxLayout.Y_AXIS));
			storedTagsPanel.setBorder(compoundBorder);

			tagPanel = new JPanel(new BorderLayout());
			tagPanel.add(tagsLabelsPanel, BorderLayout.CENTER);
			tagPanel.add(tagsFieldsPanel, BorderLayout.EAST);
			tagPanel.add(storedTagsPanel, BorderLayout.SOUTH);

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

		importButton.addActionListener(l.new ImportButtonListener());

		// change picture thumbnail size when slider is used
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				final ArrayList<PictureLabel> thumbs = getThumbsOnDisplay();

				Thread sliderChangeThread = new Thread() {
					public void run() {

						try {
							for (PictureLabel currentThumbnail : thumbs) {
								currentThumbnail
										.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider
												.getValue()]);
							}
						} finally {
							adjustColumnCount();
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

				adjustColumnCount();
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
				 * adjustColumnCount(); }
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
						for (PictureLabel currentThumbnail : getThumbsOnDisplay()) {
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

		picturePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setFocusable(true);
				requestFocus();
			}
		});

		areaField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (areaField.getText() != "") {
					boolean areaExists = false;
					for (Area a : Library.getAreaList()) {
						if (areaField.getText().toLowerCase()
								.equals(a.getName().toLowerCase())) {
							areaExists = true;
							break;
						}
					}
					if (!areaExists) {
						Area newArea = new Area(WordUtils.capitalize(areaField
								.getText()));
						for (Picture p : getSelectedPictures()) {
							if (p.getTag().getArea() == null
									|| !p.getTag().getArea().equals(newArea)) {
								p.getTag().setArea(newArea);
								newArea.addTaggedPicture(p);
							}
						}
						areaField.setSuggestData(Library.getAreaNamesVector());
						createTagLabels();

					}
				}
			}
		});

		areaField.addSelectionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Picture> picturesToTag = getSelectedPictures();
				if (picturesToTag.size() == 0) {
					// TODO: no thumnails selected, either reset texfield or
					// simply disable them until picture/s selected
				} else {
					for (Area a : Library.getAreaList()) {
						if (areaField.getText().toLowerCase()
								.equals(a.getName().toLowerCase())) {
							for (Picture p : getSelectedPictures()) {
								if (p.getTag().getArea() == null
										|| !p.getTag().getArea().equals(a)) {
									p.getTag().setArea(a);
									a.addTaggedPicture(p);
								}
							}
						}
					}
				}
			}
		});

		dateField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<Picture> picturesToTag = getSelectedPictures();
				if (picturesToTag.size() == 0) {
					// TODO: no thumnails selected, either reset texfield or
					// simply disable them until pictures selected
				} else {
					Date date = Library.getDate(dateField.getText());
					if (date != null) {
						for (Picture p : getSelectedPictures()) {
							p.getTag().setDate(date);
						}
					} else {
						// TODO do something to alert a date is not good
					}
				}

			}
		});

		childField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (childField.getText() != "") {
					boolean childExists = false;
					for (Child c : Library.getChildrenList()) {
						if (childField.getText().toLowerCase()
								.equals(c.getName().toLowerCase())) {
							childExists = true;
							break;
						}
					}
					if (!childExists) {
						Child newChild = new Child(WordUtils
								.capitalize(childField.getText()));
						System.out.println(WordUtils.capitalize(childField
								.getText()));
						for (Picture p : getSelectedPictures()) {
							if (!p.getTag().getChildren().contains(newChild)) {
								p.getTag().addChild(newChild);
								newChild.addTaggedPicture(p);
							}
						}
						childField.setSuggestData(Library
								.getChildrenNamesVector());
						createTagLabels();

					}
					childField.setText("");
				}

			}
		});

		childField.addSelectionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Picture> picturesToTag = getSelectedPictures();
				if (picturesToTag.size() == 0) {
					// TODO: no thumnails selected, either reset texfield or
					// simply disable them until picture/s selected
				} else {
					for (Child c : Library.getChildrenList()) {
						if (childField.getText().toLowerCase()
								.equals(c.getName().toLowerCase())) {
							for (Picture p : getSelectedPictures()) {
								if (!p.getTag().getChildren().contains(c)) {
									p.getTag().addChild(c);
									c.addTaggedPicture(p);
									createTagLabels();
								}
							}
						}
					}
				}
				childField.setText("");
			}
		});

		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				super.focusGained(e);
				searchField.setSuggestData(Library.getAllNamesVector());
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
				Library.importPicture(importDialog.getFiles());
			}
		}

		/**
		 * method to action the search
		 */
		private class SearchListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (searchField.getText().equals("All Pictures")) {
					picturePanel.removeAll();
					picturePanel.repaint();
					removeAllThumbsFromDisplay();
					addThumbnailsToView(Library.getPictureLibrary());
				} else {
					ArrayList<Child> allChildren = Library.getChildrenList();
					ArrayList<Area> allAreas = Library.getAreaList();

					// gets text from GUI to a string
					String searchString = searchField.getText().toLowerCase();
					boolean foundMatch = false;

					// loops to the end of tagged children
					for (int i = 0; i < allChildren.size(); ++i) {
						if (searchString.equalsIgnoreCase(allChildren.get(i)
								.getName())) {
							picturePanel.removeAll();
							picturePanel.repaint();
							removeAllThumbsFromDisplay();
							addThumbnailsToView(allChildren.get(i)
									.getTaggedPictures());
							createThumbnailArray();
							foundMatch = true;
							break;
						}
					}
					if (!foundMatch) {
						for (int i = 0; i < allAreas.size(); ++i) {
							if (searchString.equalsIgnoreCase(allAreas.get(i)
									.getName())) {
								picturePanel.removeAll();
								picturePanel.repaint();
								removeAllThumbsFromDisplay();
								addThumbnailsToView(allAreas.get(i)
										.getTaggedPictures());
								createThumbnailArray();
								// foundMatch = true;
								break;
							}
						}
					}
				}
			}
		}

		public class ThumbnailClickListener implements KeyListener {

			private int currentRow = 0;
			private int currentColumn = 0;

			public void setCurrentPosition(int row, int col) {
				currentRow = row;
				currentColumn = col;
			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

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
					createTagLabels();
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
				if (thumbsOnDisplay2DArray[currentRow + row][currentColumn
						+ col] != null) {
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

				if (thumbsOnDisplay2DArray[currentRow + row][currentColumn
						+ col] != null) {
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

		}
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

	/**
	 * Gets the MainFrame objects. In general a list with one window - the app
	 * 
	 * @return
	 */
	public static ArrayList<MainFrame> getMainFrames() {
		return frames;
	}

	public void refresh() {
		if (getMostRecentSelection() != null) {
			for (int i = 0; i < thumbsOnDisplay2DArray.length; ++i) {
				for (int j = 0; j < thumbsOnDisplay2DArray[i].length; ++j) {
					if (thumbsOnDisplay2DArray[i][j] != null
							&& thumbsOnDisplay2DArray[i][j]
									.equals(getMostRecentSelection())) {
						tcl.setCurrentPosition(i, j);
						break;
					}
				}
			}
		}
	}

	public boolean isShiftPressed() {
		return shiftIsPressed;
	}

	public boolean isControlPressed() {
		return controlIsPressed;
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
	 * This method creates child tag labels when a chlidren are tagged in a
	 * selected thumbnail. It redraws all labels for a picture every time a
	 * child is tagged or removed from the picture metadata.
	 */
	public void createTagLabels() {

		// array list to keep all children tagged in a selected
		// thumbnail or thumbnails
		ArrayList<Child> taggedChildren;
		// counter to determine whether even or odd number of labels exist
		tagCounter = 1;

		storedTagsPanel.removeAll();
		storedTagsPanel.revalidate();

		// if no pictures are selected
		if (getSelectedPictures().size() == 0) {
			// TODO: hide panel if there are no tagged pictures
		} else {
			taggedChildren = new ArrayList<Child>();
			// for every selected picture
			for (int i = 0; i < getSelectedPictures().size(); ++i) {
				// for every child that exists on any selected picture
				for (int j = 0; j < getSelectedPictures().get(i).getTag()
						.getChildren().size(); ++j) {
					// if the child isn't already on the temp array list
					if (!taggedChildren.contains(getSelectedPictures().get(i)
							.getTag().getChildren().get(j))) {
						// add child
						taggedChildren.add(getSelectedPictures().get(i)
								.getTag().getChildren().get(j));
					}
				}
			}
			// for every child found in previous loop
			for (Child c : taggedChildren) {
				// check if tagged in all selected pictures
				boolean childInAllPictures = true;
				for (Picture p : getSelectedPictures()) {
					if (!p.getTag().getChildren().contains(c)) {
						childInAllPictures = false;
						break;
					}
				}
				// if tagged in all selected pictures create a tag label and add
				// to existing panel (if odd number of labels) or create a new
				// JPanel one line below (if even number)
				if (childInAllPictures) {
					if (tagCounter % 2 == 1) {
						currentTagPanel = new JPanel();
						currentTagPanel.add(new TagTextLabel(c,
								currentTagPanel, this));
						storedTagsPanel.add(currentTagPanel);
						storedTagsPanel.validate();
						++tagCounter;
					} else {
						currentTagPanel.add(new TagTextLabel(c,
								currentTagPanel, this));
						storedTagsPanel.validate();
						++tagCounter;
					}
				}
			}

			updateSingleTags();
		}

	}

	/**
	 * Updates date and area fields. Used when there is a change in picture
	 * selection.
	 */
	private void updateSingleTags() {
		// selected pictures array and date string to go in text field
		ArrayList<Picture> picturesToTag = getSelectedPictures();

		/* date */
		String date = null;
		// if no pics selected
		if (picturesToTag.size() == 0) {
			// TODO: no thumnails selected, either reset texfield or
			// simply disable them until picture/s selected
		} else {
			// get the first picture's date
			Date date1 = picturesToTag.get(0).getTag().getDate();
			// for every pic see if the date is the same as the firs one's
			for (Picture p : picturesToTag) {
				Date date2 = p.getTag().getDate();
				if (!Library.getFormattedDate(date1).equals(
						Library.getFormattedDate(date2))) {
					date = "";
					break;
				}
			}
			// if all have same dates put the date in the field
			if (date == null)
				date = Library.getFormattedDate(date1);

			dateField.setText(date);

		}

		/* room */
		String room = null;
		// if no pics selected
		if (picturesToTag.size() == 0) {
			// TODO: no thumnails selected, either reset texfield or
			// simply disable them until picture/s selected
		} else {
			// get the first picture's area
			String area1 = null;
			if (picturesToTag.get(0).getTag().getArea() != null) {
				area1 = picturesToTag.get(0).getTag().getArea().getName();
			}

			// for every pic see if the area is the same as the first one's
			String area2 = null;
			for (Picture p : picturesToTag) {
				if (p.getTag().getArea() != null) {
					area2 = p.getTag().getArea().getName();
				}
				if (area2 == null || !area1.equals(area2)) {
					room = "";
					break;
				}
			}
			// if all have same areas put the area in the field
			if (room == null)
				room = area1;

			areaField.setText(room);

		}

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
				addThumbnailsToView(savedPictures);
				Library.getPictureLibrary().add(recreatedPicture);
			}
			restoredObject.close();
		} catch (EOFException exception) {
			exception.printStackTrace();
		}
	}

	// returns CenterPanel
	public static JPanel getCenterPanel() {
		return centerPanel;
	}

	// returns innerCenterPanel
	public static JPanel getInnerCenterPanel() {
		return innerCenterPanel;
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

	{
		/**
		 * If a new thumbnail is selected and only part of the thumbnail is
		 * visible in the scrollpane, the scrollbar goes up or down depending on
		 * direction.
		 *
		 */
		/*
		 * public static void scrollVertical(String direction) { Rectangle
		 * currentView = picturePanel.getVisibleRect(); JScrollBar jsb =
		 * picturePanelPane.getVerticalScrollBar(); if
		 * (ThumbnailClickListener.getMostRecentSelection() != null) { if
		 * (tcl.getMostRecentSelection().getVisibleRect().isEmpty()) { if
		 * (direction == "UP") { jsb.setValue(jsb.getValue() - 1); } else if
		 * (direction == "DOWN") { jsb.setValue(jsb.getValue() + 1); } } else {
		 * if (direction == "UP") { jsb.setValue(jsb.getValue() - 150); } else
		 * if (direction == "DOWN") { jsb.setValue(jsb.getValue() + 150); } } }
		 * }
		 */
	}

	/**
	 * Divides the current size of the picture panel by current thumbnail size
	 * to determine required number of columns in GridLayout.
	 */
	private void adjustColumnCount() {

		/*
		 * if(picturePanelBiggerThanFrame) {
		 * 
		 * //picturePanelBiggerThanFrame = false; } else { currentPanelSize =
		 * (int) Math.round(picturePanel.getSize().getWidth()); }
		 */

		int newColumnCount;
		int currentPanelSize = ((int) Math.round(MainFrame.this.getSize()
				.getWidth())) - 460;

		newColumnCount = currentPanelSize
				/ Settings.THUMBNAIL_SIZES[zoomSlider.getValue()];

		if (picturePanelLayout.getColumns() != newColumnCount
				&& newColumnCount != 0) {
			picturePanelLayout.setColumns(newColumnCount);

			picturePanelLayout.setHgap(2);
			picturePanelLayout.setVgap(2);

			picturePanel.revalidate();
			picturePanel.repaint();
			createThumbnailArray();
		}
	}

	/**
	 * Creates a 2D array of currently shown thumbnails. used in the key
	 * listener for selection using keyboard
	 */
	private void createThumbnailArray() {
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

	public void addThumbnailsToView(ArrayList<Picture> picturesToDisplay) {

		for (int i = 0; i < picturesToDisplay.size(); ++i) {
			PictureLabel currentThumb = new PictureLabel(
					picturesToDisplay.get(i), this);
			addThumbToDisplay(currentThumb);
			picturePanel.add(currentThumb);
			currentThumb.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider
					.getValue()]);
		}
		createThumbnailArray();
	}

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			new MainFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
