package ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class Cell extends JLabel {

	private static final long serialVersionUID = -2944973531092418239L;
	
	public static final int HEADER_ROW_INDEX = -1;
	
	private boolean selected;
	private int row;
	private int col;
	private Object property;
	
	public Cell(int row, int col, String text, Object property) {
		this.selected = false;
		this.row = row;
		this.col = col;
		this.property = property;
		super.setText(text);
		super.setPreferredSize(SelectionTable.STANDART_CELL_SIZE);
		super.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		super.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public Cell() {
		this(0, 0, "", null);
	}
	
	public void setProperty(Object property) {
		this.property = property;
	}
	
	public Object getProperty() {
		return property;
	}
	
	public boolean isHeader() {
		return this.row == HEADER_ROW_INDEX;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
}
