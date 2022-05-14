package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulator.control.Controller;

public class MainWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Controller ctlr;
	
	public MainWindow(Controller ctlr) {
		super("Physics Simulator");
		this.ctlr = ctlr;
		InitGUI();
	}
	
	protected void InitGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.add(mainPanel);
		Image icono = Toolkit.getDefaultToolkit().getImage("resources/icons/logosim.png");
		this.setIconImage(icono);
		
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		// Creacion de las distintas componentes
		
		// Control Panel
		ControlPanel controlPanel = new ControlPanel(ctlr);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		
		// Bodies Table
		// BodiesInfo bodiesInfo = new BodiesInfo(ctlr);
		BodiesTable bodiesTable = new BodiesTable(ctlr);
		// bodiesInfo.setPreferredSize(new Dimension(800, 200));
		bodiesTable.setPreferredSize(new Dimension(800, 200));
		// contentPanel.add(bodiesInfo);
		contentPanel.add(bodiesTable);
		
		// Viewer
		Viewer universeView = new Viewer(ctlr);
		universeView.setPreferredSize(new Dimension(800, 450));
		contentPanel.add(new JScrollPane(universeView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		// Status Bar
		StatusBar statusBar = new StatusBar(ctlr);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
		this.setLocationRelativeTo(null);
		
		
		
	}
}
