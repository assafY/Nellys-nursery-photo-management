package GUI;

import Core.Library;
import Core.Settings;
import Data.Picture;
import Data.PictureLabel;
import static javax.swing.ScrollPaneConstants.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Hashtable;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

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
    GridLayout picturePanelLayout = new GridLayout(0, 1, 5, 5);
    //private BorderLayout picturePanelBorderLayout = new BorderLayout();

    private JPanel centerPanel = new JPanel(new BorderLayout());
    private JPanel picturePanel = new JPanel(picturePanelLayout);
    private JScrollPane picturePanelPane = new JScrollPane(picturePanel);
    private JPanel scrollPanel = new JPanel();
    private JSlider zoomSlider;
    //create east components
    private JPanel eastPanel = new JPanel(new BorderLayout());
    private JPanel tagPanel = new JPanel(new BorderLayout());
    private JPanel tagsLabelsPanel = new JPanel(new GridLayout(0, 1));
    private JPanel tagsFieldsPanel = new JPanel(new GridLayout(0, 1));
    private JPanel descriptionPanel = new JPanel(new BorderLayout());
    private JPanel donePanel = new JPanel();

    private int currentColumnCount = 0;
    private boolean picturePanelBiggerThanFrame = false;

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
        setVisible(true);
        initialiseListeners();
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
        zoomSlider = new JSlider(Adjustable.HORIZONTAL, 0, 10, 5);
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
        donePanel.add(new JButton("Reset"));
        donePanel.add(new JButton("Done"));
        eastPanel.add(tagPanel, BorderLayout.NORTH);
        tagPanel.add(tagsLabelsPanel, BorderLayout.CENTER);
        tagsLabelsPanel.setBorder(new EmptyBorder(7, 7, 7, 7));
        tagsLabelsPanel.add(new JLabel("Child name"));
        tagsLabelsPanel.add(new JLabel("Area"));
        tagsLabelsPanel.add(new JLabel("Date"));
        tagPanel.add(tagsFieldsPanel, BorderLayout.EAST);
        tagsFieldsPanel.setBorder(new EmptyBorder(17, 17, 17, 17));
        tagsFieldsPanel.add(new JTextField(12));
        tagsFieldsPanel.add(new JTextField(12));
        //field to enter/display date
        JFormattedTextField dateField = new JFormattedTextField();
        dateField.setColumns(12);
        tagsFieldsPanel.add(dateField);
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

                final ArrayList<PictureLabel> thumbs = Library.getPictureLabels();

                Thread sliderChangeThread = new Thread() {
                    public void run() {
                        for (PictureLabel picLabel : thumbs) {
                            picLabel.createThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider.getValue()]);
                        }
                    }
                };

                Thread runAfterResize = new Thread() {
                    public void run() {
                        try {
                            sleep(500);
                        } catch (InterruptedException e1) {

                        }
                        adjustColumnCount();
                    }
                };

                sliderChangeThread.start();
                runAfterResize.start();

            }
        });

        // adjust number of columns when window size changes
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {

                int currentPanelSize = (int) Math.round(picturePanel.getSize().getWidth());
                int currentWindowSize = (int) Math.round(e.getComponent().getSize().getWidth());
                int framePanelGap = currentWindowSize - currentPanelSize;

                if (framePanelGap < 450) {
                    picturePanelBiggerThanFrame = true;
                    adjustColumnCount();

                }
                else {
                    picturePanelBiggerThanFrame = false;
                    adjustColumnCount();
                }
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });
    }

    private void adjustColumnCount() {
        int newColumnCount = 0;
        int currentPanelSize;

        //if(picturePanelBiggerThanFrame) {
            currentPanelSize = ((int) Math.round(MainFrame.this.getSize().getWidth())) - 450;
            //picturePanelBiggerThanFrame = false;
        /*}
        else {
            currentPanelSize = (int) Math.round(picturePanel.getSize().getWidth());
        }*/
        switch (zoomSlider.getValue()) {
            case 0: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[0];
                     break;
            case 1: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[1];
                break;
            case 2: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[2];
                break;
            case 3: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[3];
                break;
            case 4: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[4];
                break;
            case 5: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[5];
                break;
            case 6: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[6];
                break;
            case 7: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[7];
                break;
            case 8: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[8];
                break;
            case 9: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[9];
                break;
            case 10: newColumnCount = currentPanelSize / Settings.THUMBNAIL_SIZES[10];
                break;
        }

        if (picturePanelLayout.getColumns() != newColumnCount && newColumnCount != 0) {
            picturePanelLayout.setColumns(newColumnCount);
            picturePanel.revalidate();
        }
    }

    public class ImportButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            FileDialog importDialog = new FileDialog(MainFrame.this, "Choose picture(s) to import", FileDialog.LOAD);
            importDialog.setFile("*.jpg");
            importDialog.setMultipleMode(true);
            importDialog.setVisible(true);

            final File[] importedPictures = importDialog.getFiles();

            for (int i = 0; i < importedPictures.length; ++i)

            {
                final int currentIndex = i;
                Thread newPictureImport = new Thread() {

                    Picture currentPicture;
                    PictureLabel currentThumb;

                    public void run() {

                        currentPicture = new Picture(importedPictures[currentIndex]);
                        importedPictures[currentIndex] = null;
                        currentThumb = new PictureLabel(currentPicture);
                        currentPicture = null;
                        currentThumb.createThumbnail(Settings.THUMBNAIL_SIZES[zoomSlider.getValue()]);

                        Library.addPictureLabel(currentThumb);
                        picturePanel.add(currentThumb);
                        currentThumb = null;

                    }
                };

                newPictureImport.start();

                // if this is the last picture in batch
                if (i == importedPictures.length - 1) {

                    try {
                        // stall until all pictures processed
                        newPictureImport.sleep(1000);
                        newPictureImport.join();
                    } catch (InterruptedException ex) {
                    }
                }
            }
            System.out.println("USED MEMORY: " + Runtime.getRuntime().totalMemory() + "FREE MEMORY: " + Runtime.getRuntime().freeMemory());

            pack();
        }


    }

    public static void main(String[] args){
        try {
                // Set System L&F
                UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
            }
        catch (Exception e){}
        new MainFrame();
    }
}
