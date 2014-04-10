/*
* @(\#) GMLParser.java 1.1 28 March 14
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

package graphvis.io.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class extends the abstract parent {@link InputParser} class for parsing GML input files
 * into a format which the graphvis giraph engine can use for its  computation.
 * <p>
 * @author James Pierce
 * @version 1.1 
 */
public class GMLParser extends InputParser
{
	@Override
	public void parseMethod(BufferedReader br, PrintWriter outFile)	throws IOException 
	{
		String line;
		boolean flag = false;
		while ((line = br.readLine()) != null) 
		{
			line = line.trim();

			if (line.equals("edge")||flag)
			{
				if (flag && line.equals("edge"))
				{
					// the end edge marker is found so we set flag to false to close 
					// collection until the next edge marker
					flag = false;
					outFile.print("\n");
				}
				
				// if line contains edge, set flag to true enabling all 
				// lines between this and the next edge marker to be collected
				flag = true;
				outFile.print(" " + line);
			}	
		}
		
	}
	
	

}
