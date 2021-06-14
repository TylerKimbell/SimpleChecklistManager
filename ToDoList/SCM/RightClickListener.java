import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class RightClickListener extends MouseAdapter {
	static Window frame; 
	
	RightClickListener(Window UI){
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
		if (e.getSource() instanceof JCheckBox) {
			frame.currentCheckbox = (JCheckBox) e.getSource();
			frame.currentCategory = null;
		}
		if (e.getSource() instanceof JPanel) {
			frame.currentCategory = (JPanel) e.getSource();
			frame.currentCheckbox = null;
		}
		PopUpMenu rightClick = new PopUpMenu(frame);
		rightClick.show(e.getComponent(), e.getX(), e.getY());
	}
}
