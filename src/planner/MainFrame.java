package planner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;

import ui.SelectionTable;
import util.SortableListModel;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private SortableListModel<Worker> workers;
	private JList<Worker> list;
	
	public MainFrame() {
		this.setTitle("Planner");
		this.table = new JTable(18, 7);
		this.workers = new SortableListModel<Worker>();
		this.list = new JList<Worker>(this.workers);
		this.list.addKeyListener(new KeyListener());
		JButton buttonAddWorker = new JButton("add");
		buttonAddWorker.addActionListener(new AddButtonListener());
		JButton buttonRemoveWorker = new JButton("remove");
		buttonRemoveWorker.addActionListener(new RemoveButtonListener());
		JButton buttonEditWorker = new JButton("edit");
		buttonEditWorker.addActionListener(new EditButtonListener());
		JButton buttonOrganizeWorker = new JButton("organize");
		buttonOrganizeWorker.addActionListener(new OrganizeButtonListener());
		
		JPanel listArea = new JPanel();
		JPanel buttonPart = new JPanel();
//		JPanel tableArea = new JPanel();
		
//		this.list.setBackground(Color.BLUE);
		this.list.setCellRenderer(new ColoredListCellRenderer());
		
		this.setLayout(new BorderLayout());
		buttonPart.setLayout(new GridLayout(2, 2));
		buttonPart.add(buttonAddWorker);
		buttonPart.add(buttonRemoveWorker);
		buttonPart.add(buttonEditWorker);
		buttonPart.add(buttonOrganizeWorker);
		listArea.setLayout(new BorderLayout());
		listArea.add(this.list, BorderLayout.CENTER);
		listArea.add(new JScrollPane(this.list), BorderLayout.CENTER);
		listArea.add(buttonPart, BorderLayout.SOUTH);
		listArea.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));
		listArea.addKeyListener(new KeyListener());
		
		this.add(listArea, BorderLayout.WEST);
		this.add(this.table, BorderLayout.CENTER);
		this.pack();
	}
	
	public void addNewWorker() {
		try {
			String name = (String) JOptionPane.showInputDialog(this,
					"Type the new workers name:",
					"Add worker",
					JOptionPane.QUESTION_MESSAGE);
			name = Worker.validateName(name);
			this.workers.addElement(new Worker(name));
			this.workers.sort();
			this.list.grabFocus();
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void removeSelectedWorker() {
		try {
			this.workers.remove(this.list.getSelectedIndex());
			this.list.grabFocus();
		} catch (ArrayIndexOutOfBoundsException ex) {
			
		}
	}
	
	public void editSelectedWorker() {
		Worker selectedWorker = this.list.getSelectedValue();
		try {
			String name = JOptionPane.showInputDialog(
					this,
					"Type the new name for " + selectedWorker.getName() + ":",
					"Edit " + selectedWorker,
					JOptionPane.QUESTION_MESSAGE);
			name = Worker.validateName(name);
			selectedWorker.setName(name);
			this.list.grabFocus();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(
					this,
					(this.workers.isEmpty()? "U must first add a worker":"Select a worker")
						+ " to edit them.",
					"No worker " + (this.workers.isEmpty()? "found":"selected"),
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void organizeSelectedWorker() {
		Worker selectedWorker = this.list.getSelectedValue();
		try {
			if (selectedWorker == null) throw new NullPointerException();
			SelectionTable sTable = new SelectionTable(18, 7, true);

			JOptionPane.showMessageDialog(
					this,
					sTable,
					"Organize " + selectedWorker,
					JOptionPane.DEFAULT_OPTION);

			for (Shift shift : sTable.<Shift>getSelectedPropertiesAs())
				selectedWorker.addShift(shift);
			this.list.grabFocus();
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(
					this,
					(this.workers.isEmpty()? "U must first add a worker":"Select a worker")
						+ " to organize them.",
					"No worker " + (this.workers.isEmpty()? "found":"selected"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ColoredListCellRenderer implements ListCellRenderer<Worker> {
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends Worker> list,
				Worker value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			JPanel cell = new JPanel();
			JLabel inner = new JLabel(" " + value.toString() + " ");
			cell.setPreferredSize(new Dimension(100, 25));
			if (cellHasFocus)
				inner.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			if (isSelected)
				inner.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
			cell.setBackground(value.getColor());
			cell.add(inner);
			return cell;
		}
	}
	
	/////////////////////
	///// Listeners ///// 
	/////////////////////
	
	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.this.addNewWorker();
		}
	}
	
	private class RemoveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.this.removeSelectedWorker();
		}
	}
	
	private class EditButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.this.editSelectedWorker();
		}
	}
	
	private class OrganizeButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.this.organizeSelectedWorker();
		}
		
	}
	
	private class KeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 65) MainFrame.this.addNewWorker();
			if (e.getKeyCode() == 82) MainFrame.this.removeSelectedWorker();
			if (e.getKeyCode() == 127) MainFrame.this.removeSelectedWorker();
			if (e.getKeyCode() == 69) MainFrame.this.editSelectedWorker();
			if (e.getKeyCode() == 79) MainFrame.this.organizeSelectedWorker();
		}
	}
	
}


