/*
* @(\#) GMLTest.java 1.1 28 March 14
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
import graphvis.webui.parsers.GMLParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**

 * <p>
 * @author James Pierce
 * @version 1.1
 */
public class GMLTest {

	GMLParser gML = new GMLParser();
	BufferedReader br;
	PrintWriter outWriter;
	File testFile;
	File resultsFile;

	
	@Before
	public void setUp() throws Exception 
	{
		final String rawData  = "graph"+ "\n"+
								"["+"\n"+
								" node"+"\n"+
								" ["+"\n"+
								"  id 1"+"\n"+
								" ]"+"\n"+
								" node"+"\n"+
								" ["+"\n"+
								"  id 2"+"\n"+
								" ]"+"\n"+
								" node"+"\n"+
								" ["+"\n"+
								"  id 3"+"\n"+
								" ]"+"\n"+
								" node"+"\n"+
								" ["+"\n"+
								"  id 4"+"\n"+
								" ]"+"\n"+
								" node"+"\n"+
								" ["+"\n"+
								"  id 5"+"\n"+
								" ]"+"\n"+
								"  edge"+"\n"+
								" ["+"\n"+
								"  source 2"+"\n"+
								"  target 1"+"\n"+
								" ]"+"\n"+
								" edge"+"\n"+
								" ["+"\n"+
								"  source 3"+"\n"+
								"  target 1"+"\n"+
								" ]"+"\n"+
								" edge"+"\n"+
								" ["+"\n"+
								"  source 4"+"\n"+
								"  target 1"+"\n"+
								" ]"+"\n"+
								" edge"+"\n"+
								" ["+"\n"+
								"  source 5"+"\n"+
								"  target 1"+"\n"+
								" ]"+"\n"+
								" edge"+"\n"+
								" ["+"\n"+
								"  source 2"+"\n"+
								"  target 5"+"\n"+
								" ]"+"\n"+
								" edge"+"\n"+
								" ["+"\n"+
								"  source 3"+"\n"+
								"  target 5"+"\n"+
								"  ]"+"\n"+
								"]";
		
		gML = new GMLParser();
		testFile = new File("testFile.gml");
		testFile.createNewFile();
		resultsFile = new File("resultsFile.gml");
		resultsFile.createNewFile();
		
		FileWriter fw = new FileWriter(testFile.getAbsolutePath());
		fw.write(rawData);
		fw.close();
		
	}

	@After
	public void tearDown() throws Exception 
	{
		// remove temporary test files post execution
		resultsFile.delete();
		testFile.delete();
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
		
		gML.parseMethod(br, outWriter);
		
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
		final String expected= " edge [ source 2 target 1 ] "+
			 "edge [ source 3 target 1 ] "+
			 "edge [ source 4 target 1 ] "+
			 "edge [ source 5 target 1 ] "+
			 "edge [ source 2 target 5 ] "+
			 "edge [ source 3 target 5 ] ]";
		
		assertArrayEquals(null, new String[]{expected} , new String[]{newContents});
	}

}
