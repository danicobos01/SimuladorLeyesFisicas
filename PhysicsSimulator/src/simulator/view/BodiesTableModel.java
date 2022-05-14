package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Body> _bodies;
	private String[] columnNames = {"Id", "Mass", "Position", "Velocity", "Force"};
	
	public BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return _bodies.size();
	}
	
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Body b = _bodies.get(row);
		String s = "";
		
		switch(col) {
		case 0: 
			s = b.getId();
			break;
		case 1:
			s = "" + b.getMass();
			break;
		case 2: 
			s = "" + b.getPosition().toString();
			break;
		case 3: 
			s = "" + b.getVelocity().toString();
			break;
		case 4: 
			s = "" + b.getForce().toString();
			break;
		}
		
		return s;
	}
	
	public void update(List<Body> bodies) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
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
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
	}

}
