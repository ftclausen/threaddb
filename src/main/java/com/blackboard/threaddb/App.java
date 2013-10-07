package com.blackboard.threaddb;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Hello world!
 * Enhancements
 * * Proper logging
 * * TDDify
 * 
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Starting threaddb thread inventory" );
        if (args.length == 0) {
        	System.err.println("Usage: threaddb thread_dump_file.txt");
        	System.exit(1);
        }
        
        String threadDumpFile = args[0];
        SunJDKParser sunJDK = null;
        System.out.println("DEBUG: Using file " + threadDumpFile);
        try {
        	sunJDK = new SunJDKParser(threadDumpFile);
        	Map<String, ThreadInfo> allThreads;
        	allThreads = sunJDK.returnAllThreads();
        	List<ThreadInfo> threads = new ArrayList<ThreadInfo>(allThreads.values());
        	Collections.sort(threads);
        	System.out.println("DEBUG: Thread report follows : \n");
        	for (ThreadInfo currentThread : threads) {
	    		System.out.println("DEBUG: full thread hash is : " + currentThread.getFullHash());
	    		System.out.println("DEBUG: display hash is : " + currentThread.getDisplayHash());
        		System.out.println("Occurences of below thread " + currentThread.getOccurrences());
        		System.out.println(currentThread.getStack());
        	}
        	sunJDK.close();
        } catch (IOException e) {
        	System.out.println("ERROR: Cannot open file - " + e.getLocalizedMessage());
        }
    }
}
