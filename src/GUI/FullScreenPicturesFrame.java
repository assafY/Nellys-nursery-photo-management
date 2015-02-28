package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import mediautil.image.jpeg.LLJTran;
import mediautil.image.jpeg.LLJTranException;

import org.imgscalr.Scalr;

import Core.Library;
import Core.RotatingPictureThread;
/**
 * Creates a FullScreenPictures Inner Frame.
 */
public class FullScreenPicturesFrame extends JInternalFrame {

	private JLabel fullScreenPicture;
	private String filePath;
	private BufferedImage picture;
	private JButton rotateLeftButton;
	private JButton rotateRightButton;
	private JButton nextButton;
	private JButton previousButton;
	private JPanel buttonsPanel;
	private JPanel mainPanel;
	private int specifyRotation;
	private int a;

	public FullScreenPicturesFrame(String filePath) {
		super("", false, true, false, false);
		this.filePath = filePath;
		a = Library.getPictureLibrary().indexOf(picture);
		getPicture();
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
			picture = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (picture != null) {
			if (picture.getHeight() > picture.getWidth()) {
				picture = Scalr.resize(picture, picture.getWidth() / 3);
			} else {
                picture = Scalr.resize(picture, picture.getHeight() / 3);
			}
		}
	}
	
	/**
	 * Creates the Label on which the picture is displayed.
	 */
	private void createLabel() {
		fullScreenPicture = new JLabel();
		fullScreenPicture.setIcon(new ImageIcon(picture));
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
			public void internalFrameOpened(InternalFrameEvent arg0) {}
			public void internalFrameIconified(InternalFrameEvent arg0) {}
			public void internalFrameDeiconified(InternalFrameEvent arg0) {}
			public void internalFrameDeactivated(InternalFrameEvent arg0) {}
			public void internalFrameClosing(InternalFrameEvent arg0) {}
			public void internalFrameClosed(InternalFrameEvent arg0) {
				picture = null;
				MainFrame.getCenterPanel().add(MainFrame.getInnerCenterPanel(), BorderLayout.CENTER);
			}
			public void internalFrameActivated(InternalFrameEvent arg0) {}
		});
	}
	
	
	/**
	 * Creates all the Listeners(Rotation and switch between pictures).
	 */
	private void createListeners() {
		rotateLeftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				specifyRotation = 270;
				RotatingPictureThread thread = new RotatingPictureThread(filePath, specifyRotation);
				thread.start();
				picture = Scalr.rotate(picture, Scalr.Rotation.CW_270, null);
				resizeFullScreenPicture();
			}
		});
		rotateRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				specifyRotation = 90;
				RotatingPictureThread thread = new RotatingPictureThread(filePath, specifyRotation);
				thread.start();
				picture = Scalr.rotate(picture, Scalr.Rotation.CW_90, null);
				resizeFullScreenPicture();
			}
		});
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!Library.getPictureLibrary().isEmpty()) {
					if (a >= Library.getPictureLibrary().size() - 1) {
						a = 0;
						getPreviousAndNextPicture();
					} else {
						a++;
						getPreviousAndNextPicture();
					}
				}
			}
		});
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!Library.getPictureLibrary().isEmpty()) {
					if (a <= 0) {
						a = Library.getPictureLibrary().size() - 1;
						getPreviousAndNextPicture();
					} else {
						a--;
						getPreviousAndNextPicture();
					}
				}
			}
		});
	}
	
	/**
	 * Gets the previous and next thumbnails from the picture library.
	 */
	private void getPreviousAndNextPicture() {
		try {
			picture = ImageIO.read(new File(Library.getPictureLibrary().get(a).getImagePath()));
		} catch (IOException e1) {
            //TOTO: Handle exception
			e1.printStackTrace();
		}
		//resizeFullScreenPicture();
	}
	
	/**
	 * Resizes the thumbnails.
	 */
	private void resizeFullScreenPicture() {
		if (picture.getHeight() > picture.getWidth()) {
			fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(picture, 560)));
		} else {
			fullScreenPicture.setIcon(new ImageIcon(Scalr.resize(picture, 800)));
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
}
