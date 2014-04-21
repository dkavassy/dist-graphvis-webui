/*
* @(\#) DownloadServlet.java 1.1 28 March 14
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
package graphvis.webui.servlets;

import graphvis.webui.config.Configuration;
import graphvis.webui.parsers.OutputParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadServlet
 * 

 * <p>
 * @author James Pierce
 * @version 1.1
 */
 
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadServlet() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		String tempDirectory = Configuration.tempDirectory;
		String resultFile = tempDirectory + "/CombinedResults.txt";
		
		System.out.println("Download request GOT...combining now");
		
		OutputParser op = new OutputParser();
		op.parseAndCombine(tempDirectory + File.separator + Configuration.hdfsWorkingDirectory, resultFile);
		
		System.out.println("Redirecting now..");
		
		// Send SVG file
		response.setContentType("image/svg+xml");
		
		File file = new File(resultFile);
		response.setContentLength((int) file.length());
		
		FileInputStream  fileIn = new FileInputStream(file);
		ServletOutputStream out = response.getOutputStream();
		
		byte[] outputByte = new byte[1];
		while(fileIn.read(outputByte) != -1)
		{
			out.write(outputByte);
		}
		fileIn.close();
		
		out.flush();
		out.close();
		
	}


}
