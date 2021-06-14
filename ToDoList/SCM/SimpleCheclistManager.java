import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Human Task Manager 
 * 
 * Bugs: 
 * 
 * Things to do:
 * Need to update tasktypes in menubar when changing category types
 * Grey Out Checked Items
 * 	Should there be a difference in persistent tasks or no? I think there should be but iunno. 
 * 
 * Things to do in the far future: 
 * Periodic checks for if the date has changed so it updates automatically rather than having to refresh. 
 * Stats
 * Port to Mobile 
 * Last Thing To Do? Addons so users can create what they want such as stats options. 
 */

public class SimpleCheclistManager{
	static Window mainWindow;
	
	public static void main(String[] args) throws IOException, ParseException {
		//UI 
		mainWindow = new Window();
	}
}
