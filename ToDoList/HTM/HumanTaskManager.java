import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Human Task Manager 
 * Things to do:
 * Arrange categories and tasks. Right click -> move up/move down buttons. 
 * 			pop/push style with the panels will have to be the way I think. 
 * Edit Entries/Categories
 * Adding More Than One Task At A Time. Perhaps if you separate by a , it will separate each one.
 * Create ReadME for Github and make it Public
 * 
 * Things to do in the far future: 
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
