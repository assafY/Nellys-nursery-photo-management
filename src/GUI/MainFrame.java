package GUI;

import Core.Library;
import Core.Settings;
import Data.Child;
import Data.Picture;
import Data.ThumbnailClickListener;
import Data.Tag;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class MainFrame extends JFrame {

    // menu bar components
    private MenuBar menuBar = new MenuBar();
    Menu file = new Menu("File");
    Menu edit = new Menu("Edit");
    Menu tools = new Menu("Tools");
    Menu help = new Menu("Help");
    MenuItem imp = new MenuItem("Import");
    MenuItem exp = new MenuItem("Export");
    MenuItem backup = new MenuItem("Backup");
    MenuItem exit = new MenuItem("Exit");
    MenuItem rotate = new MenuItem("Rotate");
    MenuItem resize = new MenuItem("Resize");
    MenuItem crop = new MenuItem("Crop");
    MenuItem sel = new MenuItem("Select");
    MenuItem tag = new MenuItem("Tag");
    MenuItem delete = new MenuItem("Delete");
    MenuItem print = new MenuItem("Print");
    //creates root panel
    private JPanel mainPanel = new JPanel(new BorderLayout());
    //create north components
    private JPanel northPanel = new JPanel(new BorderLayout());
    private JPanel searchPanel = new JPanel();
    private JTextField filterField = new JTextField(22);
    private JCheckBox taggedButton = new JCheckBox("Tagged");
    private JCheckBox unTaggedButton = new JCheckBox("Untagged");
    private JCheckBox incompleteButton = new JCheckBox("Incomplete");
    private JCheckBox allButton = new JCheckBox("All");
    private JPanel sortByPanel = new JPanel();
    private JLabel labelSortby = new JLabel("Sort by: ");
    private JCheckBox nameAZ = new JCheckBox("name A-Z");
    private JCheckBox nameZA = new JCheckBox("name Z-A");
    //create west components
    private JPanel westPanel = new JPanel(new BorderLayout());
    private JPanel buttonPanel = new JPanel();
    private JButton importButton = new JButton("Import");
    private JButton exportButton = new JButton("Export");
    private JButton backupButton = new JButton("Backup");
    //create center components
    private JPanel functionPanel = new JPanel(new GridBagLayout());
    private JButton rotateButton = new JButton("Rotate");
    private JButton deleteButton = new JButton("Delete");
    private JButton printButton = new JButton("Print");
    //create center components
    private static GridLayout picturePanelLayout = new GridLayout(0, 1, 2, 2);
    //private BorderLayout picturePanelBorderLayout = new BorderLayout();

    private JPanel centerPanel = new JPanel(new BorderLayout());
    private static JPanel picturePanel = new JPanel(picturePanelLayout);
    public static PictureLabel[][] thumbsOnDisplayArray; // possible will be used for all labels currently on display
                                              // 2D array to enable moving between pictures using keyboard
                                              // ambitious but let's see what happens :o
    private JScrollPane picturePanelPane = new JScrollPane(picturePanel);
    private JPanel scrollPanel = new JPanel();
    private static JSlider zoomSlider;
    //create east components
    private JPanel eastPanel = new JPanel(new BorderLayout());
    private JPanel tagPanel = new JPanel(new BorderLayout());
    private JPanel tagsLabelsPanel = new JPanel(new GridLayout(0, 1));
    private JPanel tagsFieldsPanel = new JPanel(new GridLayout(0, 1));
    private JPanel descriptionPanel = new JPanel(new BorderLayout());
    private JPanel donePanel = new JPanel();
    private JPanel storeTagsPanel = new JPanel(new FlowLayout());
    JFormattedTextField dateField;
    private JTextField childField = new JTextField(12);
    private JTextField areaField = new JTextField(12);
    private JLabel areaLabel = new JLabel("Area");
    private JLabel dateLabel = new JLabel("Date");
    private JButton doneButton = new JButton("Done");
    private JButton resetButton = new JButton("Reset");

    private Tag tagLabel = new Tag();
    private int currentColumnCount = 0;
    private boolean picturePanelBiggerThanFrame = false;
    private ThumbnailClickListener tcl = new ThumbnailClickListener();

    public MainFrame(){
        setTitle("Photo Management Software");
        //setMinimumSize(new Dimension(1333, 766));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMenuBar(menuBar);
        createMenuBar();
        createNorthPanel();
        createWestPanel();
        createCenterPanel();
        createEastPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(mainPanel);
        pack();
        initialiseListeners();
        setVisible(true);
    }
    private void createMenuBar(){

        menuBar.add(file);
        file.add(imp);
        file.add(backup);
        file.add(exp);
        file.addSeparator();
        file.add(exit);

        menuBar.add(edit);
        edit.add(rotate);
        edit.add(resize);
        edit.add(crop);

        //menuBar.add(view);

        menuBar.add(tools);
        tools.add(sel);
        tools.add(tag);
        tools.add(delete);
        tools.add(print);

        menuBar.add(help);
    }
    private void createNorthPanel(){
        mainPanel.add(northPanel, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.WEST);
        northPanel.add(sortByPanel, BorderLayout.EAST);

        taggedButton.setMnemonic(KeyEvent.VK_T);
        taggedButton.setSelected(false);
        unTaggedButton.setMnemonic(KeyEvent.VK_T);
        unTaggedButton.setSelected(false);
        incompleteButton.setMnemonic(KeyEvent.VK_T);
        incompleteButton.setSelected(false);
        allButton.setMnemonic(KeyEvent.VK_T);
        allButton.setSelected(true);
        searchPanel.add(filterField);
        searchPanel.add(taggedButton);
        searchPanel.add(unTaggedButton);
        searchPanel.add(incompleteButton);
        searchPanel.add(allButton);
        nameAZ.setSelected(false);
        nameZA.setSelected(false);
        sortByPanel.add(labelSortby);
        sortByPanel.add(nameAZ);
        sortByPanel.add(nameZA);

        TitledBorder titledBorder = new TitledBorder("Search: ");
        EmptyBorder emptyBorder = new EmptyBorder(3, 3, 3, 3);
        CompoundBorder compoundBorder = new CompoundBorder(emptyBorder, titledBorder);
        northPanel.setBorder(compoundBorder);
    }
    private void createWestPanel(){
        mainPanel.add(westPanel, BorderLayout.WEST);
        westPanel.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4,4,4,4);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanel.add(importButton, c);
        c.gridy = 1;
        buttonPanel.add(exportButton, c);
        c.gridy = 2;
        buttonPanel.add(backupButton, c);
        //buttonPanel.add(Box.createVerticalStrut(5));
        c.gridy = 3;
        buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        //buttonPanel.add(Box.createVerticalStrut(5));
        c.gridy = 6;
        buttonPanel.add(rotateButton, c);
        c.gridy = 9;
        buttonPanel.add(deleteButton, c);
        c.gridy = 10;
        buttonPanel.add(printButton, c);

        TitledBorder titledBorder = new TitledBorder("Tools: ");
        westPanel.setBorder(titledBorder);


    }
    private void createCenterPanel(){
        //centerPanel.add(picturePanel, BorderLayout.CENTER);


        /*centerPanel.add(functionPanel, BorderLayout.SOUTH);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.SOUTH;
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.25;
        functionPanel.add(rotateButton, c);
        c.gridx = 1;
        functionPanel.add(printButton, c);
        c.gridx = 2;
        functionPanel.add(deleteButton, c);
        c.gridx = 3;
        c.weightx = 1.0;
        functionPanel.add(Box.createHorizontalGlue(), c);
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.gridx = 4;
        functionPanel.add(zoomSlider, c);*/
//        mainPanel.add(picturePanel, BorderLayout.CENTER);

        picturePanel.add(scrollPanel, BorderLayout.SOUTH);
/*        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPreferredSize(new Dimension(400,45));
        Hashtable labelTable = new Hashtable();
        labelTable.put( 0, new JLabel("0%") );
        labelTable.put( 50, new JLabel("50%") );
        labelTable.put( 100, new JLabel("100%") );
        labelTable.put( 150, new JLabel("150%") );
        labelTable.put( 200, new JLabel("200%") );
        zoomSlider.setLabelTable(labelTable);
        zoomSlider.setPaintLabels(true);
*/
        zoomSlider = new JSlider(Adjustable.HORIZONTAL, 0, 9, 4);
        picturePanelPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        scrollPanel.add(zoomSlider);
        centerPanel.add(picturePanelPane, BorderLayout.CENTER);
        centerPanel.add(scrollPanel, BorderLayout.SOUTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);


        TitledBorder titledBorder = new TitledBorder("Pictures: ");
        EmptyBorder emptyBorder = new EmptyBorder(7, 7, 1, 7);
        CompoundBorder compoundBorder = new CompoundBorder(emptyBorder, titledBorder);
        centerPanel.setBorder(compoundBorder);


    }
    private void createEastPanel(){

        mainPanel.add(eastPanel, BorderLayout.EAST);

        eastPanel.setBorder(new TitledBorder("Add Tag: "));
        eastPanel.add(descriptionPanel, BorderLayout.CENTER);
        descriptionPanel.setBorder(new TitledBorder("Add desription: "));
        descriptionPanel.add(new JScrollPane(new JTextArea(7, 21)),
                BorderLayout.NORTH);
        descriptionPanel.add(donePanel, BorderLayout.SOUTH);
        donePanel.add(resetButton);
        donePanel.add(doneButton);
        eastPanel.add(tagPanel, BorderLayout.NORTH);
        tagPanel.add(tagsLabelsPanel, BorderLayout.CENTER);
        tagsLabelsPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
        tagsLabelsPanel.add(new JLabel("Child name"));
        tagsLabelsPanel.add(areaLabel);
        tagsLabelsPanel.add(dateLabel);
        tagPanel.add(tagsFieldsPanel, BorderLayout.EAST);
        tagsFieldsPanel.setBorder(new EmptyBorder(17, 17, 17, 17));
        tagsFieldsPanel.add(childField);
        tagsFieldsPanel.add(areaField);
        //field to enter/display date
        dateField = new JFormattedTextField();
        dateField.setColumns(12);
        tagsFieldsPanel.add(dateField);

        //Additional panel for storing current tags
        TitledBorder titledBorder = new TitledBorder(" Current Children ");
        EmptyBorder emptyBorder = new EmptyBorder(20, 15, 20, 15);
        CompoundBorder compoundBorder = new CompoundBorder(titledBorder, emptyBorder);
        storeTagsPanel.setBorder(compoundBorder);
        tagPanel.add(storeTagsPanel, BorderLayout.SOUTH);
    }
    private void initialiseListeners(){
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.showConfirmDialog (null, "Are you sure you want to quit?", "Warning!", JOptionPane.YES_NO_OPTION);
                if(dialogButton == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        importButton.addActionListener(new ImportButtonListener());

        // change picture thumbnail size when slider is used
        zoomSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                final ArrayList<PictureLabel> thumbs = Library.getThumbsOnDisplay();

                Thread sliderChangeThread = new Thread() {
                    public void run() {

                        try {
                            for (PictureLabel currentThumbnail : thumbs) {
                                currentThumbnail.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider.getValue()]);
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
                setFocusable(true);
                requestFocus();

                /*int currentPanelSize = (int) Math.round(picturePanel.getSize().getWidth());
                int currentWindowSize = (int) Math.round(e.getComponent().getSize().getWidth());
                int framePanelGap = currentWindowSize - currentPanelSize;

                if (framePanelGap < 450) {
                    picturePanelBiggerThanFrame = true;


                }
                else {
                    picturePanelBiggerThanFrame = false;
                    adjustColumnCount();
                }*/
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });

        /**
         * Whenever the scroll pane is scrolled, generates thumbnails coming into view
         * and deletes thumbnails exiting view.
         */
        picturePanelPane.getViewport().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                Rectangle currentView = picturePanel.getVisibleRect();
                for (PictureLabel currentThumbnail: Library.getThumbsOnDisplay()) {
                    if (isInView(currentThumbnail, currentView)) {
                        if (currentThumbnail.getIcon() == null) {
                            currentThumbnail.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider.getValue()]);
                        }
                    } else {
                        currentThumbnail.hideThumbnail();
                    }
                }
                currentView = null;
            }
        });

        this.addKeyListener(tcl);

        areaField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!areaField.getText().isEmpty())
					areaLabel.setForeground(Color.GREEN);
				else
					areaLabel.setForeground(Color.BLACK);
			}
		});
        areaField.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				String text = areaField.getText();
				if (text == null || text.equals(""))
					tagLabel.removeRoom();
				else
					tagLabel.setRoom(areaField.getText());
			}
			
		});
        dateField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!dateField.getText().isEmpty())
					dateLabel.setForeground(Color.GREEN);
				else
					dateLabel.setForeground(Color.BLACK);
			}
		});
        dateField.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				Date date = new Date();
				dateField.setText(""+date);
				tagLabel.setDate(date);
				//TODO implement date properly
			}
			
		});
        childField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = childField.getText().trim();
				if (text != null && !text.equals("")) {
					storeTagsPanel.add(new JLabel(childField.getText()));
					pack();
				}
				childField.setText("");
			}
		});
        storeTagsPanel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int c = storeTagsPanel.getComponentCount();
				if (c != 0) {
					storeTagsPanel.remove(c-1);
					pack();
				}
			}
		});
        resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				areaLabel.setForeground(Color.BLACK);
				areaField.setText("");
				dateLabel.setForeground(Color.BLACK);
				dateField.setText("");
				storeTagsPanel.removeAll();
				childField.setText("");
				tagLabel = new Tag();
				pack();
			}
		});
        doneButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Component[] children = storeTagsPanel.getComponents();
				tagLabel.removeAllChildren();
				for (Component component : children) {
					//TODO check if the child exists
					String nameOfChild = ((JLabel) component).getText();
					Child child = new Child(nameOfChild);
					tagLabel.addChild(child);
				}
				System.out.println(tagLabel);
			}
		});
    }

    private static boolean isInView(PictureLabel thumbnail, Rectangle currentView) {

        if (thumbnail.getBounds().intersects(currentView)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void main(String[] args){
        try {
            // Set System L&F
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e){}
        new MainFrame();
    }

    /**
     * Divides the current size of the picture panel by current thumbnail size
     * to determine required number of columns in GridLayout.
     */

    private void adjustColumnCount() {

        /*if(picturePanelBiggerThanFrame) {

            //picturePanelBiggerThanFrame = false;
        }
        else {
            currentPanelSize = (int) Math.round(picturePanel.getSize().getWidth());
        }*/

        int newColumnCount = 0;
        int currentPanelSize = ((int) Math.round(MainFrame.this.getSize().getWidth())) - 460;

        newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[zoomSlider.getValue()];

        if (picturePanelLayout.getColumns() != newColumnCount && newColumnCount != 0) {
            picturePanelLayout.setColumns(newColumnCount);

            picturePanelLayout.setHgap(2);
            picturePanelLayout.setVgap(2);


            picturePanel.revalidate();
            picturePanel.repaint();
            createThumbnailArray();
        }
    }


    /*public void setPreferredLayoutSize(JPanel panel) {

            int rows = picturePanelLayout.getRows();
            int cols = picturePanelLayout.getColumns();
            if (rows > 0) {
                cols = (cells + rows - 1) / rows;
            }
            else {
                rows = (cells + cols - 1) / cols;
            }


        }
    }*/

    private void createThumbnailArray() {
        int columnCount = picturePanelLayout.getColumns();
        int rowCount = Library.getThumbsOnDisplay().size() / columnCount;
        if (Library.getThumbsOnDisplay().size() % columnCount != 0) {
            ++rowCount;
        }
        System.out.println(rowCount);
        int thumbnailCounter = 0;

        thumbsOnDisplayArray = new PictureLabel[rowCount][columnCount];
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < columnCount; ++j) {
                if (thumbnailCounter != Library.getThumbsOnDisplay().size()) {
                    thumbsOnDisplayArray[i][j] = Library.getThumbsOnDisplay().get(thumbnailCounter++);
                }
            }
        }
        tcl.refresh();


    }

    public static void addThumbnailsToView(ArrayList<Picture> picturesToDisplay) {

        Rectangle currentView = picturePanel.getVisibleRect();

        for(int i = 0; i < picturesToDisplay.size(); ++i){
            PictureLabel currentThumb = new PictureLabel(picturesToDisplay.get(i));
            Library.addThumbToDisplay(currentThumb);
            picturePanel.add(currentThumb);
            currentThumb.showThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider.getValue()]);

        }
    }

    private class ImportButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //open system file manager to ask user for pictures to import
            FileDialog importDialog = new FileDialog(MainFrame.this, "Choose picture(s) to import", FileDialog.LOAD);
            importDialog.setFile("*.jpg");
            importDialog.setMultipleMode(true);
            importDialog.setVisible(true);
            //import pictures into library
            Library.importPicture(importDialog.getFiles());
            importDialog = null;
        }
    }
}
