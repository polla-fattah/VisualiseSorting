import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Formee extends JFrame implements ActionListener
{
	private JComboBox sortType, speedCommand, ascending;
	private JButton buttons[] = new JButton[5];
	private JComboBox box [] = new JComboBox[2];
	private MyPanel panel;

	private String sortBy;

	Formee()
	{
		super ("Sort Application");
		setLocation(100,100);
		setSize(600,420);
		setResizable(false);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		});

		sortBy = "Bubble Sort";

		Container pane = getContentPane();
		pane.setLayout(null);

		String names[] = {"Play","Stop", "Reset", "Next >", "< Prev"};
		for (int i = 0; i < buttons.length;i++)
		{
			buttons[i] = new JButton(names[i]);
			buttons[i].setBounds(500,100 + 40 * i ,70,30);
			buttons[i].addActionListener(this);
			buttons[i].setEnabled(false);
			pane.add(buttons[i]);
		}
		buttons[0].setEnabled(true);
		buttons[2].setEnabled(true);

		//Sort type combo box
		sortType = new JComboBox();
		sortType.setBounds(490,20,100,20);
		sortType.setMaximumRowCount(2);
		sortType.addItem("Bubble Sort");
		sortType.addItem("Selection Sort");
		sortType.addItem("Squential Sort");
		sortType.addActionListener(this);
		pane.add(sortType);

		//Ascending and decending combo box
		ascending = new JComboBox();
		ascending.setBounds(490,50,100,20);
		ascending.addItem("Ascending");
		ascending.addItem("Descending");
		ascending.addActionListener(this);
		pane.add(ascending);

		//speed combo box
		speedCommand = new JComboBox();
		speedCommand.setBounds(500,300,50,20);
		for (int i =1 ;i<=10;i++)
			speedCommand.addItem(""+i);
		speedCommand.setMaximumRowCount(2);
		speedCommand.setSelectedIndex(1);
        speedCommand.addActionListener(this);
		pane.add(speedCommand);

		box[0] = sortType;
		box[1] = ascending;
		panel = new MyPanel(buttons, box);
		pane.add(panel);
	}//end constructor

	public MyPanel getPanel()
	{
		return panel;
	}

	public void actionPerformed(ActionEvent evt)
   	{
		Object source = evt.getSource();

      	if (source == buttons[0])//play
      	{
			panel.startSort(sortBy);
		}
      	else if (source == buttons[1])//stop
      	{
			panel.setStop(true);
		}
      	else if (source == buttons[2])//reset
      	{
			panel.setStop(true);
			setEnabled(false);
			new Show(this, panel.getMark());
		}
      	else if (source == buttons[3])//next
      	{
			buttons[4].setEnabled(true);
			buttons[3].setEnabled(false);
			panel.next();
		}
      	else if (source == buttons[4])//prev
      	{
			buttons[3].setEnabled(true);
			buttons[4].setEnabled(false);
			panel.previous();
		}
		else if( source == sortType)
		{
			JComboBox cb = (JComboBox)evt.getSource();
			sortBy = (String)cb.getSelectedItem();
		}
		else if( source == speedCommand)
		{
			JComboBox cb = (JComboBox)evt.getSource();
			panel.setSpeed(Integer.parseInt((String)cb.getSelectedItem()));
		}
		else if( source == ascending)
		{
			JComboBox cb = (JComboBox)evt.getSource();
			String str = (String)cb.getSelectedItem();
			panel.setDescending(!str.equals("Ascending"));

		}
	}

	public static void main(String str[])
	{
		Formee p = new Formee();
		p.show();
	}
}

