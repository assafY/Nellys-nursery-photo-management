package GUI;

import Core.Library;
import Core.Settings;
import Core.Taggable;
import Data.Picture;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class TagPanel extends JPanel {

    // counter to determine whether even or odd number of labels exist
    private int tagCounter;
    private EmptyBorder mainEmptyBorder;
    private EmptyBorder smallEmptyBorder;
    private JPanel currentTagPanel;
    private JPanel childTagPanel;
    private JPanel areaTagPanel;
    private JPanel dateTagPanel;
    private MainFrame mainFrame;


    public TagPanel(MainFrame mainFrame) {

        mainEmptyBorder = new EmptyBorder(10, 10, 10, 10);
        smallEmptyBorder = new EmptyBorder(3, 3, 3, 3);
        TitledBorder titledBorder = new TitledBorder(" Existing Tags ");
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
        CompoundBorder childCompoundBorder = new CompoundBorder(childTitledBorder,
                mainEmptyBorder);

        childTagPanel.setBorder(childCompoundBorder);
    }

    private void createAreaTagPanel() {
        areaTagPanel = new JPanel();
        TitledBorder areaTitledBorder = new TitledBorder(" Area ");
        CompoundBorder areaCompoundBorder = new CompoundBorder(areaTitledBorder,
                smallEmptyBorder);

        areaTagPanel.setBorder(areaCompoundBorder);
    }

    private void createDateTagPanel() {
        dateTagPanel = new JPanel();
        TitledBorder dateTitledBorder = new TitledBorder(" Date Taken ");
        CompoundBorder dateCompoundBorder = new CompoundBorder(dateTitledBorder,
                smallEmptyBorder);

        dateTagPanel.setBorder(dateCompoundBorder);
    }

    public void resetTagLabels() {

        childTagPanel.removeAll();
        childTagPanel.revalidate();
        areaTagPanel.removeAll();
        areaTagPanel.revalidate();
        dateTagPanel.removeAll();
        dateTagPanel.revalidate();

        // array list to keep all children tagged in a selected
        // thumbnail or thumbnails
        ArrayList<Taggable> taggedComponents = new ArrayList<Taggable>();
        ArrayList<Picture> selectedPictures = mainFrame.getSelectedPictures();

        tagCounter = 1;

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

        boolean areaInAllPictures = true;
        Taggable area1 = selectedPictures.get(0).getTag().getArea();
        Taggable area2 = null;

        // for every tag found in previous loop
        for (Taggable t : taggedComponents) {
            // check if tagged in all selected pictures

            boolean childInAllPictures = true;
            boolean areaTagged = false;
            if (t.getType() == Settings.CHILD_TAG) {
                for (Picture p : selectedPictures) {

                    if (!p.getTag().getTaggedComponents().contains(t)) {
                        childInAllPictures = false;
                    }

                }
            }

            else if (t.getType() == Settings.AREA_TAG && !areaTagged) {
                areaTagged = true;
                for (Picture p: selectedPictures) {
                    Taggable areaTag = p.getTag().getArea();
                    if (areaTag != null) {
                        area2 = areaTag;
                    }
                }
                if (area2 == null || !area1.equals(area2)) {
                    System.out.println(area1 + " does not equal " + area2);
                    areaInAllPictures = false;
                }
                else {
                    areaInAllPictures = true;
                }
            }
            if (!areaTagged) {
                areaInAllPictures = false;
            }


            /* room */
            /*String room = null;
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

            }*/


            // if this is a child tag
            if (t.getType() == Settings.CHILD_TAG) {
                // if tagged in all selected pictures create a tag label and add
                // to existing panel (if odd number of labels) or create a new
                // JPanel one line below (if even number)
                if (childInAllPictures) {
                    if (tagCounter % 2 == 1) {
                        currentTagPanel = new JPanel();
                        currentTagPanel.add(new TagTextLabel(t,
                                currentTagPanel, mainFrame));
                        childTagPanel.add(currentTagPanel);
                        validate();
                        ++tagCounter;
                    } else {
                        currentTagPanel.add(new TagTextLabel(t,
                                currentTagPanel, mainFrame));
                        validate();
                        ++tagCounter;
                    }
                }
            }
        }


        if (areaInAllPictures) {
            Taggable areaTag = selectedPictures.get(0).getTag().getArea();
            areaTagPanel.removeAll();
            areaTagPanel.revalidate();
            if (areaTag != null) {
                areaTagPanel.add(new TagTextLabel(areaTag, areaTagPanel, mainFrame));
                System.out.println("same area in all pics");
            }
        }
        else {
            areaTagPanel.removeAll();
            areaTagPanel.validate();
        }

		/* date */
            String date = null;
            // if no pics selected
            if (selectedPictures.size() == 0) {
                // TODO: no thumnails selected, either reset texfield or
                // simply disable them until picture/s selected
            } else {
                // get the first picture's date
                Date date1 = selectedPictures.get(0).getTag().getDate();
                // for every pic see if the date is the same as the firs one's
                for (Picture p : selectedPictures) {
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

                dateTagPanel.add(new JLabel(date));
            }
        }

}
