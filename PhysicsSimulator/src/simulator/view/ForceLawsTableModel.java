package simulator.view;


import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;


public class ForceLawsTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String columnNames[] = {"Key", "Value", "Description"};
	private String[][] data;
	
	public ForceLawsTableModel(String[][] data) {
		this.data = data;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if (data != null) {
			return data.length;
		}
		else return 0;
	}
	
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	public boolean isCellEditable(int row, int col) {
		if (col == 1) return true;
		else return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		String s = "";
		
		switch(col) {
		case 0: 
			s = data[row][0];
			break;
		case 1:
			if (data[row][1] == null) {
				s = "";
			}
			else{
				s = data[row][1]; 
			}
			break;
		case 2:
			s = data[row][2];
			break;			
		}
		
		return s;
	}
	
	public void setValueAt(Object value, int row, int col) {
		if (col == 1) {
			data[row][1] = String.valueOf(value);
		}
	}
	
	public void update(String[][] _data) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				data = _data;
				fireTableDataChanged();
			}
		});
	}
}
