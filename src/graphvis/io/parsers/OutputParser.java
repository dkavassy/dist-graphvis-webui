package graphvis.io.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputParser 
{

	public void parseAndCombine(String inputFolder, String outputFile) throws FileNotFoundException, IOException
	{
		File folder = new File(inputFolder);
		
		File outputFileObject = new File(outputFile);
		
		outputFileObject.createNewFile();
		
		PrintWriter outFile = new PrintWriter(outputFileObject);
		
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
			outFile.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n"
					+ "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n"
					+ "<script xlink:href=\"SVGPan.js\"/>\n"
					+ "<g id=\"viewport\" transform=\"translate(200,200)\">\n");
			
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
			
			outFile.println("</g>\n</svg>");
			outFile.close();
		}
		else
		{
			throw new FileNotFoundException("Invalid outputFolder given!");
		}
	}

}
