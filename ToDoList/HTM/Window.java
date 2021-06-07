import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Window extends JFrame implements ItemListener{
	private static final long serialVersionUID = 1L;
	static String path = ""; 
	JCheckBox currentCheckbox;
	JPanel currentCategory; 

	JPanel panelScroll = new JPanel();
	JScrollPane scroller = new JScrollPane(panelScroll);

	JCheckBox selected;
	JCheckBox unSelected;
	List<JCheckBox> selecteds = new ArrayList<>();
	List<JCheckBox> unSelecteds = new ArrayList<>();
	List<JPanel> categories = new ArrayList<JPanel>();
	List<JMenuItem> items = new ArrayList<JMenuItem>();
	List<Component> updatedCheckboxes = new ArrayList<Component>();
	List<String> updatedNames = new ArrayList<String>();
	List<String> categoryTypes = new ArrayList<String>();
	List<String> savedCategory= new ArrayList<String>();

	MenuBar menuBar;
	JMenu editMenu;
	JMenu taskMenu;
	JMenuItem once;
	JMenuItem appointments;
	JMenuItem dailies;
	JMenuItem weeklies;
	
	AddTask newTask; 
	
	GridBagConstraints c = new GridBagConstraints();
	
	int gridRow = 0; 
	
	public void cInitialize() {
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = gridRow;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
	}

	public void categoryPanel(String category, String autoDelete) {
		JPanel catPanel = new JPanel();
		JPanel previous;
		catPanel.setBackground(Color.black);
		catPanel.setLayout(new GridLayout(0,1));
		catPanel.setName(category);
		catPanel.addMouseListener(new RightClickListener(this));
		categories.add(catPanel);
		categoryTypes.add(autoDelete);
		
		c.gridy = gridRow;

		//pop last category, change weight to 0 add it back and set weight to 1 to add next category 
		if(categories.get(0)!= null) {
			if(categories.size()>1)
				previous = categories.get(categories.size()-2);
			else 
				previous = categories.get(categories.size()-1);
			panelScroll.remove(previous);
			c.weighty = 0;
			panelScroll.add(previous, c);
			c.weighty = 1; 
		}
		gridRow++;
		c.gridy = gridRow;
		panelScroll.add(catPanel, c);

		displayText(category, category);
		panelScroll.revalidate();
		panelScroll.repaint();
	}

	public void categoryMove(JPanel category, String autoDelete) {
		JPanel previous;

		categories.add(category);
		categoryTypes.add(autoDelete);

		c.gridy = gridRow;

		//pop last category, change weight to 0 add it back and set weight to 1 to add next category 
		if(categories.get(0)!= null) {
			if(categories.size()>1)
				previous = categories.get(categories.size()-2);
			else 
				previous = categories.get(categories.size()-1);
			panelScroll.remove(previous);
			c.weighty = 0;
			panelScroll.add(previous, c);
			c.weighty = 1; 
		}
		gridRow++;
		c.gridy = gridRow;
		panelScroll.add(category, c);

		panelScroll.revalidate();
		panelScroll.repaint();
	}

	public void displaySelectedCheck(String text, String category){
		selected = new JCheckBox();
		selected.setText(text);
		selected.setFocusable(false);
		selected.setBackground(Color.black);
		selected.setForeground(Color.white);
		selected.setSelected(true);
		selected.addItemListener(this);
		selected.addMouseListener(new RightClickListener(this));
		selecteds.add(selected);
		for (JPanel cat : categories) {
			if(cat.getName().equals(category)){
				cat.add(selected);
			}
		}
		panelScroll.revalidate();
		panelScroll.repaint();
	}

	public void displayUnselectedCheck(String text, String category){
		if(text.equals("")) {
			System.out.println("Blank space");
		}
		unSelected = new JCheckBox();
		unSelected.setText(text);
		unSelected.setFocusable(false);
		unSelected.setBackground(Color.black);
		unSelected.setForeground(Color.white);
		unSelected.addItemListener(this);
		unSelected.addMouseListener(new RightClickListener(this));
		unSelecteds.add(unSelected);
		for (JPanel cat : categories) { 
			if(cat.getName().equals(category)){ 
				cat.add(unSelected);
			}
		}
		panelScroll.revalidate();
		panelScroll.repaint();
	}

	public void displayText(String text, String category){
		JLabel textDisplay= new JLabel();
		textDisplay.setText("<HTML><U>" + text + "</U></HTML>");
		textDisplay.setForeground(Color.white);
		for (JPanel cat : categories) {
			if(cat.getName().equals(category) && !text.equals("")) {
				cat.add(textDisplay);
			}
		}
	}
	
	public void writeTemplate() throws IOException {
		String tempPath = "template.txt";
		FileWriter template = new FileWriter(tempPath);
		String autoDelete = ".";
		String manualDelete = "*";
		List<JPanel> tempCategories = new ArrayList<JPanel>(categories);
		for(JPanel category : tempCategories) {
			currentCategory = category;
			deleteCategory();
		}

		currentCategory = null;

		template.write(".\nAppointments:" + "\n" + "\n");
		categoryPanel("Appointments:", autoDelete);
		JMenuItem taskAppt = new JMenuItem("Appointments:");
		taskAppt.addActionListener(menuBar);
		MenuBar.taskTypes.add(taskAppt);
		menuBar.taskMenu.add(taskAppt);

		template.write(".\nOnce:" + "\n" + "\n");
		categoryPanel("Once:", autoDelete);
		JMenuItem taskOnce = new JMenuItem("Once:");
		taskOnce.addActionListener(menuBar);
		MenuBar.taskTypes.add(taskOnce);
		menuBar.taskMenu.add(taskOnce);

		template.write("*\nDailies:" + "\n" + "\n");
		categoryPanel("Dailies:", manualDelete);
		JMenuItem taskDailies = new JMenuItem("Dailies:");
		taskDailies.addActionListener(menuBar);
		MenuBar.taskTypes.add(taskDailies);
		menuBar.taskMenu.add(taskDailies);

		template.write("*\nWeeklies:" + "\n" + "\n");
		categoryPanel("Weeklies:", manualDelete);
		JMenuItem taskWeek = new JMenuItem("Weeklies:");
		taskWeek.addActionListener(menuBar);
		MenuBar.taskTypes.add(taskWeek);
		menuBar.taskMenu.add(taskWeek);
		menuBar.taskMenu.revalidate();
		menuBar.taskMenu.repaint();
		
		template.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);	
	}

	public void create() throws IOException {
		this.setVisible(false);
		String tempPath = "temp.txt";
		FileWriter tempWrite = new FileWriter(tempPath);
		File template = new File("template.txt");
		if(!template.exists()) {
			writeTemplate();
		}
		Scanner reader = new Scanner(new File("template.txt"));
		String line;
		String category = "none";
		String autoDelete = ".";
		Boolean delete = false; 
		File todayFile = new File(path);
		
		//Create and Display New File
		while (reader.hasNextLine()){
			line = reader.nextLine();
			if(line.equals(".") || line.equals("*")){ 
				autoDelete = line; 
				if(line.equals("."))
					delete = true; 
				else 
					delete = false; 
				tempWrite.write(line + "\n");
				line = reader.nextLine();
				category = line;
				categoryPanel(category, autoDelete);
				tempWrite.write(line + "\n");
				continue;
			}

			//The differentiation between auto and manual delete categories. 
			if(delete == true && !todayFile.exists()) { 
				if(line.equals("[]")) {
					tempWrite.write(line + "\n");
					line = reader.nextLine();
					this.displayUnselectedCheck(line, category);
					tempWrite.write(line + "\n");
				}
				else if(line.equals("[x]")) { 
					line = reader.nextLine();
				}
				else {
					this.displayText(line, category);
					tempWrite.write(line + "\n");
				}
			}
			else {
				if(line.equals("[]")) {
					tempWrite.write(line + "\n");
					line = reader.nextLine();
					this.displayUnselectedCheck(line, category);
				}
				else if(line.equals("[x]")) {
					tempWrite.write(line + "\n");
					line = reader.nextLine();
					this.displaySelectedCheck(line, category);
				}
				else {
					this.displayText(line, category);
				}
			tempWrite.write(line + "\n");
			}
		}
		tempWrite.close();
		reader.close();	
		Path saveDay = Paths.get(path);
		Path saveTemplate = Paths.get("template.txt");
		Path temporary = Paths.get(tempPath);
		
		Files.copy(temporary, saveDay, StandardCopyOption.REPLACE_EXISTING);
		Files.copy(temporary, saveTemplate, StandardCopyOption.REPLACE_EXISTING);
		File tempFile = new File (tempPath);
		tempFile.delete();
	}

	public void saveCheck(String position) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String check = "[x]";
		String unCheck = "[]";

		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(check)) {
				line = reader.nextLine();
				if(line.equals(position)) {
					rewrite.write(unCheck + "\n");
					rewrite.write(line + "\n");
				}
				else {
					rewrite.write(check + "\n");
					rewrite.write(line + "\n");
				}
			}
			else if(line.equals(unCheck)) {
				line = reader.nextLine();
				if(line.equals(position)) {
					rewrite.write(check + "\n");
					rewrite.write(line + "\n");
				}
				else {
					rewrite.write(unCheck + "\n");
					rewrite.write(line + "\n");
				}
			}
			else
				rewrite.write(line + "\n");
		}

		rewrite.close();
		reader.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void deleteSave() throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String check = "[x]";
		String unCheck = "[]";
		String checkStatus = "";
		String autoDelete = "";
		String position = "";
		boolean category = false; 

		if (currentCheckbox != null)
			position = currentCheckbox.getText();
		else if (currentCategory != null) {
			position = currentCategory.getName();
			category = true; 
		}
		
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(check) || line.equals(unCheck)) {
				checkStatus = line;
				line = reader.nextLine();
				if(line.equals(position)) {
					//do nothing
				}
				else {
					rewrite.write(checkStatus + "\n");
					rewrite.write(line + "\n");
				}
			}
			else if((line.equals(".") || line.equals("*")) && category == true) {
				autoDelete = line;
				line = reader.nextLine();
				if(line.equals(position)) {
					while(!line.equals(".") && !line.equals("*") && reader.hasNext()) {
						line = reader.nextLine();
					}
					if(reader.hasNext())
						rewrite.write(line + "\n");
				}
				else {
					rewrite.write(autoDelete + "\n");
					rewrite.write(line + "\n");
				}
			}
			else
				rewrite.write(line + "\n");
		}

		rewrite.close();
		reader.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void deleteCheckBox() throws IOException {
		deleteSave();
		currentCheckbox.getParent().remove(currentCheckbox);
		panelScroll.revalidate();
		panelScroll.repaint();
		currentCheckbox = null;
	}

	public void deleteCategory() throws IOException {
		deleteSave();
		currentCategory.getParent().remove(currentCategory);
		panelScroll.revalidate();
		panelScroll.repaint();
		menuBar.deleteTaskType(currentCategory.getName());
		int counter = 0; 
		for (JPanel category : categories) {
			if (category.getName().equals(currentCategory.getName()))
				break;
			counter ++; 
		}
		categories.remove(counter);

		currentCategory = null;
		
		//Set last category to weight 1
		JPanel last;
		if(categories.size()>0) {
			last = categories.get(categories.size()-1);
			panelScroll.remove(last);
			c.weighty = 1;
			panelScroll.add(last, c);
			panelScroll.revalidate();
			panelScroll.repaint();
		}
	}
	
	public void saveMove(String categoryPanel) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String check = "[x]";
		String unCheck = "[]";
		String checkStatus = "";
		String autoDelete = "";
		
		int counter = 0;
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(".") || line.equals("*")) {
				autoDelete = categoryTypes.get(counter);
				line = reader.nextLine();
				if(line.equals(categoryPanel)) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(line + "\n");
					line = reader.nextLine();
					for(String updated : updatedNames) {
						rewrite.write(updated + "\n");
						if(reader.hasNext())
							line = reader.nextLine();
					}
					rewrite.write("\n");
				}
				else {
					rewrite.write(autoDelete + "\n");
					rewrite.write(line + "\n");
				}
				counter++;
			}
			else if(line.equals(check) || line.equals(unCheck)) {
				checkStatus = line;
				line = reader.nextLine();
				rewrite.write(checkStatus + "\n");
				rewrite.write(line + "\n");
			}
			else
				rewrite.write(line + "\n");
		}

		rewrite.close();
		reader.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}

	public void saveMoveCategoryUp(String categoryPanel) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String check = "[x]";
		String unCheck = "[]";
		String checkStatus = "";
		String autoDelete = "";
		boolean written = false; 
		boolean limit = false;
		
		int counter = 0;
		int remainder = 0;
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(".") || line.equals("*")){
				String reorderedCats = categories.get(counter).getName();
				autoDelete = categoryTypes.get(counter);
				line = reader.nextLine();
				if(!(line.equals(reorderedCats)) && written == false) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
					line = reader.nextLine();
					for(String updated : updatedNames) {
						rewrite.write(updated + "\n");
						if(reader.hasNext()){
							if(limit == false) {
								savedCategory.add(line);
								line = reader.nextLine();
								if(line.equals("")) {
									limit = true;
								}
							}
						}
					}
					while(limit == false) {
						if(reader.hasNext()) {
							savedCategory.add(line);
							line = reader.nextLine();
							if(line.equals(""))
								limit = true; 
						}
					}
					written = true; 
					rewrite.write("\n");
				}
				else if(!(line.equals(reorderedCats)) && written == true) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
					int updateSize = updatedNames.size();
					for(String saved : savedCategory) {
						rewrite.write(saved + "\n");
						if(updateSize > remainder){
							if(reader.hasNext())
								line = reader.nextLine();
						}
						remainder++;
					}
					if(savedCategory.size() < updatedNames.size()) {
						int saved = savedCategory.size();
						int updated = updatedNames.size();
						while (saved < updated) {
							if (reader.hasNext())
								line = reader.nextLine();
							saved++;
						}
						
					}
					written = false;
				}
				else {
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
				}
				counter++;
			}
			else if(line.equals(check) || line.equals(unCheck)) {
				checkStatus = line;
				line = reader.nextLine();
				rewrite.write(checkStatus + "\n");
				rewrite.write(line + "\n");
			}
			else
				rewrite.write(line + "\n");
		}

		savedCategory.clear();
		rewrite.close();
		reader.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}

	public void saveMoveCategoryDown(String categoryPanel) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String check = "[x]";
		String unCheck = "[]";
		String checkStatus = "";
		String autoDelete = "";
		boolean written = false; 
		boolean secondPass = true; 
		
		int counter = 0;
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(".") || line.equals("*")){
				String reorderedCats = categories.get(counter).getName();
				//System.out.println(reorderedCats);
				autoDelete = categoryTypes.get(counter);
				line = reader.nextLine();
				if(!(line.equals(reorderedCats)) && written == false){
					line = reader.nextLine();
					for(String updated : updatedNames) {
						if(reader.hasNext()) {
							line = reader.nextLine();
						}
					}
					written = true; 
					secondPass = false;
					counter--;
				}
				else if(!(line.equals(reorderedCats)) && written == true && secondPass == false) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
					for(String updated : updatedNames) {
						rewrite.write(updated + "\n");
					}
					rewrite.write("\n");
					secondPass = true;
					counter++;
					reorderedCats = categories.get(counter).getName();
					autoDelete = categoryTypes.get(counter);
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
					counter--;
				}
				else {
					rewrite.write(autoDelete + "\n");
					rewrite.write(line + "\n");
				}
				counter++;
			}
			else if(line.equals(check) || line.equals(unCheck)) {
				checkStatus = line;
				if(reader.hasNext())
					line = reader.nextLine();
				rewrite.write(checkStatus + "\n");
				rewrite.write(line + "\n");
			}
			else
				rewrite.write(line + "\n");
		}

		if(secondPass == false) {
			rewrite.write("\n");
			String reorderedCats = categories.get(categories.size()-1).getName();
			autoDelete = categoryTypes.get(categoryTypes.size()-1);
			rewrite.write(autoDelete + "\n");
			rewrite.write(reorderedCats + "\n");
			for(String updated : updatedNames) {
				rewrite.write(updated + "\n");
			}
		}

		savedCategory.clear();
		rewrite.close();
		reader.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void moveMenuItem() {
		int counter = 0; 
		List<JMenuItem> updatedMenuItems = new ArrayList<JMenuItem>();
		List<JMenuItem> menuIterator = new ArrayList<JMenuItem>();
		JMenuItem savedItem = null;
		boolean firstItem = false;
		boolean findFirstItem = false;
		boolean fixedOrder = false;
		for(JMenuItem item: MenuBar.taskTypes) {
			menuIterator.add(item);
		}
		for(JMenuItem item: menuIterator) {
			menuBar.deleteTaskType(item.getText());
			if(item.getText().equals(categories.get(counter).getName()) && findFirstItem == false) {
				updatedMenuItems.add(item);
			}
			else if (fixedOrder == false){
				findFirstItem = true; 
				if (firstItem == false) {
					savedItem = item;
					firstItem = true;
				}
				else{
					updatedMenuItems.add(item);
					updatedMenuItems.add(savedItem);
					fixedOrder = true; 
				}
			}
			else {
				updatedMenuItems.add(item);
			}
			counter++;
		}

		for(JMenuItem updated : updatedMenuItems) {
			JMenuItem taskType = new JMenuItem(updated.getText());
			taskType.addActionListener(menuBar);
			MenuBar.taskTypes.add(taskType);
			menuBar.taskMenu.add(taskType);
			menuBar.taskMenu.revalidate();
			menuBar.taskMenu.repaint();
		}
	}
	public void moveUp() throws IOException{
		int counter = 0; 
		int position = 0;
		updatedCheckboxes.clear();
		updatedNames.clear();
		updatedNames = new ArrayList<String>();
		updatedCheckboxes = new ArrayList<Component>();
		List<JPanel> categoryIterator = new ArrayList<JPanel>(categories);
		for(JPanel category : categoryIterator) {
			if(currentCheckbox != null) {
				if(currentCheckbox.getParent() == category) {
					Component[] checkboxes = category.getComponents();
					for(Component checkbox : checkboxes) {
						if(checkbox instanceof JCheckBox && checkbox == currentCheckbox) {
							position = counter -1;
						}
						else
							updatedCheckboxes.add(checkbox);
						category.remove(checkbox);
						counter++; 
					}
					counter = 0; 
					for(Component updatedCB : updatedCheckboxes) {
						if(counter == position && counter != 0) {
							category.add(currentCheckbox);
							category.add(updatedCB);
							if(updatedCB instanceof JCheckBox) {
								if(currentCheckbox.isSelected())
									updatedNames.add("[x]");
								else
									updatedNames.add("[]");
								updatedNames.add(currentCheckbox.getText());
								if(((JCheckBox) updatedCB).isSelected())
									updatedNames.add("[x]");
								else
									updatedNames.add("[]");
								updatedNames.add(((JCheckBox)updatedCB).getText());
							}
						}
						else if (counter == 0 && counter == position) {
							category.add(updatedCB);
							category.add(currentCheckbox);
							if(currentCheckbox.isSelected())
								updatedNames.add("[x]");
							else
								updatedNames.add("[]");
							updatedNames.add(currentCheckbox.getText());
						}
						else {
							category.add(updatedCB);
							if(updatedCB instanceof JCheckBox) {
								if(((JCheckBox) updatedCB).isSelected())
									updatedNames.add("[x]");
								else
									updatedNames.add("[]");
								updatedNames.add(((JCheckBox)updatedCB).getText());
							}
						}
						counter++;
					}
					category.revalidate();
					category.repaint();
					saveMove(category.getName());
					currentCheckbox = null; 
				}
			}
			else if (currentCategory != null) {
				String savedCatType = "."; 
				Component[] categoryContents = currentCategory.getComponents();
				if(currentCategory == category) {
					List<JPanel> cats = new ArrayList<JPanel>(categories);
					List<JPanel> newCats = new ArrayList<JPanel>();
					List<String> updatedCatTypes = new ArrayList<String>();
					for(Component names : categoryContents) {
						if (names instanceof JPanel) {
							updatedNames.add(((JPanel)names).getName());
						}
						else if (names instanceof JCheckBox) {
							if(((JCheckBox)names).isSelected()) {
								updatedNames.add("[x]");
							}
							else{
								updatedNames.add("[]");
							}
							updatedNames.add(((JCheckBox)names).getText());
						}
					}

					for(JPanel cat : cats) {
						if(cat == currentCategory) {
							position = counter -1;
							savedCatType = categoryTypes.get(counter);
						}
						else {
							newCats.add(cat);
							updatedCatTypes.add(categoryTypes.get(counter));
						}
						categories.remove(cat);
						panelScroll.remove(cat);
						counter++; 
					}
					categoryTypes.clear();
					counter = 0; 
					gridRow = 0; 
					for(JPanel updatedCat : newCats) {
						if(counter == position && !(position < 0)) {
							categoryMove(currentCategory, savedCatType);
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						else if (counter == 0 && position < 0) {
							categoryMove(currentCategory, savedCatType);
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						else {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						counter++;
					}
					saveMoveCategoryUp(currentCategory.getName());
					moveMenuItem();
					currentCategory = null; 
				}
			}
		}
	}
	
	public void moveDown() throws IOException{
		int counter = 0; 
		int position = 0;
		updatedCheckboxes.clear();
		updatedNames.clear();
		updatedNames = new ArrayList<String>();
		updatedCheckboxes = new ArrayList<Component>();
		List<JPanel> categoryIterator = new ArrayList<JPanel>(categories);
		for(JPanel category : categoryIterator) {
			if(currentCheckbox != null) {
				if(currentCheckbox.getParent() == category) {
					Component[] checkboxes = category.getComponents();
					for(Component checkbox : checkboxes) {
						if(checkbox instanceof JCheckBox && checkbox == currentCheckbox) {
							position = counter;
						}
						else
							updatedCheckboxes.add(checkbox);
						category.remove(checkbox);
						counter++; 
					}
					counter = 0; 
					for(Component updatedCB : updatedCheckboxes) {
						if(counter != 0 && counter != updatedCheckboxes.size() && counter == position){
							category.add(updatedCB);
							category.add(currentCheckbox);
							if(updatedCB instanceof JCheckBox) {
								if(((JCheckBox) updatedCB).isSelected())
									updatedNames.add("[x]");
								else
									updatedNames.add("[]");
								updatedNames.add(((JCheckBox)updatedCB).getText());
								if(currentCheckbox.isSelected())
									updatedNames.add("[x]");
								else
									updatedNames.add("[]");
								updatedNames.add(currentCheckbox.getText());
							}
						}
						else if (counter == updatedCheckboxes.size()-1 && position == updatedCheckboxes.size()) {
							category.add(updatedCB);
							category.add(currentCheckbox);
							if(((JCheckBox) updatedCB).isSelected())
								updatedNames.add("[x]");
							else
								updatedNames.add("[]");
							updatedNames.add(((JCheckBox)updatedCB).getText());
							if(currentCheckbox.isSelected())
								updatedNames.add("[x]");
							else
								updatedNames.add("[]");
							updatedNames.add(currentCheckbox.getText());
						}
						else {
							category.add(updatedCB);
							if(updatedCB instanceof JCheckBox) {
								if(((JCheckBox) updatedCB).isSelected())
									updatedNames.add("[x]");
								else
									updatedNames.add("[]");
								updatedNames.add(((JCheckBox)updatedCB).getText());
							}
						}
						counter++;
					}
					category.revalidate();
					category.repaint();
					saveMove(category.getName());
					currentCheckbox = null; 
				}
			}
			else if (currentCategory != null) {
				String savedCatType = "."; 
				Component[] categoryContents = currentCategory.getComponents();
				if(currentCategory == category) {
					List<JPanel> cats = new ArrayList<JPanel>(categories);
					List<JPanel> newCats = new ArrayList<JPanel>();
					List<String> updatedCatTypes = new ArrayList<String>();
					for(Component names : categoryContents) {
						if (names instanceof JPanel) {
							updatedNames.add(((JPanel)names).getName());
						}
						else if (names instanceof JCheckBox) {
							if(((JCheckBox)names).isSelected()) {
								updatedNames.add("[x]");
							}
							else{
								updatedNames.add("[]");
							}
							updatedNames.add(((JCheckBox)names).getText());
						}
					}
					for(JPanel cat : cats) {
						if(cat == currentCategory) {
							position = counter;
							savedCatType = categoryTypes.get(counter);
						}
						else {
							newCats.add(cat);
							updatedCatTypes.add(categoryTypes.get(counter));
						}
						categories.remove(cat);
						panelScroll.remove(cat);
						counter++; 
					}
					categoryTypes.clear();
					counter = 0; 
					gridRow = 0; 
					for(JPanel updatedCat : newCats) {
						if(counter == position && !(position < 0)) {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
							categoryMove(currentCategory, savedCatType);
						}
						else if (counter == newCats.size()-1 && position == newCats.size()) {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
							categoryMove(currentCategory, savedCatType);
						}
						else {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						counter++;
					}
					saveMoveCategoryDown(currentCategory.getName());
					moveMenuItem();
					currentCategory = null; 	
				}
			}
		}
	}

	Window(String todayPath, String month, String day) throws IOException{
		path = todayPath;

		this.setTitle("Human Task Manager " + month + "/" + day);
		this.setSize(600, 900);
		this.getContentPane().setBackground(Color.black);	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panelScroll.setBackground(Color.black);
		panelScroll.setLayout(new GridBagLayout());

		scroller.getVerticalScrollBar().setUnitIncrement(16);
		this.add(scroller);
		cInitialize();
		create();
		menuBar = new MenuBar(path, this, categories);

		this.setJMenuBar(menuBar);
		this.setVisible(true);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		String position = ""; 
		for (Object check : selecteds) { 
			if (source == check) {
				position = ((JCheckBox) check).getText();
				try {
					saveCheck(position);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}
		for (Object unCheck : unSelecteds) { 
			if (source == unCheck) {
				position = ((JCheckBox) unCheck).getText();
				try {
					saveCheck(position);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}
	}
}
