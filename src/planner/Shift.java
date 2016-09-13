package planner;

import java.time.DayOfWeek;

public class Shift {
	
	private DayOfWeek day;
	private Hour hour;
	
	public Shift(DayOfWeek day, Hour hour) {
		this.day = day;
		this.hour = hour;
	}
	
	public DayOfWeek getDay() {
		return day;
	}
	
	public Hour getHour() {
		return hour;
	}
	
}
