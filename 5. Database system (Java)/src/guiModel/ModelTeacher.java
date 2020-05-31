package guiModel;
import javax.swing.table.AbstractTableModel;

public class ModelTeacher extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Id", "Name", "Surname", "Birth_date", "Email address", "Salary", "years_teaching"};
	private Object[][] data = null;

	public ModelTeacher(Object[][] data) {
	    this.data = data;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
}