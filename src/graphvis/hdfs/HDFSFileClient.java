/*
* @(\#) HDFSFileClient.java 1.1 28 March 14
*
* Copyright (\copyright) 2014 University of York & British Telecommunications plc
* This Software is granted under the MIT License (MIT)

*

* Permission is hereby granted, free of charge, to any person obtaining a copy

* of this software and associated documentation files (the "Software"), to deal

* in the Software without restriction, including without limitation the rights

* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell

* copies of the Software, and to permit persons to whom the Software is

* furnished to do so, subject to the following conditions:

*

* The above copyright notice and this permission notice shall be included in

* all copies or substantial portions of the Software.

*

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR

* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,

* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE

* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER

* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,

* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN

* THE SOFTWARE.
*
*/

package graphvis.hdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
/**
 * This class provides java methods to move files between the
 * HDFS and local file system based on the environment variables 
 * set for HADOOP_HOME.
 * <p>
 * @author James Pierce
 * @version 1.1 Initial development
 */
public class HDFSFileClient 
{
	// Private field to store configuration object
	private Configuration conf ;
	public  FileSystem fileSystem;
	
	public HDFSFileClient() throws IOException
	{
		conf = new Configuration();
		// Get the requisite environment variable from the host system below. 
		String hadoop_home = System.getenv("HADOOP_HOME");
		// Set the required information needed for the configuration object to work.
		conf.addResource(new Path(hadoop_home + "/etc/hadoop/core-site.xml"));
		conf.addResource(new Path(hadoop_home + "/etc/hadoop/hdfs-site.xml"));
		conf.addResource(new Path(hadoop_home + "/etc/hadoop/mapred-site.xml"));
		
		// Assign the file system object the configuration
		fileSystem = FileSystem.get(conf); 
	}
	
	
	/**
	 * Takes a file from the local file system and copies it to the HDFS.
	 * @param source the {@link String} representation of the source file 
	 * and its path in the local file system.
	 * @param dest the {@link String} representation of the destination
	 * directory in HDFS to place the copied local file.
	 * @throws IOException
	 */
	public void moveFromLocal (String source, String dest) throws IOException 
	{		 
		Path srcPath = new Path(source);
		Path dstPath = new Path(dest);
		
		// Check if the file already exists
		if (!(fileSystem.exists(dstPath))) 
		{
			throw new FileNotFoundException();
		}
		 
		// Get the filename out of the file path
		String filename = source.substring(source.lastIndexOf('/') + 1, source.length());
		 
		fileSystem.moveFromLocalFile(srcPath, dstPath);
		System.out.println("File " + filename + " moved to " + dest);
	}

	/**
	 * Takes a file from the HDFS and moves it into the local file system.
	 * @param source the {@link String} representation of the file source file
	 * and its path in the HDFS
	 * @param dest the {@link String} representation of the destination directory in 
	 * the local file system.	
	 * @throws IOException
	 */
	public void moveFromHdfs(String source, String dest) throws IOException 
	{	
		
		Path srcPath = new Path(source);
		Path dstPath = new Path(dest);
		 
		// Get the filename out of the file path
		String filename = srcPath.getName();

		fileSystem.moveToLocalFile(srcPath, dstPath);
		System.out.println("File " + filename + " moved to " + dest);
	}
	
	@Override
	protected void finalize() throws Throwable 
	{
		fileSystem.close();
		super.finalize();
	}
}
