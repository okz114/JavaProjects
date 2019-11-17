package tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class TimeTable
{
	public final String _header;

	private List<TimeTableRow> _rows;

	private String _istRow;

	private String _sollRow;

	private String _overRow;

	public  String _separator;

	private int _todaysRowIndx;

	private static final String[] COL_NAMES = {"Tag", "Start-Zeit", "Ende-Zeit", "Pause", "Gesamt Arbeitszeit"};

	private static final Calendar CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);

	private static final double WORKING_HOURS = 8.0;

	public TimeTable()
	{
		_header = String.format(" %1$5s | %2$-10s | %3$-10s | %4$-5s | %5$-10s", COL_NAMES[0], COL_NAMES[1], COL_NAMES[2], COL_NAMES[3], COL_NAMES[4]);

		initializeSeparator();

		_istRow = String.format(" %1$39s | %2$-10s", "Ist Stunden", "");

		_sollRow = String.format(" %1$39s | %2$-10s", "Soll Stunden", "");

		_overRow = String.format(" %1$39s | %2$-10s", "Ueberstunden", "");

		_rows = initializeRows();
		_todaysRowIndx = getCurrentDaysIndx();
	}

	public TimeTable(final List<String> content)
	{

		_header = content.get(0);
		_separator = content.get(1);
		List<String> rowContent = content.subList(2, (content.size() - 6));
		getRowsfromContent(rowContent);
		_istRow = content.get(content.size() - 5);
		_sollRow = content.get(content.size() - 3);
		_overRow = content.get(content.size() - 1);
		_todaysRowIndx = getCurrentDaysIndx();
	}

	private int getCurrentDaysIndx()
	{
		for (int indRow = 0; indRow < _rows.size(); indRow++)
		{
			if(_rows.get(indRow).getDay().equals(CalendarUtil.getDay()))
			{
				return indRow;
			}
		}
		return -1;
	}


	public TimeTableRow getCurrentRow()
	{
		return _rows.get(_todaysRowIndx);
	}
	
	private void updateCalculations()
	{
		double totalIstTime = 0.0;
		double totalSollTime = 0.0;
		for (int indRow = 0; indRow <= _todaysRowIndx; indRow++)
		{
			if (!_rows.get(indRow).getTotalTime().isEmpty())
			{
				totalIstTime = totalIstTime + Double.valueOf(_rows.get(indRow).getTotalTime().replace(",", "."));
				totalSollTime = totalSollTime + WORKING_HOURS;
			}
		}

		double overTime = totalIstTime - totalSollTime;

		_istRow = String.format(" %1$39s | %2$.2f", "Ist Stunden", totalIstTime);

		_sollRow = String.format(" %1$39s | %2$.2f", "Soll Stunden", totalSollTime);

		_overRow = String.format(" %1$39s | %2$.2f", "Ueberstunden", overTime);

	}

	private List<TimeTableRow> initializeRows()
	{
		List<TimeTableRow> rows = new ArrayList<TimeTableRow>();

		int year = CALENDAR.get(Calendar.YEAR);
		int month = CALENDAR.get(Calendar.MONTH);
		int maxDay = CALENDAR.getActualMaximum(Calendar.DATE);

		for (int indDay = 1; indDay <= maxDay; indDay++)
		{
			Calendar cal1 = (Calendar) Calendar.getInstance().clone();
			cal1.set(year, month, indDay);
			rows.add(new TimeTableRow(String.format("%1$td-%1$ta", cal1), "", "", "", ""));
		}

		return rows;
	}

	private void getRowsfromContent(List<String> rowContentList)
	{
		_rows = new ArrayList<TimeTableRow>();
		for (int indRow = 0; indRow < rowContentList.size(); indRow = indRow + 2)
		{
			String[] rowContent = rowContentList.get(indRow).split("\\|");
			if (rowContent.length == COL_NAMES.length)
			{
				_rows.add(new TimeTableRow(rowContent[0], rowContent[1], rowContent[2], rowContent[3], rowContent[4]));
			}			
		}

	}

	private void initializeSeparator() {
		StringBuilder strBldr = new StringBuilder();
		strBldr.append("+");
		for (int i = 0; i < _header.length(); i++)
		{
			strBldr.append("-");			
		}
		strBldr.append("+");
		_separator = strBldr.toString();
	}

	public List<String> toTemplate()
	{
		List<String> content = new ArrayList<String>();
		content.add(_header);
		content.add(_separator);

		for (int indRow = 0; indRow < _rows.size(); indRow++)
		{
			content.add(_rows.get(indRow).toString());
			content.add(_separator);
		}

		content.add(_istRow);
		content.add(_separator);
		content.add(_sollRow);
		content.add(_separator);
		content.add(_overRow);
		return content;
	}

	public void setWorkingHours()
	{
		if ( _rows.get(_todaysRowIndx).getStartingTime().isEmpty())
		{
			_rows.get(_todaysRowIndx).setStartTime();
		}
		else
		{
			_rows.get(_todaysRowIndx).setEndTime();
		}
		updateCalculations();
		
	}

	public String getCurrentDay()
	{
		return _rows.get(_todaysRowIndx).getDay();
	}
	
	public void startPause(boolean userdefined)
	{
		_rows.get(_todaysRowIndx).startPause(userdefined);
	}
	
	public void stopPause()
	{
		_rows.get(_todaysRowIndx).stopPause();
	}

	public boolean isInPause()
	{
		
		return _rows.get(_todaysRowIndx).isInPause();
	}

	public String getPauseTime()
	{
		// TODO Auto-generated method stub
		return _rows.get(_todaysRowIndx).getPauseTime();
	}

	public boolean isUserDefinedPause() {
		// TODO Auto-generated method stub
		return _rows.get(_todaysRowIndx).isUserDefinedPause();
	}

	public String getTotalTime() {
		// TODO Auto-generated method stub
		return _rows.get(_todaysRowIndx).getTotalTime();
	}
}
