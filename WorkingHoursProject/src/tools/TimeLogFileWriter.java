package tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TimeLogFileWriter
{

	private final File _txtFile;

	public TimeLogFileWriter(File txtFile)
	{
		_txtFile = txtFile;
	}

	public void writeContent(List<String> content)
	{
		try
		{
			if(!_txtFile.exists())
			{
				_txtFile.createNewFile();
			}
			
			Files.write(Paths.get(_txtFile.getAbsolutePath()), content, Charset.forName("UTF-8"), StandardOpenOption.WRITE);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
