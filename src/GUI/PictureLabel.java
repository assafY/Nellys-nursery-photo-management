package GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import javax.swing.*;

import Core.Library;
import org.imgscalr.Scalr;

import Core.Settings;
import Data.Picture;

public class PictureLabel extends JLabel{

    private static final int DEFAULT_SIZE = Settings.THUMBNAIL_SIZES[2];

    private Picture picture;
    private int currentSize;
    private boolean isSelected;
    private boolean firstDrag = true;
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

    public void createThumbnail(int size) {
        Library.getThumbnailProcessor().addThumbnail(picture, size);
    }

    private BufferedImage getThumbnail() {
        if (getIcon() != null) {
            BufferedImage thumbnail = new BufferedImage(
                    picture.getPictureLabel().getIcon().getIconWidth(),
                    picture.getPictureLabel().getIcon().getIconHeight(),
                    BufferedImage.TYPE_INT_RGB);
            picture.getPictureLabel().getIcon().paintIcon(null, thumbnail.createGraphics(), 0, 0);
            return thumbnail;
        }
        return null;
    }

    public void showThumbnail(int size, boolean readFromFile) {

        if (!readFromFile) {
            BufferedImage thumbnail = getThumbnail();
            if (thumbnail != null) {
                currentSize = size;
                if (thumbnail.getHeight() > thumbnail.getWidth()) {
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
                        case 260:
                            setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 72)));
                            this.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));
                            break;
                        case 290:
                            setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 94)));
                            this.setBorder(BorderFactory.createEmptyBorder(0, 78, 0, 0));
                            break;
                        case 320:
                            setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize - 136)));
                            this.setBorder(BorderFactory.createEmptyBorder(0, 114, 0, 0));
                            break;
                    }
                } else {
                    setIcon(new ImageIcon(Scalr.resize(thumbnail, Scalr.Method.BALANCED, currentSize)));
                }
                thumbnail.flush();
                thumbnail = null;
                System.gc();
            }
        }
        else {
            Library.getThumbnailProcessor().setThumbnail(this, picturePanel.getMainFrame().getZoomValue());
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

    /**
     * if this thumbnail is selected, display a grey selection rectangle
     * over it.
     *
     * @param g Graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(0xD3, 0xD3, 0xD3, 0x50));
            g2.fillRect(2, 2, currentSize - 2, getHeight() - 3);
            g2.setColor(Color.GRAY);
            g2.drawRect(0, 0, currentSize - 2, getHeight() - 2);

        }
    }
    
    /**
     * Only for use when selecting a different picture from the current selection.
     * Selects this picture as the only one selected
     */
    public void setAsOnlySelection() throws ParseException {
    	
            picturePanel.removeAllSelectedThumbs();
            picturePanel.addSelectedThumb(PictureLabel.this);
            picturePanel.setMostRecentSelection(PictureLabel.this);
            picturePanel.refresh();
            picturePanel.createTagLabels();
            toggleSelection();

    }

    /**
     * sets whether this is the first intersection of the thumbnail with the
     * mouse drag rectangle. Its purpose is to only toggle selection on the
     * first intersection, and not every time the mouse moves while dragged.
     */
    public void setFirstDrag(boolean firstDrag) {
        this.firstDrag = firstDrag;
    }

    /**
     * returns whether this is the first intersection.
     *
     * @return firstDrag - did this thumbnail intersect with mouse
     * drag rectangle before.
     */
    public boolean isFirstDrag() {
        return firstDrag;
    }
    
    public void setSelected() throws ParseException {
    	if (picturePanel.isShiftPressed()) {
            picturePanel.shiftMouseClick(PictureLabel.this);
        }
        else {
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
            } else {
                picturePanel.addSelectedThumb(PictureLabel.this);
                picturePanel.setMostRecentSelection(PictureLabel.this);
                picturePanel.refresh();
                picturePanel.createTagLabels();
            }
            toggleSelection();
        }
    }
    
    public void openFullScreen() throws ParseException {
    	PictureLabel.this.setAsOnlySelection();
    	frame = new FullScreenPicturesFrame(picture.getImagePath(), picturePanel.getMainFrame());
		picturePanel.getCenterPanel().removeAll();
		picturePanel.getCenterPanel().add(frame,BorderLayout.CENTER);
		picturePanel.getCenterPanel().revalidate();
		picturePanel.getCenterPanel().repaint();
		picturePanel.getMainFrame().releaseKeys();
    }

    public class ThumbnailMouseListener extends MouseAdapter {

        /**
         * Handles mouse click thumbnail selection, for single thumbnails and
         * multiple selection when holding shift and control keys.
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
                try {
                    setSelected();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
            else if (clickCount == 2) {
                try {
                    openFullScreen();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            picturePanel.dispatchEvent(SwingUtilities.convertMouseEvent(PictureLabel.this, e, picturePanel));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            picturePanel.dispatchEvent(SwingUtilities.convertMouseEvent(PictureLabel.this, e, picturePanel));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            picturePanel.dispatchEvent(SwingUtilities.convertMouseEvent(PictureLabel.this, e, picturePanel));
        }

    }
}
