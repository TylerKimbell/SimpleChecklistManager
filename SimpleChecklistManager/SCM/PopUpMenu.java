import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

//Implement Menu Functionality 
//Move Up/Down Bottom/Top /Priority system like torrents?
public class PopUpMenu extends JPopupMenu implements ActionListener{
	private static final long serialVersionUID = 1L;

	Window frame;
	
	JMenuItem edit;
	JMenu changeType;
	JMenuItem once;
	JMenuItem persistent;
	JMenuItem moveUp;
	JMenuItem moveDown;
	JMenuItem delete;

	JMenuItem up;
	JMenuItem down;
	
	PopUpMenu(Window mainFrame) {
		frame = mainFrame;
		
		edit = new JMenuItem("Edit");
		edit.addActionListener(this);
		changeType = new JMenu("Change Type");
		changeType.addActionListener(this);
		once = new JMenuItem("Once");
		once.addActionListener(this);
		persistent = new JMenuItem("Persistent");
		persistent.addActionListener(this);
		moveUp = new JMenuItem("Move Up");
		moveUp.addActionListener(this);
		moveDown = new JMenuItem("Move Down");
		moveDown.addActionListener(this);
		delete = new JMenuItem("Delete");
		delete.addActionListener(this);
		add(edit);
		if(frame.currentCategory != null) {
			add(changeType);
			changeType.add(once);
			changeType.add(persistent);
		}
		addSeparator();
		add(moveUp);
		add(moveDown);
		addSeparator();
		add(delete);
	}

	public void updateTypeList(int savedCounter, boolean once) {
		int counter = 0;
		List<String> updatedCategoryTypes = new ArrayList<String>();
		List<String> categoryTypesIterator = new ArrayList<String>(frame.categoryTypes);
		String newType = "";
		for(String types : categoryTypesIterator) {
			if(counter == savedCounter) {
				if(once == true) {
					newType = ".";
				}
				else {
					newType = "*";
				}
			}
			else {
				newType = types;
			}
			updatedCategoryTypes.add(newType);
			counter++;
		}
		
		frame.categoryTypes.clear();

		for(String strong : updatedCategoryTypes) {
			frame.categoryTypes.add(strong);
		}
	}

	public void changeTypeOnce() throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String savedType = "";
		String once = ".";

