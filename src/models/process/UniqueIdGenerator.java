package models.process;

import java.util.HashSet;
import java.util.Set;

final public class UniqueIdGenerator {
    private static Set<Integer> usedIds = new HashSet<>();
    
    private UniqueIdGenerator() {
    	
    }

    public static int generateUniqueId() {
		int newId, lastAssignedId = 0;
        do {
            newId = ++lastAssignedId;
        } while (usedIds.contains(newId));
        
        usedIds.add(newId);
        return newId;
    }

    public static void releaseId(int id) {
        usedIds.remove(id);
    }
}