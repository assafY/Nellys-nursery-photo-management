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


        // for every tag found in previous loop
        for (Taggable t : taggedComponents) {
            // check if tagged in all selected pictures
            boolean childInAllPictures = true;
            boolean areaInAllPictures = true;

            for (Picture p : selectedPictures) {
                if (!childInAllPictures && !areaInAllPictures) {
                    break;
                }
                if (!p.getTag().getTaggedComponents().contains(t)) {
                    if (t.getType() == Settings.CHILD_TAG) {
                        childInAllPictures = false;
                    }
                    else if (t.getType() == Settings.AREA_TAG) {
                        areaInAllPictures = false;
                        System.out.println(t.getName() + ": " + t.getType());

                    }
                }
            }

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
            else if (t.getType() == Settings.AREA_TAG) {
                if (areaInAllPictures) {
                    areaTagPanel.add(new TagTextLabel(t,
                            areaTagPanel, mainFrame));
                    validate();
                }
            }

            //System.out.println(t.getName() + ": " + t.getType());
        }

        // reset date label
        // selected pictures array and date string to go in text field
        ArrayList<Picture> picturesToTag = mainFrame.getSelectedPictures();

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

            dateTagPanel.add(new JLabel(date));
        }
    }
}
