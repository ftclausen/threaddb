package com.blackboard.threaddb;

/**
 * abstract dump parser class, contains all generic dump parser
 * stuff, which doesn't have any jdk specific parsing code.
 * 
 * All Dump Parser should extend from this class as it already provides
 * a basic parsing interface.
 * 
 * @author irockel
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractDumpParser implements DumpParser {
    protected BufferedReader threadDumpFile = null;
    
    private int maxThreads = 10000;
    
    protected AbstractDumpParser(String fileName) throws IOException, NullPointerException {
    	// TODO: Make properties file for these things
        // maxCheckLines = PrefManager.get().getMaxRows();
        // markSize = PrefManager.get().getStreamResetBuffer();   
        // millisTimeStamp = PrefManager.get().getMillisTimeStamp();
        this.threadDumpFile = open(fileName);
    }
    
    
    /**
     * open the thread dump file
     */
    public BufferedReader open(String fileName) throws IOException {
    	BufferedReader threadDump = new BufferedReader(new FileReader(fileName));
    	return(threadDump);
    }
    
    /**
     * close this dump parser, also closes the passed dump stream
     */
    public void close() throws IOException {
        if (threadDumpFile != null) {
            threadDumpFile.close();
        }        
    }
    

    /**
     * specifies the maximum amounts of lines to check if the dump is followed
     * by a class histogram or a deadlock.
     * @return the amount of lines to check, defaults to 10.
     */
    protected int getMaxRetrievableThreads() {
        return maxThreads;
    }

    protected void setMaxRetrievableThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }
    
    /**
     * Compute the SHA-256 hash for identification purposes
     * @param String representing whole thread dump
     * @return Hex string representing checksum
     */
    protected String computeHash(String thread) {
       	MessageDigest currentThreadHash = null;
    	try {
    		currentThreadHash = MessageDigest.getInstance("SHA-256");
    		currentThreadHash.update(thread.getBytes()); // Convert string builder to bytes
    	} catch (NoSuchAlgorithmException e) {
    		System.err.println("ERROR: Cannot use hashing algorithm : " + e.getLocalizedMessage());
    		System.exit(1);
    	}
    	
    	// Could not find a built in way to do this
    	String hash = computeHex(currentThreadHash.digest());
    	return(hash);
    }
    
    /**
     * Compute hex representation of digest. Info taken from 
     * http://stackoverflow.com/questions/5470219/java-get-md5-string-from-message-digest
     * 
     * @param digest
     * @return hexString
     */
    private static String computeHex(byte[] digest) {
    	StringBuffer hexString = new StringBuffer();
    	for (int currentByte : digest) {
    		if ((0xff & currentByte) < 0x10 ) {
    			hexString.append("0" + Integer.toHexString((0xff & currentByte)));
    		} else {
    			hexString.append(Integer.toHexString(0xff & currentByte));
    		}
    	}
    	
    	return(hexString.toString());
    }
}
