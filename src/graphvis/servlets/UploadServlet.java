/*
* @(\#) UploadServlet.java 1.1 28 March 14
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

package graphvis.servlets;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import graphvis.hdfs.HDFSFileClient;
import graphvis.io.parsers.GMLParser;
import graphvis.io.parsers.GraphMLParser;

/**
 * This servlet class provides a back end to the index.jsp page
 * and loads the uploaded graph, processes it and proceeds to next page.
 * <p>
 * @author James Pierce
 * @version 1.1
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	// Application caters for very large files.
    private static final int MAX_MEMORY_SIZE  = 1024 * 1024 * 1024 * 5;
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 1024 * 5;
    
    public static final String tempDirectory = System.getProperty("java.io.tmpdir") + "/graphvis";

    /**
     * This method receives POST from the index.jsp page and uploads file, 
     * converts into the correct format then places in the HDFS.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {

        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) 
        {
        	response.setStatus(403);
            response.sendRedirect("/error.jsp");
            return;
        }
        
        File tempDirFileObject = new File(tempDirectory);
        
        // Create/remove temp folder
        if (tempDirFileObject.exists())
        {
        	FileUtils.deleteDirectory(tempDirFileObject);
        }
        
        // (Re-)create temp directory
        tempDirFileObject.mkdir();
        FileUtils.copyFile(
        		new File(getServletContext().getRealPath("giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar")),
        		new File(tempDirectory + "/giraph-1.1.0-SNAPSHOT-for-hadoop-2.2.0-jar-with-dependencies.jar")
        		);
        FileUtils.copyFile(
        		new File(getServletContext().getRealPath("dist-graphvis.jar")),
        		new File(tempDirectory + "/dist-graphvis.jar")
        		);

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Sets the size threshold beyond which files are written directly to
        // disk.
        factory.setSizeThreshold(MAX_MEMORY_SIZE);

        // Sets the directory used to temporarily store files that are larger
        // than the configured size threshold. We use temporary directory for
        // java
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Set overall request size constraint
        upload.setSizeMax(MAX_REQUEST_SIZE);

        String fileName = "";
        try 
        {
            // Parse the request
            List<?>    items = upload.parseRequest(request);
            Iterator<?> iter = items.iterator();
            while (iter.hasNext()) 
            {
                FileItem item = (FileItem) iter.next();

                if (!item.isFormField())
                {
                    fileName = new File(item.getName()).getName();
                    String filePath = tempDirectory + File.separator + fileName;
                    File uploadedFile = new File(filePath);
                    System.out.println(filePath);
                    // saves the file to upload directory
                    item.write(uploadedFile);
                }
            }

            String fullFilePath = tempDirectory + File.separator + fileName;
            
            String extension = FilenameUtils.getExtension(fullFilePath);

            // Load Files intoHDFS
            // (This is where we do the parsing.)
            loadIntoHDFS(fullFilePath, extension);

            getServletContext().setAttribute("fileName",      new File(fullFilePath).getName());
    		getServletContext().setAttribute("fileExtension", extension);
    		
    		// Displays fileUploaded.jsp page after upload finished
            getServletContext().getRequestDispatcher("/fileUploaded.jsp").forward(
                    request, response);
            
        }
        catch (FileUploadException ex) 
        {
            throw new ServletException(ex);
        }
        catch (FileNotFoundException ex) 
        {
        	getServletContext().setAttribute("errorMessage", ex);
            response.sendRedirect("/error.jsp");
        } 
        catch (IOException ex) 
        {
        	getServletContext().setAttribute("errorMessage", ex);
            response.sendRedirect("/error.jsp");
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
		}

    }
    
    /**
     * This method takes the uploaded file and pre-processes
     * where necessary and then inputs into the HDFS.
     * 
     * @param fullFilePath is the {@link String} representation of the
     * full file path of the file to be processed.
     * @param extension is the {@link String} representation of the file extension.
     * @throws Exception
     */
    private void loadIntoHDFS(String fullFilePath, String extension) throws IOException
    {
    	// Choose appropriate procedure based on extension.
        if (extension.equalsIgnoreCase("csv"))	
        {
        	// No problem - just upload it
        	HDFSFileClient hfc = new HDFSFileClient();
            hfc.moveFromLocal(fullFilePath, "."); 
            System.out.println("File has been uploaded to server");
        }
        else if (extension.equalsIgnoreCase("gml"))
        {
        	System.out.println("GML Format entered");
        	GMLParser gmlParser = new GMLParser();
        	gmlParser.parse(fullFilePath);
        	
        	HDFSFileClient hfc = new HDFSFileClient();
            hfc.moveFromLocal(fullFilePath, "."); 
            System.out.println("GML File has been parsed and uploaded to server"); 
        }
        else if (extension.equalsIgnoreCase("graphml"))
        {
        	System.out.println("GraphML Format entered");
        	GraphMLParser graphmlParser = new GraphMLParser();
        	graphmlParser.parse(fullFilePath);
        	
        	HDFSFileClient hfc = new HDFSFileClient();
            hfc.moveFromLocal(fullFilePath, "."); 
            System.out.println("GraphML File has been parsed and uploaded to server");            	
        }
        else
        {
        	// invalid input file has been uploaded - throw below exception.
        	throw new IOException("Invalid input files: Please check input file extension - valid formats are .csv .gml .graphml ");
        }
    }

}
