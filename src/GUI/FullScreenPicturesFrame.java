package GUI;

import Core.Library;
import Data.Picture;

import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Creates a FullScreenPictures Inner Frame.
 */
public class FullScreenPicturesFrame extends JInternalFrame {

	private JLabel fullScreenPicture;
	private String filePath;
	//private BufferedImage resizedPicture;
	//private BufferedImage actualPicture;
    private boolean isHorizontal;
	private JButton rotateLeftButton;
	private JButton rotateRightButton;
	private JButton nextButton;
	private JButton previousButton;
	private JPanel buttonsPanel;
	private JPanel mainPanel;
	private int a;
	private MainFrame mainFrame;

	public FullScreenPicturesFrame(String filePath, MainFrame mainFrame) {
		super("", false, true, false, false);
		this.mainFrame = mainFrame;
		this.filePath = filePath;
        mainFrame.getSearchField().setFocusable(false);

		//getPicture();
        getThePictureIndex();
		createLabel();
		createButtons();
		createButtonsPanel();
		createButtonsPanel();
		createMainPanel();
		onClose();
		createListeners();
		this.setVisible(true);

	}
	
	/**
	 * Creates the thumbnail from the filepath that is passed.
	 */
	/*private void getPicture() {
		try {
			actualPicture = ImageIO.read(new File(filePath));
			resizedPicture = actualPicture;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ImageIO.read(new File(filePath)) != null) {
			if (resizedPicture.getHeight() > resizedPicture.getWidth()) {
				resizedPicture = Scalr.resize(resizedPicture, 560);
			} else {
                resizedPicture = Scalr.resize(resizedPicture, 800);
			}
		}
	}*/

    private void getThePictureIndex() {
        for(int i = 0; i < mainFrame.getPicturesPanel().getThumbsOnDisplay().size(); i++) {
            if((mainFrame.getPicturesPanel().getThumbsOnDisplay().get(i).getPicture().getImagePath()).equals(filePath)){
                a = i;
            }
        }
    }
	
