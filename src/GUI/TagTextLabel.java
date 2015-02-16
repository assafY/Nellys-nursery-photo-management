package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TagTextLabel extends JPanel {

    private static BufferedImage tagDeleteButton;

    private JLabel tagLabel;
    private JLabel deleteButton;

    public TagTextLabel(String tag) {

        if (tagDeleteButton == null) {
            loadTagDeleteButton();
        }

        tagLabel = new JLabel(tag);
        setBorder(BorderFactory.createLineBorder(Color.black));

        deleteButton = new JLabel();
        deleteButton.setIcon(new ImageIcon(tagDeleteButton));

        setLayout(new BorderLayout());
        add(tagLabel, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.EAST);

        addListener();

    }

    private void loadTagDeleteButton() {
        try {
            tagDeleteButton = ImageIO.read(TagTextLabel.class.getResource("/images/delete_button.png"));
        } catch (IOException e) {
            //TODO: Handle exception
        }
    }

    private void addListener() {
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                MainFrame.storeTagsPanel.remove(TagTextLabel.this);
                MainFrame.storeTagsPanel.revalidate();
            }
        });
    }
}