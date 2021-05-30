import java.awt.Color;
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
	
	MenuBar menuBar;
	JMenu editMenu;
	JMenu taskMenu;
	JMenuItem once;
	JMenuItem appointments;
	JMenuItem dailies;
	JMenuItem weeklies;
	
	AddTask newTask; 
	
	
	int gridRow = 0; 
	public void categoryPanel(String category) {
		JPanel catPanel = new JPanel();
		catPanel.setBackground(Color.black);
		catPanel.setLayout(new GridLayout(0,1));
		catPanel.setName(category);
		catPanel.addMouseListener(new RightClickListener(this));
		categories.add(catPanel);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = gridRow;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.anchor = GridBagConstraints.NORTH;
		gridRow++;
		panelScroll.add(catPanel, c);

		displayText(category, category);
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
		textDisplay.setText(text);
		textDisplay.setHorizontalAlignment(JLabel.LEFT);
		textDisplay.setVerticalAlignment(JLabel.TOP);
		textDisplay.setForeground(Color.white);
		for (JPanel cat : categories) {
			if(cat.getName().equals(category)&& !text.equals("")) {
				cat.add(textDisplay);
			}
		}
	}
	
	public void writeTemplate() throws IOException {
		FileWriter template = new FileWriter("template.txt");
		template.write(".\nAppointments:" + "\n" + "\n");
		template.write(".\nOnce:" + "\n" + "\n");
		template.write("*\nDailies:" + "\n" + "\n");
		template.write("*\nWeeklies:" + "\n" + "\n");
		template.close();
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
		Boolean delete = false; 
		File todayFile = new File(path);
		
		

		//Create and Display New File
		while (reader.hasNextLine()){
			line = reader.nextLine();
			if(line.equals(".") || line.equals("*")){ 
				if(line.equals("."))
					delete = true; 
				else 
					delete = false; 
				tempWrite.write(line + "\n");
				line = reader.nextLine();
				category = line;
				categoryPanel(category);
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
		
		System.out.println(position + "\n");


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

	//Need to delete task type right? 
	public void deleteCategory() throws IOException {
		deleteSave();
		currentCategory.getParent().remove(currentCategory);
		panelScroll.revalidate();
		panelScroll.repaint();
		menuBar.deleteTaskType(currentCategory.getName());
		currentCategory = null;
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
