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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	String path = ""; 
	String month; 
	String day;
	JCheckBox currentCheckbox;
	JPanel currentCategory; 

	JPanel panelScroll;
	JScrollPane scroller;

	JCheckBox selected;
	JCheckBox unSelected;
	List<JCheckBox> selecteds;
	List<JCheckBox> unSelecteds;
	List<JPanel> categories;
	List<JMenuItem> items;
	List<Component> updatedCheckboxes;
	List<String> updatedNames;
	List<String> categoryTypes;
	List<String> savedCategory;

	MenuBar menuBar;
	JMenu editMenu;
	JMenu taskMenu;
	JMenuItem Tasks;
	JMenuItem appointments;
	JMenuItem dailies;
	JMenuItem weeklies;

	AddTask newTask; 
	
	GridBagConstraints c = new GridBagConstraints();

	Color style; 
	Color style2;	

	int gridRow = 0; 
	
	boolean darkMode = false;
	
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
		catPanel.setLayout(new GridLayout(0,1));
		if(darkMode == true)
			catPanel.setBackground(Color.black);
		else
			catPanel.setBackground(Color.white);
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

	public void displaySelectedCheck(String text, String category){
		selected = new JCheckBox();
		selected.setText(text);
		selected.setFocusable(false);
		selected.setForeground(Color.gray);
		if(darkMode == true) {
			selected.setBackground(Color.black);
		}
		else {
			selected.setBackground(Color.white);
		}
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
		if(darkMode == true) {
			unSelected.setBackground(Color.black);
			unSelected.setForeground(Color.white);
		}
		else {
			unSelected.setBackground(Color.white);
			unSelected.setForeground(Color.black);
		}
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
		textDisplay.setText("<HTML><U><span style='font-size:12px'>" + text + "</span></U></HTML>");
		if(darkMode == true)
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
		menuBar = new MenuBar(this);
		this.setJMenuBar(menuBar);
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

		template.write(".\nTasks:" + "\n" + "\n");
		categoryPanel("Tasks:", autoDelete);
		JMenuItem taskTasks = new JMenuItem("Tasks:");
		taskTasks.addActionListener(menuBar);
		MenuBar.taskTypes.add(taskTasks);
		menuBar.taskMenu.add(taskTasks);

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

	public void saveCheck(String position, JCheckBox checkColor) throws IOException {
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
					checkColor.setForeground(style2);
					panelScroll.revalidate();
					panelScroll.repaint();
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
					checkColor.setForeground(Color.gray);
					panelScroll.revalidate();
					panelScroll.repaint();
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

	public void styleInit() throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;


		if(reader.hasNext() && darkMode == true) {
			line = reader.nextLine();
			if(!line.equals("[Dark Mode]")) {
				rewrite.write("[Dark Mode]\n");
				rewrite.write(line + "\n");
			}
			else
				rewrite.write(line + "\n");
		}

		else if(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals("[Dark Mode]")) {
				if(reader.hasNext()) {
					line = reader.nextLine();
					rewrite.write(line + "\n");
				}
			}
			else
				rewrite.write(line + "\n");
		}

		while(reader.hasNext()) {
			line = reader.nextLine();
			rewrite.write(line + "\n");
		}
		
		rewrite.close();
		reader.close();
		Path save = Paths.get(path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);	
		
		if(darkMode == true) {
			style = Color.black;
			style2 = Color.white;
		}
		else {
			style = Color.white;
			style2 = Color.black;
		}
		
		panelScroll.setBackground(style);
		this.getContentPane().setBackground(style);	
		for(JPanel catPanel : categories) {
			Component label = catPanel.getComponent(0);
			catPanel.setBackground(style);
			if (label instanceof JLabel)
				label.setForeground(style2);
		}
		for(JCheckBox selected: selecteds) {
			selected.setBackground(style);
			selected.setForeground(Color.gray);
		}
		for(JCheckBox unSelected: unSelecteds) {
			unSelected.setBackground(style);
			unSelected.setForeground(style2);
		}
		panelScroll.revalidate();
		panelScroll.repaint();
		this.revalidate();
		this.repaint();
	}

	public void updateDate(){
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd");
		DateTimeFormatter monthFormat= DateTimeFormatter.ofPattern("MM");
		DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yyyy");
		LocalDateTime now = LocalDateTime.now();  
		String d = dateFormat.format(now);
		String m = monthFormat.format(now);
		String y = yearFormat.format(now);
		int intM = Integer.parseInt(m); 
		String sMonth = String.format("%02d", intM);
		String newPath = y + "/" + sMonth + d + ".txt";
		
		File currentYear = new File(y);
		month = sMonth;
		day = d;
		path = newPath;	
		this.setTitle("Simple Checklist Manager " + month + "/" + day);
		this.setSize(600, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if(currentYear.mkdir()) {
			//New Year
		}
	}

	public void create() throws IOException {
		panelScroll = new JPanel();
		scroller = new JScrollPane(panelScroll);
		selecteds = new ArrayList<>();
		unSelecteds = new ArrayList<>();
		categories = new ArrayList<JPanel>();
		items = new ArrayList<JMenuItem>();
		updatedCheckboxes = new ArrayList<Component>();
		updatedNames = new ArrayList<String>();
		categoryTypes = new ArrayList<String>();
		savedCategory= new ArrayList<String>();	
		menuBar.taskTypes = new ArrayList<JMenuItem>();

		this.getContentPane().removeAll();
		
		panelScroll.setLayout(new GridBagLayout());
		panelScroll.setBackground(Color.white);

		scroller.getVerticalScrollBar().setUnitIncrement(16);
		cInitialize();	
		this.setVisible(false);
		this.add(scroller);

		File template = new File("template.txt");

		if(!template.exists()) {
			writeTemplate();
		}

		else {
			String tempPath = "temp.txt";
			FileWriter tempWrite = new FileWriter(tempPath);
			Scanner reader = new Scanner(new File("template.txt"));
			String line;
			String category = "none";
			String autoDelete = ".";
			Boolean delete = false; 
			File todayFile = new File(path);
		
			//Create and Display New File
			while (reader.hasNextLine()){
				line = reader.nextLine();
				if(line.equals("[Dark Mode]"))
					darkMode = true; 
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
			reader.close();	
			tempWrite.close();
			Path saveDay = Paths.get(path);
			Path saveTemplate = Paths.get("template.txt");
			Path temporary = Paths.get(tempPath);
			
			Files.copy(temporary, saveDay, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(temporary, saveTemplate, StandardCopyOption.REPLACE_EXISTING);
			File tempFile = new File (tempPath);
			tempFile.delete();
		}
		menuBar = new MenuBar(this);
		this.setJMenuBar(menuBar);
		styleInit();
		this.revalidate();
		this.repaint();
		this.setVisible(true);
	}
	
	Window() throws IOException{
		updateDate();
		create();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		String position = ""; 
		for (Object check : selecteds) { 
			if (source == check) {
				position = ((JCheckBox) check).getText();
				try {
					saveCheck(position, ((JCheckBox) check));
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
					saveCheck(position, ((JCheckBox) unCheck));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}
	}
}
