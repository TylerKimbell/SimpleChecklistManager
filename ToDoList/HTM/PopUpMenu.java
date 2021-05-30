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
	
	JMenuItem delete;
	JMenuItem up;
	JMenuItem down;
	
	PopUpMenu(Window UI) {
		frame = UI;
		
		delete = new JMenuItem("Delete");
		delete.addActionListener(this);
		add(delete);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == delete) {
			try {
				if (frame.currentCheckbox != null)
					frame.deleteCheckBox();
				else if (frame.currentCategory != null)
					frame.deleteCategory();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Find the current button and remove it.
			//Also need to rewrite file to save.
		}
	}

}
