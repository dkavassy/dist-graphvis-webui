/*
* @(\#) GraphMLParser.java 1.1 28 March 14
*
* Copyright (\copyright) 2014 University of York & British Telecommunications plc
* This Software is granted under the MIT License (MIT)

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
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class extends the abstract parent {@link InputParser} class for parsing GraphML input files
 * into a format which the graphvis giraph engine can use for its  computation.
 * <p>
 * @author James Pierce
 * @version 1.1
 *
 */
public class GraphMLParser extends InputParser
{
	@Override
	public void parseMethod(BufferedReader br, PrintWriter outFile) throws IOException 
	{
		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.contains("<edge"))
			{
				// write edge tag contents to output file.
				outFile.println(line);
			}
		}
	}
	
}
