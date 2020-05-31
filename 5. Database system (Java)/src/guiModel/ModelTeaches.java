package guiModel;
import javax.swing.table.AbstractTableModel;

public class ModelTeaches extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Teacher_id", "Subject_id", "dateFrom"};
    private Object[][] data = null;

    public ModelTeaches(Object[][] data) {
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