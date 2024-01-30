package models;

public class PCB {
	
	public enum state{
		New,
		Ready,
		Waiting,
		Running,
		Terminated
	};
	
	private int processID;				/* use UniqueIdGenerator to deal with it */
	
	state processState;
	
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
}
