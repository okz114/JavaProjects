package workingHoursCounter;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tools.CalendarUtil;
import tools.TimeTextFile;

public class WorkingHoursCounter extends TimerTask  implements ActionListener
{
	private TimeTextFile _timeFile;


	private final Image _clockIm;
	private final Image _clock8hIm;
	private final Image _lunchIm;
	private final Image _alarm;
	private final PopupMenu _popup;
	private final MenuItem _endAppItem;
	private final MenuItem _pauseItem;
	private TrayIcon _trayIcon;

	private static final String PAUSE = "pause";
	private static final String WEITER = "weiter!";

	private Image _currentImage;
	public WorkingHoursCounter()
	{
		_timeFile = new TimeTextFile();
		_clockIm = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/clock.png"));
		_clock8hIm = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/clock8h.png"));
		_lunchIm = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/lunchBreak.png"));
		_alarm = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/alarm.png"));
		_popup = new PopupMenu();
		_pauseItem = new MenuItem(PAUSE);
		_endAppItem = new MenuItem("beenden");
		_popup.add(_pauseItem);
		_popup.add(_endAppItem);

		addImageToTray(_clockIm);
		_currentImage = _clockIm;
		_trayIcon.addActionListener(this);

		_pauseItem.addActionListener(this);
		_endAppItem.addActionListener(this);

	}

	public TimeTextFile getTimeFile() 
	{
		return _timeFile;
	}
	public void setTimeFile()
	{
		_timeFile = new TimeTextFile();
	}
	public Image getClockIm()
	{
		return _clockIm;
	}

	public Image getClock8hIm()
	{
		return _clock8hIm;
	}

	public Image getLunchIm()
	{
		return _lunchIm;
	}

	public Image getAlarm()
	{
		return _alarm;
	}

	public void addImageToTray(Image image)
	{
		SystemTray tray = SystemTray.getSystemTray();

		Dimension trayIconSize = tray.getTrayIconSize();
		_trayIcon = new TrayIcon(image.getScaledInstance(((Double)trayIconSize.getWidth()).intValue(), ((Double)trayIconSize.getHeight()).intValue(), Image.SCALE_SMOOTH), "WorkTimer", _popup);
		try
		{
			tray.add(_trayIcon);
		} 
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}


	public TrayIcon getTrayIcon()
	{
		return _trayIcon;
	}

	public void updateImageTray(Image image)
	{
		SystemTray tray = SystemTray.getSystemTray();

		Dimension trayIconSize = tray.getTrayIconSize();
		
		_trayIcon.setImage(image.getScaledInstance(((Double)trayIconSize.getWidth()).intValue(), ((Double)trayIconSize.getHeight()).intValue(), Image.SCALE_SMOOTH));
		
	}
	
	public static void run(WorkingHoursCounter whc) throws IOException
	{
		
		whc.updateImageTrayTimeBased();

	}
	
	public void updateImageTrayTimeBased()
	{
		if (!this.getTimeFile().getCurrentDay().equals(CalendarUtil.getDay()) ||
				!this.getTimeFile().getCurrentMonth().equals(CalendarUtil.getMonthName()) ||
				!this.getTimeFile().getCurrentYear().equals(CalendarUtil.getYear()))
		{
			this.setTimeFile();

		}
		this._timeFile.updateWorkingHours();
		boolean inPause = this._timeFile.getTimeTable().isInPause();
		double totalTime = Double.valueOf(_timeFile.getTimeTable().getTotalTime().replace(",", "."));
		if (inPause)
		{
			updateImageTray(_lunchIm);
			_trayIcon.setToolTip(String.format("%1$.2f[%2$s]", totalTime, _timeFile.getTimeTable().getPauseTime()));
			
			if (_currentImage != _lunchIm && _timeFile.getTimeTable().isInPause() && !_timeFile.getTimeTable().isUserDefinedPause())
			{
				_trayIcon.displayMessage("Pause!", "Geh mal spazieren, und hol dich mal was zum essen ;)", MessageType.INFO);
			}
			
			_currentImage = _lunchIm;
		}
		else if (totalTime < 8.00)
		{
			updateImageTray(_clockIm);
			_trayIcon.setToolTip(String.format("%1$.2f", totalTime));
			double hourPercent = (totalTime - Math.floor(totalTime)) * 60.0;
			if (hourPercent % 15.0 == 0.0)
			{
				_trayIcon.displayMessage("Vergiss nicht zu trinken!", "Lust auf Schluck Wasser! ;)", MessageType.INFO);
			}
		}
		else if (totalTime >= 8.00 && totalTime < 9.75)
		{
			updateImageTray(_clock8hIm);
			if (_currentImage != _clock8hIm)
			{
				_trayIcon.setToolTip(String.format("%1$.2f", totalTime));
				_trayIcon.displayMessage("Feierabend! juhuu", "8 Stunden sind schon rum ;)", MessageType.WARNING);
			}
			_currentImage = _clock8hIm;
		}
		else
		{
			updateImageTray(_alarm);
			_trayIcon.setToolTip(String.format("%1$.2f", totalTime));
			_trayIcon.displayMessage("10 Stunden! WIEDER!!!", "Lass uns trainieren gehen oder zusammen kochen ;) :*", MessageType.ERROR);
			
		}

	}

	public static void main(String[] args) throws IOException 
	{
		WorkingHoursCounter whc = new WorkingHoursCounter();
		final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(()->{whc.run();}, 0, 1, TimeUnit.MINUTES);

	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(_trayIcon))
		{
			ProcessBuilder pb = new ProcessBuilder("notepad.exe", _timeFile.getFileAbsPath());
			try
			{
				pb.start();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if (e.getSource().equals(_pauseItem))
		{
			if (_pauseItem.getLabel().equals(PAUSE))
			{
				this._timeFile.getTimeTable().startPause(true);
				_pauseItem.setLabel(WEITER);
//				updateImageTray(_lunchIm);
			}
			else
			{
				this._timeFile.getTimeTable().stopPause();
				_pauseItem.setLabel(PAUSE);
			}
			updateImageTrayTimeBased();
		}

		if (e.getSource().equals(_endAppItem))
		{
			updateImageTrayTimeBased();
			System.exit(1);
		}

	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		updateImageTrayTimeBased();
		
	}

}
