import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Human Task Manager 
 * 
 * Things to do:
 * Bug in which editting tasks doesn't work? 
 * 
 * Things to do in the far future: 
 * Periodic checks for if the date has changed so it updates automatically rather than having to refresh. 
 * Stats
 * Port to Mobile 
 * Last Thing To Do? Addons so users can create what they want such as stats options. 
 */

public class HumanTaskManager{
	static Window mainWindow;
	
	public static void main(String[] args) throws IOException, ParseException {
		//UI 
		mainWindow = new Window();
	}
}
