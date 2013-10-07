/*
 * DumpParser.java
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


import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Dump Parser Interface, defines base methods for all dump parsers.
 *
 * @author fclausen
 * @author irockel
 */
public interface DumpParser {
	
	// Returns the checksum keyed map. I could have used a list of ThreadInfo objects but 
	// a map will make a direct query faster
	public Map<String, ThreadInfo> returnAllThreads() throws IOException, NoSuchAlgorithmException; 
    
	public BufferedReader open(String fileName) throws IOException;
	
    public void close() throws IOException;
    
}