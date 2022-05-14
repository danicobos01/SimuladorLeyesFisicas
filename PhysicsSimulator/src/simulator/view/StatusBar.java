package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;


import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel _currTime;
	private JLabel _currLaws;
	private JLabel _numBodies;
	

	public StatusBar(Controller ctrl) {
		InitGUI();
		ctrl.addObserver(this);
	}
	
	protected void InitGUI() {
		
		//this.setLayout( new FlowLayout( FlowLayout.CENTER, 100, 5));
		this.setLayout(new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
		
		_currTime = new JLabel("Time: " );
		_currTime.setPreferredSize(new Dimension(130, 20));	
		this.add(_currTime);
		
		JSeparator sp1 = new JSeparator(JSeparator.VERTICAL);
		sp1.setPreferredSize(new Dimension(2, 18));
		sp1.setBackground(Color.LIGHT_GRAY);
		this.add(sp1);
		
		_numBodies = new JLabel("Bodies: " );		
		_numBodies.setPreferredSize(new Dimension(130, 20));
		this.add(_numBodies);
		
		JSeparator sp2 = new JSeparator(JSeparator.VERTICAL);
		sp2.setPreferredSize(new Dimension(2, 18));
		sp2.setBackground(Color.LIGHT_GRAY);
		
		this.add(sp2);
		_currLaws = new JLabel("Laws: ");
		this.add(_currLaws);
		
	}
	
	
	public JLabel createLabel(String caption, Color color, int x, int y, int w, int h){
		JLabel label = new JLabel(caption); 			// se crea la etiqueta
		label.setBounds(x,y,w,h); 						// se coloca y da tamaño
		label.setHorizontalAlignment(JLabel.CENTER); 	// se centra en su rectángulo
		label.setForeground(color); 					// se le da color a la fuente
		return label; 									// se devuelve
		}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_currTime.setText("Time: " + time);
		_numBodies.setText("Bodies: " + bodies.size());
		_currLaws.setText("Laws: " + fLawsDesc);
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_currTime.setText("Time: " + time);
		_numBodies.setText("Bodies: " + bodies.size());
		_currLaws.setText("Laws: " + fLawsDesc);
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_numBodies.setText("Bodies: " + bodies.size());
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_currTime.setText("Time: " + time);
		_numBodies.setText("Bodies: " + bodies.size());
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		_currLaws.setText("Laws: " + fLawsDesc);
		
	}

}
