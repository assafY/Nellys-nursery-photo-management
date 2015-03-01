package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
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

import org.imgscalr.Scalr;

import Core.Library;
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
	private boolean escPressed;
	private boolean leftPressed;
	private boolean rightPressed;
	private int a;

	public FullScreenPicturesFrame(String filePath) {
		super("", false, true, false, false);
		this.filePath = filePath;
		a = Library.getPictureLibrary().indexOf(resizedPicture);
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
			public void internalFrameOpened(InternalFrameEvent arg0) {}
			public void internalFrameIconified(InternalFrameEvent arg0) {}
			public void internalFrameDeiconified(InternalFrameEvent arg0) {}
			public void internalFrameDeactivated(InternalFrameEvent arg0) {}
			public void internalFrameClosing(InternalFrameEvent arg0) {}
			public void internalFrameClosed(InternalFrameEvent arg0) {
				resizedPicture = null;
				actualPicture = null;
				MainFrame.getCenterPanel().add(MainFrame.getInnerCenterPanel(), BorderLayout.CENTER);
			}
			public void internalFrameActivated(InternalFrameEvent arg0) {}
		});
	}
	
	
	/**
	 * Creates all the Listeners(Rotation and switch between pictures).
	 */
	private void createListeners() {
		
		this.addKeyListener(new KeyListeners());
		rotateLeftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actualPicture = Scalr.rotate(actualPicture, Scalr.Rotation.CW_270, null);
				rotateActualPictureFile();
				resizedPicture = Scalr.rotate(resizedPicture, Scalr.Rotation.CW_270, null);
				resizeFullScreenPicture();
			}
		});
		rotateRightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				actualPicture = Scalr.rotate(actualPicture, Scalr.Rotation.CW_90, null);
				rotateActualPictureFile();
				resizedPicture = Scalr.rotate(resizedPicture, Scalr.Rotation.CW_90, null);
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
			resizedPicture = ImageIO.read(new File(Library.getPictureLibrary().get(a).getImagePath()));
		} catch (IOException e1) {
            //TOTO: Handle exception
			e1.printStackTrace();
		}
		filePath = Library.getPictureLibrary().get(a).getImagePath();
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
	private void rotateActualPictureFile() {
		try {
			ImageIO.write(actualPicture, "jpg", new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class KeyListeners implements KeyListener{

		public KeyListeners(){
			super();
			escPressed = false;
			leftPressed = false;
			rightPressed = false;

		}
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == 27){
				escPressed = true;
				System.out.println("escape");
			}
			else if(e.getKeyCode() == 37){
				leftPressed = true;
				System.out.println("left");
			}
			else if(e.getKeyCode() == 39){
				rightPressed = true;
				System.out.println("right");
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == 27){
				escPressed = false;
				System.out.println("escape");
			}
			else if(e.getKeyCode() == 37){
				leftPressed = false;
				System.out.println("left");
			}
			else if(e.getKeyCode() == 39){
				rightPressed = false;
				System.out.println("right");
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
		
	}
}
