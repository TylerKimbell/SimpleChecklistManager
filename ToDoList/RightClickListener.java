import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;

public class RightClickListener extends MouseAdapter {
	static window frame; 
	
	RightClickListener(window UI){
		frame = UI;
	}

	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			openMenu(e);
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			openMenu(e);
		}
	}
	
	private void openMenu(MouseEvent e) {
		PopUpMenu rightClick = new PopUpMenu(frame);
		rightClick.show(e.getComponent(), e.getX(), e.getY());
		frame.current = (JCheckBox) e.getSource();
	}
}
