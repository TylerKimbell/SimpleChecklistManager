import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
	static JMenuItem newDay;
	
	static JMenuItem autoDelete;
	static JMenuItem manualDelete;

	static AddTask newTask;
	static AddCategory newCat;
	
	String month;
	String day;

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
	
	MenuBar(String todayPath, Window frame, List<JPanel> cats, String mon, String date){
		month = mon;
		day = date; 
		path = todayPath;
		UI = frame;
		categories = cats;
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu categoryMenu = new JMenu("Add Category");

		newTaskManager = new JMenuItem ("New Task Manager");
		newTaskManager.addActionListener(this);
		fileMenu.add(newTaskManager);

		newDay = new JMenuItem ("New Day");
		newDay.addActionListener(this);
		fileMenu.add(newDay);

		autoDelete = new JMenuItem ("Auto Delete Tasks");
		manualDelete = new JMenuItem ("Manual Delete Tasks");
		autoDelete.addActionListener(this);
		manualDelete.addActionListener(this);
		categoryMenu.add(autoDelete);
		categoryMenu.add(manualDelete);

		taskMenu = new JMenu("Add Task");
		taskMenu.addActionListener(this);

		fileMenu.addActionListener(this);
		categoryMenu.addActionListener(this);

		editMenu.add(taskMenu);
		editMenu.add(categoryMenu);
		this.add(fileMenu);
		this.add(editMenu);
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
		if(e.getSource() == newDay) {
			try {
				UI.create(month, day);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == newTaskManager) {
			areYouSureCheck sure = new areYouSureCheck(UI);
		}
		if (e.getSource() == autoDelete) {
			newCat = new AddCategory(path, UI, categories, true, this);
		}
		if (e.getSource() == manualDelete) {
			newCat = new AddCategory(path, UI, categories, false, this);
		}

		for(JMenuItem taskType : taskTypes) {
			if(e.getSource() == taskType) {
				newTask = new AddTask(path, UI, taskType.getText());
			}
		}
		
	}

}