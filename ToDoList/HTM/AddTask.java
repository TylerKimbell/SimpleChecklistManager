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
import javax.swing.JFrame;
import javax.swing.JTextField;

public class AddTask extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;

	static String path;
	static String taskCategory;
	JTextField task;
	Window frame;
	JButton enter; 
	String input; 
	
	static boolean duplicate;

	public String getInput(){
		return input;
	}

	public void addOnce(){
		task = new JTextField();
		task.setPreferredSize(new Dimension(250, 40));
		task.addKeyListener(this);
	}
	
	AddTask(String todayPath, Window UI, String category){
		path = todayPath;
		taskCategory = category;
		frame = UI;

		this.setTitle(category);
		this.setLayout(new FlowLayout());
		this.setSize(270, 90);
		this.getContentPane().setBackground(Color.black);	
		
		addOnce();
		this.add(task);
		
		this.setVisible(true);
	}
	
	public void addTask(String task) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String category = "";
		boolean written = false; 
		duplicate = false;

		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(taskCategory)) {
				category = taskCategory;
			}
			if(line.equals(task) && category.equals(taskCategory)){
				duplicate = true;
			}
			if(line.equals("") && category.equals(taskCategory) && duplicate == false) {
				category = "";
				rewrite.write("[]" + "\n");
				rewrite.write(task + "\n");
				rewrite.write(line + "\n");
				line = reader.nextLine();
				rewrite.write(line + "\n");
				written = true; 
			}
			else
				rewrite.write(line + "\n");
		}
		if(written == false){
				rewrite.write("[]" + "\n");
				rewrite.write(task + "\n");
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
			input = task.getText();
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			try {
				addTask(input);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(duplicate == false)
				frame.displayUnselectedCheck(input, taskCategory);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
