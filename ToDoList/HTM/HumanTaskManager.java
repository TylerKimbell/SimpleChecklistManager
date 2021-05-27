import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* Human Task Manager 
 * Things to do:
 * The Ability To Create Categories. The Different categories will be categorized as either auto delete or manual delete 
 * Original Template That You Can Revert To If You Want to Reset (Literally Just a Text File) 
 *  	Such That There's A Reset Option That Just Reverts To The Default Template
 *  	Just a menu item that overwrites the template with the function I already wrote
 *  Adding More Than One Task At A Time. Perhaps if you separate by a , it will separate each one.
 * 
 * Port to Mobile 
 * Last Thing To Do? Addons so users can create what they want such as stats options. 
 */

public class HumanTaskManager{
	static window mainWindow;
	
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
		mainWindow = new window(newPath, sMonth, d);
	}
}
