package models.process;

public class PCB {
	
	private enum ProcessState{
		New,
		Ready,
		Waiting,
		Running,
		Terminated
	};
	
	private int processID;				/* use UniqueIdGenerator to deal with it */
	
	ProcessState processState;
	
	int programCounter;
	
	boolean isLoaded;
	
	int[] memoryBoundaries = new int[2];
	
	
	
    public PCB() {
        this.processID = UniqueIdGenerator.generateUniqueId();
    }

    public int getId() {
        return processID;
    }

    public void deleteProcess() {
        UniqueIdGenerator.releaseId(this.processID);
    }
    
    public ProcessState getState() {
    	return processState;
    }
    
    public void setState(ProcessState processState) {
    	this.processState=processState;
    }
    
    public int getPC() {
    	return programCounter;
    }
   
    public void incrementPC() {
    	programCounter++;
    }
    
    
    //TODO
    public int[] getMemoryTable() {
    	return getMemoryTable();
    }
    
    //TODO
    public void setMemTable() {
    	
    }
}