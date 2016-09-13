package planner;

public enum Hour {
	
	H_0,
	H_1,
	H_2,
	H_3,
	H_4,
	H_5,
	H_6,
	H_7,
	H_8,
	H_9,
	H_10,
	H_11,
	H_12,
	H_13,
	H_14,
	H_15,
	H_16,
	H_17,
	H_18,
	H_19,
	H_20,
	H_21,
	H_22,
	H_23;
	
	public static Hour of(int h) {
		if (h < 0 || h > 23) throw new IllegalArgumentException(h + " is not on the clock.");
		return values()[h];
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.ordinal()) + "h";
	}
	
}
