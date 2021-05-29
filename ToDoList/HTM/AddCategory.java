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
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddCategory extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;

	static String path;
	static String taskCategory;
	JTextField cat;
	Window frame;
	JButton enter; 
	String input; 
	MenuBar menu; 
	static boolean aD = false; //AutoDelete 
	
	//Don't Know If I Need to Check For Duplicates Or Not
	//static boolean duplicate;

	public String getInput(){
		return input;
	}

	public void addCat(){
		cat = new JTextField();
		cat.setPreferredSize(new Dimension(250, 40));
		cat.addKeyListener(this);
	}
	
	AddCategory(String todayPath, Window UI, List<JPanel> categories, boolean autoDelete, MenuBar men){
		path = todayPath;
		aD = autoDelete;
		menu = men;
		frame = UI;

		this.setTitle("New Category");
		this.setLayout(new FlowLayout());
		this.setSize(270, 90);
		this.getContentPane().setBackground(Color.black);	
		
		addCat();
		this.add(cat);
		
		this.setVisible(true);
	}
	
	public static void addNewCat(String newCat) throws IOException {
		String tempPath = "template.txt";
		Scanner reader = new Scanner(new File(path));
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
			input = cat.getText();
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			try {
				addNewCat(input);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.categoryPanel(input);
			JMenuItem taskType = new JMenuItem(cat.getText());
			taskType.addActionListener(menu);
			menu.taskTypes.add(taskType);
			menu.taskMenu.add(taskType);
			menu.taskMenu.revalidate();
			menu.taskMenu.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
