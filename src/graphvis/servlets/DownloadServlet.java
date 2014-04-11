package graphvis.servlets;

import graphvis.io.parsers.OutputParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.hdfs.server.namenode.FileChecksumServlets.GetServlet;

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
		String fileName = (String) getServletContext().getAttribute("fileName");
		String tempDirectory = (String) getServletContext().getAttribute("tempDirectory");
		String resultFile = tempDirectory.replaceAll(fileName,"CombinedResults.txt");
		System.out.println("Download request GOT...combining now");
		OutputParser op = new OutputParser();
		try 
		{
			//op.parseAndCombine(tempDirectory.replaceAll(fileName, "graphvis"), resultFile);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("Redirecting now..");
		
		//response.sendRedirect(resultFile);
		response.setContentType("text/xml");
		File file = new File(resultFile);
		FileInputStream fileIn = new FileInputStream(file);
		ServletOutputStream out = response.getOutputStream();
		byte[] outputByte = new byte[4096];
		while(fileIn.read(outputByte, 0, 4096) != -1)
		{
			out.write(outputByte, 0, 4096);
		}
		fileIn.close();
		out.flush();
		out.close();
		
	}


}
