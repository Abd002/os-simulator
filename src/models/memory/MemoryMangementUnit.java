package Memory;

public class MemoryMangementUnit {
    private boolean[] isLocated;
    private final Memory memory;

    public MemoryMangementUnit (Memory memory){
        this.memory = memory;
        this.isLocated = new boolean[memory.getMaxNumberOfWords()] ;
    }

    public int[] allocateMemory(int size){
        int[] result = new int[size];
        int index = 0;
        for(int i = 0;i < isLocated.length;i++){
            if(!isLocated[i]){
                result[index++] = i;
                isLocated[i] = true;
            }
        }
        return result;
    }

    public void deAllocateMemory(int [] locations){
        for(int i = 0;i < locations.length;i++){
            isLocated[locations[i]] = false;
        }
    }

}
