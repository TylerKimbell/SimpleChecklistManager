import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	List<JMenuItem> taskTypes = new ArrayList<JMenuItem>();
	static List<JPanel> categories;
	
	static JMenuItem autoDelete;
	static JMenuItem manualDelete;

	static AddTask newTask;
	static AddCategory newCat;
	
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
	
	MenuBar(String todayPath, Window frame, List<JPanel> cats){
		path = todayPath;
		UI = frame;
		categories = cats;
		
		JMenu editMenu = new JMenu("Edit");
		JMenu categoryMenu = new JMenu("Add Category");

		autoDelete = new JMenuItem ("Auto Delete Tasks");
		manualDelete = new JMenuItem ("Manual Delete Tasks");
		autoDelete.addActionListener(this);
		manualDelete.addActionListener(this);
		categoryMenu.add(autoDelete);
		categoryMenu.add(manualDelete);

		taskMenu = new JMenu("Add Task");
		taskMenu.addActionListener(this);

		categoryMenu.addActionListener(this);

		editMenu.add(taskMenu);
		editMenu.add(categoryMenu);
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