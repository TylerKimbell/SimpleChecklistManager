import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuBar extends JMenuBar implements ActionListener{ 
	private static final long serialVersionUID = 1L;
	static JMenu taskMenu;
	static String path = "";
	static Window UI; 
	static List<JMenuItem> items = new ArrayList<JMenuItem>();
	static AddTask newTask;
	
	MenuBar(String todayPath, Window frame, List<JPanel> categories){
		path = todayPath;
		UI = frame;
		JMenu editMenu = new JMenu("Edit");
		JMenu categoryMenu = new JMenu("Categories");

		taskMenu = new JMenu("Add Task");
		taskMenu.addActionListener(this);
		categoryMenu.addActionListener(this);
		editMenu.add(taskMenu);
		editMenu.add(categoryMenu);
		this.add(editMenu);
		for(JPanel cat : categories) {
			JMenuItem item = new JMenuItem(cat.getName());
			item.addActionListener(this);
			items.add(item);
			taskMenu.add(item);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(JMenuItem item : items) {
			if(e.getSource() == item) {
				newTask = new AddTask(path, UI, item.getText());
			}
		}
		
	}

}