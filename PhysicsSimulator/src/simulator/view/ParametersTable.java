package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.json.*;

import simulator.control.Controller;


public class ParametersTable extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<JSONObject> fList;
	private Controller ctrl;
	
	private JComboBox<String> box;
	
	private JTable lawsTable;
	private ForceLawsTableModel model;
	private String[][] data;

	public ParametersTable(Controller ctrl) {
		this.ctrl = ctrl;
		this.setTitle("Force Laws Selection");
		this.setLayout(new BorderLayout(5, 10));
		this.setBounds(600, 600, 600, 350);
		this.setLocationRelativeTo(null); // De esta forma se centra en el medio de la pantalla
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JLabel helpLabel = new JLabel( "<html><p>Select a force law and provide values for the parametes in the <b>Value column</b> "
	                + "(default values are used for parametes with no value).</p></html>");
		 
		this.add(helpLabel, BorderLayout.PAGE_START);
		 
		 
		
		model = new ForceLawsTableModel(null);
		lawsTable = new JTable(model);
		TableColumn desc = lawsTable.getColumn("Description");
		desc.setPreferredWidth(300);
		
		
		this.add(new JScrollPane(lawsTable), BorderLayout.CENTER);
		this.setVisible(true);
		this.add(createInferiorPanel(), BorderLayout.PAGE_END);
		this.setVisible(true);
		
	}
	
	
	public JPanel createInferiorPanel() {
		
		
		JPanel inf = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
		JPanel sup = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		JPanel finalPanel = new JPanel();
		finalPanel.setLayout(new GridLayout(3, 3, 5, 10));
		
		
		// Un label y su combo box
		JLabel label = new JLabel("Force laws:");
		
		box = new JComboBox<>();
		fList = new ArrayList<JSONObject>();
		fList = ctrl.getForceLawsInfo();
		
		for (JSONObject fl : fList) { 
			box.addItem(fl.getString("desc")); // Añadimos a la caja cada opcion de fuerza
		}
		box.setEditable(false);
		
		
		data = generateData();
		model.update(data);
		lawsTable.setModel(model);
		
		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				data = generateData();
				model.update(data);
				lawsTable.setModel(model);
			}
		});
		
		sup.add(label);
		sup.add(box);
		
		
		// Boton OK 
		JButton okB = new JButton("Ok");
		okB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i <= box.getComponentCount(); i++) {
					if (fList.get(i).getString("desc").equals(box.getSelectedItem())) {
						JSONObject law = new JSONObject();
						law.put("type", fList.get(i).getString("type"));
						JSONObject dataLaw = new JSONObject();
						try {
							if (data != null) {
								for (int j = 0; j < data.length; j++) {
									if (data[j][1] != null) {
										if (data[j][1].charAt(0) == '[') {
											JSONTokener parse = new JSONTokener(data[j][i]);
											parse.next();
											String first = parse.nextTo(',');
											parse.next();
											String second = parse.nextTo(']');
											JSONArray arr = new JSONArray();
											arr.put(Double.parseDouble(first));
											arr.put(Double.parseDouble(second));
											parse.next();
											dataLaw.put(String.valueOf(data[j][0]), arr);
										}
										else {
											dataLaw.put(data[j][0], Double.parseDouble(data[j][1]));
										}
									}
								}
							}
							law.put("data", dataLaw);
							ctrl.setForceLaws(law);
							visib(false);
						}catch(Exception ex){
							JOptionPane.showMessageDialog(null, "Los datos introducidos no son validos", "Error", JOptionPane.WARNING_MESSAGE);
							data = generateData();
						}
					}
				}
			}
		});
		
		// Boton CANCEL
		JButton cancelB = new JButton("Cancel");
		cancelB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visib(false);
			}

		});
		
		inf.add(okB);
		inf.add(cancelB);
		
		finalPanel.add(sup);
		finalPanel.add(inf);
		
		return finalPanel;
	}
	
	public void visib(boolean b) {
		this.setVisible(b);
	}
	
	public String[][] generateData(){
		
		JSONObject fuerza = fList.get(box.getSelectedIndex());
		JSONObject propFuerza = fuerza.getJSONObject("data");
		String[] props = JSONObject.getNames(propFuerza);
		
		data = null;
		if (props != null) {
			data = new String[props.length][3];
			for (int i = 0; i < props.length; i++) {
				data[i][0] = props[i];
				data[i][1] = null;
				data[i][2] = String.valueOf(propFuerza.get(props[i]));
			}
		}
		model.update(data);
		return data;
	}
	
}
