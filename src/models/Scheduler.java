package models;

import java.util.LinkedList;
import java.util.Queue;

public class Scheduler 
{
	
	public int timeSlice;
	public Queue<PCB> readyQueue;
	public Queue<PCB> waitingQueue;
	
	public Scheduler(int timeSlice)
	{
		this.timeSlice = timeSlice;
		readyQueue = new LinkedList<>();
		waitingQueue = new LinkedList<>();
	}
	
	public void schedule()
	{
		PCB pcb = readyQueue.remove();
		dispatchProcess(pcb);
	}
	
	
	public Process restoreState(PCB pcb)
	{
		return new Process();
	}
	
	public void saveState(Process process)
	{
		
	}
	
	public void admitProcess(PCB pcb)
	{
		readyQueue.add(pcb);
	}
	
	public PCB dispatchProcess(PCB pcb)
	{
		return new PCB();
	}
	
	public PCB terminateProcess(Process process)
	{
		return new PCB();
	}
	
	public void interruptProcess()
	{
		
	}
	
	public void blockProcess()
	{
		
	}
}