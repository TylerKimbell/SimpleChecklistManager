import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

//Implement Menu Functionality 
//Move Up/Down Bottom/Top /Priority system like torrents?
public class PopUpMenu extends JPopupMenu implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;

	window frame;
	
	JMenuItem delete;
	JMenuItem up;
	JMenuItem down;
	
	public PopUpMenu(window UI) {
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
				frame.deleteCheckBox();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Find the current button and remove it.
			//Also need to rewrite file to save.
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
