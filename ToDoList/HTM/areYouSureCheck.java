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
	JLabel question = new JLabel();
	JButton yes = new JButton();
	JButton no = new JButton();
	
	Window UI;

	areYouSureCheck(Window frame){
		UI = frame;
		this.setTitle("Are You Sure?");
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		this.setSize(350, 100);
		this.getContentPane().setBackground(Color.black);	
		
		//set buttons. add action listener?selected.setText(text)JLabel textDisplay= new JLabel();
		question.setText("Are You Sure? (This Will Overwrite You Current Tasks)");
		question.setForeground(Color.white);;

		yes.setText("Yes");
		yes.setFocusable(false);
		yes.setBackground(Color.black);
		yes.setForeground(Color.white); 
		yes.addActionListener(this);

		no.setText("No");
		no.setFocusable(false);
		no.setBackground(Color.black);
		no.setForeground(Color.white); 
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
