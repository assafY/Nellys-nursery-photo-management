package GUI;

import Core.Library;
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

    private Child child;
    private JLabel tagLabel;
    private JLabel deleteButton;
    private JPanel tagPanel;

    public TagTextLabel(Child c, JPanel tagPanel) {

        if (tagDeleteButton == null) {
            loadTagDeleteButton();
        }

        this.child = c;

        tagLabel = new JLabel(c.getName());
        setBorder(BorderFactory.createLineBorder(Color.black));

        deleteButton = new JLabel();
        deleteButton.setIcon(new ImageIcon(tagDeleteButton));

        setLayout(new BorderLayout());
        add(tagLabel, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.EAST);

        this.tagPanel = tagPanel;

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
                for (Picture p: Library.getSelectedPictures()) {
                    if (p.getTag().getChildren().contains(child)) {
                        p.getTag().removeChild(child);
                        MainFrame.createTagLabels();
                    }
                }
            }
        });
    }
}