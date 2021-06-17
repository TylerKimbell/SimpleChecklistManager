import java.awt.Color;
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
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Edit extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;

	static String path;
	JPanel currentCategory;
	JCheckBox currentCheckbox;
	JTextField inputField;
	Window frame;
	JButton enter; 
	String input; 
	String updatedText;
	
	static boolean duplicate;
	boolean style = false;

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
	
	Edit(String todayPath, Window mainFrame, JPanel category, JCheckBox checkbox, boolean mode){
		path = todayPath;
		frame = mainFrame;
		currentCategory = category; 
		currentCheckbox = checkbox; 
		style = mode;

		this.setTitle("Edit");
		this.setLayout(new FlowLayout());
		this.setSize(270, 90);
		if (style == true)
			this.getContentPane().setBackground(Color.black);	
		else
			this.getContentPane().setBackground(Color.white);	
		
		editField();
		this.add(inputField);
		
		this.setVisible(true);
	}
	
	public void rewriteEdit(String element) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		duplicate = false;
		if(currentCheckbox != null)
			updatedText = currentCheckbox.getText();
		if(currentCategory != null)
			updatedText = currentCategory.getName();

		int counter = 0; 
		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(element))
				counter ++; 
			if(counter > 1 )
				duplicate = true; 
		}
		reader.close();
		reader = new Scanner(new File(path));
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
		Path save = Paths.get(path);
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
				frame.displayEdit(updatedText, input, currentCategory, currentCheckbox);
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
