package tools;

public class TimeTableRow
{

	private String _day;

	private String _startTime;

	private String _endTime;

	private String _pauseTime;

	private String _totalTime;

	private String _startPause;

	private String _endPause;

	private boolean _userDefinedPause = false;

	private boolean _inPause = false;


	private static final double BREAK_TIME = 1.0; 


	public TimeTableRow(String day, String startTime, String endTime, String pauseTime, String totalTime)
	{
		_day = day.trim();
		_startTime = startTime.trim();
		_pauseTime = pauseTime.trim();
		_endTime = endTime.trim();
		_totalTime = totalTime.trim();
	}

	public String getDay()
	{
		return _day;
	}

	public String getTotalTime()
	{
		return _totalTime;
	}

	public String getStartingTime()
	{
		return _startTime;
	}


	public String getPauseTime()
	{
		return _pauseTime;
	}

	public boolean isUserDefinedPause()
	{
		return _userDefinedPause;
	}

	public boolean isInPause()
	{
		return _inPause;
	}

	public void setStartTime()
	{
		_startTime = CalendarUtil.getCurrentTime().trim();
		setEndTime();

	}
	public void setEndTime()
	{

		_endTime = CalendarUtil.getCurrentTime().trim();

		updateTotalTime();


	}


	public void updateTotalTime()
	{
		if (_inPause)
		{
			String currTime = CalendarUtil.getCurrentTime();
			double pauseTime = Double.valueOf(CalendarUtil.getTimeDifference(_startPause, currTime).replace(",", "."));
			double currPause = _pauseTime.trim().isEmpty() ? 0.0 : Double.valueOf(_pauseTime.replace(",", "."));
			double totalPauseTime = (pauseTime + currPause);
			if (!_userDefinedPause && totalPauseTime >= BREAK_TIME)
			{
				stopPause();

			}

			_pauseTime = String.format("%1$.2f", (Math.round(totalPauseTime * 100.0)/100.0));
			_startPause = currTime;
		}
		else
		{
			double pauseTime = _pauseTime.trim().isEmpty() ? 0.0 :  Math.round(Double.valueOf(_pauseTime.replace(",", ".")) * 100.0) / 100.0;

			double totalNoBreak =  Math.round(Double.valueOf(CalendarUtil.getTimeDifference(_startTime, _endTime).replace(",", ".")) * 100.0) / 100.0;
			System.out.println("totalNoBreak: " + totalNoBreak);
			double totalWithBreak = totalNoBreak - pauseTime;
			System.out.println("totalWithBreak: " + totalWithBreak);
			if (pauseTime < BREAK_TIME && totalWithBreak >= 6.0)
			{
				startPause(false);
			}

			_totalTime = String.format("%1$.2f", totalWithBreak);
		}
	}

	public void startPause(boolean userDefinedPause)
	{
		_startPause = CalendarUtil.getCurrentTime();
		System.out.println("StartPause: " + _startPause);
		_userDefinedPause = userDefinedPause;
		_inPause = true;
	}

	public void stopPause()
	{
		_inPause = false;
		_userDefinedPause = false;
		_endPause = CalendarUtil.getCurrentTime();
		System.out.println("StartPause: " + _startPause);
		System.out.println("EndPause: " + _endPause);
		double newPause = Double.valueOf(CalendarUtil.getTimeDifference(_startPause, _endPause).replace(",", "."));
		double currPause = _pauseTime.trim().isEmpty() ? 0.0 : Double.valueOf(_pauseTime.replace(",", "."));
		_pauseTime = String.format("%1$.2f", (Math.round((newPause + currPause) * 100.0) / 100.0));	
	}

	@Override
	public String toString()
	{
		return String.format(" %1$5s | %2$-10s | %3$-10s | %4$-5s | %5$-10s", _day, _startTime, _endTime, _pauseTime, _totalTime);	
	}



}
