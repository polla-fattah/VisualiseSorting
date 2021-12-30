import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class MyPanel extends JPanel
{
	//the valuse of the array to be sorted
	int mark[]={100, 40, 85, 70, 120, 20, 110, 50, 35, 25, 60, 10, 15, 180, 5};

	//the location of the x-axis
	int X_AXIS[]={30, 55, 80, 105, 130, 155, 180, 205, 230, 255, 280, 305, 330, 355, 380};

	//the sum of changes of the rectangls while they are swaped
	final double CHANGES[] = {10.7, 9, 7.3 , 5.7, 4, 2.3, 0.7, -1, -2.7, -4.3, -6, -7.7, -9.4, -11, -12.7 };

	//change each time of swaped rectangles
	double xChange, yChange;

	private final static int SLEEP_TIME = 5,//the delay time of any operation
			  		 		 WIDTH = 20, 	//the width of the rectangls
			  		 		 Y_AXIS = 200;//to revers y axis

	private int index1, index2, //the to location for sorting
				speed, 			//speed of the operation bu default is 2 ranges between 1-10
				state;			// -1 = nothing; 0 = falling; 1 = rizing; 2 = jumpping ; 3 = flickering found

	private boolean stop,		//to break out the current sort
					descending;	//ascending or descending whene it is false then ascending

	java.util.List holder;	//to hold the sequense oof the sort operation
	ListIterator stack;		//to point to the holder

	JButton [] buttons;		//get ti from frame
	JComboBox []command;	//get ti from frame

	Thread thr = new Thread();//genaral thread for tasks which need to sleep

	//constructor whith two parameters
	MyPanel(JButton [] butn, JComboBox[] comm)
	{
		buttons = butn;
		command = comm;
		//the initial conditions
		state = -1;//the initial condition
		speed = 2;
		setBounds(5,20,480,350);
		holder = new ArrayList(50);
		stack = holder.listIterator(holder.size());
	}
	//get methods
	public int [] getMark()	{ return mark;}

	//set methods
	public void setDescending(boolean b){ descending = b;}
	public void setSpeed(int s){ speed = s;	}
	public void setStop( boolean b){stop = b;}
	//to reset the whole pane
	public void reset()
	{
		index1 = 0;
		index2 = 0;
		state = -1;
		buttons[4].setEnabled(false);
		buttons[3].setEnabled(false);
		holder.clear();
		stack = holder.listIterator(holder.size());
		repaint();
	}
	//At end of the sort or when the sort are stopped
	public void endSort()
	{
		buttons[0].setEnabled(true);
		buttons[4].setEnabled(true);
		buttons[1].setEnabled(false);
		command[0].setEnabled(true);
		command[1].setEnabled(true);
		index1 = 0;
		index2 = 0;
		System.gc();
	}

	//chooses the desired swap type
	//which determine by user selection
	public void startSort(String str)
	{

		Thread s;
		if(str.equals("Selection Sort"))
			s = new SelectionSort();
		else if(str.equals("Bubble Sort"))
			s = new BubbleSort();
		else
			s = new SquentialSort();

		buttons[1].setEnabled(true);
		buttons[0].setEnabled(false);
		buttons[3].setEnabled(false);
		buttons[4].setEnabled(false);
		command[0].setEnabled(false);
		command[1].setEnabled(false);

		s.start();
	}

	//stack operations
	void addToStack(Pair polla)
	{
		if (stack == null) return;

		//to remove any revious if it is exst at Re-play time
		while(stack.hasNext())
		{	stack.next();
			stack.remove();
		}
		holder.add(polla);
		stack = holder.listIterator(holder.size());
	}

	public void next()
	{
		if(!stack.hasNext())
		{
			buttons[3].setEnabled(false);
			return;
		}

		Pair p = (Pair) stack.next();
		index1 = p.ind1;
		index2 = p.ind2;
		oneStep();
		if(!stack.hasNext())
			buttons[3].setEnabled(false);
	}

	public void previous()
	{
		if(!stack.hasPrevious())
		{
			buttons[4].setEnabled(false);
			return;
		}

		Pair p = (Pair) stack.previous();
		index1 = p.ind1;
		index2 = p.ind2;
		oneStep();
		if(!stack.hasPrevious())
			buttons[4].setEnabled(false);
	}

	private void oneStep()
	{
		//instead of thread safe
		Thread thr = new Thread()
		{
			public void run()
			{
				try
				{
					mainSwap(this);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				buttons[3].setEnabled(true);
				buttons[4].setEnabled(true);
			}
		};
		try
		{
			thr.start();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	private void swap(Thread d)throws InterruptedException
	{
		mainSwap(d);
		addToStack(new Pair(index1,index2));
	}

	//grafical swap operations
	private void mainSwap(Thread d)throws InterruptedException
	{
		int temp;
		//befor swap operation
		flickering(d);
		falling(d);

		//swap opration
		temp = mark[index1];
		mark[index1] = mark[index2];
		mark[index2] = temp;

		//after swap operation
		jumpping(d);
		rizing(d);

		//finalize the current swap operation
		state = -1;
		repaint();
		d.sleep(4 * SLEEP_TIME * speed);
	}
	//while the two values are found
	private void flickering(Thread d)throws InterruptedException
	{
		state = 3;
		repaint();
		d.sleep(2 * SLEEP_TIME * speed);
	}
	//falling the rectangul
	private void falling(Thread d)throws InterruptedException
	{
		state = 0;
		xChange = 0;
		for (yChange = 0 ; yChange < 60 ; yChange+=4)
		{
			xChange += CHANGES[index1];
			d.sleep(SLEEP_TIME * speed);
			repaint();
		}
		d.sleep(SLEEP_TIME * speed);
	}
	//jump to the new place
	private void jumpping(Thread d)throws InterruptedException
	{
		state = 2;
		repaint();
		d.sleep(2 * SLEEP_TIME * speed);
	}
	//go to the new place for the falling rectangl
	private void rizing(Thread d)throws InterruptedException
	{
		state = 1;
		for (; yChange > 0 ; yChange-=4)
		{
			xChange -= CHANGES[index2];
			repaint();
			d.sleep(SLEEP_TIME * speed);
		}
		d.sleep(SLEEP_TIME * speed);
	}

	public void paint(Graphics screen)
	{
		Graphics2D screen2D = (Graphics2D)screen;

		//setting backGround
		screen2D.setColor(Color.gray);
		screen2D.fill(new Rectangle2D.Float(0,0,480,350));

		//drawing tow axis Linse
		screen2D.setColor(Color.black);
		screen2D.draw(new Line2D.Float(10F,10F,10F,Y_AXIS));
		screen2D.draw(new Line2D.Float(10F,Y_AXIS,410F,Y_AXIS));

		//drawing the content
		for (int j=0;j<mark.length;j++)
		{
			//tiny lines
			screen2D.setColor(Color.black);
			screen2D.draw(new Line2D.Float(X_AXIS[j] + 10 ,Y_AXIS ,X_AXIS[j] + 10 ,Y_AXIS + 10));

			//the rectangular value
			screen2D.setColor(Color.blue);
			screen2D.fill(new Rectangle2D.Float(X_AXIS[j],(Y_AXIS - mark[j]),WIDTH,mark[j]));

			//numbers
			screen2D.setColor(Color.white);
			screen2D.drawString(""+mark[j],X_AXIS[j]+5,Y_AXIS + 20);
		}

		//creating boxes and red numbers
		if(state != -1)
		{
			screen2D.setColor(Color.black);
			screen2D.draw(new Rectangle2D.Float( X_AXIS[index1] ,10 ,WIDTH ,190 ));
			screen2D.draw(new Rectangle2D.Float( X_AXIS[index2] ,10 ,WIDTH ,190 ));

			screen2D.setColor(Color.green);
			screen2D.drawString(""+mark[index1],X_AXIS[index1]+5,Y_AXIS+20);
			screen2D.drawString(""+mark[index2],X_AXIS[index2]+5,Y_AXIS+20);
		}
		//filkering
		if(state == 3)
		{
			screen2D.setColor(Color.pink);
			screen2D.fill(new Rectangle2D.Float(X_AXIS[index1],(Y_AXIS - mark[index1]),WIDTH,mark[index1]));
			screen2D.fill(new Rectangle2D.Float(X_AXIS[index2],(Y_AXIS - mark[index2]),WIDTH,mark[index2]));
		}
		//moving to the default place//falling
		if(state == 0)
		{
			screen2D.setColor(Color.blue);
			screen2D.fill(new Rectangle2D.Double(X_AXIS[index1] + xChange ,Y_AXIS + 25 + yChange ,WIDTH,mark[index1]));

			screen2D.setColor(Color.gray);
			screen2D.fill(new Rectangle2D.Float(X_AXIS[index1] + 1 ,(Y_AXIS - mark[index1]),WIDTH-1,mark[index1]));
		}
		//moving to the new place//rizing
		if(state == 1 || state == 2)
		{
			screen2D.setColor(Color.blue);
			screen2D.fill(new Rectangle2D.Double(X_AXIS[index1] + xChange ,Y_AXIS + 25 + yChange ,WIDTH ,mark[index2]));

			screen2D.setColor(Color.gray);
			screen2D.fill(new Rectangle2D.Float(X_AXIS[index2]+1 ,(Y_AXIS - mark[index2]) ,WIDTH - 1 ,mark[index2]));
			//flikering red
			if(state == 2)
			{	screen2D.setColor(Color.red);
				screen2D.fill(new Rectangle2D.Float(X_AXIS[index1],(Y_AXIS-mark[index1]),WIDTH,mark[index1]));
			}
		}

	}//end of paint method
//==========================================================================================
//==========================================================================================
	//Inner classes for sorting each sort type represents by
	//a class to make objects responsible for sortting array
	public class BubbleSort extends Thread
	{
		public void run()
		{
			boolean decide;
			try
			{
				for (int i = 0 ; i < mark.length -1; i++)
				{
					for (index1 = 0; index1 < mark.length -1 ; index1++)
					{
						decide = mark[index1] > mark[index1 + 1];
						if(descending)
							decide =  !decide;
						if (decide)
						{
							index2 = index1 + 1;
							if(stop)
								break;

							swap(this);
						}
					}//end if(descending)
					if(stop)
					{
						stop = false;
						break;
					}
				}//end for (i)
			}//end try
			catch(InterruptedException ie)
			{
				System.err.println(ie);
			}
			endSort();
		}//end method run()
	}

	public class SquentialSort extends Thread
	{
		public void run()
		{
			boolean decide;
			try
			{
				for (index2 = 0 ; index2 < mark.length -1; index2++)
				{
					for (index1 = index2 + 1 ; index1 < mark.length ; index1++)
					{
						decide = mark[index1] < mark[index2];
						if(descending)
							decide = !decide;
						if (decide)
						{
							if(stop)
								break;
							swap(this);
						}
					}
					if(stop)
					{
						stop = false;
						break;
					}
				}
			}
			catch(InterruptedException ie)
			{
				System.err.println(ie);
			}
			endSort();
		}
	}

	public class SelectionSort extends Thread
	{
		public void run()
		{
			try
			{
				for (index1 = 0 ; index1 < mark.length -1; index1++)
				{
					if(descending)
						index2 = maximum(index1);
					else
						index2 = minimum(index1);

					if(index1 == index2) continue;
					swap(this);
					if(stop)
					{	stop = false;
						break;
					}
				}
			}
			catch(InterruptedException ie)
			{
				System.err.println(ie);
			}
			endSort();
		}
		private int minimum(int start)
		{
			int temp = mark[start];
			int index = start;
			for (int i = index; i < mark.length ; i++)
			{
				if(mark[i] < temp)
				{
					temp = mark[i];
					index = i;
				}
			}
			return index;
		}// end of minimum function
		private int maximum(int start)
		{
			int temp = mark[start];
			int index = start;
			for (int i = index; i < mark.length ; i++)
			{
				if(mark[i] > temp)
				{
					temp = mark[i];
					index = i;
				}
			}
			return index;
		}// end of maximum function

	}//end of the SelectionSort class
}//end of MyPanel class

class Pair
{
	int ind1,ind2;
	Pair(int a, int b)
	{
		ind1 = a;
		ind2 = b;
	}
}