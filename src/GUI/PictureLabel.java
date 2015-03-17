package GUI;

import Core.Settings;
import Data.Picture;
import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PictureLabel extends JLabel{

    private static final int DEFAULT_SIZE = Settings.THUMBNAIL_SIZES[2];

    private Picture picture;
    private int currentSize;
    private boolean isSelected;
    private BufferedImage thumbnail;
    private boolean horizontal = true;
    private FullScreenPicturesFrame frame;
    private PicturesFrame picturePanel;

    public PictureLabel(Picture picture, PicturesFrame mainFrame) {
        this.picture = picture;
        isSelected = false;
        this.picturePanel = mainFrame;

        ThumbnailMouseListener mouseInput = new ThumbnailMouseListener();
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.setAlignmentX(JLabel.CENTER);
    }

    public void createThumbnail() {
        BufferedImage newThumbnail = null;
        Settings.LOADED_THUMBNAILS_COUNT++;
        try {
            newThumbnail = ImageIO.read(picture.getImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (newThumbnail != null) {
            thumbnail = Scalr.resize(newThumbnail, Scalr.Method.BALANCED, DEFAULT_SIZE);
        }
    }

    public void showThumbnail(int size) {
        if (thumbnail != null) {
            currentSize = size;
            if (thumbnail.getHeight() > thumbnail.getWidth()) {
                horizontal = false;
                switch (currentSize) {
                    case 109:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 27)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 23, 0, 0));
                        break;
                    case 119:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 30)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 26, 0, 0));
                        break;
                    case 132:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 33)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 29, 0, 0));
                        break;
                    case 148:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 37)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
                        break;
                    case 169:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 42)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 0));
                        break;
                    case 196:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 49)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 41, 0, 0));
                        break;
                    case 233:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 58)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 49, 0, 0));
                        break;
                    case 288:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 72)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));
                        break;
                    case 377:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 94)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 78, 0, 0));
                        break;
                    case 545:
                        setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 136)));
                        this.setBorder(BorderFactory.createEmptyBorder(0, 114, 0, 0));
                        break;
                }
            } else {
                horizontal = true;
                setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize)));
            }
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

    public boolean isHorizontal() {
        return horizontal;
    }

    public Picture getPicture() {
        return picture;
    }

    public BufferedImage getThumbnail() {
        return thumbnail;
    }

    public void deleteThumbnail() {
        thumbnail = null;
        Settings.LOADED_THUMBNAILS_COUNT--;
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
    
    /**
     * !Only for use when selecting another picture than the current selection!
     * Selects this picture as the only one selected
     */
    public void setAsOnlySelection() {
    	
            picturePanel.removeAllSelectedThumbs();
            picturePanel.addSelectedThumb(PictureLabel.this);
            picturePanel.setMostRecentSelection(PictureLabel.this);
            picturePanel.refresh();
            picturePanel.createTagLabels();
            toggleSelection();

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

                if (!picturePanel.isControlPressed()) {
                    picturePanel.removeAllSelectedThumbs();
                }

                if (isSelected) {
                    picturePanel.removeSelectedThumb(PictureLabel.this);
                    picturePanel.setMostRecentSelection(null);
                    picturePanel.refresh();
                    picturePanel.createTagLabels();
                    if (!picturePanel.isControlPressed()) {
                        toggleSelection();
                    }
                }
                else {
                    picturePanel.addSelectedThumb(PictureLabel.this);
                    picturePanel.setMostRecentSelection(PictureLabel.this);
                    picturePanel.refresh();
                    picturePanel.createTagLabels();
                }
                toggleSelection();
            }
            else if (clickCount == 2) {
            	frame = new FullScreenPicturesFrame(picture.getImagePath(), picturePanel.getMainFrame());
				picturePanel.getCenterPanel().removeAll();
				picturePanel.getCenterPanel().add(frame,BorderLayout.CENTER);
				picturePanel.getCenterPanel().revalidate();
				picturePanel.getCenterPanel().repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            /*picturePanel.setFocusable(true);
            picturePanel.requestFocus();
            for (MouseListener l: picturePanel.getMouseListeners()) {
                l.mousePressed(e);
            }*/
            picturePanel.dispatchEvent(SwingUtilities.convertMouseEvent(PictureLabel.this, e, picturePanel));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            /*for (MouseListener l: picturePanel.getMouseListeners()) {
                l.mouseReleased(e);
            }*/
            picturePanel.dispatchEvent(SwingUtilities.convertMouseEvent(PictureLabel.this, e, picturePanel));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            /*for (MouseMotionListener l: picturePanel.getMouseMotionListeners()) {
                l.mouseMoved(e);
            }*/
            picturePanel.dispatchEvent(SwingUtilities.convertMouseEvent(PictureLabel.this, e, picturePanel));
        }

    }
}
