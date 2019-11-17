package tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarUtil 
{

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	
	
	private CalendarUtil()
	{
		
	}

	public static String getMonthName()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
		return String.format("%1$tb", cal);
	}
	
	public static String getYear()
	{	
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
		return String.format("%1$tY", cal);
	}
	
	public static String getDay()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
		return String.format("%1$td-%1$ta", cal);
	}
	
	public static String getCurrentTime()
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");		
		return dateFormat.format(cal.getTime());
	}
	
	public static String getTimeDifference(String start, String end)
	{
		
		Date date1 = null;
		Date date2 = null;
		try 
		{
			date1 = TIME_FORMAT.parse(start);
			date2 = TIME_FORMAT.parse(end);
			
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		long difference = date2.getTime() - date1.getTime();
		
		double hoursDiff = (difference / 1000.0)/3600.0;
		
		return (String.format("%1$.2f", hoursDiff));
	}

}
