
import java.awt.event.*;
import java.applet.Applet;

public abstract class GameBase extends Applet implements Runnable, KeyListener, MouseListener
{
	Thread t;
		
	public static final int UP = KeyEvent.VK_UP;
	public static final int DN = KeyEvent.VK_DOWN;
	public static final int LT = KeyEvent.VK_LEFT;
	public static final int RT = KeyEvent.VK_RIGHT;
	
	public static final int _1 = KeyEvent.VK_1;
	public static final int _2 = KeyEvent.VK_2;
	public static final int _3 = KeyEvent.VK_3;
	public static final int _4 = KeyEvent.VK_4;

	public static final int ESCAPE = KeyEvent.VK_ESCAPE;
	public static final int SPACE = KeyEvent.VK_SPACE;
	public static final int SEMICOLON = KeyEvent.VK_SEMICOLON;
	public static final int QUOTE = KeyEvent.VK_QUOTE;
	
	static boolean[] pressed = new boolean[1024];
	
	
	public abstract void initialize();

	
	public void init()
	{
		initialize();
		
		requestFocus();
		addKeyListener(this);
		
		addMouseListener(this);
		
		t = new Thread(this);
		
		t.start();		
	}

	
	public abstract void inTheGameLoop();

	
	public void run()
	{
		while(true)
		{
			inTheGameLoop();
			
 			repaint();
			
			try
			{
		      t.sleep(16);
			}
			catch(Exception x) {};
		}
	}
	
	
	public void keyPressed(KeyEvent e)
	{  
		pressed[e.getKeyCode()] = true;
	}

	
	public void keyReleased(KeyEvent e)
	{
		pressed[e.getKeyCode()] = false;
	}

	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e)  {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e)  {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e)   {}
	
}
