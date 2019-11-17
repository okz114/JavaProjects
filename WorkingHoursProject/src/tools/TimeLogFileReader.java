package tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TimeLogFileReader
{

	private final File _txtFile;

	private List<String> _content;
	
	public TimeLogFileReader(File txtFile)
	{
		_txtFile = txtFile;	
		readFile();
	}
	
	private void readFile() 
	{
		List<String> content = null;
		try
		{ 
			content = Files.readAllLines(Paths.get(_txtFile.getAbsolutePath()), StandardCharsets.UTF_8); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		
		_content = new ArrayList<String>();
		for (String con : content)
		{
			if (!con.isEmpty() || !con.trim().isEmpty())
			{
				_content.add(con);
			}
		}
	} 
	
	
	public List<String> getContent()
	{
		return _content;
	}
	
}
