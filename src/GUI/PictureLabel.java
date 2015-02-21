package GUI;

import Core.Library;
import Core.Settings;
import Data.Picture;
import Data.ThumbnailClickListener;
import org.imgscalr.Scalr;
import sun.applet.Main;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PictureLabel extends JLabel {

    private static final int DEFAULT_SIZE = Settings.THUMBNAIL_SIZES[4];

    private Picture picture;
    private int currentSize;
    private boolean isSelected;
    private Image image;

    public PictureLabel(Picture picture) {
        this.picture = picture;
        isSelected = false;

        this.addMouseListener(new ThumbnailMouseListener());
    }

    public void showThumbnail(int size) {
        currentSize = size;
        if(picture.getThumbnail().getHeight() > picture.getThumbnail().getWidth()) {
            if (currentSize == 109) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 27)));
                this.setBorder(BorderFactory.createEmptyBorder(0,23,0,0));
            } else if (currentSize == 119) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 30)));
                this.setBorder(BorderFactory.createEmptyBorder(0,26,0,0));
            } else if (currentSize == 132) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 33)));
                this.setBorder(BorderFactory.createEmptyBorder(0,29,0,0));
            } else if (currentSize == 148) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 37)));
                this.setBorder(BorderFactory.createEmptyBorder(0,32,0,0));
            } else if (currentSize == 169) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 42)));
                this.setBorder(BorderFactory.createEmptyBorder(0,35,0,0));
            } else if (currentSize == 196) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 49)));
                this.setBorder(BorderFactory.createEmptyBorder(0,41,0,0));
            } else if (currentSize == 233) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 58)));
                this.setBorder(BorderFactory.createEmptyBorder(0,49,0,0));
            } else if (currentSize == 288) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 72)));
                this.setBorder(BorderFactory.createEmptyBorder(0,60,0,0));
            } else if (currentSize == 377) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 94)));
                this.setBorder(BorderFactory.createEmptyBorder(0,78,0,0));
            } else if (currentSize == 545) {
                setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize - 136)));
                this.setBorder(BorderFactory.createEmptyBorder(0,114,0,0));
            }
        } else {
            setIcon(new ImageIcon(Scalr.resize(picture.getThumbnail(), currentSize)));
        }
    }

    public void hideThumbnail() {
        setIcon(null);
    }

    public void toggleSelection() {
        if (!isSelected) {
            isSelected = true;
            repaint();
        }
        else {
            isSelected = false;
            repaint();
        }

    }

    public boolean isSelected() {
        return isSelected;
    }

    public Picture getPicture() {
        return picture;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(0xD3, 0xD3, 0xD3, 0x50));
            g2.fillRect(2, 2, currentSize-2, getHeight() - 3);
            g2.setColor(Color.GRAY);
            g2.drawRect(0, 0, currentSize-2, getHeight() - 2);

        }
    }

    public class ThumbnailMouseListener extends MouseAdapter {

        /**
         * Incomlete picture selection method. Ideally a 2D array should store all labels and enable moving between
         * thumbnails using arrow keys and viewing a single image using spacebar. Additionally only one picture should be
         * selected at one time unless mouse is dragged across pictures or ctrl key is held. Good luck with that.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (PictureLabel.this.getParent() != null){
                MouseListener[] m = PictureLabel.this.getParent().getMouseListeners();
                for (MouseListener ml : m) {
                    ml.mouseClicked(e);
                }
            }
            int clickCount = e.getClickCount();
            if (clickCount == 1) {

                if (!ThumbnailClickListener.shiftIsPressed) {
                    Library.removeAllSelectedThumbs();
                }

                if (isSelected) {
                    Library.removeSelectedThumb(PictureLabel.this);
                    ThumbnailClickListener.mostRecentSelection = null;
                    ThumbnailClickListener.refresh();
                    MainFrame.createTagLabels();
                    toggleSelection();
                }
                else {
                    Library.addSelectedThumb(PictureLabel.this);
                    ThumbnailClickListener.mostRecentSelection = PictureLabel.this;
                    ThumbnailClickListener.refresh();
                    MainFrame.createTagLabels();
                }
                toggleSelection();
            }
            else if (clickCount == 2) {
                //TODO: Open single image view
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

    }
}
