package workingHoursCounter;

import java.io.File;

public abstract class XmlBase
{
	
	public abstract void saveToXml();
	
	public abstract void getFromXml(File xmlDir);
}
