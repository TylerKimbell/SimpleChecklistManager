import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

//Implement Menu Functionality 
//Move Up/Down Bottom/Top /Priority system like torrents?
public class PopUpMenu extends JPopupMenu implements ActionListener{
	private static final long serialVersionUID = 1L;

	Window frame;
	
	JMenuItem edit;
	JMenuItem moveUp;
	JMenuItem moveDown;
	JMenuItem delete;

	JMenuItem up;
	JMenuItem down;
	
	PopUpMenu(Window UI) {
		frame = UI;
		

		edit = new JMenuItem("Edit");
		edit.addActionListener(this);
		moveUp = new JMenuItem("Move Up");
		moveUp.addActionListener(this);
		moveDown = new JMenuItem("Move Down");
		moveDown.addActionListener(this);
		delete = new JMenuItem("Delete");
		delete.addActionListener(this);
		add(edit);
		add(moveUp);
		add(moveDown);
		addSeparator();
		add(delete);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if (source == edit) {
			frame.createEdit();
		}
		else if(source == moveUp) {
			try {
				frame.moveUp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(source == moveDown) {
			try {
				frame.moveDown();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(source == delete) {
			try {
				if (frame.currentCheckbox != null)
					frame.deleteCheckBox();
				else if (frame.currentCategory != null)
					frame.deleteCategory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
