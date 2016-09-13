package planner;

import java.awt.Color;
import java.time.DayOfWeek;
import java.util.HashMap;

public class Worker implements Comparable<Worker>{

	private String name;
	private HashMap<DayOfWeek, TimeTable> wishTable;
	private Color color;

	public Worker(String name, Color color) {
		this.name = name;
		this.color = color;
		this.wishTable = new HashMap<DayOfWeek, TimeTable>();
		for (DayOfWeek day : DayOfWeek.values())
			this.wishTable.put(day, new TimeTable());
	}
	
	public Worker(String name) {
//		this(name, Color.getHSBColor((float)Math.random(), 1.0f, 1.0f));
		this(name, new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
	}

	public static String validateName(String name) throws IllegalArgumentException {
		if (name == null || name.length() == 0) throw new IllegalArgumentException();
		return name;
	}

	public void addShift(Shift shift) {
		this.wishTable.get(shift.getDay()).setAvailableAt(shift.getHour());
	}

	public void removeShift(Shift shift) {
		this.wishTable.get(shift.getDay()).setUnavailableAt(shift.getHour());
	}

	public TimeTable getTimeTableOn(DayOfWeek day) {
		return this.wishTable.get(day);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public int compareTo(Worker o) {
		return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}
