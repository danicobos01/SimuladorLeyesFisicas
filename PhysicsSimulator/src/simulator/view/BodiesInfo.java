package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesInfo extends JPanel implements SimulatorObserver{


	private static final long serialVersionUID = 1L;
	
	private List<Body> _bodies;
	private JTextArea text;
	
	public BodiesInfo(Controller ctlr) {
		super();
		InitGUI();
		ctlr.addObserver(this);
	}
	
	private void InitGUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				"Bodies", TitledBorder.LEFT, TitledBorder.TOP));
		text = new JTextArea();
		JScrollPane scroll = new JScrollPane(text);
		add(scroll);
	}
	
	private void update(List<Body> bodies) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_bodies = bodies;
				text.setText("");
				for (int i = 0; i < _bodies.size(); i++) {
					Body b = _bodies.get(i);
					text.append(b.getId() + "	" + b.getPosition() + "\n");
				}
			}
		});
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		update(bodies);
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		update(bodies);
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		update(bodies);
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		update(bodies);
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

}
