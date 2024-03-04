package models.memory;

public final class Memory {
    public final int MAX_SIZE;
    private final MemoryWord[] memory;
    public Memory(int maxNumberOfWords){
        this.MAX_SIZE = maxNumberOfWords;
        this.memory = new MemoryWord[maxNumberOfWords];
    }
    public MemoryWord getMemoryWord(int address){
        return memory[address];
    }

    public void setMemoryWord(int address,MemoryWord word){
        this.memory[address] = word;
    }

    public MemoryWord[] getMemory(){
        return this.memory.clone();
    }

}
