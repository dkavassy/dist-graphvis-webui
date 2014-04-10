package graphvis.servlets;

import graphvis.io.parsers.OutputParser;

import java.io.IOException;

import javax.servlet.ServletException;
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
		System.out.println("Download request GOT...combining now");
		OutputParser op = new OutputParser();
		try 
		{
			op.parseAndCombine("/home/james/crashBunker/conglom/", getServletContext().getContextPath().concat("/CombinedResults.txt"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		System.out.println("Redirecting now..");
		
		response.sendRedirect("/home/james/crashBunker/conglom/CombinedResults.txt");
	}


}
