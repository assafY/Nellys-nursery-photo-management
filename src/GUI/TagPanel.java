package GUI;

import Core.Library;
import Core.Settings;
import Core.Taggable;
import Data.Picture;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TagPanel extends JPanel implements PropertyChangeListener {

    // counter to determine whether even or odd number of labels exist
    private int tagCounter;
    private EmptyBorder mainEmptyBorder;
    private EmptyBorder smallEmptyBorder;
    private JPanel currentTagPanel;
    private JPanel childTagPanel;
    private JPanel areaTagPanel;
    private JPanel dateTagPanel;
    private MainFrame mainFrame;
    private Font biggerFont = new Font("Serif", Font.PLAIN, 1);
    private JFormattedTextField dateTextField;
    private Date newDate;



    public TagPanel(MainFrame mainFrame) {

        mainEmptyBorder = new EmptyBorder(10, 10, 10, 10);
        smallEmptyBorder = new EmptyBorder(3, 3, 3, 3);
        TitledBorder titledBorder = new TitledBorder(" Existing Tags ");
        titledBorder.setTitleFont(biggerFont);
        CompoundBorder compoundBorder = new CompoundBorder(titledBorder,
                mainEmptyBorder);
        setBorder(compoundBorder);
        setLayout(new GridLayout(3, 1));

        createChildTagPanel();
        createAreaTagPanel();
        createDateTagPanel();

        add(childTagPanel);
        add(areaTagPanel);
        add(dateTagPanel);

        this.mainFrame = mainFrame;
    }

    private void createChildTagPanel() {
        childTagPanel = new JPanel();
        childTagPanel.setLayout(new BoxLayout(childTagPanel,
                BoxLayout.Y_AXIS));

        TitledBorder childTitledBorder = new TitledBorder(" Children ");
        childTitledBorder.setTitleFont(biggerFont);
        CompoundBorder childCompoundBorder = new CompoundBorder(childTitledBorder,
                mainEmptyBorder);

        childTagPanel.setBorder(childCompoundBorder);
    }

    private void createAreaTagPanel() {
        areaTagPanel = new JPanel();
        TitledBorder areaTitledBorder = new TitledBorder(" Area ");
        areaTitledBorder.setTitleFont(biggerFont);
        CompoundBorder areaCompoundBorder = new CompoundBorder(areaTitledBorder,
                smallEmptyBorder);

        areaTagPanel.setBorder(areaCompoundBorder);
    }

    private void createDateTagPanel() {
        dateTagPanel = new JPanel();
        TitledBorder dateTitledBorder = new TitledBorder(" Date Taken ");
        dateTitledBorder.setTitleFont(biggerFont);
        CompoundBorder dateCompoundBorder = new CompoundBorder(dateTitledBorder,
                smallEmptyBorder);

        dateTagPanel.setBorder(dateCompoundBorder);
    }

    public void removeTagLabels() {
        childTagPanel.removeAll();
        childTagPanel.repaint();
        areaTagPanel.removeAll();
        areaTagPanel.repaint();
        dateTagPanel.removeAll();
        dateTagPanel.repaint();
    }

    /**
     * This method creates tag labels when components are tagged in a
     * selected thumbnail. It redraws all labels for a picture every time a
     * component is tagged or removed from the picture metadata.
     */
    public void resetTagLabels() throws ParseException {

        removeTagLabels();

        // array list to keep all children tagged in a selected
        // thumbnail or thumbnails
        ArrayList<Taggable> taggedComponents = new ArrayList<Taggable>();
        ArrayList<Picture> selectedPictures = mainFrame.getPicturesPanel().getSelectedPictures();

        tagCounter = 1;
        boolean areaInAllPictures = true;
        ArrayList<Taggable> areaList = new ArrayList<Taggable>();

        // if no pictures are selected
        if (selectedPictures.size() == 0) {
            // TODO: hide panel if there are no tagged pictures
        } else {
            // for every selected picture
            for (int i = 0; i < selectedPictures.size(); ++i) {
                // for every tag that exists on any selected picture
                for (int j = 0; j < selectedPictures.get(i).getTag()
                        .getTaggedComponents().size(); ++j) {
                    // if the tag isn't already on the temp array list
                    if (!taggedComponents.contains(selectedPictures.get(i)
                            .getTag().getTaggedComponents().get(j))) {
                        // add tag to list
                        taggedComponents.add(selectedPictures.get(i)
                                .getTag().getTaggedComponents().get(j));
                    }
                }
            }
        }

        // for every tag found in previous loop
        for (Taggable t : taggedComponents) {
            // check if tagged in all selected pictures

            // child tags
            boolean childInAllPictures = true;
            if (t.getType() == Settings.CHILD_TAG) {
                for (Picture p : selectedPictures) {

                    if (!p.getTag().getTaggedComponents().contains(t)) {
                        childInAllPictures = false;
                    }
                }
            }

            // area tags
            else if (t.getType() == Settings.AREA_TAG && areaInAllPictures) {
                for (Picture p : selectedPictures) {
                    if (p.getTag().isAreaSet()) {
                        if (!areaList.contains(t)) {
                            areaList.add(t);
                        }
                    } else {
                        areaInAllPictures = false;
                    }
                }
            }
            if (!areaInAllPictures || areaList.size() > 1) {
                areaInAllPictures = false;
            }

            // if this is a child tag
            if (t.getType() == Settings.CHILD_TAG) {
                // if tagged in all selected pictures create a tag label and add
                // to existing panel (if odd number of labels) or create a new
                // JPanel one line below (if even number)
                if (childInAllPictures) {
                    if (tagCounter % 2 == 1) {
                        currentTagPanel = new JPanel();
                        currentTagPanel.add(new TagTextLabel(false, t,
                                currentTagPanel, mainFrame));
                        childTagPanel.add(currentTagPanel);
                        validate();
                        ++tagCounter;
                    } else {
                        currentTagPanel.add(new TagTextLabel(false, t,
                                currentTagPanel, mainFrame));
                        validate();
                        ++tagCounter;
                    }
                }
            }
        }

        // if area is the same in all pictures
        if (areaInAllPictures) {
            if (selectedPictures.size() > 0) {
                Taggable areaTag = selectedPictures.get(0).getTag().getArea();
                areaTagPanel.removeAll();
                areaTagPanel.revalidate();
                if (areaTag != null) {
                    // add tag label to area panel
                    areaTagPanel.add(new TagTextLabel(false, areaTag, areaTagPanel, mainFrame));
                }
            }
        }

        // check for similar date on all selected pictures
        String date = null;
        // if no pics selected
        if (selectedPictures.size() == 0) {
            // TODO: no thumnails selected, either reset texfield or
            // simply disable them until picture/s selected
        } else {
            // get the first picture's date
            Date date1 = selectedPictures.get(0).getTag().getDate();
            // for every pic see if the date is the same as the first one's
            for (Picture p : selectedPictures) {
                Date date2 = p.getTag().getDate();
                // if there are any two pictures with different dates
                if (!Library.getFormattedDate(date1).equals(
                        Library.getFormattedDate(date2))) {
                    // don't show date
                    date = "";
                    break;
                }
            }
                // if all have same dates put the date in the field
                if (date == null ) {
                    date = Library.getFormattedDate(date1);
                    dateTagPanel.add(new JLabel(date));

                 //   ArrayList<Picture> picasToChange = mainFrame.getPicturesPanel().getSelectedPictures();
                    for (int i = 0; i < selectedPictures.size(); ++i) {
                        //  for (int j = 0; j < dateTagPanel.getParent().getComponentCount(); ++j) {
                        //dateTagPanel.getParent().getComponent(j) instanceof JLabel
                        if (selectedPictures.get(i).dateIsWrong() == true && selectedPictures.get(i).getTag().getDate() == null) {
                            addDateField();
                        }
                    }
                        }
                    }
                }


    /*  public Date getDate() throws ParseException
      {
          return format.parse((String) dateTextField.getValue());
      }
  */
    public void addDateField() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateTextField = new JFormattedTextField(format);
        dateTextField.setColumns(8);
        dateTextField.setEditable(true);
        dateTextField.setFocusable(true);
        dateTextField.setEnabled(true);

     /*   MaskFormatter mf = new MaskFormatter(MASKFORMAT);
        mf.setValidCharacters("0123456789");
        mf.setPlaceholderCharacter('_');
        DefaultFormatterFactory dff = new DefaultFormatterFactory(mf);
        dateTextField.setFormatterFactory(dff);
*/
      /*  deleteButton = new JLabel();
        deleteButton.setIcon(new ImageIcon(Library.DELETE_BUTTON));
        dateTextField.setLayout(new BorderLayout());
        dateTextField.setBorder(BorderFactory.createLineBorder(Color.black));
        dateTextField.add(deleteButton, BorderLayout.EAST);*/

  /*      deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                dateTagPanel.remove(dateTextField);
                dateTagPanel.revalidate();
            }
        });*/

        dateTagPanel.add(dateTextField);
    //    dateTextField.addPropertyChangeListener(this);
        dateTextField.addPropertyChangeListener("value", this);
       /* PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String text = evt.getNewValue() != null ? evt.getNewValue().toString() : "";
                label.setText(evt.getNewValue());
            }
        };*/
    }

    /*
     PropertyChangeListener l = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String text = evt.getNewValue() != null ? evt.getNewValue().toString() : "";
            label.setText(evt.getNewValue());
        }
    };
    formattedTextField.addPropertyChangeListener("value", l);
     */

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ArrayList<Picture> selectedP = mainFrame.getPicturesPanel().getSelectedPictures();
            // ArrayList<Date> manuallyCreatedDates = new ArrayList<Date>();
            Object source = evt.getSource();
            if (source == dateTextField) {
                newDate = (Date) dateTextField.getValue();
                dateTextField.setValue(newDate);
                System.out.println("New date tag field value: " + dateTextField.getValue().toString());

                for (Picture p : selectedP) {
                    Date taggedDate = (Date) dateTextField.getValue();
                    p.getTag().setDate(taggedDate);
                    //manuallyCreatedDates.add(taggedDate);
                    System.out.println("New picture date tag  " + p.getTag().getDate());
                }
            }
          //  dateTextField.setValue(newDate);
        }


        /**
     * tag labels created when tags are displayed in the tag panel
     */
    public static class TagTextLabel extends JPanel {

        private boolean searchLabel;

        private Taggable taggableItem;
        private JLabel tagLabel;
        private JLabel deleteButton;
        private JPanel tagPanel;
        private MainFrame mainFrame;

        /**
         * the constructor loads the image for the delete button, initialises
         * private fields, and adds the text and button to the label. It calls
         * the addListener() method which sets the delete button click listener.
         *
         * @param t         the tag item to create a label for
         * @param tagPanel  the tag panel
         * @param mainFrame the main software window
         */
        public TagTextLabel(boolean isSearchLabel, Taggable t, JPanel tagPanel, MainFrame mainFrame) {

            this.searchLabel = isSearchLabel;
            this.taggableItem = t;

            tagLabel = new JLabel(t.getName());
            setBorder(BorderFactory.createLineBorder(Color.black));

            deleteButton = new JLabel();
            deleteButton.setIcon(new ImageIcon(Library.DELETE_BUTTON));

            setLayout(new BorderLayout());
            add(tagLabel, BorderLayout.CENTER);
            add(deleteButton, BorderLayout.EAST);

            this.tagPanel = tagPanel;
            this.mainFrame = mainFrame;

            addListener();

        }

        @Override
        public int getWidth() {
            return tagLabel.getWidth() + deleteButton.getWidth();
        }

        private void addListener() {
            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    tagPanel.remove(TagTextLabel.this);
                    tagPanel.revalidate();
                    if (searchLabel) {
                        mainFrame.removeSearchTag(taggableItem);
                        mainFrame.refreshSearch();
                    }
                    else {
                        for (Picture p : mainFrame.getPicturesPanel().getSelectedPictures()) {
                            if (p.getTag().getTaggedComponents().contains(taggableItem)) {
                                p.getTag().removeTag(taggableItem);
                                taggableItem.removeTaggedPicture(p);
                                try {
                                    mainFrame.createTagLabels();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                    tagPanel.revalidate();
                }
            });
        }
    }
}

