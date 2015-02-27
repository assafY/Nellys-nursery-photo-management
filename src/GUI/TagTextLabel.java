package GUI;

import Core.Library;
import Core.Taggable;
import Data.Child;
import Data.Picture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TagTextLabel extends JPanel {

    private static BufferedImage tagDeleteButton;

    private Taggable taggableItem;
    private JLabel tagLabel;
    private JLabel deleteButton;
    private JPanel tagPanel;
    private MainFrame mainFrame;

    public TagTextLabel(Taggable t, JPanel tagPanel, MainFrame mainFrame) {

        if (tagDeleteButton == null) {
            loadTagDeleteButton();
        }

        this.taggableItem = t;

        tagLabel = new JLabel(t.getName());
        setBorder(BorderFactory.createLineBorder(Color.black));

        deleteButton = new JLabel();
        deleteButton.setIcon(new ImageIcon(tagDeleteButton));

        setLayout(new BorderLayout());
        add(tagLabel, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.EAST);

        this.tagPanel = tagPanel;
        this.mainFrame = mainFrame;

        addListener();

    }

    private void loadTagDeleteButton() {
        try {
            tagDeleteButton = ImageIO.read(TagTextLabel.class.getResource("/images/delete_button.png"));
        } catch (IOException e) {
            //TODO: Handle exception
        }
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
                for (Picture p: mainFrame.getSelectedPictures()) {
                    if (p.getTag().getTaggedComponents().contains(taggableItem)) {
                        p.getTag().removeTag(taggableItem);
                        taggableItem.removeTaggedPicture(p);
                        mainFrame.createTagLabels();
                    }
                }
            }
        });
    }
}