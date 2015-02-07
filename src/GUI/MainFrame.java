package GUI;

import Core.Library;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by val on 05/02/2015.
 */
public class MainFrame extends JFrame {

    //create menu bar
    private MenuBar menuBar = new MenuBar();
    Menu file = new Menu("File");
    Menu edit = new Menu("Edit");
    //Menu view = new Menu("View");
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
    private JPanel centerPanel = new JPanel(new BorderLayout());
    private JPanel functionPanel = new JPanel(new GridBagLayout());
    private JButton rotateButton = new JButton("Rotate");
    private JButton deleteButton = new JButton("Delete");
    private JButton printButton = new JButton("Print");
    private GridLayout picturePanelLayout = new GridLayout(1, 3);
    private JPanel picturePanel = new JPanel(picturePanelLayout);
    private JPanel scrollPanel = new JPanel();
    private JSlider zoomSlider = new JSlider();
    //create east components
    private JPanel eastPanel = new JPanel(new BorderLayout());
    private JPanel tagPanel = new JPanel(new BorderLayout());
    private JPanel tagsLabelsPanel = new JPanel(new GridLayout(0, 1));
    private JPanel tagsFieldsPanel = new JPanel(new GridLayout(0, 1));
    private JPanel descriptionPanel = new JPanel(new BorderLayout());
    private JPanel donePanel = new JPanel();

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
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(picturePanel, BorderLayout.CENTER);
        centerPanel.add(functionPanel, BorderLayout.SOUTH);
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
        functionPanel.add(zoomSlider, c);
        mainPanel.add(picturePanel, BorderLayout.CENTER);

        picturePanel.add(scrollPanel, BorderLayout.SOUTH);
        zoomSlider.setOrientation(Adjustable.HORIZONTAL);
        zoomSlider.setMajorTickSpacing(50);
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




        TitledBorder titledBorder = new TitledBorder("Pictures: ");
        EmptyBorder emptyBorder = new EmptyBorder(7, 7, 1, 7);
        CompoundBorder compoundBorder = new CompoundBorder(emptyBorder, titledBorder);
        picturePanel.setBorder(compoundBorder);


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

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog importDialog = new FileDialog(MainFrame.this, "Choose picture(s) to import", FileDialog.LOAD);
                importDialog.setFile("*.jpg");
                importDialog.setMultipleMode(true);
                importDialog.setVisible(true);

                File[] importedPictures = importDialog.getFiles();
                //Double numofCols = Math.sqrt(importedPictures.length);
                //int newNum = numofCols.intValue();
                //picturePanelLayout.setColumns(newNum);
                //picturePanelLayout = new GridLayout(1, 3);

                for(int i = 0; i < importedPictures.length; ++i) {

                    BufferedImage currentPic = new BufferedImage(10, 10, 1);

                    try {
                        currentPic = ImageIO.read(importedPictures[i]);
                    } catch (IOException ex) {

                    }
                    JLabel currentThumb = new JLabel(new ImageIcon(currentPic));
                    picturePanel.add(currentThumb);
                }
            }
        });
    }

    private void loadPictures() {

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
