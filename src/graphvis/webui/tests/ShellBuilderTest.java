/*
* @(\#) ShellBuilderTest.java 1.1 28 March 14
*
* Copyright (\copyright) 2014 University of York & British Telecommunications plc
* This Software is granted under the MIT License (MIT)

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*
*/
package graphvis.webui.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import graphvis.webui.config.Configuration;
import graphvis.webui.shellUtils.ShellBuilder;

import org.junit.Test;
/**

 * <p>
 * @author James Pierce
 * @version 1.1
 */
public class ShellBuilderTest 
{

	@Test
	public void testScriptBuilder() throws Exception 
	{
		
		ShellBuilder sb = new ShellBuilder();
		sb.toString();
		String resultsCSV = ShellBuilder.scriptBuilder("csv", "TestFile.csv", "testPackage.testComp", 1);
		
		// As this is a parameter within the method we must declare it as a variable here.
		String hadoop_home = System.getenv("HADOOP_HOME");
		
		String expectedCSV = "export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./dist-graphvis.jar:./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar "+"\n"+
                           	hadoop_home + "/bin/hadoop "+"\\\n"+
							"org.apache.giraph.GiraphRunner testPackage.testComp "+"\\\n"+
							"-eif graphvis.io.CSVEdgeInputFormat "+"\\\n"+
							"-eip TestFile.csv "+"\\\n"+
							"-vof graphvis.io.SVGVertexOutputFormat "+"\\\n"+
							"-op " + Configuration.hdfsWorkingDirectory + " "+"\\\n"+
							"-mc graphvis.engine.GraphvisMasterCompute \\\n"+
							"-w 1 "+"\\\n"+
							"-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar,dist-graphvis.jar";
		
		// Assert true that the method produces the correct shell contents for CSV
		assertEquals(expectedCSV, resultsCSV);
		
		String resultsGML = ShellBuilder.scriptBuilder("gml", "TestFile.gml", "testPackage.testComp", 1);
		
		String expectedGML = "export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./dist-graphvis.jar:./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar "+"\n"+
               	hadoop_home + "/bin/hadoop "+"\\\n"+
				"org.apache.giraph.GiraphRunner testPackage.testComp "+"\\\n"+
				"-eif graphvis.io.GMLEdgeInputFormat "+"\\\n"+
				"-eip TestFile.gml "+"\\\n"+
				"-vof graphvis.io.SVGVertexOutputFormat "+"\\\n"+
				"-op " + Configuration.hdfsWorkingDirectory + " "+"\\\n"+
				"-mc graphvis.engine.GraphvisMasterCompute \\\n"+
				"-w 1 "+"\\\n"+
				"-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar,dist-graphvis.jar";
		
		// Assert true that the method produces the correct shell contents for GML
		assertEquals(expectedGML, resultsGML);
		
		String resultsGraphML = ShellBuilder.scriptBuilder("graphml", "TestFile.graphml", "testPackage.testComp", 1);
		
		String expectedGraphML = "export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./dist-graphvis.jar:./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar "+"\n"+
               	hadoop_home + "/bin/hadoop "+"\\\n"+
				"org.apache.giraph.GiraphRunner testPackage.testComp "+"\\\n"+
				"-eif graphvis.io.GraphMLEdgeInputFormat "+"\\\n"+
				"-eip TestFile.graphml "+"\\\n"+
				"-vof graphvis.io.SVGVertexOutputFormat "+"\\\n"+
				"-op " + Configuration.hdfsWorkingDirectory + " "+"\\\n"+
				"-mc graphvis.engine.GraphvisMasterCompute \\\n"+
				"-w 1 "+"\\\n"+
				"-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar,dist-graphvis.jar";
		
		// Assert true that the method produces the correct shell contents for GraphML
		assertEquals(expectedGraphML, resultsGraphML);
	}
	
	@Test(expected=IOException.class)
	public void testInvalid() throws Exception
	{
		ShellBuilder.scriptBuilder("xml", "TestFile.xml", "testPackage.testComp", 1);
	}
	
}
