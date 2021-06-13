import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class areYouSureCheck extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel question = new JLabel();
	JButton yes = new JButton();
	JButton no = new JButton();
	
	Window UI;
	boolean style = false;

	areYouSureCheck(Window frame, boolean mode){
		Color style1;
		Color style2;

		style = mode;
		UI = frame;
		this.setTitle("Are You Sure?");
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		this.setSize(350, 100);
		if(style == true) {
			style1 = Color.black;
			style2 = Color.white;
		}
		else {
			style1 = Color.white;
			style2 = Color.black;
		}
		
		this.getContentPane().setBackground(style1);	
		//set buttons. add action listener?selected.setText(text)JLabel textDisplay= new JLabel();
		question.setText("Are You Sure? (This Will Overwrite You Current Tasks)");
		question.setForeground(style2);;

		yes.setText("Yes");
		yes.setFocusable(false);
		yes.setBackground(style1);
		yes.setForeground(style2); 
		yes.addActionListener(this);

		no.setText("No");
		no.setFocusable(false);
		no.setBackground(style1);
		no.setForeground(style2); 
		no.addActionListener(this);

		this.add(question);
		this.add(yes);
		this.add(no);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== yes) {
			try {
				UI.writeTemplate();
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
}
