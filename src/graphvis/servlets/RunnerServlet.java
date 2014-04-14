package graphvis.servlets;

import graphvis.hdfs.HDFSFileClient;
import graphvis.shellUtils.ShellBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

/**
 * Servlet implementation class RunnerServlet
 */
@WebServlet("/RunnerServlet")
public class RunnerServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public static String hdfsWorkingDirectory  = "graphvis-output";
       
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
		String tempDirectory = UploadServlet.tempDirectory;
		
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
	        hc.moveFromHdfs(hdfsWorkingDirectory, tempDirectory + File.separator);
	        
	        // displays computationComplete.jsp page after upload finished
	        getServletContext().getRequestDispatcher("/computationComplete.jsp").forward(
	                request, response);
		}
        /*catch (FileNotFoundException ex) 
        {
        	getServletContext().setAttribute("errorMessage", ex);
            response.sendRedirect("/error.jsp");
        } 
        catch (IOException ex) 
        {
        	getServletContext().setAttribute("errorMessage", ex);
            response.sendRedirect("/error.jsp");
        }*/
        catch (Exception ex)
        {
            throw new ServletException(ex);
		}
	} 
	

	
	

}
