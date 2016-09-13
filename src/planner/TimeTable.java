package planner;

import java.util.TreeSet;

public class TimeTable {
	
	private TreeSet<Hour> timeTable;
	
	public TimeTable() {
		this.timeTable = new TreeSet<Hour>();
	}
	
	public void setAvailableAt(Hour hour){
		this.timeTable.add(hour);
	}
	
	public void setUnavailableAt(Hour hour) {
		if (isAvailableAt(hour))
			this.timeTable.remove(hour);
	}
	
	public boolean isAvailableAt(Hour hour) {
		return this.timeTable.contains(hour);
	}
	
}
