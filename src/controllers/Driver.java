package controllers;

public final class Driver {
	
	private static int clock = 0;
	
	public static void main(String[] args) {
	
    
    }
	
	public static void tick() {
		clock++;
	}
	
	public static int getClock() {
		return clock;
	}
	
	private Driver() {}

	
}
