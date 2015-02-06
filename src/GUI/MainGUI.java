package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

public class MainGUI extends JFrame implements TreeSelectionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JFrame mainFrame;
    BorderLayout mainLayout;
    JLabel labelFilter;
    JTextField filterField;

    JCheckBox taggedButton, unTaggedButton, incompleteButton, allButton;

    JTree foldersList;

    JPanel mainPanel, filterPanel, checkPanel, leftPanel, rightPanel,
            tagsLabels, tagsFields, descriptionPanel, rightPan2;

    String childName = "Child name: ";
    String area = "Area :";
    String date = "Date: ";

    static final int ZOOM_MIN = 0;
    static final int ZOOM_MAX = 100;
    static final int ZOOM_INIT = 50;

    JFormattedTextField nameField;
    JFormattedTextField areaField;
    JFormattedTextField dateField;

    DateFormat dateFormat;
    JMenuBar bar;
    JMenu fileMenu, editMenu, viewMenu, helpMenu, toolsMenu;

    JButton deleteButton, printButton, rotateButton;

    DefaultTreeModel treeModel;
    JTree foldersTree;

    File root;

    public MainGUI() throws IOException {

        mainLayout = new BorderLayout();
        mainPanel = new JPanel(mainLayout);
        Color colour = new Color(83, 104, 114);
        // this.setBackground(colour);
        // setLayout(mainLayout);
        // mainFrame.add(mainPanel);

        bar = new JMenuBar();
        this.setJMenuBar(bar);

        // Menu bars
        fileMenu = new JMenu("File");
        bar.add(fileMenu);
        editMenu = new JMenu("Edit");
        bar.add(editMenu);
        viewMenu = new JMenu("View");
        bar.add(viewMenu);
        toolsMenu = new JMenu("Tools");
        bar.add(toolsMenu);
        helpMenu = new JMenu("Help");
        bar.add(helpMenu);

        // Left hand side
        leftPanel = new JPanel(new BorderLayout());
        TitledBorder title = BorderFactory.createTitledBorder("Folders: ");
        title.setTitleJustification(TitledBorder.LEFT);
        EmptyBorder titleEmpty = new EmptyBorder(5, 7, 5, 2);
        CompoundBorder myCompound = BorderFactory.createCompoundBorder(title,
                titleEmpty);
        leftPanel.setBorder(myCompound);

		/*
		 * FoldersList test = new FoldersList(); String[] folders =
		 * FoldersList.getDirectories(new File("C:\\")); for (int i = 0; i <
		 * folders.length; i++) { System.out.println(folders[i]); }
		 */

        // System folders tree

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(rootNode);

        File[] systemRoots = FileSystemView.getFileSystemView().getRoots();
        for (File systemFile : systemRoots) {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
                    systemFile);
            rootNode.add(newNode);

            File[] systemFiles = FileSystemView.getFileSystemView().getFiles(
                    systemFile, true);
            for (File file : systemFiles) {
                if (file.isDirectory()) {
                    newNode.add(new DefaultMutableTreeNode(file));
                }
            }
        }

        // (new File(System.getProperty("C:\\")));
        foldersTree = new JTree(treeModel);
        leftPanel.add(new JScrollPane(foldersTree,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        foldersTree.expandRow(0);
        foldersTree.setSelectionInterval(0, 0);

        foldersTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        foldersTree.addTreeSelectionListener(this);

        // TOP panel, filter text field
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel = new JPanel();
      // filterPanel.setLayout(new GridBagLayout());


        labelFilter = new JLabel("Filter:");
        filterField = new JTextField(22);

        // Check boxes
        taggedButton = new JCheckBox("Tagged");
        // taggedButton.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        taggedButton.setMnemonic(KeyEvent.VK_T);
        taggedButton.setSelected(false);

        unTaggedButton = new JCheckBox("Untagged");
        // unTaggedButton.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        unTaggedButton.setMnemonic(KeyEvent.VK_T);
        unTaggedButton.setSelected(false);

        incompleteButton = new JCheckBox("Incomplete");
        // incompleteButton.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        incompleteButton.setMnemonic(KeyEvent.VK_T);
        incompleteButton.setSelected(false);

        allButton = new JCheckBox("All");
        allButton.setMnemonic(KeyEvent.VK_T);
        allButton.setSelected(true);

        checkPanel = new JPanel(new GridLayout(1, 0));

        checkPanel.add(taggedButton);
        checkPanel.add(unTaggedButton);
        checkPanel.add(incompleteButton);
        checkPanel.add(allButton);
        checkPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        filterPanel.add(labelFilter);
        labelFilter.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        filterPanel.add(filterField);

        // Central part of the main window
        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        // Buttons at the bottom left
        JPanel centralLeft = new JPanel(new BorderLayout());
        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        printButton = new JButton("Print");
        rotateButton = new JButton("Rotate..");
        deleteButton = new JButton("Delete");
        printPanel.add(printButton);
        printPanel.add(rotateButton);
        printPanel.add(deleteButton);
        printPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centralLeft.add(printPanel, BorderLayout.SOUTH);

        // Zoom slider at the bottom right
        JPanel zoomPanel = new JPanel(new BorderLayout());
        zoomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JSlider zoom = new JSlider(JSlider.HORIZONTAL, ZOOM_MIN, ZOOM_MAX,
                ZOOM_INIT);
        zoom.setMajorTickSpacing(100);
        zoom.setPaintLabels(true);
        zoomPanel.add(zoom, BorderLayout.SOUTH);

        centralPanel.add(centralLeft, BorderLayout.WEST);
        centralPanel.add(zoomPanel, BorderLayout.EAST);

        // Right hand side - Tagging
        rightPanel = new JPanel(new BorderLayout());
        JPanel rightPan1 = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Add Tag: "));

        tagsLabels = new JPanel(new GridLayout(0, 1));
        tagsLabels.setBorder(new EmptyBorder(7, 7, 7, 7));
        tagsLabels.add(new JLabel(childName));
        tagsLabels.add(new JLabel(area));
        tagsLabels.add(new JLabel(date));

        tagsFields = new JPanel(new GridLayout(0, 1));
        tagsFields.setBorder(new EmptyBorder(17, 17, 17, 17));

		/*
		 * int i=0; for(i=1; i<4; ++i){ tagsFields.add(new JTextField(10)); }
		 */
        tagsFields.add(new JTextField(12));
        tagsFields.add(new JTextField(12));
        dateField = new JFormattedTextField(dateFormat);
        dateField.setColumns(12);
        tagsFields.add(dateField);

        rightPan1.add(tagsLabels, BorderLayout.CENTER);
        rightPan1.add(tagsFields, BorderLayout.EAST);

        rightPan2 = new JPanel(new BorderLayout());
        descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        descriptionPanel.setBorder(new TitledBorder("Add desription: "));
        descriptionPanel.add(new JScrollPane(new JTextArea(7, 21)),
                BorderLayout.CENTER);
        JPanel donePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        donePanel.add(new JButton("Reset"));
        donePanel.add(new JButton("Done"));
        descriptionPanel.add(donePanel, BorderLayout.SOUTH);

        rightPanel.add(descriptionPanel, BorderLayout.CENTER);
        rightPanel.add(rightPan1, BorderLayout.NORTH);
        rightPanel.add(donePanel, BorderLayout.SOUTH);

        topPanel.add(filterPanel, BorderLayout.LINE_START);
        topPanel.add(checkPanel, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(centralPanel, BorderLayout.CENTER);

        // getContentPane().setBackground(colour);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Dimension mainSize = Toolkit.getDefaultToolkit().getScreenSize();

        //this.setSize(1200, 900);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);

    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath selectedPath = foldersTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) foldersTree
                .getLastSelectedPathComponent();
        // System.out.print(selectedPath.toString());
        listSubfolders(node);
    }

    private void listSubfolders(DefaultMutableTreeNode node) {
        File fileObject = (File) node.getUserObject();
        if (fileObject.isDirectory()) {
            File[] myFiles = FileSystemView.getFileSystemView().getFiles(
                    fileObject, true);
            if (node.isLeaf()) {
                for (File kiddo : myFiles) {
                    if (kiddo.isDirectory()) {

                        //kiddo.listFiles();
                        expand(node, kiddo);
                    }

                }

            }
        }

    }

    // FIX THIS FFS!!
    private DefaultMutableTreeNode expand(DefaultMutableTreeNode defNode, File kiddo) {

        String path = kiddo.getPath();
        DefaultMutableTreeNode thisNode = new DefaultMutableTreeNode(path);
        if (defNode != null) {
            defNode.add(thisNode);
        }
		
		/*Vector ol = new Vector();
	    String[] tmp = kiddo.list();
	    for (int i = 0; i < tmp.length; i++)
	      ol.addElement(tmp[i]);
	    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
	    File f;*/
        return thisNode;

    }



    public static void main(String args[]) throws IOException {
        MainGUI test = new MainGUI();

        // This is not working for some reason, sloppily created frame I guess
        Color colour = new Color(83, 104, 114);
        test.getContentPane().setBackground(colour);


    }
}
