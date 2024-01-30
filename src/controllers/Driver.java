package controllers;

import models.Kernel;

public class Driver {

	public static void main(String[] args) {
		
		Kernel kernel=new Kernel();
		
		String ok=kernel.readFromDisk("dummy");
		System.out.println(ok);
		
		Kernel.writeToDisk("k",ok);
    
    }
	
}
