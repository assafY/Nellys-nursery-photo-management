package GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import Core.Library;
import Core.Settings;

public class OptionsFrame extends JFrame {

	JPanel mainPanel;
	MainFrame mainFrame;

	OptionSlab locationOption;
	OptionSlab homeDirOption;
	OptionSlab csvOption;

	public OptionsFrame(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		initComponents();
		createStructure();
		addListeners();
		pack();
		setVisible(true);
	}

	private void initComponents() {
		GridLayout grid = new GridLayout(3, 1);
		grid.setVgap(10);
		mainPanel = new JPanel(grid);

		initiateFields();
	}
	
	private void initiateFields() {
		locationOption = new OptionSlab("Location", Settings.NURSERY_LOCATION);
		homeDirOption = new OptionSlab("Home Directory",
				Settings.PICTURE_HOME_DIR);
		csvOption = new OptionSlab("Save Path", Settings.CSV_PATH);
	}

	private void createStructure() {
		this.getContentPane().setLayout(new BorderLayout());
		this.add(new JLabel(" "), BorderLayout.NORTH);
		this.add(new JLabel(" "), BorderLayout.SOUTH);
		this.add(new JLabel("     "), BorderLayout.WEST);
		this.add(new JLabel("     "), BorderLayout.EAST);
		this.add(mainPanel, BorderLayout.CENTER);
		addComponents();
	}

	private void addComponents() {
		mainPanel.add(locationOption);
		mainPanel.add(homeDirOption);
		mainPanel.add(csvOption);
	}
	
	private void addListeners() {
		
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				JOptionPane.showMessageDialog(mainFrame,
					    "Please restart the program for the changes to take effect.",
					    "Message",
					    JOptionPane.WARNING_MESSAGE);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		locationOption.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Library.promptSelectSite(OptionsFrame.this);
				updateView();
			}
		});

		homeDirOption.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Library.promptSelectHomeDir();
				updateView();
			}
		});

		csvOption.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Library.promptSelectCSV(OptionsFrame.this);
				updateView();
			}
		});
	}
	
	public void updateView() {
		new OptionsFrame(mainFrame).setLocation(this.getX(), this.getY());
		this.dispose();
	}

	private class OptionSlab extends JPanel {

		final Dimension dimensions = new Dimension(300, 50);

		JPanel labelPanel;
		JPanel buttonPanel;

		TitledBorder border;
		String title;
		JLabel label;
		Button button;

		String path;
		File pathf;

		private OptionSlab(String title) {
			super(new BorderLayout());
			this.title = title;
			initComponentsInner();
			createStructureInner();
			this.setPreferredSize(dimensions);
		}

		public OptionSlab(String title, String path) {
			this(title);
			this.path = path;
			setPath();
		}

		public OptionSlab(String title, File pathf) {
			this(title);
			this.pathf = pathf;
			setPath();
		}

		private void initComponentsInner() {
			labelPanel = new JPanel();
			buttonPanel = new JPanel(new BorderLayout());
			border = BorderFactory.createTitledBorder(
					BorderFactory.createLoweredBevelBorder(), title);
			border.setTitlePosition(TitledBorder.ABOVE_TOP);
			label = new JLabel("");
			button = new Button("Change");
		}

		private void createStructureInner() {
			this.add(labelPanel, BorderLayout.CENTER);
			this.add(button, BorderLayout.EAST);
			labelPanel.setBorder(border);
			labelPanel.add(label);
			buttonPanel.add(button, BorderLayout.EAST);
			buttonPanel.add(new JLabel("   "), BorderLayout.WEST);
			this.add(buttonPanel, BorderLayout.EAST);
		}

		public void addListener(ActionListener l) {
			button.addActionListener(l);
		}

		public void setPath() {

			if (pathf != null) {
				path = pathf.getAbsolutePath();
			}
			label.setText(path);

		}
	}

}
