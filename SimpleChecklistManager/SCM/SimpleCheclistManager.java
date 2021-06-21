import java.io.IOException;
import java.text.ParseException;

/* Human Task Manager 
 * 
 * Bugs: 
 * 
 * Things to do:
 * Grey Out Checked Items
 * 	Should there be a difference in persistent tasks or no? I think there should be but iunno. 
 * 			Blue? 
 * Create tab system to better organize categories. 
 * Some way to differentiate between once and persistent tasks. Different color or smth?
 * Option to Move to top or bottom.
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
