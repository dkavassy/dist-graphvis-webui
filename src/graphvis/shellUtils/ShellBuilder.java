/*
* @(\#) ShellBuilder.java 1.1 28 March 14
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

package graphvis.shellUtils;

import graphvis.servlets.RunnerServlet;

import java.io.File;
import java.io.IOException;


/**
 * This class provides a means to generate the contents of valid shell
 * script which can be used to execute the graphvis giraph job.
 * 
 * @author James Pierce
 * @version 1.1
 *
 */
public class ShellBuilder 
{	
	/**
	 * 
	 * @param ext this is the {@link String} input for the file extension of 
	 * the input data. This method is exclusively used by the scriptBuilder method.
	 * @return
	 * @throws Exception
	 */
	private static String inputFormatChooser(String ext) throws IOException
	{
		// Select input format name. 
		String format;
		switch(ext)
		{
			case "graphml":
				format = "graphvis.io.GraphMLEdgeInputFormat";
				break;
			case "gml":
				format = "graphvis.io.GMLEdgeInputFormat";
				break;
			case "csv":
				format = "graphvis.io.CSVEdgeInputFormat";
				break;
			default:
				throw new IOException("Invalid input format!");
		}
		
		return format;
	}
	
	
	/**
	 * 
	 * @param inputExt is the {@link String} input data file extension.
	 * @param fileName is the {@link String} name of the file.
	 * @param compClass is the {@link String} name of the computation/engine class
	 * @param workers is the int number of workers to be given to the computation
	 * from the cluster.
	 * @return {@link String} containing the shell script to be executed.
	 * @throws Exception
	 */
	public static String scriptBuilder(String inputExt, String fileName, String compClass, int workers) throws Exception
	{
		// Get the hadoop environment variable
		String hadoop_home = System.getenv("HADOOP_HOME");
		
		String script ="export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:"
				+ "./dist-graphvis.jar" + File.pathSeparator + "./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar \n"
				+ hadoop_home+"/bin/hadoop \\\n"
				+ "org.apache.giraph.GiraphRunner " + compClass + " \\\n"
				+ "-eif "+ inputFormatChooser(inputExt) + " \\\n"
				+ "-eip " + fileName + " \\\n"
				+ "-vof" + " graphvis.io.SVGVertexOutputFormat \\\n"
				+ "-op " + RunnerServlet.hdfsWorkingDirectory + " \\\n"
				+ "-mc graphvis.engine.GraphvisMasterCompute \\\n"
				+ "-w " + workers + " \\\n"
				+ "-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar"+ ",dist-graphvis.jar" ;
		
		return script;
	}
}
