package ecc;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CodeCollection extends AbstractTableModel{
	

	private static final long serialVersionUID = 1L;
	List<CodeData> codes = new ArrayList<CodeData>();

	@Override
	public int getRowCount() {
		return codes.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CodeData code = codes.get(rowIndex);
		switch(columnIndex) {
		case 0: return code.getName();
		case 1: return code.getTime();
		case 2: return code.getCorrect();
		default: return -1;
		}
	}
	public String getColumnName(int column) {
		switch(column) {
		case 0: return "Code";
		case 1: return "Time (ms)";
		case 2: return "Correct";
		} return null;
	}
	public Class<?> getColumnClass(int columnIndex){
		if(columnIndex == 1) return Double.class;
		if(columnIndex == 2) return Boolean.class;
		return Object.class;
	}
}
