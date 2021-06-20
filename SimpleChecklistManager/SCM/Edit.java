import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Edit extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;

	JTextField inputField;
	Window frame;
	JButton enter; 
	String input; 
	String updatedText;
	JCheckBox currentCheckbox; 
	JPanel currentCategory;

	static boolean duplicate;

	Edit(Window mainFrame){
		frame = mainFrame;
		currentCategory = frame.currentCategory;
		currentCheckbox = frame.currentCheckbox;

		this.setTitle("Edit");
		this.setLayout(new FlowLayout());
		this.setSize(270, 90);
		if (frame.darkMode == true)
			this.getContentPane().setBackground(Color.black);	
		else
			this.getContentPane().setBackground(Color.white);	
		
		editField();
		this.add(inputField);
		
		this.setVisible(true);
	}
	
	public String getInput(){
		return input;
	}

	public void editField(){
		String currentText = "";
		if(currentCheckbox != null)
			currentText = currentCheckbox.getText();
		if(currentCategory != null)
			currentText = currentCategory.getName();
		inputField = new JTextField(currentText);
		inputField.setPreferredSize(new Dimension(250, 40));
		inputField.addKeyListener(this);
	}
	
	public void displayEdit(String updatedElement, String updatedText) {
		if(currentCategory != null) {
			Component[] iterator = currentCategory.getComponents(); 
			List<JCheckBox> saved = new ArrayList<JCheckBox>();
			updatedElement = currentCategory.getName();

			currentCategory.setName(updatedText);

			//Update Components
			for(Component del : iterator) { 
				currentCategory.remove(del);
				if (del instanceof JCheckBox)
					saved.add((JCheckBox) del);
			}
			JLabel textDisplay= new JLabel();
			textDisplay.setText("<HTML><U><span style='font-size:12px'>" + updatedText + "</span></U></HTML>");
			if (frame.darkMode == true)
				textDisplay.setForeground(Color.white);
			else
				textDisplay.setForeground(Color.black);
			currentCategory.add(textDisplay);
			for(JCheckBox readd : saved)
				currentCategory.add(readd);

			//Update Menu Items
			List<JMenuItem> menuIterator = new ArrayList<JMenuItem>(MenuBar.taskTypes);
			for(JMenuItem item: menuIterator) {
				frame.menuBar.deleteTaskType(item.getText());
			}

			for(JPanel cat : frame.categories) {
				JMenuItem taskType = new JMenuItem(cat.getName());
				taskType.addActionListener(frame.menuBar);
				MenuBar.taskTypes.add(taskType);
				frame.menuBar.taskMenu.add(taskType);
				frame.menuBar.taskMenu.revalidate();
				frame.menuBar.taskMenu.repaint();
			}
			frame.currentCategory = null; 
		}
		else{
			updatedElement = currentCheckbox.getText();
			currentCheckbox.setText(updatedText);
			frame.currentCheckbox = null; 
		}

		frame.panelScroll.revalidate();
		frame.panelScroll.repaint();
		frame.revalidate();
		frame.repaint();
	}

	public void rewriteEdit(String element) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		duplicate = false;
		if(frame.currentCheckbox != null)
			updatedText = frame.currentCheckbox.getText();
		if(frame.currentCategory != null)
			updatedText = frame.currentCategory.getName();

		int counter = 0; 
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(element))
				counter ++; 
			if(counter > 1 )
				duplicate = true; 
		}
		reader.close();
		reader = new Scanner(new File(frame.path));
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(updatedText) && duplicate == false){
				rewrite.write(element + "\n");
				if(reader.hasNext())
					line = reader.nextLine();
				if(!line.equals(updatedText))
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


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			input = inputField.getText();
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			try {
				rewriteEdit(input);
				displayEdit(updatedText, input);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(duplicate == false) {
				//write method to update the name of object frame.displayUnselectedCheck(input, taskCategory);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
