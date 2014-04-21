/*
* @(\#) RunnerServlet.java 1.1 28 March 14
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
import graphvis.webui.hdfs.HDFSFileClient;
import graphvis.webui.shellUtils.ShellBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RunnerServlet

 * <p>
 * @author James Pierce
 * @version 1.1
 */
@WebServlet("/RunnerServlet")
public class RunnerServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public RunnerServlet() 
    {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("SERVLET ACTIVATED!");

		// workers given here
		// computation given here
		String      fileName = (String) getServletContext().getAttribute("fileName");
		String       fileExt = (String) getServletContext().getAttribute("fileExtension");
		String tempDirectory = Configuration.tempDirectory;
		
		System.out.println("Temp directory to use is " + tempDirectory );
		
		String compute = request.getParameter("algoChoice");
		int    workers = Integer.parseInt(request.getParameter("workers"));
		
		try
		{
			String command = ShellBuilder.scriptBuilder(fileExt, fileName, compute, workers);
			
			System.out.println(command);
			
			String giraphRunnerFilePath = tempDirectory + File.separator + "GiraphShellRunnerTemp.sh";
			PrintWriter shellRunnerTemp = new PrintWriter(giraphRunnerFilePath);			
			
			shellRunnerTemp.println(command);
			shellRunnerTemp.close();
			System.out.println("shellRunner finished");
	
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", giraphRunnerFilePath);
			pb.directory(new File(tempDirectory.replaceAll(fileName,"")));
			
			System.out.println("Process built for " +giraphRunnerFilePath + "....pb: "+ pb.toString());
			Process process = pb.start();
			System.out.println("Process started....");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		        System.out.println(line);
		    }
		    		    
	        System.out.println("Passed the buffered stream..");
	        HDFSFileClient hc = new HDFSFileClient();
	        hc.moveFromHdfs(Configuration.hdfsWorkingDirectory, tempDirectory + File.separator);
	        
	        // displays computationComplete.jsp page after upload finished
	        getServletContext().getRequestDispatcher("/computationComplete.jsp").forward(
	                request, response);
		}
        catch (Exception ex)
        {
            throw new ServletException(ex);
		}
	} 
	

	
	

}
