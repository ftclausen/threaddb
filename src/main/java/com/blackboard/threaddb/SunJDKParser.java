/*
 * SunJDKParser.java
 *
 * This file is part of threaddb - taken from TDA (http://java.net/projects/tda)
 *
 * threaddb is free software; you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * threaddb is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with threaddb; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package com.blackboard.threaddb;

import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * Parses SunJDK Thread Dumps.
 * Needs to be closed after use
 *
 * @author fclausen
 * 
 */
public class SunJDKParser extends AbstractDumpParser {

    private Map<String, ThreadInfo> allThreads = new HashMap<String,ThreadInfo>();

    /** 
     * Creates a new instance of SunJDKParser 
     */
    public SunJDKParser(String fileName) throws IOException {
        super(fileName);
        if (threadDumpFile != null) {
        	System.out.println("DEBUG: Successfully opened thread dump file");
        	if(!checkForSupportedThreadDump(threadDumpFile.readLine())) {
        		System.err.println("ERROR: This does not appear to be a valid thread dump file");
        		System.exit(1);
        	}
        	
        }
        
    }
    
    /**
     * Parse the next stack
     *  *
     * @throws IOException  
     * @returns The thread summary
     * 
     */
    public Map<String, ThreadInfo> returnAllThreads() throws IOException {
    	
    	String currentLine;
    	Pattern threadStart = Pattern.compile("^\"(.*?)\"(.*)");
    	Pattern threadState = Pattern.compile("^\\s+java\\.lang\\.Thread\\.State");
    	Pattern threadStack = Pattern.compile("^\\s+(at|- )");
    	Pattern threadEnd = Pattern.compile("^$");
    	Matcher currentMatcher = null;
    	boolean foundThreadStart = false;
    	boolean foundThreadEnd = false;
		StringBuilder currentThread = new StringBuilder();
		
    	while ((currentLine = threadDumpFile.readLine()) != null) {
    		// System.out.println("DEBUG: Checking line : " + "\"" + currentLine + "\"");
    		
    		currentMatcher = threadStart.matcher(currentLine);
    		if (currentMatcher.find()) {
    			// System.out.println("DEBUG: Found start of thread stack : " + currentLine);
    			currentThread = new StringBuilder();
    			String threadTitle = currentMatcher.group(1);
    			String threadMetadata = currentMatcher.group(2);
    			// Strip out ID numbers, etc from thread title. BUG: Might munge legitimate thread names
    			// that contain numbers e.g. UberSoftVersion2
    			threadTitle = threadTitle.replaceAll("([0-9]+)", "N");
    			// Strip out user ID
    			threadTitle = threadTitle.replaceAll("userId=(.*?),", "userId=N");
    			// String out sessionID
    			threadTitle = threadTitle.replaceAll("sessionId=([0-9A-Z]+)", "sessionId=N");
    			// Strip out hex throws NullPointerException
    			threadMetadata = threadMetadata.replaceAll("(0x[0-9a-f]+)", "N");
    			// Strip out remaining decimals
    			threadMetadata = threadMetadata.replaceAll("([0-9]+)", "N");
    			foundThreadStart = true;
    			currentThread.append(threadTitle + threadMetadata + "\n");
    			currentMatcher = null;
    		}
    		
    		currentMatcher = threadState.matcher(currentLine);
    		if (currentMatcher.find()) {
    			// System.out.println("DEBUG: Found thread state line : " + currentLine);
    			currentThread.append(currentLine + "\n");
    			currentMatcher = null;
    		}
    		
    		currentMatcher = threadStack.matcher(currentLine);
    		if (currentMatcher.find()) {
    			// Substitute away the lock ID
    			currentLine = currentLine.replaceAll("(0x[0-9a-f]+)", "N");
    			currentThread.append(currentLine + "\n");
    			currentMatcher = null;
    		}
    		
    		currentMatcher = threadEnd.matcher(currentLine);
    		if (currentMatcher.find() && foundThreadStart) {
    			currentMatcher = null;
    			foundThreadEnd = true;
    		}
    		
    		// We have a complete thread. Add to allThreads or increment counter, as appropriate
    		if (foundThreadStart && foundThreadEnd) {
    			String currentThreadFinal = currentThread.toString();
    	    	String hash = computeHash(currentThreadFinal);
    	    	
    	    	if (allThreads.containsKey(hash)) {
    	    		allThreads.get(hash).inc();
    	    	} else {
    	    		allThreads.put(hash, new ThreadInfo(currentThreadFinal, hash));
    	    	}
    	    	foundThreadStart = false;
    	    	foundThreadEnd = false;
    	    	currentThread = null;
    	    	hash = null;
    		}
    	}
    	return(allThreads);
    }

    /**
     * check if the passed log line contains the beginning of a sun jdk thread
     * dump.
     * @param logLine the line of the logfile to test
     * @return true, if the start of a sun thread dump is detected.
     */
    public static boolean checkForSupportedThreadDump(String logLine) {
        return (logLine.trim().indexOf("Full thread dump") >= 0);
    }
}