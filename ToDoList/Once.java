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

public class Once extends JFrame implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String path;
	static String once = "Once:";
	JTextField task;
	window frame;
	JButton enter; 
	String input; 

	public String getInput(){
		return input;
	}

	public void addOnce(){
		task = new JTextField();
		task.setPreferredSize(new Dimension(250, 40));
		task.addKeyListener(this);
	}
	
	Once(String todayPath, window UI){

		path = todayPath;
		frame = UI;

		this.setTitle("Once");
		this.setLayout(new FlowLayout());
		this.setSize(270, 90);
		this.getContentPane().setBackground(Color.black);	
		
		addOnce();
		this.add(task);
		
		this.setVisible(true);
	}
	
	public static void addTask(String task) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
		String category = "";

		while(reader.hasNext()) {
			line = reader.nextLine();
			if(line.equals(once)) {

				category = once;
			}
			if(line.equals("") && category.equals(once)) {
				category = "";
				rewrite.write("[]" + "\n");
				rewrite.write(task + "\n");
				rewrite.write(line + "\n");
				line = reader.nextLine();
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
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				input = task.getText();
				frame.displayUnselectedCheck(input, once);
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				try {
					addTask(input);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
