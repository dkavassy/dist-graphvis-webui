package graphvis.webui.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import graphvis.webui.parsers.GraphMLParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphMLTest 
{

	GraphMLParser graphML = new GraphMLParser();
	BufferedReader br;
	PrintWriter outWriter;
	File testFile;
	File resultsFile;
	
	File testFile2;
	String inputDir;
	
	@Before
	public void setUp() throws Exception 
	{
		final String rawData  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
				"<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n"+
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance \n" + 
				"xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns \n"+
				"http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n"+
				"<key id=\"d0\" for=\"node\" attr.name=\"color\" attr.type=\"string\">\n"+
				"<default>yellow</default>\n"+
				"</key>\n" +
				"<key id=\"d1\" for=\"edge\" attr.name=\"weight\" attr.type=\"double\"/>\n"+
				"<graph id=\"G\" edgedefault=\"undirected\">\n"+
				"<node id=\"n0\">\n"+
				"<data key=\"d0\">green</data>\n"+
				"</node>\n" +
				"<node id=\"n1\"/>\n"+
				"<node id=\"n2\">\n" +
				"<data key=\"d0\">blue</data>\n"+
				"</node>\n"+
				"<node id=\"n3\">n"+
				"<data key=\"d0\">red</data>\n"+
				"</node>\n"+
				"<node id=\"n4\"/>\n"+
				"<node id=\"n5\">\n"+
				"<data key=\"d0\">turquoise</data>\n"+
				"</node>\n"+
				"<edge id=\"e0\" source=\"n0\" target=\"n2\">\n"+
				"<data key=\"d1\">1.0</data>\n" +
				"</edge>\n" +
				"edge id=\"e1\" source=\"n0\" target=\"n1\">\n"+
				"<data key=\"d1\">1.0</data>\n"+
				"</edge>\n"+
				"<edge id=\"e2\" source=\"n1\" target=\"n3\">\n"+
				"<data key=\"d1\">2.0</data>\n"+
				"</edge>\n"+
				"<edge id=\"e3\" source=\"n3\" target=\"n2\"/>\n"+
				"<edge id=\"e4\" source=\"n2\" target=\"n4\"/>\n"+
				"<edge id=\"e5\" source=\"n3\" target=\"n5\"/>\n"+
				"<edge id=\"e6\" source=\"n5\" target=\"n4\">\n"+
				"<data key=\"d1\">1.1</data>\n"+
				"</edge>\n"+
				"</graph>\n"+
				"</graphml>\n";
		
		graphML = new GraphMLParser();
		testFile = new File("testFile.graphml");
		testFile.createNewFile();
		resultsFile = new File("resultsFile.graphml");
		resultsFile.createNewFile();
		
		FileWriter fw = new FileWriter(testFile.getAbsolutePath());
		fw.write(rawData);
		fw.close();
		
		
		testFile2 = new File("testFile2.graphml");
		testFile2.createNewFile();
		
		FileWriter fw2 = new FileWriter(testFile2.getAbsolutePath());
		fw2.write(rawData);
		fw2.close();
		
		inputDir = testFile2.getAbsolutePath(); 
	}

	@After
	public void tearDown() throws Exception 
	{
		// remove temporary test files post execution
		resultsFile.delete();
		testFile.delete();
		testFile2.delete();
	}

	@Test
	public void testParseMethod() throws IOException 
	{
		BufferedReader preBuff = new BufferedReader(new FileReader(resultsFile));
		String line, previousContents = "";
		while((line = preBuff.readLine())!=null)
		{
			previousContents = previousContents.concat(line);
		}
		preBuff.close();

		br = new BufferedReader(new FileReader(testFile));
		outWriter = new PrintWriter(resultsFile);
		
		graphML.parseMethod(br, outWriter);
		
		outWriter.close();
		
		BufferedReader postBuff = new BufferedReader(new FileReader(resultsFile));
		String newContents="";
		String line2;
		while((line2 = postBuff.readLine())!=null)
		{
			newContents = newContents.concat(line2);
		}
		postBuff.close();
		
		// This is a correct sample output based on the above input. 
		final String expected= "<edge id=\"e0\" source=\"n0\" target=\"n2\">"+
		"<edge id=\"e2\" source=\"n1\" target=\"n3\">"+
		"<edge id=\"e3\" source=\"n3\" target=\"n2\"/>"+
		"<edge id=\"e4\" source=\"n2\" target=\"n4\"/>"+
		"<edge id=\"e5\" source=\"n3\" target=\"n5\"/>"+
		"<edge id=\"e6\" source=\"n5\" target=\"n4\">";
		
		assertArrayEquals(null, new String[]{expected} , new String[]{newContents});
	}

	@Test
	public void testParse() throws Exception 
	{
		// file object reference before the parse method
		File before = testFile2;
		
		// read the contents of the file to a string before parsing
		BufferedReader preBuff = new BufferedReader(new FileReader(testFile2));
		String line, previousContents = "";
		while((line = preBuff.readLine())!=null)
		{
			previousContents = previousContents.concat(line);
		}
		preBuff.close();
		
		// run the parse method
		graphML.parse(inputDir);
		
		// read the contents of the file to a string after parsing
		BufferedReader postBuff = new BufferedReader(new FileReader(testFile2));
		String newContents="";
		String line2;
		while((line2 = postBuff.readLine())!=null)
		{
			newContents = newContents.concat(line2);
		}
		postBuff.close();
	
		// file object reference after the parse method
		File after = testFile2;
		
		// assert true that the references remained the same therefore this is the same file
		assertTrue(before.getAbsolutePath().equalsIgnoreCase(after.getAbsolutePath()));
		// assert true that the contents and therefore the file have no changed from before parsing.
		assertTrue(!previousContents.equalsIgnoreCase(newContents));
	}

}
