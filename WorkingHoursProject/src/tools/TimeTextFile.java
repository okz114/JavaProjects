package tools;

import java.io.File;
import java.io.IOException;

public class TimeTextFile
{

	protected TimeLogFileReader _reader;

	protected TimeLogFileWriter _writer;

	private File _file; 

	private TimeTable _timeTable;

	private String _currentMonth;
	
	private String _currentYear;
	
	protected static final File MAIN_FOLDER = new File("C:\\users\\" + System.getProperty("user.name") + "\\WorkingHours");
	

	public TimeTextFile()
	{
		
		if(!MAIN_FOLDER.exists())
		{
			MAIN_FOLDER.mkdirs();
		}

		_currentMonth = CalendarUtil.getMonthName();
		
		_currentYear = CalendarUtil.getYear();
		
		initializeFile();
	}


	private void initializeFile()
	{
		String filename =  _currentMonth + "_" + _currentYear + ".txt";
		_file = new File(MAIN_FOLDER, filename);
		
		if (_file.exists())
		{
			_reader = new TimeLogFileReader(_file);
			_writer = new TimeLogFileWriter(_file);
			_timeTable = new TimeTable(_reader.getContent());
		}
		else
		{
			try
			{
				_file.createNewFile();
				_reader = new TimeLogFileReader(_file);
				_writer = new TimeLogFileWriter(_file);
				_timeTable = new TimeTable();
				_writer.writeContent(_timeTable.toTemplate());
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}


	public String getFileAbsPath()
	{
		return _file.getAbsolutePath();
	}


	public void updateWorkingHours()
	{
		_timeTable.setWorkingHours();
		_writer.writeContent(_timeTable.toTemplate());
	}
	
	public TimeTable getTimeTable()
	{
		return _timeTable;
	}
	
	public String getCurrentDay()
	{
		return _timeTable.getCurrentDay();
	}

	public String getCurrentMonth()
	{
		return _currentMonth;
	}
	
	public String getCurrentYear()
	{
		return _currentYear;
	}
}
