package graphvis.servlets;

import graphvis.engine.FruchtermanReingoldGraphVis;
import graphvis.engine.GraphvisMasterCompute;
import graphvis.hdfs.HDFSFileClient;
import graphvis.io.CSVEdgeInputFormat;
import graphvis.io.GMLEdgeInputFormat;
import graphvis.io.GraphMLEdgeInputFormat;
import graphvis.io.SVGVertexOutputFormat;
import graphvis.shellUtils.ShellBuilder;

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

import org.apache.giraph.GiraphRunner;
import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.giraph.io.EdgeInputFormat;
import org.apache.giraph.io.formats.GiraphFileInputFormat;
import org.apache.giraph.job.GiraphJob;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * Servlet implementation class RunnerServlet
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
		String fileName = (String) getServletContext().getAttribute("fileName");
		String fileExt = (String) getServletContext().getAttribute("fileExtension");
		String tempDirectory = (String) getServletContext().getAttribute("tempDirectory");
		
		System.out.println("Temp directory to use is " + tempDirectory );
		
		String compute = request.getParameter("algoChoice");
		int workers = Integer.parseInt(request.getParameter("workers"));
		
		String command = "";
		try {
			command = ShellBuilder.scriptBuilder(fileExt, fileName, compute, workers);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(command);
		
		String giraphRunnerFilePath = tempDirectory.replaceAll(fileName,"GiraphShellRunnerTemp.sh");
		PrintWriter shellRunnerTemp = new PrintWriter(giraphRunnerFilePath);			
		
		shellRunnerTemp.println(command);
		shellRunnerTemp.close();
		System.out.println("shellRunner finished");

		ProcessBuilder pb = new ProcessBuilder("/bin/bash",giraphRunnerFilePath);
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
	    
	    boolean failed = false;
	    BufferedReader ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	    String eline;
	    while ((eline = ebr.readLine()) != null) 
	    {
	        System.out.println(eline);
	        failed = true;
	    }
        
	    if(failed)
	    {
	    	try {
				throw new Exception("Computation has failed, check input file or refer FAQs for reasons why");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
        System.out.println("Passed the buffered stream..");
        HDFSFileClient hc = new HDFSFileClient();
        hc.moveFromHdfs("output/graphvis/", tempDirectory.replaceAll(fileName, ""));
        
        // displays computationComplete.jsp page after upload finished
        getServletContext().getRequestDispatcher("/computationComplete.jsp").forward(
                request, response);
	} 
	

	
	

}
