package graphvis.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import graphvis.servlets.RunnerServlet;
import graphvis.shellUtils.ShellBuilder;

import org.junit.Test;

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
		
		String expectedCSV = "export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar "+"\n"+
                           	hadoop_home + "/bin/hadoop "+"\\\n"+
							"org.apache.giraph.GiraphRunner testPackage.testComp "+"\\\n"+
							"-eif graphvis.io.CSVEdgeInputFormat "+"\\\n"+
							"-eip TestFile.csv "+"\\\n"+
							"-vof graphvis.io.SVGVertexOutputFormat "+"\\\n"+
							"-op " + RunnerServlet.hdfsWorkingDirectory + " "+"\\\n"+
							"-mc graphvis.engine.GraphvisMasterCompute \\\n"+
							"-w 1 "+"\\\n"+
							"-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar";
		
		// Assert true that the method produces the correct shell contents for CSV
		assertEquals(expectedCSV, resultsCSV);
		
		String resultsGML = ShellBuilder.scriptBuilder("gml", "TestFile.gml", "testPackage.testComp", 1);
		
		String expectedGML = "export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar "+"\n"+
               	hadoop_home + "/bin/hadoop "+"\\\n"+
				"org.apache.giraph.GiraphRunner testPackage.testComp "+"\\\n"+
				"-eif graphvis.io.GMLEdgeInputFormat "+"\\\n"+
				"-eip TestFile.gml "+"\\\n"+
				"-vof graphvis.io.SVGVertexOutputFormat "+"\\\n"+
				"-op " + RunnerServlet.hdfsWorkingDirectory + " "+"\\\n"+
				"-mc graphvis.engine.GraphvisMasterCompute \\\n"+
				"-w 1 "+"\\\n"+
				"-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar";
		
		// Assert true that the method produces the correct shell contents for GML
		assertEquals(expectedGML, resultsGML);
		
		String resultsGraphML = ShellBuilder.scriptBuilder("graphml", "TestFile.graphml", "testPackage.testComp", 1);
		
		String expectedGraphML = "export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:./giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar "+"\n"+
               	hadoop_home + "/bin/hadoop "+"\\\n"+
				"org.apache.giraph.GiraphRunner testPackage.testComp "+"\\\n"+
				"-eif graphvis.io.GraphMLEdgeInputFormat "+"\\\n"+
				"-eip TestFile.graphml "+"\\\n"+
				"-vof graphvis.io.SVGVertexOutputFormat "+"\\\n"+
				"-op " + RunnerServlet.hdfsWorkingDirectory + " "+"\\\n"+
				"-mc graphvis.engine.GraphvisMasterCompute \\\n"+
				"-w 1 "+"\\\n"+
				"-yj giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar";
		
		// Assert true that the method produces the correct shell contents for GraphML
		assertEquals(expectedGraphML, resultsGraphML);
	}
	
	@Test(expected=IOException.class)
	public void testInvalid() throws Exception
	{
		ShellBuilder.scriptBuilder("xml", "TestFile.xml", "testPackage.testComp", 1);
	}
	
}
