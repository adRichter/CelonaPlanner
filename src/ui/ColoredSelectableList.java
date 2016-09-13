package ui;

import javax.swing.JPanel;
import javax.swing.ListModel;

import util.SortableListModel;

public class ColoredSelectableList<E> extends JPanel {

	private static final long serialVersionUID = -5996865307279949817L;

	private ListModel<E> model;
	
	public ColoredSelectableList() {
		this.model = new SortableListModel<E>();
	}

}
