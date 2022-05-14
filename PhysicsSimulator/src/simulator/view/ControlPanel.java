package simulator.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;



import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller ctlr;
	private boolean stopped;
	
	private int nSteps;
	private double deltaTime;
	
	private JButton loadB;
	private JButton forceB;
	private JButton stopB;
	private JButton playB;
	private JButton exitB;
	private JTextField dtText;
	
	private boolean parameterCreated = false;
	private ParametersTable parametersTable;
	
	
	public ControlPanel(Controller ctlr) {
		this.ctlr = ctlr;
		stopped = true;
		nSteps = 10000;
		initGUI();
		ctlr.addObserver(this);
	}
	
	public void initGUI() {
		
		JToolBar toolBar = new JToolBar();
		this.setLayout(new BorderLayout(5, 5));
		
		
		// Boton load
		loadB = new JButton();
		loadB.setToolTipText("Load a file");
		loadB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int selection = fileChooser.showOpenDialog(loadB);
				File fichero = fileChooser.getSelectedFile(); 
				if (selection == JFileChooser.APPROVE_OPTION) {
					if (fichero != null) {
						InputStream in;
						try {
							in = new FileInputStream(fichero);
							ctlr.reset();
							ctlr.loadBodies(in);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Los datos introducidos no son validos", "Error", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		});
		loadB.setIcon(new ImageIcon("resources/icons/open.png"));
		
		
		// Separador
		JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		separator1.setBackground(Color.black);
		
		
		// Boton forceLaws
		forceB = new JButton();
		forceB.setToolTipText("Selecte Force Laws");
		forceB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	if (!parameterCreated) {
		            		parametersTable = new ParametersTable(ctlr);
		            		parameterCreated = true;
		            	}
		            	else {
		            		parametersTable.setVisible(true);
		            	}
		            	
		            }
		        });
			}
			
		});
		forceB.setIcon(new ImageIcon("resources/icons/physics.png"));
		
		
		// Boton stop
		stopB = new JButton();
		stopB.setToolTipText("Stop");
		stopB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopped = true;
			}
		});
		stopB.setIcon(new ImageIcon("resources/icons/stop.png"));
		
		
		
		// Boton exit
		exitB = new JButton();
		exitB.setToolTipText("Exit");
		exitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "¿Está seguro/a que desea salir?", "Confirmacion", JOptionPane.WARNING_MESSAGE); 
				if (n == 0) System.exit(0);
			}
		});
		exitB.setIcon(new ImageIcon("resources/icons/exit.png"));
		
		
		
		// Boton play
		playB = new JButton();
		playB.setToolTipText("Play");
		playB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadB.setEnabled(false);
				forceB.setEnabled(false);
				exitB.setEnabled(false);
				stopped = false;
				ctlr.setDeltaTime(deltaTime);
				run_sim(nSteps);
			}
		});
		playB.setIcon(new ImageIcon("resources/icons/run.png"));
		
		
		
		
		// JSpinner steps
		SpinnerNumberModel model = new SpinnerNumberModel(10000, 0, 10000000, 100);
		nSteps = 10000;
		JSpinner steps = new JSpinner(model);
		steps.setPreferredSize(new Dimension(100, 40));
		steps.setValue(10000);
		steps.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try{
					nSteps = Integer.parseInt(String.valueOf(steps.getValue())); 
				}catch(Exception excp){
					JOptionPane.showMessageDialog(steps, "Porfavor, introduzca un número valido");
				}
			}
		});
		
		JLabel stepsText = new JLabel();
		stepsText.setText("Steps: ");
		
		JPanel stepsPanel = new JPanel();
		stepsPanel.add(stepsText);
		stepsPanel.add(steps);
		
		
		
		
		// JTextField deltaTime
		dtText = new JTextField(5);
		dtText.setPreferredSize(new Dimension(100, 40));
		dtText.setText("2500.0");
		deltaTime = 2500.0;
		dtText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				deltaTime = Double.parseDouble(dtText.getText()); 
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Porfavor, introduzca un número");
				}
			}
		});
		
		JLabel deltaTimeLabel = new JLabel();
		deltaTimeLabel.setText("Delta-Time: ");
		
		JPanel deltaTimePanel = new JPanel();
		deltaTimePanel.add(deltaTimeLabel);
		deltaTimePanel.add(dtText);
		
		toolBar.add(loadB);
		toolBar.addSeparator();
		toolBar.add(forceB);
		toolBar.addSeparator();
		toolBar.add(playB);
		toolBar.add(stopB);
		toolBar.add(stepsPanel).setLocation(5, 15);
		toolBar.add(deltaTimePanel);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(exitB);
				
		this.add(toolBar);
	}
	
	private void run_sim (int n) {
		if (n > 0 && !stopped) {
			try {
				ctlr.run(1, null, null, null);
			}catch(Exception e) {
				// TODO show the error in a dialog box
				JOptionPane.showMessageDialog(null, "ERROR");
				// TODO enable all buttons
				activateButtons();
				stopped = true;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					run_sim(n - 1);
				}
			});
		}
		else {
			stopped = true;
			activateButtons();
		}
	}
	
	public void activateButtons() {
		loadB.setEnabled(true);
		exitB.setEnabled(true);
		forceB.setEnabled(true);
		playB.setEnabled(true);
		stopB.setEnabled(true);
	}
	

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		dtText.setText(String.valueOf(dt));
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		dtText.setText(String.valueOf(dt));
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		dtText.setText(String.valueOf(dt));
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
