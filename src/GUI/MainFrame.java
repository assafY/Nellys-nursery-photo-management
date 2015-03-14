package GUI;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import Core.Library;
import Core.Settings;
import Core.Taggable;
import Data.Area;
import Data.Child;
import Data.Picture;
import ch.rakudave.suggest.JSuggestField;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

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
    private JPanel searchLabelPanel;
	private JSuggestField searchField;
    private ArrayList<Taggable> currentSearchTags;
	private JRadioButton taggedRadioButton;
	private JRadioButton untaggedRadioButton;
	private JRadioButton incompleteRadioButton;
	private JRadioButton allRadioButton;
    private ButtonGroup radioButtonGroup;
	private JPanel sortByPanel;
	private JLabel sortByLabel;

	// west component declaration
	private JPanel westPanel;
	private JPanel buttonPanel;
	private JPanel fileTreePanel;
	private JPanel virtualTreePanel;
	private JButton exportButton;
	private JButton backupButton;
	private JButton rotateButton;
	private JButton deleteButton;
	private JButton printButton;
	private JTabbedPane tabbedPane;
	private JTree fileSystemTree;
	private JScrollPane fileSystemTreeScrollPane;

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
	public TagPanel storedTagsPanel;
	private JSuggestField tagField;

	private Listeners.ThumbnailClickListener tcl;
	private static ArrayList<MainFrame> frames = new ArrayList<MainFrame>();

    private Font biggerFont = new Font("Georgia", Font.PLAIN, 16);
    private boolean noPicturesFound = false;

    /**
	 * Constructor for the application
	 */
	public MainFrame()  {

        try {
            Class.forName("java.awt.color.ICC_ColorSpace");
            Class.forName("sun.java2d.cmm.lcms.LCMS");
        } catch (ClassNotFoundException e) {

        }

		// root panel assignment
		mainPanel = new JPanel(new BorderLayout());

		addSavedData();
        startUpChecks();
        loadTaggableComponents();
        loadTagDeleteButton();
		createMenuBar();
		createPanels();
		addListeners();
		saveData();


		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(mainPanel);

		setTitle("Photo Management Software");
		// setMinimumSize(new Dimension(1333, 766));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		frames.add(this);

        if (Settings.PICTURE_HOME_DIR != null) {
            Library.importFolder(Settings.PICTURE_HOME_DIR);
        }
	}
	
	/*
	 * Automatically adds the saved picture library ArrayList, the taggable components ArrayList, the Nursery Location and
	 * the Pictures home directory to the application.
	 */
	private void addSavedData() {
		getSavedPictureLibrary();
		getSavedTaggableComponents();
		getSavedNurseryLocation();
		getSavedPicturesHomeDIR();
        getSavedLastVisitedDIR();
        getSavedDirectoryFileMap();
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

		if (Library.getTaggableComponentsList().size() == 0 && Settings.CSV_PATH == null) {
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
            searchField.setFocusable(false);
			taggedRadioButton = new JRadioButton("Tagged");
			untaggedRadioButton = new JRadioButton("Untagged");
			incompleteRadioButton = new JRadioButton("Incomplete");
			allRadioButton = new JRadioButton("All");
            radioButtonGroup = new ButtonGroup();
			sortByPanel = new JPanel();
			sortByLabel = new JLabel("Filter: ");
            sortByLabel.setFont(biggerFont);

            taggedRadioButton.setMnemonic(KeyEvent.VK_T);
            taggedRadioButton.setSelected(false);
            taggedRadioButton.setActionCommand("TAGGED");
            taggedRadioButton.setFont(biggerFont);
            untaggedRadioButton.setMnemonic(KeyEvent.VK_U);
			untaggedRadioButton.setSelected(false);
            untaggedRadioButton.setActionCommand("UNTAGGED");
            untaggedRadioButton.setFont(biggerFont);
            incompleteRadioButton.setMnemonic(KeyEvent.VK_I);
			incompleteRadioButton.setSelected(false);
            incompleteRadioButton.setActionCommand("INCOMPLETE");
            incompleteRadioButton.setFont(biggerFont);
			allRadioButton.setMnemonic(KeyEvent.VK_A);
			allRadioButton.setSelected(true);
            allRadioButton.setActionCommand("ALL");
            allRadioButton.setFont(biggerFont);

			searchPanel.add(sortByLabel);
			searchPanel.add(searchField);
            radioButtonGroup.add(taggedRadioButton);
            radioButtonGroup.add(untaggedRadioButton);
            radioButtonGroup.add(incompleteRadioButton);
            radioButtonGroup.add(allRadioButton);
            sortByPanel.add(taggedRadioButton);
            sortByPanel.add(untaggedRadioButton);
            sortByPanel.add(incompleteRadioButton);
            sortByPanel.add(allRadioButton);

			mainPanel.add(northPanel, BorderLayout.NORTH);
			northPanel.add(searchPanel, BorderLayout.WEST);
			northPanel.add(sortByPanel, BorderLayout.EAST);
            northPanel.add(searchLabelPanel, BorderLayout.CENTER);

			TitledBorder titledBorder = new TitledBorder("Search: ");
            titledBorder.setTitleFont(biggerFont);
            EmptyBorder emptyBorder = new EmptyBorder(3, 3, 3, 3);
			CompoundBorder compoundBorder = new CompoundBorder(emptyBorder,
					titledBorder);
			northPanel.setBorder(compoundBorder);

		}

		/* private void createWestPanel() */{

			// west component assignment
			westPanel = new JPanel(new BorderLayout());
			fileTreePanel = new JPanel(new BorderLayout());
			fileTreePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			virtualTreePanel = new JPanel();
			tabbedPane = new JTabbedPane();
			buttonPanel = new JPanel();
			exportButton = new JButton("Export");
            exportButton.setFont(biggerFont);
            backupButton = new JButton("Backup");
            backupButton.setFont(biggerFont);
			rotateButton = new JButton("Rotate");
            rotateButton.setFont(biggerFont);
			deleteButton = new JButton("Delete");
            deleteButton.setFont(biggerFont);
			printButton = new JButton("Print");
            printButton.setFont(biggerFont);

            if (Settings.PICTURE_HOME_DIR != null) {
                fileSystemTree = new JTree(new SystemTreeModel(Settings.PICTURE_HOME_DIR));
            }
            else {
                fileSystemTree = new JTree(new SystemTreeModel(FileSystemView.getFileSystemView().getHomeDirectory()));
            }
			fileSystemTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            fileSystemTree.setExpandsSelectedPaths(true);
            if (Settings.LAST_VISITED_PATH != null) {
                fileSystemTree.setSelectionPath(Settings.LAST_VISITED_PATH);
            }
            // use tree cell renderer to set tree node names
            // to the folder name, rather than the whole path
            fileSystemTree.setCellRenderer(new DefaultTreeCellRenderer() {
                @Override
                public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                    super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                    setText(value.toString().substring(value.toString().lastIndexOf(File.separator) + 1, value.toString().length()));
                    return this;
                }
            });
			fileSystemTreeScrollPane = new JScrollPane(fileSystemTree);
			
			fileTreePanel.add(fileSystemTreeScrollPane, BorderLayout.CENTER);
			tabbedPane.addTab("File Tree", fileTreePanel);
			tabbedPane.addTab("Virtual Tree", virtualTreePanel);
			tabbedPane.setFont(biggerFont);
			mainPanel.add(westPanel, BorderLayout.WEST);
			westPanel.add(tabbedPane, BorderLayout.CENTER);
			westPanel.add(buttonPanel, BorderLayout.SOUTH);
			buttonPanel.setLayout(new GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.insets = new Insets(2, 2, 2, 2);
			c.gridx = 1;
			c.gridy = 0;
			buttonPanel.add(exportButton, c);
			c.gridx = 2;
			c.gridy = 0;
			buttonPanel.add(backupButton, c);
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 1;
			buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 2;
			buttonPanel.add(rotateButton, c);
			c.gridx = 1;
			c.gridy = 2;
			buttonPanel.add(deleteButton, c);
			c.gridx = 2;
			c.gridy = 2;
			buttonPanel.add(printButton, c);

			TitledBorder titledBorder = new TitledBorder("Tools: ");
            titledBorder.setTitleFont(biggerFont);
            westPanel.setBorder(titledBorder);

		}

		/* private void createCenterPanel() */{

			// center component assignment
			picturePanel = new PicturesFrame(this);

			picturePanelScrollPane = new JScrollPane(picturePanel);
			picturePanelScrollPane
					.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

			zoomSlider = new JSlider(Adjustable.HORIZONTAL, 0, 9, 2);
			scrollPanel = new JPanel();
			scrollPanel.add(zoomSlider);

			TitledBorder titledBorder = new TitledBorder(" ");
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

			tagField = new JSuggestField(this,
					Library.getTaggableComponentNamesVector(false));
			tagField.setPreferredSize(new Dimension(300, 30));

			storedTagsPanel = new TagPanel(this);

			tagPanel = new JPanel(new BorderLayout());
			tagPanel.add(storedTagsPanel, BorderLayout.CENTER);
			tagPanel.add(tagField, BorderLayout.NORTH);

			eastPanel = new JPanel(new BorderLayout());
            eastPanel.setBorder(BorderFactory.createTitledBorder(null, "Add Tag: ",
                    TitledBorder.LEFT, TitledBorder.ABOVE_TOP, biggerFont));

            eastPanel.add(tagPanel, BorderLayout.NORTH);

			mainPanel.add(eastPanel, BorderLayout.EAST);

		}
	}

	private void addListeners() {

		Listeners l = new Listeners();

		tcl = l.new ThumbnailClickListener();

		// add key listener for thumbnail selection using keyboard
		picturePanel.addKeyListener(l.new KeysListener());
		picturePanel.addKeyListener(tcl);

		searchField.addSelectionListener(l.new SearchListener());
		tagField.addSelectionListener(l.new TagListener());

        taggedRadioButton.addActionListener(l.new RadioButtonListener());
        untaggedRadioButton.addActionListener(l.new RadioButtonListener());
        incompleteRadioButton.addActionListener(l.new RadioButtonListener());
        allRadioButton.addActionListener(l.new RadioButtonListener());

		//Key Stroke Listeners
		picturePanel.addKeyListener(l.new keyStrokes());
		picturePanel.setFocusTraversalKeysEnabled(false);
		searchField.addKeyListener(l.new keyStrokes());
		searchField.setFocusTraversalKeysEnabled(false);
		tagField.addKeyListener(l.new keyStrokes());
		tagField.setFocusTraversalKeysEnabled(false);
		//fileSystemTree.addKeyListener((l.new keyStrokes()));
		//fileSystemTree.setFocusTraversalKeysEnabled(false);

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

        fileSystemTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {

                for (Thread t: Library.getRunningThreads()) {
                    t.interrupt();
                }

                Settings.LAST_VISITED_PATH = e.getNewLeadSelectionPath();
                Settings.LAST_VISITED_DIR = (File)
                        fileSystemTree.getLastSelectedPathComponent();

                picturePanel.removeAll();
                picturePanel.repaint();
                picturePanel.removeAllThumbsFromDisplay();

                if (Settings.LAST_VISITED_DIR == null) {
                    return;
                }

                ArrayList<Picture> picturesToDisplay = MainFrame.this.getAllSubPictures(Settings.LAST_VISITED_DIR);
                for (Picture p: picturesToDisplay) {
                    picturePanel.addThumbToDisplay(p.getPictureLabel());
                }
                picturePanel.createThumbnailArray();
                Library.importPicture(picturesToDisplay);

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

		public class keyStrokes implements  KeyListener {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {


					if (picturePanel.hasFocus()) {
						tagField.requestFocus();
					}

					if (searchField.hasFocus()) {
						picturePanel.requestFocus();
					}

					if (tagField.hasFocus()) {
						searchField.requestFocus();
					}

					if(fileSystemTree.hasFocus())
					{
						picturePanel.requestFocus();
					}

					if(allRadioButton.hasFocus()|| incompleteRadioButton.hasFocus()||taggedRadioButton.hasFocus()|| untaggedRadioButton.hasFocus())
					{
						tagField.requestFocus();
						System.out.print("test ignore this if");
					}
				}

				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T) {
					tagField.requestFocus();
				}
				//TODO: Fix print shortcut, find graphic workaround

				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_P) {

					PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
					PrinterJob printJob = PrinterJob.getPrinterJob();
					printJob.setPrintable(new Printable() {
						public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
							if (pageIndex != 0) {
								return NO_SUCH_PAGE;
							}

							//Image sourceImage;
							//sourceImage = new ImageIcon(getMostRecentSelection().getGraphicsConfiguration());

							//graphics.drawImage(sourceImage,0,0, (int)pageFormat.getWidth(),(int)pageFormat.getHeight(), null);


							return PAGE_EXISTS;
						}
					});

					if (printJob.printDialog(aset))
						try {
							printJob.print(aset);
						} catch (PrinterException e1) {
							e1.printStackTrace();
						}
				}

			}


			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

		}

		/**
		 * This method
		 */
		private class TagListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
                if (picturePanel.getThumbsOnDisplay().size() > 0) {
                    ArrayList<Picture> picturesToTag = picturePanel
                            .getSelectedPictures();
                    if (picturesToTag.size() != 0) {
                        for (Taggable t : Library.getTaggableComponentsList()) {
                            if (tagField.getText().toLowerCase()
                                    .equals(t.getName().toLowerCase())) {
                                for (Picture p : picturesToTag) {
                                    if (!p.getTag().getTaggedComponents()
                                            .contains(t)) {
                                        // if this is an area tag and pictures already
                                        // has an area tag, replace it with new one
                                        if ((t.getType() == Settings.AREA_TAG && p
                                                .getTag().isAreaSet())) {
                                            p.getTag().getArea().removeTaggedPicture(p);
                                            p.getTag().removeTag(p.getTag().getArea());
                                        }
                                        p.getTag().addTag(t);
                                        t.addTaggedPicture(p);
                                    }
                                }
                            }
                        }
                    }
                    createTagLabels();
                }
                tagField.setText("");
			}
		}

		/**
		 * Listener for the search suggestion field. When a selection is made,
         * the tag being searched for is found in the full tag list. If it wasn't
         * already searched for, a search label is added for it. If it is a room
         * tag and a room has already been searched for, the previous room tag is
         * removed before the new one is added. The method calls the refresh
         * search method to update the display of pictures when search is made.
		 */
		private class SearchListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {

                radioButtonGroup.setSelected(allRadioButton.getModel(), true);

				if (searchField.getText().equals("View All")) {
                    currentSearchTags.clear();
				} else {
                    ArrayList<Taggable> allTaggableComponents = Library
                            .getTaggableComponentsList();

                    // gets text from GUI to a string
                    String searchString = searchField.getText();

                    // loops over all taggable components
                    for (int i = 0; i < allTaggableComponents.size(); ++i) {
                        // if the search selection is a taggable component
                        if (searchString.equalsIgnoreCase(allTaggableComponents
                                .get(i).getName())) {
                            // if there isn't already a search tag
                            if (!currentSearchTags.contains(allTaggableComponents.get(i))) {
                                // if the tag is a room
                                if (allTaggableComponents.get(i).getType() == Settings.AREA_TAG) {
                                    // check if a room is already tagged and needs to be replaced
                                    Taggable toRemove = null;
                                    for (Taggable t: currentSearchTags) {
                                        if (t.getType() == Settings.AREA_TAG) {
                                            toRemove = t;
                                            break;
                                        }
                                    }
                                    // if there already was a search tag for a room, remove it
                                    if (toRemove != null) {
                                        currentSearchTags.remove(toRemove);
                                    }
                                }
                                // add the search label
                                addSearchTag(allTaggableComponents.get(i));
                                searchPanel.revalidate();
                            }
                        }
                    }
				}
                refreshSearch();
                storedTagsPanel.removeTagLabels();
                searchField.setText("");
			}
		}

        private class RadioButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                storedTagsPanel.removeTagLabels();
                currentSearchTags.clear();
                refreshSearch();
                ArrayList<Picture> allPicsInFolder = getAllSubPictures(Settings.LAST_VISITED_DIR);
                ArrayList<Picture> picturesToDisplay = new ArrayList<Picture>();
                if (e.getActionCommand().equals("TAGGED")) {
                    for (Picture p: allPicsInFolder) {
                        if (p.getTag().isFullyTagged()) {
                            picturesToDisplay.add(p);
                        }
                    }
                }
                else if (e.getActionCommand().equals("UNTAGGED")) {
                    for (Picture p: allPicsInFolder) {
                        if (p.getTag().isUntagged()) {
                            picturesToDisplay.add(p);
                        }
                    }
                }
                else if (e.getActionCommand().equals("INCOMPLETE")) {
                    for (Picture p: allPicsInFolder) {
                        if (p.getTag().isPartiallyTagged()) {
                            picturesToDisplay.add(p);
                        }
                    }
                }
                else if (e.getActionCommand().equals("ALL")) {
                    picturesToDisplay = allPicsInFolder;
                }

                picturePanel.removeAll();
                picturePanel.repaint();
                picturePanel.removeAllThumbsFromDisplay();
                for (Picture p : picturesToDisplay) {
                    picturePanel.addThumbToDisplay(p.getPictureLabel());
                }
                Library.importPicture(picturesToDisplay);
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

    private ArrayList<Picture> getAllSubPictures(File currentFolder) {
        ArrayList<Picture> allSubPictures = new ArrayList<Picture>();
        for (File f: currentFolder.listFiles()) {

            if (f.isDirectory()) {
                allSubPictures.addAll(getAllSubPictures(f));
            }
        }
        if (Library.getDirectoryPictureMap().get(currentFolder) != null) {
            allSubPictures.addAll(Library.getDirectoryPictureMap().get(currentFolder));
        }
        return allSubPictures;
    }

    private void loadTagDeleteButton() {
        try {
            Library.DELETE_BUTTON = ImageIO.read(MainFrame.class.getResource("/images/delete_button.png"));
        } catch (IOException e) {
            //TODO: Handle exception
        }
    }
	
	/*
	 * Creates the File Tree Model
	 */
	private class SystemTreeModel implements TreeModel {

		private File rootFile;
        private ArrayList<TreeModelListener> listeners;

		public SystemTreeModel(File rootFile) {
			this.rootFile = rootFile;
            listeners = new ArrayList<TreeModelListener>();
		}

		@Override
		public void addTreeModelListener(TreeModelListener l) {
            if (l != null && !listeners.contains(l)) {
                listeners.add(l);
            }
		}

        private File[] getFiles(File parent) {
            File[] files = parent.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            if (files != null) {
                return files;
            }
            else {
                return new File[]{};
            }
        }

        @Override
        public int getChildCount(Object parent) {
            return getFiles((File) parent).length;
        }

        @Override
        public Object getChild(Object parent, int index) {
            return getFiles((File) parent)[index];
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            File[] files = getFiles((File) parent);
            for (int i = 0; i < files.length; ++i) {
                if (files[i].equals(child))
                    return i;
            }
            return -1;
        }

		@Override
		public Object getRoot() {
			return rootFile;
		}

		@Override
		public boolean isLeaf(Object node) {
            File f = (File) node;
            return !f.isDirectory();
		}

		@Override
		public void removeTreeModelListener(TreeModelListener l) {
            if (l != null) {
                listeners.remove(l);
            }

		}

		@Override
		public void valueForPathChanged(TreePath arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

        public void fireTreeStructureChanged(TreeModelEvent e) {
            for (TreeModelListener l: listeners) {
                l.treeStructureChanged(e);
            }
        }


	}

    /**
     * Search label panel is cleared of all components and is
     * reset using the list of current chosen tags to search by.
     * Finally the thumbnail display is reset to show pictures
     * which are tagged with all tags chosen in the search.
     */
    public void refreshSearch() {
        if (picturePanel.getThumbsOnDisplay().size() > 0 || noPicturesFound) {
            noPicturesFound = false;
            ArrayList<Picture> allPictureSet = new ArrayList<Picture>();
            searchLabelPanel.removeAll();
            searchLabelPanel.repaint();

            picturePanel.removeAll();
            picturePanel.revalidate();
            // if there are no search tags
            if (currentSearchTags.size() == 0) {
                allPictureSet = getAllSubPictures(Settings.LAST_VISITED_DIR);
            }
            else if (currentSearchTags.size() > 1) {
                ArrayList<ArrayList<Picture>> allPictureListsList = new ArrayList<ArrayList<Picture>>();
                for (int i = 0; i < currentSearchTags.size(); ++i) {
                    searchLabelPanel.add(new TagPanel.TagTextLabel(true, currentSearchTags.get(i), searchLabelPanel, MainFrame.this));
                    allPictureListsList.add(currentSearchTags.get(i).getTaggedPictures());
                }
                // for every picture in the first picture list
                for (Picture p : allPictureListsList.get(0)) {
                    boolean pictureInAllLists = true;
                    // if the picture is not in all tag lists set boolean to false
                    for (int i = 1; i < allPictureListsList.size(); ++i) {
                        if (!allPictureListsList.get(i).contains(p)) {
                            pictureInAllLists = false;
                        }
                    }
                    // if the picture is tagged with all search tags
                    // and the new picture set does not already contain it
                    if (pictureInAllLists && !allPictureSet.contains(p)) {
                        if (p.getImagePath().startsWith(Settings.LAST_VISITED_DIR.getPath())) {
                            allPictureSet.add(p);
                        }
                    }
                }
            }
            else {
                ArrayList<Picture> newPictureSet = currentSearchTags.get(0).getTaggedPictures();
                allPictureSet = new ArrayList<Picture>();
                for (Picture p: newPictureSet) {
                    if (p.getImagePath().startsWith(Settings.LAST_VISITED_DIR.getPath())) {
                        allPictureSet.add(p);
                    }
                }
                searchLabelPanel.add(new TagPanel.TagTextLabel(true, currentSearchTags.get(0), searchLabelPanel, MainFrame.this));
            }

            if (Settings.IMPORT_IN_PROGRESS) {
                for (Thread t: Library.getRunningThreads()) {
                    if (t != null) {
                        t.interrupt();
                    }
                }
            }
            searchLabelPanel.repaint();
            picturePanel.removeAllThumbsFromDisplay();
            if (allPictureSet.size() == 0) {
                noPicturesFound = true;
            }
            else {
                for (Picture p : allPictureSet) {
                    picturePanel.addThumbToDisplay(p.getPictureLabel());
                }
            }
            Library.importPicture(allPictureSet);
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
	 * Automatically saves the picture library ArrayList, the taggable components ArrayList, the Nursery Location and
	 * the Pictures home directory, when the application is closed.
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
				savePictureLibrary();
				saveAllTaggableComponents();
				saveNurseryLocation();
				savePicturesHomeDIR();
                saveLastVisitedDIR();
                saveDirectoryFileMap();
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}
		});
	}

    // returns searchField
    public JSuggestField getSearchField(){ return searchField; }

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

    public int getSidePanelsWidth() {
        return eastPanel.getWidth() + westPanel.getWidth();
    }

    public JPanel getSearchPanel() {
        return searchPanel;
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

	/*
	 * Saves the pictureLibrary ArrayList.
	 */
	private void savePictureLibrary() {
		try {
			FileOutputStream savedPictureLibraryFile = new FileOutputStream(
					"savedPictureLibrary.ser");
            FSTObjectOutput savedPictureLibraryObject = new FSTObjectOutput(
					savedPictureLibraryFile);
			savedPictureLibraryObject.writeObject(Library.getPictureLibrary());
			savedPictureLibraryObject.close();
			System.out.println("Picture Library Saved");
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
		Library.deletePictureLibrary();
	}
	
	/*
	 * Saves the taggable components ArrayList.
	 */
	private void saveAllTaggableComponents() {
		try{
			FileOutputStream savedAllTaggableComponentSFile = new FileOutputStream(
					"savedAllTaggableComponents.ser");
            FSTObjectOutput savedAllTaggableComponentsObject = new FSTObjectOutput(
					savedAllTaggableComponentSFile);
			savedAllTaggableComponentsObject.writeObject(Library.getTaggableComponentsList());
			savedAllTaggableComponentsObject.close();
			System.out.println("All Taggable Components Saved");
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
		Library.deleteTaggableComponentsList();	
	}
	
	/*
	 * Saves the NurseryLocation.
	 */
	private void saveNurseryLocation() {
		try{
			FileOutputStream savedNurseryLocation = new FileOutputStream("savedNurseryLocation.ser");
            FSTObjectOutput savedNurseryLocationObject = new FSTObjectOutput(savedNurseryLocation);
			savedNurseryLocationObject.writeObject(Settings.NURSERY_LOCATION);
			savedNurseryLocationObject.close();
			System.out.println("Nursery Location Saved");
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
		Settings.NURSERY_LOCATION = "";
	}
	
	/*
	 * Saves the pictures Home Directory.
	 */
	private void savePicturesHomeDIR() {
		try{
			FileOutputStream savedPicturesHomeDIR = new FileOutputStream("savedPicturesHomeDIR.ser");
            FSTObjectOutput savedPicturesHomeDIRObject = new FSTObjectOutput(savedPicturesHomeDIR);
			savedPicturesHomeDIRObject.writeObject(Settings.PICTURE_HOME_DIR);
			savedPicturesHomeDIRObject.close();
			System.out.println("Pictures Home Directory Saved");
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}
		Settings.PICTURE_HOME_DIR = null;
	}

    /*
	 * Saves the last visited Directory.
	 */
    private void saveLastVisitedDIR() {
        try{
            FileOutputStream savedLastVisitedDIR = new FileOutputStream("savedLastVisitedDIR.ser");
            FSTObjectOutput savedLastVisitedDIRObject = new FSTObjectOutput(savedLastVisitedDIR);
            savedLastVisitedDIRObject.writeObject(Settings.LAST_VISITED_PATH);
            savedLastVisitedDIRObject.close();
            System.out.println("Last Visited Directory Saved");
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        Settings.LAST_VISITED_PATH = null;
    }

    /*
	 * Saves the directory file hashmap.
	 */
    private void saveDirectoryFileMap() {
        try{
            FileOutputStream savedDirectoryFileMap = new FileOutputStream("savedDirectoryFileMap.ser");
            FSTObjectOutput savedDirectoryFileMapObject = new FSTObjectOutput(savedDirectoryFileMap);
            savedDirectoryFileMapObject.writeObject(Library.getDirectoryPictureMap());
            savedDirectoryFileMapObject.close();
            System.out.println("Directory File Map Saved");
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
        Library.setDirectoryPictureMap(null);
    }
	
	/*
	 * Returns the saved Picture Library ArrayList.
	 */
	private void getSavedPictureLibrary() {
		try {
			FileInputStream savedPictureLibraryFile = new FileInputStream("savedPictureLibrary.ser");
            FSTObjectInput restoredPictureLibraryObject = new FSTObjectInput(savedPictureLibraryFile);
			ArrayList<Picture> savedPictureLibraryData = (ArrayList<Picture>) restoredPictureLibraryObject.readObject();
			for (int i = 0; i < savedPictureLibraryData.size(); i++) {
				Picture recreatedPicture = new Picture(new File(savedPictureLibraryData.get(i).getImagePath()));
				recreatedPicture.setTag(savedPictureLibraryData.get(i).getTag());
				ArrayList<Picture> savedPictures = new ArrayList<Picture>();
				savedPictures.add(recreatedPicture);
				Library.getPictureLibrary().add(recreatedPicture);
			}
			restoredPictureLibraryObject.close();
		} catch (EOFException exception) {
			exception.printStackTrace();
		} catch (ClassNotFoundException ex2) {
			ex2.printStackTrace();
		}  catch (IOException ex1) {
			System.out.println("File savedPictureLibrary.ser not found!");
		}
	}
	
	/*
	 * Returns the saved taggable components ArrayList.
	 */
	private void getSavedTaggableComponents() {
		try{
			FileInputStream savedTaggableComponents = new FileInputStream("savedAllTaggableComponents.ser");
            FSTObjectInput restoredTaggableComponentsObject = new FSTObjectInput(savedTaggableComponents);
			ArrayList<Taggable> savedTaggableComponentsData = (ArrayList<Taggable>) restoredTaggableComponentsObject.readObject();
			for(int i = 0; i < savedTaggableComponentsData.size(); i++) {
				Library.getTaggableComponentsList().add(savedTaggableComponentsData.get(i));
			}
			restoredTaggableComponentsObject.close();
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex2) {
			ex2.printStackTrace();
		}  catch (IOException ex1) {
			System.out.println("File savedAllTaggableComponents.ser not found!");
		}
	}
	
	/*
	 * Returns the saved Nursery Location.
	 */
	private void getSavedNurseryLocation() {
		try{
			FileInputStream savedNurseryName = new FileInputStream("savedNurseryLocation.ser");
            FSTObjectInput restoredNurseryName = new FSTObjectInput(savedNurseryName);
			Settings.NURSERY_LOCATION = (String)restoredNurseryName.readObject();
			restoredNurseryName.close();
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex2) {
			ex2.printStackTrace();
		}  catch (IOException ex1) {
			System.out.println("File savedNurseryLocation.ser not found!");
		}
	}
	
	/*
	 * Returns the saved Pictures Home Directory.
	 */
	private void getSavedPicturesHomeDIR() {
		try{
			FileInputStream savedPicturesHomeDIR = new FileInputStream("savedPicturesHomeDIR.ser");
            FSTObjectInput restoredPicturesHomeDIR = new FSTObjectInput(savedPicturesHomeDIR);
			Settings.PICTURE_HOME_DIR = (File)restoredPicturesHomeDIR.readObject();
			restoredPicturesHomeDIR.close();
		} catch (EOFException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex2) {
			ex2.printStackTrace();
		}  catch (IOException ex1) {
			System.out.println("File savedPicturesHomeDIR.ser not found!");
		}
	}

    /*
	 * Returns the saved last visited directory.
	 */
    private void getSavedLastVisitedDIR() {
        try{
            FileInputStream savedLastVisitedDIR = new FileInputStream("savedLastVisitedDIR.ser");
            FSTObjectInput restoredLastVisitedDIR = new FSTObjectInput(savedLastVisitedDIR);
            Settings.LAST_VISITED_PATH = (TreePath) restoredLastVisitedDIR.readObject();
            restoredLastVisitedDIR.close();
        } catch (EOFException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex2) {
            ex2.printStackTrace();
        }  catch (IOException ex1) {
            System.out.println("File savedLastVisitedDIR.ser not found!");
        }
    }

    /*
	 * Returns the saved directory file hashmap.
	 */
    private void getSavedDirectoryFileMap() {
        try{
            FileInputStream savedDirectoryFileMap = new FileInputStream("savedDirectoryFileMap.ser");
            FSTObjectInput restoredDirectoryFileMap = new FSTObjectInput(savedDirectoryFileMap);
            Library.setDirectoryPictureMap((Map<File, ArrayList<Picture>>) restoredDirectoryFileMap.readObject());
            restoredDirectoryFileMap.close();
        } catch (EOFException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex2) {
            ex2.printStackTrace();
        }  catch (IOException ex1) {
            System.out.println("File savedDirectoryFileMap.ser not found!");
        }
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
