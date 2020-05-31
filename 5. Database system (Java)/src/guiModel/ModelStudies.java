package guiModel;
import javax.swing.table.AbstractTableModel;

public class ModelStudies extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Student_id", "Subject_id", "dateTo", "Grade"};
    private Object[][] data = null;
    
    public ModelStudies(Object[][] data) {
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