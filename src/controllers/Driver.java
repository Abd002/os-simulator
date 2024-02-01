package controllers;

import models.Kernel;

public final class Driver {
	
	private static int clock = 0;
	
	public static void main(String[] args) {
		
		Kernel kernel=new Kernel();
		
		String ok=kernel.readFromDisk("dummy");
		System.out.println(ok);
		
		Kernel.writeToDisk("k",ok);
    
    }
	
	public static void tick() {
		clock++;
	}
	
	public static int getClock() {
		return clock;
	}
	
	private Driver() {}

	
}
