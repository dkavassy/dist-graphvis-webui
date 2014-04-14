package graphvis.webui.tests;

import static org.junit.Assert.*;
import graphvis.webui.parsers.OutputParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OutputParserTest {
	
	File partFileFolder,combinedFile,partFile,partFile2;
	OutputParser op = new OutputParser();
	
	@Before
	public void setUp() throws Exception {
		
		partFileFolder = new File("Outputtest");
		partFileFolder.mkdir();
		
		//create new 
		partFile = new File(partFileFolder + "/part-m-test.txt");
		partFile.createNewFile();
		FileWriter fw = new FileWriter(partFile);
		fw.write("Test");
		fw.close();
		
		partFile2 = new File(partFileFolder + "/part-m-test2.txt");
		partFile2.createNewFile();
		FileWriter fw2 = new FileWriter(partFile2);
		fw2.write("1");
		fw2.close();
		
		//create new 
		File otherFile = new File(partFileFolder + "/part-m-test.crc");
		otherFile.createNewFile();
		
		//create new 
		File otherFile2 = new File(partFileFolder + "/test");
		otherFile2.createNewFile();
	}

	@After
	public void tearDown() throws Exception {

		partFile.delete();
		partFile2.delete();
		partFileFolder.delete();	
	}

	@Test
	public void testParseAndCombine() throws FileNotFoundException, IOException {
		op.parseAndCombine(partFileFolder.getAbsolutePath(), "CombinedFile");
		combinedFile = new File("CombinedFile");
		assertTrue(combinedFile.exists());
		@SuppressWarnings("resource")
		String text = new Scanner( combinedFile).useDelimiter("\\A").next();
		String expected = "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n"
				+ "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
				+ "<script xlink:href=\"SVGPan.js\"/>\n"
				+ "<g id=\"viewport\" transform=\"translate(200,200)\">\n\n"
				+"1\nTest\n"
				+"</g>\n</svg>\n";
		combinedFile.delete();
		assertEquals(expected,text);
	}

	@Test(expected=FileNotFoundException.class)
	public void testNotDirectory() throws FileNotFoundException, IOException {
		op.parseAndCombine(partFile.getAbsolutePath(), "irrelevant");
	}
}
