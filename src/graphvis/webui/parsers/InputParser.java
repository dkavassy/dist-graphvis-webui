/*
* @(\#) InputParser.java 1.1 28 March 14
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

package graphvis.webui.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This abstract class is to be used as the basis for subclasses to write
 * parsers for Graph formats. The class contains one abstract method and
 * a parse(..) method which uses the abstract method.
 * <p>
 * @author James Pierce
 * @version 1.1
 */
public abstract class InputParser 
{
	// parseMethod to be implemented/overriden by all subclasses
	public abstract void parseMethod(BufferedReader br, PrintWriter outFile) throws IOException;
	
	/**
	 * This method takes a input file and parses it into a useful format
	 * for the graphvis project. The parsed output is then effectively overwritten
	 * over the original input file for the user to access. Therefore once parsed,
	 * there is no original document left but only the parsed version. Note that parsing
	 * is performed by the abstract method parseMethod(..) and therefore the subclasses
	 * control how the file is technically parsed.
	 * 
	 * @param inputDir is the directory where the user specifies the full path of 
	 * the file to be parsed.
	 * @throws IOException 
	 * @throws Exception 
	 */
	public void parse(String inputDir) throws IOException
	{
		File inputFile = new File (inputDir);
		// new temporary file has string flag *gv-temp* added to identify it from the original
		String tempFileName = inputDir.concat("*gv-temp*");
		// write output to temporary file
		PrintWriter outFile = new PrintWriter(tempFileName);
		// Open a buffered reader as an input stream for the pre-parsed file.
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		// abstract method to be overriden by subclasses
		parseMethod(br, outFile);
		
		// close input and output file streams
		br.close();
		outFile.close();
		
		// clean up files:
		// delete original input file as this is overwriting it
		inputFile.delete();
		
		// rename new input file to be the old one
		File outputFile = new File(tempFileName);
		outputFile.renameTo(new File(inputDir));
	}
}