	/**
	 * Creates the Label on which the picture is displayed.
	 */
	private void createLabel() {
		fullScreenPicture = new JLabel();
        try {
            BufferedImage currentImage = ImageIO.read(new File(filePath));
            if (currentImage != null) {
                if (currentImage.getHeight() > currentImage.getWidth()) {
                    fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(currentImage, 560)));
                }
                else {
                    fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(currentImage, 800)));
                }
                currentImage.flush();
                currentImage = null;
                System.gc();
            }
        } catch (IOException e) {

        }
		fullScreenPicture.setHorizontalAlignment(JLabel.CENTER);
		fullScreenPicture.setVerticalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Creates all the buttons.
	 */
	private void createButtons() {
        try {
            rotateLeftButton = new JButton(new ImageIcon(ImageIO.read(MainFrame.class
                    .getResource("/buttonIcons/rotateLeftPNG.png"))));
            rotateRightButton = new JButton(new ImageIcon(ImageIO.read(MainFrame.class
                                        .getResource("/buttonIcons/rotateRightPNG.png"))));
            previousButton = new JButton(new ImageIcon(ImageIO.read(MainFrame.class
                    .getResource("/buttonIcons/previousButtonPNG.png"))));
            nextButton = new JButton(new ImageIcon(ImageIO.read(MainFrame.class
                    .getResource("/buttonIcons/nextButtonPNG.png"))));
        } catch (IOException e) {

        }
	}
	
	/**
	 * Creates the button`s panel.
	 */
	private void createButtonsPanel() {
		buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(rotateRightButton);
		buttonsPanel.add(rotateLeftButton);
		buttonsPanel.add(previousButton);
		buttonsPanel.add(nextButton);
	}
	
	/**
	 * Creates the main Panel.
	 */
	private void createMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(fullScreenPicture, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		this.add(mainPanel);
		pack();
	}
	
	/**
	 * Returns back to the list of imported pictures when the inner frame is closed.
	 */
	private void onClose() {
		this.addInternalFrameListener(new InternalFrameAdapter() {

			public void internalFrameClosed(InternalFrameEvent arg0) {
                fullScreenPicture.setIcon(null);
				System.gc();
				mainFrame.getCenterPanel().add(mainFrame.getInnerCenterPanel(), BorderLayout.CENTER);
				mainFrame.getPicturesPanel().requestFocus();
			}

			public void internalFrameActivated(InternalFrameEvent arg0) {
			}
		});
	}
	
	
	/**
	 * Creates all the Listeners(Rotation and switch between pictures).
	 */
	private void createListeners() {
		rotateLeftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                try {
                    rotateActualPictureFile(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
				resizeFullScreenPicture();
			}
		});
		rotateRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
                try {
                    rotateActualPictureFile(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
				resizeFullScreenPicture();
			}
		});
		
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToNextPicture();
			}
		});
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveToPreviousPicture();
			}
		});
		
		this.getActionMap().put("close", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		this.getActionMap().put("next", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveToNextPicture();
			}
		});
		
		this.getActionMap().put("previous", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				moveToPreviousPicture();
			}
		});
        InputMap map = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke nextStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        KeyStroke previousStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        map.put(escapeStroke, "close");
        map.put(nextStroke, "next");
        map.put(previousStroke, "previous");
	}
	
	/*
	 * Moves to next picture.
	 */
	private void moveToNextPicture() {
		if (!mainFrame.getPicturesPanel().getThumbsOnDisplay().isEmpty()) {
			if (a >= mainFrame.getPicturesPanel().getThumbsOnDisplay().size() - 1) {
				a = 0;
				getPreviousAndNextPicture();
			} else {
				a++;
				getPreviousAndNextPicture();
			}
		}
	}
	
	/*
	 * Moves to previous picture.
	 */
	private void moveToPreviousPicture() {
		if (!mainFrame.getPicturesPanel().getThumbsOnDisplay().isEmpty()) {
			if (a <= 0) {
				a = mainFrame.getPicturesPanel().getThumbsOnDisplay().size() - 1;
				getPreviousAndNextPicture();
			} else {
				a--;
				getPreviousAndNextPicture();
			}
		}
	}
	/**
	 * Gets the previous and next thumbnails from the picture library.
	 */
	private void getPreviousAndNextPicture() {
        fullScreenPicture.setIcon(null);
		// moves index in the background to the new picture
		PictureLabel currentPictureLabel = mainFrame.getPicturesPanel().getPicturesOnDisplay().get(a).getPictureLabel();
		currentPictureLabel.setAsOnlySelection();
		// --- end of move
		filePath = currentPictureLabel.getPicture().getImagePath();
		/*try {
			actualPicture = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		resizeFullScreenPicture();
	}
	
	/**
	 * Resizes the thumbnails.
	 */
	private void resizeFullScreenPicture() {
        try {

            BufferedImage currentImage = ImageIO.read(new File(filePath));
            if (currentImage.getHeight() > currentImage.getWidth()) {
                fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(currentImage, 560)));
            } else {
                fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(currentImage, 800)));
            }
            currentImage.flush();
            currentImage = null;
        } catch (IOException e) {

        } finally {
            System.gc();
            mainPanel.revalidate();
            mainPanel.repaint();
        }
	}
	
	/*
	 * Rotates the actual picture file.
	 */
	private void rotateActualPictureFile(boolean rotateLeft) throws IOException {
        BufferedImage currentImage = ImageIO.read(new File(filePath));
        Iterator writersBySuffix = ImageIO.getImageWritersBySuffix("jpeg");
        if(!writersBySuffix.hasNext()){
            throw new IllegalStateException("No writers");
        }
        ImageWriter writer = (ImageWriter) writersBySuffix.next();
        ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File(filePath));
        writer.setOutput(imageOutputStream);
        if (rotateLeft) {
            writer.write(null, new IIOImage(Scalr.rotate(currentImage, Scalr.Rotation.CW_270, null), null, null), imageWriteParam);
        }
        else {
            writer.write(null, new IIOImage(Scalr.rotate(currentImage, Scalr.Rotation.CW_90, null), null, null), imageWriteParam);
        }
        imageOutputStream.close();
        writer.dispose();
        currentImage.flush();
        currentImage = null;
        System.gc();
	}
	
	/*
	 * Close the Inner Frame.
	 */
	private void close(){
		this.dispose();
	}
}
