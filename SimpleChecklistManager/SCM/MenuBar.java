import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
	static Window frame; 

	static List<JMenuItem> taskTypes = new ArrayList<JMenuItem>();

	static JMenuItem newChecklistManager;
	static JMenuItem refresh;
	static JMenuItem exit;
	
	static JMenuItem autoDelete;
	static JMenuItem manualDelete;

	static JMenuItem darkMode;

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
	
	MenuBar(Window mainFrame){
		frame = mainFrame;
		
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu categoryMenu = new JMenu("Add Category");

		newChecklistManager = new JMenuItem ("New Checklist Manager");
		newChecklistManager.addActionListener(this);
		refresh = new JMenuItem ("Refresh");
		refresh.addActionListener(this);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		fileMenu.add(newChecklistManager);
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
		for(JPanel cat : frame.categories) {
			JMenuItem taskType = new JMenuItem(cat.getName());
			taskType.addActionListener(this);
			taskTypes.add(taskType);
			taskMenu.add(taskType);
		}
		this.setVisible(true);
	}
	
	public void styleToggle() throws IOException {
		if(frame.darkMode == true)
			frame.darkMode = false;
		else 
			frame.darkMode = true;
		frame.styleInit();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == newChecklistManager) {
			@SuppressWarnings("unused")
			areYouSureCheck sure = new areYouSureCheck(frame);
		}
		if(e.getSource() == refresh) {
			try {
				frame.updateDate();
				frame.create();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == exit) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		if (e.getSource() == autoDelete) {
			newCat = new AddCategory(frame, true);
		}
		if (e.getSource() == manualDelete) {
			newCat = new AddCategory(frame, false);
		}

		for(JMenuItem taskType : taskTypes) 
		{
			if(e.getSource() == taskType) {
				newTask = new AddTask(frame, taskType.getText());
			}
		}
		if (e.getSource() == darkMode) {
			try {
				styleToggle();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}