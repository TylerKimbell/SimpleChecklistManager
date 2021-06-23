import java.io.IOException;
import java.text.ParseException;

/* Human Task Manager 
 * 
 * Bugs: 
 * There may be an issue with checked items reversing color, but I can't replicate it. 
 * 
 * Things to do:
 * Option to Move to top or bottom.
 * Create tab system to better organize categories. 
 * color options for user? 
 * f5 for refresh
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
