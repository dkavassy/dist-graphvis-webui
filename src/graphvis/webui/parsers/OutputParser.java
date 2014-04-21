/*
* @(\#) OutputParser.java 1.1 28 March 14
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
/**

 * <p>
 * @author James Pierce
 * @version 1.1
 */
public class OutputParser 
{

	public void parseAndCombine(String inputFolder, String outputFile) throws FileNotFoundException, IOException
	{
		File folder = new File(inputFolder);
		
		File outputFileObject = new File(outputFile);
		
		outputFileObject.createNewFile();
		
		PrintWriter outFile = new PrintWriter(outputFileObject);
		
		if (folder.isDirectory())
		{
			File[] fileParts = folder.listFiles(new FilenameFilter() 
			{	
				@Override
				public boolean accept(File dir, String name) 
				{
					if (name.contains("part-m") && !name.endsWith("crc"))
					{
						return true;
					}
					return false;
				}
			});
			
			// Insert SVG/HTML+SVG Headers here
			outFile.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n"
					+ "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
					+ "<script xlink:href=\"SVGPan.js\"/>\n"
					+ "<g id=\"viewport\" transform=\"translate(200,200)\">\n");
			
			for (File part: fileParts )
			{
				System.out.println("Name of file is " + part.toString());
				BufferedReader br = new BufferedReader(new FileReader(part));
				String line;
				
				while((line = br.readLine())!=null)
				{
					outFile.println(line);
				}
				
				br.close();
			}
			
			outFile.println("</g>\n</svg>");
			outFile.close();
		}
		else
		{
			outFile.close();
			throw new FileNotFoundException("Invalid outputFolder given!");
		}
	}

}
