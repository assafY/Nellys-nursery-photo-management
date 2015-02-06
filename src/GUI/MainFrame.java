package GUI;

import Core.Library;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
    MenuItem bckup = new MenuItem("Backup");
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
    private JButton selectButton = new JButton("Select");
    private JButton tagButton = new JButton("Tag");
    private JButton rotateButton = new JButton("Rotate");
    private JButton resizeButton = new JButton("Resize");
    private JButton cropButton = new JButton("Crop");
    private JButton deleteButton = new JButton("Delete");
    private JButton printButton = new JButton("Print");
    //create center components
    private JPanel centerPanel = new JPanel(new BorderLayout());
    private JPanel picturePanel = new JPanel();
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
        file.add(bckup);
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
        c.insets = new Insets(4,0,4,0);
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
        c.gridy = 4;
        buttonPanel.add(selectButton, c);
        c.gridy = 5;
        buttonPanel.add(tagButton, c);
        c.gridy = 6;
        buttonPanel.add(rotateButton, c);
        c.gridy = 7;
        buttonPanel.add(resizeButton, c);
        c.gridy = 8;
        buttonPanel.add(cropButton, c);
        c.gridy = 9;
        buttonPanel.add(deleteButton, c);
        c.gridy = 10;
        buttonPanel.add(printButton, c);

        TitledBorder titledBorder = new TitledBorder("Tools: ");
        EmptyBorder emptyBorder = new EmptyBorder(7, 10, 7, 10);
        CompoundBorder compoundBorder = new CompoundBorder(titledBorder, emptyBorder);
        westPanel.setBorder(compoundBorder);


    }
    private void createCenterPanel(){
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(scrollPanel, BorderLayout.SOUTH);
        zoomSlider.setOrientation(Adjustable.HORIZONTAL);
        scrollPanel.add(zoomSlider);
        //scrollPanel.add(Box.createHorizontalStrut(5), BorderLayout.SOUTH);


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
