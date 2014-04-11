package graphvis.io.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;

public class OutputParser 
{
	public static void main(String[] args) throws Exception 
	{
		OutputParser op = new OutputParser();
		op.parseAndCombine("/home/james/crashBunker/conglom", "/home/james/crashBunker/conglom/CombinedResults.txt");
	}
	public void parseAndCombine(String inputFolder, String outputFile) throws Exception
	{
		File folder = new File(inputFolder);
		
		PrintWriter outFile = new PrintWriter(new File(outputFile));
		
		if (folder.isDirectory())
		{
			File[] fileParts = folder.listFiles(new FilenameFilter() 
			{	
				@Override
				public boolean accept(File dir, String name) 
				{
					if (name.contains("part-m") && !name.endsWith("crc"))
					{
						return true;
					}
					return false;
				}
			});
			
			// Insert SVG/HTML+SVG Headers here
			outFile.println("<svg xmlns=\"http://www.w3.org/2000/svg\""+
					" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"10000\" height =\"10000\" >\n");
			
			for (File part: fileParts )
			{
				System.out.println("Name of file is " + part.toString());
				BufferedReader br = new BufferedReader(new FileReader(part));
				String line;
				while((line = br.readLine())!=null)
				{
					outFile.println(line);
				}
				br.close();
			}
			
			outFile.println("</svg>");
			outFile.close();
		}
		else
		{
			throw new Exception("Invalid outputFolder given!");
		}
	}

}
