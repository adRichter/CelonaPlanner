package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import layout.TableLayout;
import planner.Hour;
import planner.Shift;

class Test {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(500, 500));
		label.setBackground(Color.BLUE);
		frame.add(label);
//		frame.add(new SelectionTable());
//		frame.add(new SelectionTable(18, 7, true));
//		frame.add(new SelectionTable(18, 7, false));
		frame.pack();
		frame.setVisible(true);
	}
}

public class SelectionTable extends JPanel {
	
	private static final long serialVersionUID = 2087985592365993895L;

	public static final Dimension STANDART_CELL_SIZE = new Dimension(100, 25);
	public static final Color SELECTED_COLOR = Color.GREEN;
	public static final int BEGIN_TIME = 8;
	
	private int rows;
	private int cols;
	private boolean daybar;
	private Cell[][] cells;
	private Cell[] headers;
	
	public SelectionTable(int rows, int cols, boolean daybar) {
		// error checking
		if (rows < 0 || cols < 0) throw new IllegalArgumentException("rows and cols must be positive.");
		if (daybar && cols > 7) throw new IllegalArgumentException("Week only has 7 days.");
		// assign fields
		this.rows = rows;
		this.cols = cols;
		this.daybar = daybar;
		this.cells = new Cell[rows][cols];
		this.headers = daybar? new Cell[cols]:null;
		// create TableLayout
		double[][] size = {{TableLayout.FILL},{TableLayout.PREFERRED, TableLayout.FILL}};
		super.setLayout(new TableLayout(size));
		if (daybar)
			super.add(createDaybar(), "0, 0");
		super.add(createTable(), "0, 1");
	}
	
	public SelectionTable(int rows, int cols) {
		this(rows, cols, false);
	}
	
	public SelectionTable() {
		this(5, 5, false);
	}
	
	@SuppressWarnings("unchecked")
	public <E> List<E> getSelectedPropertiesAs() {
		List<E> tmpList = new LinkedList<E>();
		for (Cell[] t : this.cells)
			for (Cell c : t)
				if (c.isSelected())
					try {
						tmpList.add((E) c.getProperty());
					} catch(ClassCastException e) {
						JOptionPane.showMessageDialog(
								this,
								"An internal error has occured. Program will exit.",
								"FATAL ERROR",
								JOptionPane.ERROR_MESSAGE);
						System.exit(1);
					}
		return tmpList;
	}
	
	public void selectCell(Cell cell) {
		if (cell.isHeader())
			markWholeColSelected(cell.getCol());
		markCellSelected(cell);
		if (this.daybar)
			updateColHeader(cell.getCol());
	}
	
	public void deselectCell(Cell cell) {
		if (cell.isHeader())
			markWholeColNeutral(cell.getCol());
		markCellNeutral(cell);
		if (this.daybar)
			updateColHeader(cell.getCol());
	}
	
	private void markCellSelected(Cell cell) {
		cell.setSelected(true);
		cell.setBackground(SELECTED_COLOR);
	}
	
	private void markCellNeutral(Cell cell) {
		cell.setSelected(false);
		cell.setBackground(new Cell().getBackground());
	}
	
	private void markWholeColSelected(int col) {
		for (int i = 0; i < this.rows; i++)
			if (!this.cells[i][col].isSelected())
				markCellSelected(this.cells[i][col]);
	}
	
	private void markWholeColNeutral(int col) {
		for (int i = 0; i < this.rows; i++)
			if (this.cells[i][col].isSelected())
				markCellNeutral(this.cells[i][col]);
	}
	
	private void updateColHeader(int col) {
		if (isWholeColSelected(col) && !isHeaderSelected(col))
			markCellSelected(this.headers[col]);
		if (!isWholeColSelected(col) && isHeaderSelected(col))
			markCellNeutral(this.headers[col]);
	}
	
	private boolean isHeaderSelected(int col) {
		return this.headers[col].isSelected();
	}
	
	private boolean isWholeColSelected(int col) {
		boolean allSelected = true;
		for (int i = 0; i < this.rows; i++)
			if (!this.cells[i][col].isSelected())
				allSelected = false;
		return allSelected;
	}
	
	private JLabel createTable() {
		// create TableLayout
		double[][] size = new double[2][0];
		size[0] = new double[this.cols];
		size[1] = new double[this.rows];
		for (int i = 0; i < size[0].length; i++)
			size[0][i] = TableLayout.FILL;
		for (int i = 0; i < size[1].length; i++) {
			size[1][i] = TableLayout.FILL;
		}
		JLabel table = new JLabel();
		table.setLayout(new TableLayout(size));
		
		// add cells
		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				Cell tmpCell = new Cell(
						r,
						c,
						getCorrectTime(r).toString(),
						new Shift(
								getCorrectDay(c),
								getCorrectTime(r)));
				tmpCell.addMouseListener(new SelectionListener());
				table.add(tmpCell, c + ", " + r);
				addCell(tmpCell);
			}
		}
		// set prefSize for whole table
		table.setPreferredSize(new Dimension(
				(int)(this.cols * STANDART_CELL_SIZE.getWidth()),
				(int)(this.rows * STANDART_CELL_SIZE.getHeight())));
		return table;
	}
	
	private JLabel createDaybar() {
		// create TableLayout
		double[][] size = new double[2][0];
		size[0] = new double[this.cols];
		size[1] = new double[2];
		for (int i = 0; i < size[0].length; i++)
			size[0][i] = TableLayout.FILL;
		size[1][0] = TableLayout.FILL;
		size[1][1] = 3.0;
		JLabel daybar = new JLabel();
		daybar.setLayout(new TableLayout(size));
		
		// add headers(row: -1)
		for (int c = 0; c < this.cols ; c++) {
			Cell tmpCell = new Cell(
					Cell.HEADER_ROW_INDEX,
					c,
					getCorrectDay(c).toString(),
					getCorrectDay(c));
			tmpCell.addMouseListener(new SelectionListener());
			daybar.add(tmpCell, c + ", 0");
			addHeader(tmpCell);
		}
		// add one long dash-cell
		JLabel dash = new JLabel();
		dash.setBackground(Color.BLACK);
		dash.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0,
				dash.getBackground()));
		dash.setPreferredSize(new Dimension(0, 5));
		daybar.add(dash, "0, 1, " + (this.cols-1) + ", 1");
		daybar.setPreferredSize(new Dimension(
				(int)(this.cols * STANDART_CELL_SIZE.getWidth()),
				(int)(dash.getPreferredSize().getHeight() + STANDART_CELL_SIZE.getHeight())));
		return daybar;
	}
	
	private void addCell(Cell cell) {
		this.cells[cell.getRow()][cell.getCol()] = cell;
	}
	
	private void addHeader(Cell cell) {
		this.headers[cell.getCol()] = cell;
	}
	
	private DayOfWeek getCorrectDay(int col) {
		return DayOfWeek.of(col + 1);
	}
	
	private Hour getCorrectTime(int row) {
		return Hour.of((row + BEGIN_TIME) %24);
	}
	
	/////////////////////
	///// Listeners /////
	/////////////////////
	
	private class SelectionListener extends MouseInputAdapter{
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e))
				SelectionTable.this.selectCell((Cell)e.getSource());
			if (SwingUtilities.isRightMouseButton(e))
				SelectionTable.this.deselectCell((Cell)e.getSource());
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e))
				SelectionTable.this.selectCell((Cell)e.getSource());
			if (SwingUtilities.isRightMouseButton(e))
				SelectionTable.this.deselectCell((Cell)e.getSource());
			}
		
	}
	
}
