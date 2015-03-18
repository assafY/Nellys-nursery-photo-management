package GUI;

import Core.Library;
import org.imgscalr.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
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
	private BufferedImage resizedPicture;
	private BufferedImage actualPicture;
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
		a = mainFrame.getPicturesPanel().getThumbsOnDisplay().indexOf(resizedPicture);
        mainFrame.getSearchField().setFocusable(false);

		getPicture();
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
	private void getPicture() {
		try {
			actualPicture = ImageIO.read(new File(filePath));
			resizedPicture = actualPicture;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (resizedPicture != null) {
			if (resizedPicture.getHeight() > resizedPicture.getWidth()) {
				resizedPicture = Scalr.resize(resizedPicture, resizedPicture.getWidth() / 3);
			} else {
                resizedPicture = Scalr.resize(resizedPicture, resizedPicture.getHeight() / 3);
			}
		}
	}

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
		fullScreenPicture.setIcon(new ImageIcon(resizedPicture));
		fullScreenPicture.setHorizontalAlignment(JLabel.CENTER);
		fullScreenPicture.setVerticalAlignment(JLabel.CENTER);
	}
	
	/**
	 * Creates all the buttons.
	 */
	private void createButtons() {
		rotateLeftButton = new JButton("RotateLeft");
		rotateRightButton = new JButton("RotateRight");
		previousButton = new JButton("previous");
		nextButton = new JButton("Next");
	}
	
	/**
	 * Creates the button`s panel.
	 */
	private void createButtonsPanel() {
		buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(rotateLeftButton);
		buttonsPanel.add(rotateRightButton);
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
		this.addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameOpened(InternalFrameEvent arg0) {
			}

			public void internalFrameIconified(InternalFrameEvent arg0) {
			}

			public void internalFrameDeiconified(InternalFrameEvent arg0) {
			}

			public void internalFrameDeactivated(InternalFrameEvent arg0) {
			}

			public void internalFrameClosing(InternalFrameEvent arg0) {
			}

			public void internalFrameClosed(InternalFrameEvent arg0) {
				resizedPicture = null;
				actualPicture = null;
				mainFrame.getCenterPanel().add(mainFrame.getInnerCenterPanel(), BorderLayout.CENTER);
				System.out.println("closed");
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
				actualPicture = Scalr.rotate(actualPicture, Scalr.Rotation.CW_270, null);
                try {
                    rotateActualPictureFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resizedPicture = Scalr.rotate(resizedPicture, Scalr.Rotation.CW_270, null);
				resizeFullScreenPicture();
			}
		});
		rotateRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actualPicture = Scalr.rotate(actualPicture, Scalr.Rotation.CW_90, null);
                try {
                    rotateActualPictureFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                resizedPicture = Scalr.rotate(resizedPicture, Scalr.Rotation.CW_90, null);
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
		try {
			resizedPicture = ImageIO.read(new File(mainFrame.getPicturesPanel().getThumbsOnDisplay().get(a).getPicture().getImagePath()));
		} catch (IOException e1) {
            //TODO: Handle exception
			e1.printStackTrace();
		}
		// moves index in the background to the new picture
		PictureLabel currentPictureLabel = mainFrame.getPicturesPanel().getThumbsOnDisplay().get(a);
		currentPictureLabel.setAsOnlySelection();
		// --- end of move
		filePath = currentPictureLabel.getPicture().getImagePath();
		try {
			actualPicture = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resizeFullScreenPicture();
	}
	
	/**
	 * Resizes the thumbnails.
	 */
	private void resizeFullScreenPicture() {
		if (resizedPicture.getHeight() > resizedPicture.getWidth()) {
			fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(resizedPicture, 560)));
		} else {
			fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(resizedPicture, 800)));
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	/*
	 * Rotates the actual picture file.
	 */
	private void rotateActualPictureFile() throws IOException {
        Iterator writersBySuffix = ImageIO.getImageWritersBySuffix("jpeg");
        if(!writersBySuffix.hasNext()){
            throw new IllegalStateException("No writers");
        }

        ImageWriter writer = (ImageWriter) writersBySuffix.next();
        ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new File(filePath));
        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(actualPicture, null, null), imageWriteParam);
        imageOutputStream.close();
        writer.dispose();
	}
	
	/*
	 * Close the Inner Frame.
	 */
	private void close(){
		this.dispose();
	}
}
