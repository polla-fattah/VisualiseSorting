import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Show extends JFrame implements ActionListener
{
	int mark[];
	JTextField[] field;
	JButton ok = new JButton("Ok");
	JButton cancel = new JButton("Cancel");
	Formee father;
	Show(Formee fath, int [] a)
	{
		super("Fill Values");
		setSize(655,150);
		setLocation(80,200);
		setResizable(false);
		mark = a;
		father = fath;

		addWindowListener(new WindowAdapter()
		{	public void windowClosing(WindowEvent we)
			{
				father.setEnabled(true);
				hide();
			}
		});

		Container pane = getContentPane();
		pane.setLayout(null);

		field = new JTextField[mark.length];
		for (int i = 0 ; i < mark.length ;i++)
		{
			field[i] = new JTextField(3);
			field[i].setBounds(10 + i * 42 , 50 ,40,20);
			pane.add(field[i]);

		}
		ok.setBounds(240,80,80,30);
		ok.addActionListener(this);
		pane.add(ok);
		cancel.setBounds(330,80,80,30);
		cancel.addActionListener(this);
		pane.add(cancel);

		show();
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object source = evt.getSource();
		if(source == ok)
		{
			int temp[] = new int[mark.length];
			for (int i = 0 ; i < mark.length ;i++)
			{
				try
				{
					temp[i] = Integer.parseInt(field[i].getText());
					if(temp[i] < 10 || temp[i] > 190)
						throw new Exception();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(this,
										"The numbers should be between 10 - 190", "Warning",
										JOptionPane.WARNING_MESSAGE);
					field[i].requestFocus();
					field[i].selectAll();
					return;
				}
			}
			System.arraycopy(temp, 0, mark, 0 , mark.length);
			father.setEnabled(true);
			father.getPanel().reset();
			hide();
		}
		else if(source == cancel)
		{
			father.setEnabled(true);
			hide();
		}
	}
}

