import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuBar extends JMenuBar implements ActionListener{ 
	private static final long serialVersionUID = 1L;
	JMenu taskMenu;
	static String path = "";
	static Window UI; 
	static List<JMenuItem> taskTypes = new ArrayList<JMenuItem>();

	static List<JPanel> categories;

	static JMenuItem newTaskManager;
	static JMenuItem refresh;
	static JMenuItem exit;
	
	static JMenuItem autoDelete;
	static JMenuItem manualDelete;

	static JMenuItem darkMode;

	static AddTask newTask;
	static AddCategory newCat;
	
	String month;
	String day;
	
	//true = dark false = light 
	boolean style = false;

	public void deleteTaskType(String deletedCategory) {
		JMenuItem delete = null;
		for(JMenuItem taskType: taskTypes) {
			if(taskType.getText().equals(deletedCategory)) {
				delete = taskType;
			}
		}
		taskTypes.remove(delete);
		taskMenu.remove(delete);
		taskMenu.revalidate();
		taskMenu.repaint();
	}
	
	MenuBar(String todayPath, Window frame, List<JPanel> cats, String mon, String date, boolean mode){
		month = mon;
		day = date; 
		style = mode;
		path = todayPath;
		UI = frame;
		categories = new ArrayList<JPanel>(cats);
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu categoryMenu = new JMenu("Add Category");

		newTaskManager = new JMenuItem ("New Task Manager");
		newTaskManager.addActionListener(this);
		refresh = new JMenuItem ("Refresh");
		refresh.addActionListener(this);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		fileMenu.add(newTaskManager);
		fileMenu.add(refresh);
		fileMenu.add(exit);

		autoDelete = new JMenuItem ("Once");
		manualDelete = new JMenuItem ("Persistent");
		autoDelete.addActionListener(this);
		manualDelete.addActionListener(this);
		categoryMenu.add(autoDelete);
		categoryMenu.add(manualDelete);

		taskMenu = new JMenu("Add Task");
		taskMenu.addActionListener(this);

		darkMode = new JMenuItem("Dark Mode");
		darkMode.addActionListener(this);
		toolsMenu.add(darkMode);

		fileMenu.addActionListener(this);
		categoryMenu.addActionListener(this);

		editMenu.add(taskMenu);
		editMenu.add(categoryMenu);
		this.add(fileMenu);
		this.add(editMenu);
		this.add(toolsMenu);
		for(JPanel cat : categories) {
			JMenuItem taskType = new JMenuItem(cat.getName());
			taskType.addActionListener(this);
			taskTypes.add(taskType);
			taskMenu.add(taskType);
		}
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newTaskManager) {
			areYouSureCheck sure = new areYouSureCheck(UI, style);
		}
		if(e.getSource() == refresh) {
			try {
				UI.updateDate();
				UI.create();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == exit) {
			UI.dispatchEvent(new WindowEvent(UI, WindowEvent.WINDOW_CLOSING));
		}
		if (e.getSource() == autoDelete) {
			newCat = new AddCategory(path, UI, categories, true, this, style);
		}
		if (e.getSource() == manualDelete) {
			newCat = new AddCategory(path, UI, categories, false, this, style);
		}

		for(JMenuItem taskType : taskTypes) 
		{
			if(e.getSource() == taskType) {
				newTask = new AddTask(path, UI, taskType.getText(), style);
			}
		}
		if (e.getSource() == darkMode) {
			try {
				UI.styleToggle();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}