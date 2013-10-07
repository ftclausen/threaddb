/**
 * 
 */
package com.blackboard.threaddb;

/**
 * A representation of a thread stack and a occurrence counter to see how many times 
 * it has occurred. These objects are meant to be used in a Map implementation.
 * The code adding this object to the list must first check if it is already in the Map
 * and, if not, create a new key based on the stack SHA-256 sum. The value will be a new 
 * instance of this object with the stack specified and initial occurrence set to 0.
 * 
 * If the object is already in the Map then the surrounding code just needs to increment 
 * this object's occurrences variable via the inc() method.
 * 
 * @author fred 
 *
 */
public class ThreadInfo implements Comparable<ThreadInfo> {
	
	private String stack;
	private int occurrences;
    private String fullHash;
    // For displaying to the user; we only use the first 8 chars of the hash but
    // internally we'll use the full hash value
    private String displayHash;
    
	public ThreadInfo(String stack, String fullHash) throws NullPointerException {
		if (stack == null) {
			throw new NullPointerException("ERROR: Cannot create ThreadInfo object if passed null stack or hash");
		}
		this.stack = stack;
		this.occurrences = 1;
        if (fullHash != null) {
        	this.fullHash = fullHash;
        	// For display purposes show only the first 14 chars. E.g. just like 
        	// oracle SQL ID
            this.displayHash = fullHash.substring(0, 13);
        } else {
        	throw new NullPointerException("Calculated hash cannot be null");
        }
	}
	
	public void inc() {
		this.occurrences += 1;
	}

	public int getOccurrences() {
		return occurrences;
	}
	
	public String getStack() {
		return stack;
	}
	
    public String getDisplayHash() {
    	return(displayHash);
    }
    
    public String getFullHash() {
    	return(fullHash);
    }

	
	public int compareTo(ThreadInfo thread) {
		// The below gives most occurrences first
		final int BEFORE = 1;
		final int EQUAL = 0;
		final int AFTER = -1;
		
		if (this.occurrences == thread.getOccurrences()) {
			return(EQUAL);
		}
		
		if (this.occurrences < thread.getOccurrences()) {
			return(BEFORE);
		}
		
		if (this.occurrences > thread.getOccurrences()) {
			return(AFTER);
		}
		
		return(EQUAL);
	}

}
