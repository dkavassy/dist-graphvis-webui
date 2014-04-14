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
