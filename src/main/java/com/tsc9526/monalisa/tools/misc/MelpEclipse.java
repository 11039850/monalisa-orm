/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.tools.misc;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.tsc9526.monalisa.tools.clazz.MelpClasspath;
import com.tsc9526.monalisa.tools.io.MelpFile;
import com.tsc9526.monalisa.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpEclipse {
	static Logger logger=Logger.getLogger(MelpEclipse.class);
	
	static final String URI_PREFIX = "URI//";
 	
	public static boolean inEclipseIDE() {
		try {
			Class.forName("org.eclipse.jdt.internal.apt.pluggable.core.dispatch.IdeProcessingEnvImpl");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static String findCfgBasePathByClass(Class<?> clazz){
		String basepath=null;
		
		String ewd=getEclipseWorkspaceDir();
		if(ewd!=null){
			String name=getProjectNameFromClass(clazz);
			if(name!=null){
				basepath= getProjectLocation(ewd,name);
			}
		}else{
			File f=MelpClasspath.getClassOrJarFile(clazz);
			if(f.getName().endsWith(".class")){
				String path=f.getAbsolutePath();
				path=path.replace("\\","/");
				
				int c=clazz.getName().split("\\.").length;
				for(int i=0;i<c;i++){
					int p=path.lastIndexOf("/");
					path=path.substring(0,p);
				}
				
				for(int i=0;i<=3;i++){
					if(new File(path+"/src").exists()==false){
						int p=path.lastIndexOf("/");
						if(p>0){
							path=path.substring(0,p);
						}else{
							break;
						}
					}else{
						basepath=path;
						break;
					}
				}
			}
		}
		
		return basepath;
	}
	
	public static String getEclipseWorkspaceDir(){
		String ewd=System.getProperty("eclipse.workspace.dir");
		if(ewd==null){
			ewd=tryGetDirBySystemProperty("wtp.deploy","catalina.base");
		}
		
		return ewd;
	}
	
	
	private static String tryGetDirBySystemProperty(String... props){
		for(String s:props){
			String v=System.getProperty(s);
			if(v!=null){
				v=v.replace("\\","/");
				int p=v.indexOf("/.metadata/.plugins");
				if(p>0){
					return v.substring(0,p);
				}
			}
		}
		return null;
	}
	
	public static String getProjectNameFromClass(Class<?> clazz){
		File f=MelpClasspath.getClassOrJarFile(clazz);
		
		String name=f.getName();
		if(name.endsWith(".jar")){
			//monalisa-orm-1.7.1-SNAPSHOT.jar; monalisa-orm-1.7.1.jar
			name=name.substring(0,name.length()-".jar".length()); //remove .jar
			if(name.endsWith("-SNAPSHOT")){
				name=name.substring(0,name.length()-"-SNAPSHOT".length()); //remove -SNAPSHOT
			}
			
			int p=name.lastIndexOf("-");
			if(p>0){
				return name.substring(0,p);
			}else{
				return name;
			}
			 
		}else if(name.endsWith(".class")){ 
			String path=f.getAbsolutePath();
			path=path.replace("\\","/");
			
			int p=path.indexOf("/WEB-INF/classes/");
			if(p>0){
				path=path.substring(0,p);
				p=path.lastIndexOf("/");
				
				return path.substring(p+1);
			}
		}
		
		return null;
	}
	
	/**
	 *
	 * @param eclipseWorkspaceDir eclipse workspace directory
	 * @param projectName  the project name
	 * 
	 * @see org.eclipse.core.internal.resources.LocalMetaArea#readPrivateDescription(org.eclipse.core.resources.IProject, org.eclipse.core.internal.resources.ProjectDescription)
	 * 
	 * @return location of the project, return null if not found.
	 */
	public static String getProjectLocation(String eclipseWorkspaceDir, String projectName) {
		String basePath=".metadata/.plugins/org.eclipse.core.resources/.projects";
		String lf=MelpFile.combinePath(eclipseWorkspaceDir,basePath,projectName,".location");
		
		File file=new File(lf);
		
		if(!file.exists()){
			return null;
		}
		
		try {
			SafeChunkyInputStream input = new SafeChunkyInputStream(file, 500);
			DataInputStream dataIn = new DataInputStream(input);
			
			String location = dataIn.readUTF();
			
			// location format < 3.2 was a local file system OS path
			// location format >= 3.2 is: URI_PREFIX + uri.toString()
			if (location.startsWith(URI_PREFIX)) {
				URI uri=new URI(location.substring(URI_PREFIX.length()));
				location=uri.getPath(); 
			}else if(location.length()==0){
				location= MelpFile.combinePath(eclipseWorkspaceDir,projectName);
			}
			
			dataIn.close();
			
			return location;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);	
		}
		
		return null;
	}

	static class ILocalStoreConstants {
		/** Common constants for History Store classes. */
		public final static int SIZE_LASTMODIFIED = 8;
		public static final int SIZE_COUNTER = 1;
		public static final int SIZE_KEY_SUFFIX = SIZE_LASTMODIFIED + SIZE_COUNTER;

		/** constants for safe chunky streams */

		// 40b18b8123bc00141a2596e7a393be1e
		public static final byte[] BEGIN_CHUNK = { 64, -79, -117, -127, 35, -68, 0, 20, 26, 37, -106, -25, -93, -109, -66, 30 };

		// c058fbf323bc00141a51f38c7bbb77c6
		public static final byte[] END_CHUNK = { -64, 88, -5, -13, 35, -68, 0, 20, 26, 81, -13, -116, 123, -69, 119, -58 };

		/** chunk delimiter size */
		// BEGIN_CHUNK and END_CHUNK must have the same length
		public static final int CHUNK_DELIMITER_SIZE = BEGIN_CHUNK.length;
	}

	public static class SafeChunkyInputStream extends InputStream {
		protected static final int BUFFER_SIZE = 8192;
		protected byte[] buffer;
		protected int bufferLength = 0;
		protected byte[] chunk;
		protected int chunkLength = 0;
		protected boolean endOfFile = false;
		protected InputStream input;
		protected int nextByteInBuffer = 0;
		protected int nextByteInChunk = 0;

		public SafeChunkyInputStream(File target) throws IOException {
			this(target, BUFFER_SIZE);
		}

		public SafeChunkyInputStream(File target, int bufferSize) throws IOException {
			input = new FileInputStream(target);
			buffer = new byte[bufferSize];
		}

		protected void accumulate(byte[] data, int start, int end) {
			byte[] result = new byte[chunk.length + end - start];
			System.arraycopy(chunk, 0, result, 0, chunk.length);
			System.arraycopy(data, start, result, chunk.length, end - start);
			chunk = result;
			chunkLength = chunkLength + end - start;
		}

		public int available() throws IOException {
			return chunkLength - nextByteInChunk;
		}

		protected void buildChunk() throws IOException {
			// read buffer loads of data until an entire chunk is accumulated
			while (true) {
				if (nextByteInBuffer + ILocalStoreConstants.CHUNK_DELIMITER_SIZE > bufferLength)
					shiftAndFillBuffer();
				int end = find(ILocalStoreConstants.END_CHUNK, nextByteInBuffer, bufferLength, true);
				if (end != -1) {
					accumulate(buffer, nextByteInBuffer, end);
					nextByteInBuffer = end + ILocalStoreConstants.CHUNK_DELIMITER_SIZE;
					return;
				}
				accumulate(buffer, nextByteInBuffer, bufferLength);
				bufferLength = input.read(buffer);
				nextByteInBuffer = 0;
				if (bufferLength == -1) {
					endOfFile = true;
					return;
				}
			}
		}

		public void close() throws IOException {
			input.close();
		}

		protected boolean compare(byte[] source, byte[] target, int startIndex) {
			for (int i = 0; i < target.length; i++) {
				if (source[startIndex] != target[i])
					return false;
				startIndex++;
			}
			return true;
		}

		protected int find(byte[] pattern, int startIndex, int endIndex, boolean accumulate) throws IOException {
			int pos = findByte(pattern[0], startIndex, endIndex);
			if (pos == -1)
				return -1;
			if (pos + ILocalStoreConstants.CHUNK_DELIMITER_SIZE > bufferLength) {
				if (accumulate)
					accumulate(buffer, nextByteInBuffer, pos);
				nextByteInBuffer = pos;
				pos = 0;
				shiftAndFillBuffer();
			}
			if (compare(buffer, pattern, pos))
				return pos;
			return find(pattern, pos + 1, endIndex, accumulate);
		}

		protected int findByte(byte target, int startIndex, int endIndex) {
			while (startIndex < endIndex) {
				if (buffer[startIndex] == target)
					return startIndex;
				startIndex++;
			}
			return -1;
		}

		protected void findChunkStart() throws IOException {
			if (nextByteInBuffer + ILocalStoreConstants.CHUNK_DELIMITER_SIZE > bufferLength)
				shiftAndFillBuffer();
			int begin = find(ILocalStoreConstants.BEGIN_CHUNK, nextByteInBuffer, bufferLength, false);
			if (begin != -1) {
				nextByteInBuffer = begin + ILocalStoreConstants.CHUNK_DELIMITER_SIZE;
				return;
			}
			bufferLength = input.read(buffer);
			nextByteInBuffer = 0;
			if (bufferLength == -1) {
				resetChunk();
				endOfFile = true;
				return;
			}
			findChunkStart();
		}

		public int read() throws IOException {
			if (endOfFile)
				return -1;
			// if there are bytes left in the chunk, return the first available
			if (nextByteInChunk < chunkLength)
				return chunk[nextByteInChunk++] & 0xFF;
			// Otherwise the chunk is empty so clear the current one, get the
			// next
			// one and recursively call read. Need to recur as the chunk may be
			// real but empty.
			resetChunk();
			findChunkStart();
			if (endOfFile)
				return -1;
			buildChunk();
			refineChunk();
			return read();
		}

		/**
		 * Skip over any begin chunks in the current chunk. This could be
		 * optimized to skip at the same time as we are scanning the buffer.
		 */
		protected void refineChunk() {
			int start = chunkLength - ILocalStoreConstants.CHUNK_DELIMITER_SIZE;
			if (start < 0)
				return;
			for (int i = start; i >= 0; i--) {
				if (compare(chunk, ILocalStoreConstants.BEGIN_CHUNK, i)) {
					nextByteInChunk = i + ILocalStoreConstants.CHUNK_DELIMITER_SIZE;
					return;
				}
			}
		}

		protected void resetChunk() {
			chunk = new byte[0];
			chunkLength = 0;
			nextByteInChunk = 0;
		}

		protected void shiftAndFillBuffer() throws IOException {
			int length = bufferLength - nextByteInBuffer;
			System.arraycopy(buffer, nextByteInBuffer, buffer, 0, length);
			nextByteInBuffer = 0;
			bufferLength = length;
			int read = input.read(buffer, bufferLength, buffer.length - bufferLength);
			if (read != -1)
				bufferLength += read;
			else {
				resetChunk();
				endOfFile = true;
			}
		}
	}
}
