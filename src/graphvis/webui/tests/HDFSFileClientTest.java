package graphvis.webui.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import graphvis.webui.hdfs.HDFSFileClient;

import org.apache.hadoop.fs.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HDFSFileClientTest 
{
	HDFSFileClient dfs;

	File localFile;
	ProcessBuilder pb;
	
	@Before
	public void setUp() throws Exception 
	{
		localFile = new File("testLocalFile.txt");
		localFile.createNewFile();
		FileWriter fw = new FileWriter(localFile);
		fw.write("Test Contents of a local file!");
		fw.close();
		
		// Note you must start dfs first to get these tests to work.
		dfs = new HDFSFileClient();
		System.out.println(dfs.fileSystem.getStatus().toString());
		
	}

	@After
	public void tearDown() throws Exception 
	{
		
		dfs.fileSystem.delete(new Path("./testLocalFile.txt"), true);
		localFile.delete();
	}

	@Test
	public void testMovers() throws FileNotFoundException, IllegalArgumentException, IOException 
	{
		File folder = new File(".");
		
		if (folder.isDirectory())
		{
			File[] files = folder.listFiles(new FilenameFilter() 
			{	
				@Override
				public boolean accept(File dir, String name) 
				{
					if (name.equalsIgnoreCase("testLocalFile.txt"))
					{
						return true;
					}
					return false;
				}
			});
			
			for (File f: files)
			{
				System.out.println("File found: " + f.getName());
			}
			
			
			// test moving file into HDFS
			try
			{
				System.out.println("File is present in dfs: "
						+dfs.fileSystem.getFileStatus(new Path("./testLocalFile.txt")).isFile());
				fail();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				assertTrue(true);
			}
					
			dfs.moveFromLocal("./testLocalFile.txt", ".");

			try
			{
				System.out.println("File is present in dfs: "
						+dfs.fileSystem.getFileStatus(new Path("./testLocalFile.txt")).isFile());
				assertTrue(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				fail();
			}
			
			// now check it is no longer in local directory
			if (folder.isDirectory())
			{
				File[] files2 = folder.listFiles(new FilenameFilter() 
				{	
					@Override
					public boolean accept(File dir, String name) 
					{
						if (name.equalsIgnoreCase("testLocalFile.txt"))
						{
							return true;
						}
						return false;
					}
				});
				boolean foundFlag = false;
				
				for (File f: files2)
				{
					System.out.println("File found: " + f.getName());
					foundFlag = true;
				}
				
				// this should remain false as the file should not be in local
				assertFalse(foundFlag);
			}
			
			dfs.moveFromHdfs("./testLocalFile.txt", ".");
			
			if (folder.isDirectory())
			{
				File[] files3 = folder.listFiles(new FilenameFilter() 
				{	
					@Override
					public boolean accept(File dir, String name) 
					{
						if (name.equalsIgnoreCase("testLocalFile.txt"))
						{
							return true;
						}
						return false;
					}
				});
				boolean foundFlag = false;
				
				for (File f: files3)
				{
					System.out.println("File found: " + f.getName());
					foundFlag = true;
				}
				
				// this should remain false as the file should not be in local
				assertTrue(foundFlag);
			}
		}
		
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testFileNotFound() throws FileNotFoundException, IllegalArgumentException, IOException 
	{
		dfs.moveFromLocal("./testLocalFile.txt", "/nonexistent");
	}

	
}