		int savedCounter = 0; 
		int counter = 0; 
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(once) || line.equals("*")){
				savedType = line;
				if(reader.hasNext())
					line = reader.nextLine();
				if(line.equals(frame.currentCategory.getName())) {
					rewrite.write(once + "\n");
					rewrite.write(line + "\n");
					savedCounter = counter;
				}
				else {
					rewrite.write(savedType + "\n");
					rewrite.write(line + "\n");
				}
				counter++;
			}
			else
				rewrite.write(line + "\n");
		}
		
		updateTypeList(savedCounter, true);
		rewrite.close();
		reader.close();
		Path save = Paths.get(frame.path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);		
		counter = 0;
	}

	public void changeTypePersistent() throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String savedType = "";
		String persistent = "*";
		
		int savedCounter = 0; 
		int counter = 0; 
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(".") || line.equals("*")){
				savedType = line;
				if(reader.hasNext())
					line = reader.nextLine();
				if(line.equals(frame.currentCategory.getName())) {
					rewrite.write(persistent + "\n");
					rewrite.write(line + "\n");
					savedCounter = counter;
				}
				else {
					rewrite.write(savedType + "\n");
					rewrite.write(line + "\n");
				}
				counter++;
			}
			else
				rewrite.write(line + "\n");
		}

		updateTypeList(savedCounter, false);
		rewrite.close();
		reader.close();
		Path save = Paths.get(frame.path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);		
	}

	public void saveMove(String categoryPanel) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
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
				autoDelete = frame.categoryTypes.get(counter);
				line = reader.nextLine();
				if(line.equals(categoryPanel)) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(line + "\n");
					line = reader.nextLine();
					for(String updated : frame.updatedNames) {
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
		Path save = Paths.get(frame.path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}

	public void saveMoveCategoryUp(String categoryPanel) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
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
				String reorderedCats = frame.categories.get(counter).getName();
				autoDelete = frame.categoryTypes.get(counter);
				line = reader.nextLine();
				if(!(line.equals(reorderedCats)) && written == false) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
					line = reader.nextLine();
					for(String updated : frame.updatedNames) {
						rewrite.write(updated + "\n");
						if(reader.hasNext()){
							if(limit == false) {
								frame.savedCategory.add(line);
								line = reader.nextLine();
								if(line.equals("")) {
									limit = true;
								}
							}
						}
					}
					while(limit == false) {
						if(line.equals(""))
							limit = true; 
						else if(reader.hasNext()) {
							frame.savedCategory.add(line);
							line = reader.nextLine();
							if(line.equals(""))
								limit = true; 
						}
						else {
							limit = true;
						}
					}
					written = true; 
					rewrite.write("\n");
				}
				else if(!(line.equals(reorderedCats)) && written == true) {
					rewrite.write(autoDelete + "\n");
					rewrite.write(reorderedCats + "\n");
					int updateSize = frame.updatedNames.size();
					for(String saved : frame.savedCategory) {
						rewrite.write(saved + "\n");
						if(updateSize > remainder){
							if(reader.hasNext())
								line = reader.nextLine();
						}
						remainder++;
					}
					if(frame.savedCategory.size() < frame.updatedNames.size()) {
						int saved = frame.savedCategory.size();
						int updated = frame.updatedNames.size();
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
				if(reader.hasNext())
					line = reader.nextLine();
				rewrite.write(checkStatus + "\n");
				rewrite.write(line + "\n");
			}
			else
				rewrite.write(line + "\n");
		}

		frame.savedCategory.clear();
		rewrite.close();
		reader.close();
		Path save = Paths.get(frame.path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}

	public void saveMoveCategoryDown(String categoryPanel) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
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
				String reorderedCats = frame.categories.get(counter).getName();
				//System.out.println(reorderedCats);
				autoDelete = frame.categoryTypes.get(counter);
				line = reader.nextLine();
				if(!(line.equals(reorderedCats)) && written == false){
					if(reader.hasNext()) 
						line = reader.nextLine();
					for(@SuppressWarnings("unused") String updated : frame.updatedNames) {
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
					for(String updated : frame.updatedNames) {
						rewrite.write(updated + "\n");
					}
					rewrite.write("\n");
					secondPass = true;
					counter++;
					reorderedCats = frame.categories.get(counter).getName();
					autoDelete = frame.categoryTypes.get(counter);
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
			String reorderedCats = frame.categories.get(frame.categories.size()-1).getName();
			autoDelete = frame.categoryTypes.get(frame.categoryTypes.size()-1);
			rewrite.write(autoDelete + "\n");
			rewrite.write(reorderedCats + "\n");
			for(String updated : frame.updatedNames) {
				rewrite.write(updated + "\n");
			}
		}

		frame.savedCategory.clear();
		rewrite.close();
		reader.close();
		Path save = Paths.get(frame.path);
		Path temporary= Paths.get(tempPath);
		
		Files.copy(temporary, save, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void moveMenuItem() {
		int counter = 0; 
		List<JMenuItem> updatedMenuItems = new ArrayList<JMenuItem>();
		List<JMenuItem> menuIterator = new ArrayList<JMenuItem>(frame.menuBar.taskTypes);
		JMenuItem savedItem = null;
		boolean firstItem = false;
		boolean findFirstItem = false;
		boolean fixedOrder = false;
		for(JMenuItem item: menuIterator) {
			if(item.getText().equals(frame.categories.get(counter).getName()) && findFirstItem == false) {
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
			frame.menuBar.deleteTaskType(item.getText());
			counter++;
		}

		for(JMenuItem updated : updatedMenuItems) {
			JMenuItem taskType = new JMenuItem(updated.getText());
			taskType.addActionListener(frame.menuBar);
			frame.menuBar.taskTypes.add(taskType);
			frame.menuBar.taskMenu.add(taskType);
			frame.menuBar.taskMenu.revalidate();
			frame.menuBar.taskMenu.repaint();
		}
	}

	public void moveUp() throws IOException{
		int counter = 0; 
		int position = 0;
		frame.updatedCheckboxes.clear();
		frame.updatedNames.clear();
		frame.updatedNames = new ArrayList<String>();
		frame.updatedCheckboxes = new ArrayList<Component>();
		List<JPanel> categoryIterator = new ArrayList<JPanel>(frame.categories);
		for(JPanel category : categoryIterator) {
			if(frame.currentCheckbox != null) {
				if(frame.currentCheckbox.getParent() == category) {
					Component[] checkboxes = category.getComponents();
					for(Component checkbox : checkboxes) {
						if(checkbox instanceof JCheckBox && checkbox == frame.currentCheckbox) {
							position = counter -1;
						}
						else
							frame.updatedCheckboxes.add(checkbox);
						category.remove(checkbox);
						counter++; 
					}
					counter = 0; 
					for(Component updatedCB : frame.updatedCheckboxes) {
						if(counter == position && counter != 0) {
							category.add(frame.currentCheckbox);
							category.add(updatedCB);
							if(updatedCB instanceof JCheckBox) {
								if(frame.currentCheckbox.isSelected())
									frame.updatedNames.add("[x]");
								else
									frame.updatedNames.add("[]");
								frame.updatedNames.add(frame.currentCheckbox.getText());
								if(((JCheckBox) updatedCB).isSelected())
									frame.updatedNames.add("[x]");
								else
									frame.updatedNames.add("[]");
								frame.updatedNames.add(((JCheckBox)updatedCB).getText());
							}
						}
						else if (counter == 0 && counter == position) {
							category.add(updatedCB);
							category.add(frame.currentCheckbox);
							if(frame.currentCheckbox.isSelected())
								frame.updatedNames.add("[x]");
							else
								frame.updatedNames.add("[]");
							frame.updatedNames.add(frame.currentCheckbox.getText());
						}
						else {
							category.add(updatedCB);
							if(updatedCB instanceof JCheckBox) {
								if(((JCheckBox) updatedCB).isSelected())
									frame.updatedNames.add("[x]");
								else
									frame.updatedNames.add("[]");
								frame.updatedNames.add(((JCheckBox)updatedCB).getText());
							}
						}
						counter++;
					}
					category.revalidate();
					category.repaint();
					saveMove(category.getName());
					frame.currentCheckbox = null; 
				}
			}
			else if (frame.currentCategory != null) {
				String savedCatType = "."; 
				Component[] categoryContents = frame.currentCategory.getComponents();
				if(frame.currentCategory == category) {
					List<JPanel> cats = new ArrayList<JPanel>(frame.categories);
					List<JPanel> newCats = new ArrayList<JPanel>();
					List<String> updatedCatTypes = new ArrayList<String>();
					for(Component names : categoryContents) {
						if (names instanceof JPanel) {
							frame.updatedNames.add(((JPanel)names).getName());
						}
						else if (names instanceof JCheckBox) {
							if(((JCheckBox)names).isSelected()) {
								frame.updatedNames.add("[x]");
							}
							else{
								frame.updatedNames.add("[]");
							}
							frame.updatedNames.add(((JCheckBox)names).getText());
						}
					}

					for(JPanel cat : cats) {
						if(cat == frame.currentCategory) {
							position = counter -1;
							savedCatType = frame.categoryTypes.get(counter);
						}
						else {
							newCats.add(cat);
							updatedCatTypes.add(frame.categoryTypes.get(counter));
						}
						frame.categories.remove(cat);
						frame.panelScroll.remove(cat);
						counter++; 
					}
					frame.categoryTypes.clear();
					counter = 0; 
					frame.gridRow = 0; 
					for(JPanel updatedCat : newCats) {
						if(counter == position && !(position < 0)) {
							categoryMove(frame.currentCategory, savedCatType);
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						else if (counter == 0 && position < 0) {
							categoryMove(frame.currentCategory, savedCatType);
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						else {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						counter++;
					}
					saveMoveCategoryUp(frame.currentCategory.getName());
					moveMenuItem();
					frame.currentCategory = null; 
				}
			}
		}
	}
	
	public void moveDown() throws IOException{
		int counter = 0; 
		int position = 0;
		frame.updatedCheckboxes.clear();
		frame.updatedNames.clear();
		frame.updatedNames = new ArrayList<String>();
		frame.updatedCheckboxes = new ArrayList<Component>();
		List<JPanel> categoryIterator = new ArrayList<JPanel>(frame.categories);
		for(JPanel category : categoryIterator) {
			if(frame.currentCheckbox != null) {
				if(frame.currentCheckbox.getParent() == category) {
					Component[] checkboxes = category.getComponents();
					for(Component checkbox : checkboxes) {
						if(checkbox instanceof JCheckBox && checkbox == frame.currentCheckbox) {
							position = counter;
						}
						else
							frame.updatedCheckboxes.add(checkbox);
						category.remove(checkbox);
						counter++; 
					}
					counter = 0; 
					for(Component updatedCB : frame.updatedCheckboxes) {
						if(counter != 0 && counter != frame.updatedCheckboxes.size() && counter == position){
							category.add(updatedCB);
							category.add(frame.currentCheckbox);
							if(updatedCB instanceof JCheckBox) {
								if(((JCheckBox) updatedCB).isSelected())
									frame.updatedNames.add("[x]");
								else
									frame.updatedNames.add("[]");
								frame.updatedNames.add(((JCheckBox)updatedCB).getText());
								if(frame.currentCheckbox.isSelected())
									frame.updatedNames.add("[x]");
								else
									frame.updatedNames.add("[]");
								frame.updatedNames.add(frame.currentCheckbox.getText());
							}
						}
						else if (counter == frame.updatedCheckboxes.size()-1 && position == frame.updatedCheckboxes.size()) {
							category.add(updatedCB);
							category.add(frame.currentCheckbox);
							if(((JCheckBox) updatedCB).isSelected())
								frame.updatedNames.add("[x]");
							else
								frame.updatedNames.add("[]");
							frame.updatedNames.add(((JCheckBox)updatedCB).getText());
							if(frame.currentCheckbox.isSelected())
								frame.updatedNames.add("[x]");
							else
								frame.updatedNames.add("[]");
							frame.updatedNames.add(frame.currentCheckbox.getText());
						}
						else {
							category.add(updatedCB);
							if(updatedCB instanceof JCheckBox) {
								if(((JCheckBox) updatedCB).isSelected())
									frame.updatedNames.add("[x]");
								else
									frame.updatedNames.add("[]");
								frame.updatedNames.add(((JCheckBox)updatedCB).getText());
							}
						}
						counter++;
					}
					category.revalidate();
					category.repaint();
					saveMove(category.getName());
					frame.currentCheckbox = null; 
				}
			}
			else if (frame.currentCategory != null) {
				String savedCatType = "."; 
				Component[] categoryContents = frame.currentCategory.getComponents();
				if(frame.currentCategory == category) {
					List<JPanel> cats = new ArrayList<JPanel>(frame.categories);
					List<JPanel> newCats = new ArrayList<JPanel>();
					List<String> updatedCatTypes = new ArrayList<String>();
					for(Component names : categoryContents) {
						if (names instanceof JPanel) {
							frame.updatedNames.add(((JPanel)names).getName());
						}
						else if (names instanceof JCheckBox) {
							if(((JCheckBox)names).isSelected()) {
								frame.updatedNames.add("[x]");
							}
							else{
								frame.updatedNames.add("[]");
							}
							frame.updatedNames.add(((JCheckBox)names).getText());
						}
					}
					for(JPanel cat : cats) {
						if(cat == frame.currentCategory) {
							position = counter;
							savedCatType = frame.categoryTypes.get(counter);
						}
						else {
							newCats.add(cat);
							updatedCatTypes.add(frame.categoryTypes.get(counter));
						}
						frame.categories.remove(cat);
						frame.panelScroll.remove(cat);
						counter++; 
					}
					frame.categoryTypes.clear();
					counter = 0; 
					frame.gridRow = 0; 
					for(JPanel updatedCat : newCats) {
						if(counter == position && !(position < 0)) {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
							categoryMove(frame.currentCategory, savedCatType);
						}
						else if (counter == newCats.size()-1 && position == newCats.size()) {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
							categoryMove(frame.currentCategory, savedCatType);
						}
						else {
							categoryMove(updatedCat, updatedCatTypes.get(counter));
						}
						counter++;
					}
					saveMoveCategoryDown(frame.currentCategory.getName());
					moveMenuItem();
					frame.currentCategory = null; 	
				}
			}
		}
	}

	public void categoryMove(JPanel category, String autoDelete) {
		JPanel previous;

		frame.categories.add(category);
		frame.categoryTypes.add(autoDelete);

		frame.c.gridy = frame.gridRow;

		//pop last category, change weight to 0 add it back and set weight to 1 to add next category 
		if(frame.categories.get(0)!= null) {
			if(frame.categories.size()>1)
				previous = frame.categories.get(frame.categories.size()-2);
			else 
				previous = frame.categories.get(frame.categories.size()-1);
			frame.panelScroll.remove(previous);
			frame.c.weighty = 0;
			frame.panelScroll.add(previous, frame.c);
			frame.c.weighty = 1; 
		}
		frame.gridRow++;
		frame.c.gridy = frame.gridRow;
		frame.panelScroll.add(category, frame.c);

		frame.panelScroll.revalidate();
		frame.panelScroll.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if (source == edit) {
			@SuppressWarnings("unused")
			Edit edit = new Edit(frame);
		}
		else if (source == once) {
			try {
				changeTypeOnce();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (source == persistent) {
			try {
				changeTypePersistent();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(source == moveUp) {
			try {
				moveUp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(source == moveDown) {
			try {
				moveDown();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//I have to keep these methods in main because methods in main use them. 
		else if(source == delete) {
			try {
				if (frame.currentCheckbox != null)
					frame.deleteCheckBox();
				else if (frame.currentCategory != null)
					frame.deleteCategory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
