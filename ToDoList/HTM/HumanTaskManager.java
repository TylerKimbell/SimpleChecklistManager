import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Human Task Manager 
 * 
 * Things to do:
 * Edit should have the text of the thing you are editing. 
 * Dark Mode should change the color of the pop ups as well. 
 * Adding More Than One Task At A Time. Perhaps if you separate by a , it will separate each one.
 * 	Is there a way to iterate through the text and check for ','s? If so then just have to make add them into an array and iterate through the array to add m
 * Create ReadME
 * Start KickStarter mobile version and future updates
 * 	if KS doesn't work out I'll put it on a website so people can look at and play with it. By people, I mean employers. 
 * 
 * Things to do in the far future: 
 * Periodic checks for if the date has changed so it updates automatically rather than having to restart it. 
 * Stats
 * Port to Mobile 
 * Last Thing To Do? Addons so users can create what they want such as stats options. 
 */

public class HumanTaskManager{
	static Window mainWindow;
	
	public static void main(String[] args) throws IOException, ParseException {
		DateTimeFormatter date = DateTimeFormatter.ofPattern("dd");
		DateTimeFormatter month = DateTimeFormatter.ofPattern("MM");
		DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
		LocalDateTime now = LocalDateTime.now();  
		String d = date.format(now);
		String m = month.format(now);
		String y = year.format(now);
		int intM = Integer.parseInt(m); 

		String sMonth = String.format("%02d", intM);

		String newPath = y + "/" + sMonth + d + ".txt";
		
		File currentYear = new File(y);

		if(currentYear.mkdir()) {
			//New Year
		}

		//UI 
		mainWindow = new Window(newPath, sMonth, d);
	}
}
