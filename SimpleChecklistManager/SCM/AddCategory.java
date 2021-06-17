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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddCategory extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;

	static String taskCategory;
	JTextField cat;
	Window frame;
	JButton enter; 
	String input; 
	static boolean autoDelete = false; //AutoDelete 
	
	static boolean duplicate = false;
	
	ArrayList<String> multipleCats = new ArrayList<String>();

	public String getInput(){
		return input;
	}

	public void addCat(){
		cat = new JTextField();
		cat.setToolTipText("Use commas for multiple categories.");
		cat.setPreferredSize(new Dimension(250, 40));
		cat.addKeyListener(this);
	}
	
	AddCategory(Window mainFrame, boolean autoDel){
		autoDelete = autoDel;
		frame = mainFrame;

		this.setTitle("New Category");
		this.setLayout(new FlowLayout());
		this.setSize(270, 90);
		if(frame.darkMode == true)
			this.getContentPane().setBackground(Color.black);	
		else
			this.getContentPane().setBackground(Color.white);	
		
		addCat();
		this.add(cat);
		
		this.setVisible(true);
	}
	
	public void addNewCat(String newCat, Boolean aD) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(frame.path));
		FileWriter rewrite = new FileWriter(tempPath);
		String line;
//		duplicate = false;

		while(reader.hasNext()) {
			line = reader.nextLine();
			rewrite.write(line + "\n");
		}

		rewrite.write("\n");
		if(aD == true)
			rewrite.write(".\n");
		else
			rewrite.write("*\n");
		rewrite.write(newCat + "\n");
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
			String autoDel = ".";
			if(autoDelete != true)
				autoDel = "*";

			input = cat.getText();
			String newCat = "";
			boolean comma = false;
			for(int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);
				if (c == ' ' && comma == true){
					comma = false;
				}
				else if (c != ','){
					newCat += c; 
				}
				else {
					multipleCats.add(newCat);
					newCat = "";
					comma = true; 
				}
			}
			multipleCats.add(newCat);

			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			for(JPanel category : frame.categories) {
				if(input.equals(category.getName())) {
					duplicate = true; 
				}
			}
			if(duplicate == false) {
				try {
					for(int i = 0; i < multipleCats.size(); i ++)
						addNewCat(multipleCats.get(i), autoDelete);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				for(int i = 0; i < multipleCats.size(); i ++) {
					frame.categoryPanel(multipleCats.get(i), autoDel);
					JMenuItem taskType = new JMenuItem(multipleCats.get(i));
					taskType.addActionListener(frame.menuBar);
					frame.menuBar.taskTypes.add(taskType);
					frame.menuBar.taskMenu.add(taskType);
					frame.menuBar.taskMenu.revalidate();
					frame.menuBar.taskMenu.repaint();
				}
			}
		}
		duplicate = false; 
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
