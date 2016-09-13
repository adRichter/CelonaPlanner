package util;

import java.util.Arrays;

import javax.swing.DefaultListModel;

public class SortableListModel<E> extends DefaultListModel<E> {

	private static final long serialVersionUID = -8606633532876437227L;

	@SuppressWarnings("unchecked")
	public void sort() {
		Object[] arr = this.toArray();
		if (arr instanceof String[])
			Arrays.sort((String[])arr, String.CASE_INSENSITIVE_ORDER);
		else
			Arrays.sort(arr);
		
		for (int i = 0; i < arr.length; i++)
			this.set(i, (E) arr[i]);
	}
}
