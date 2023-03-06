import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Apple {
	
	int x;
	int y;
	
	int w = 25;
	int h = w;
	
	boolean eaten = false;
	
	Random rnd = new Random();
	
	public Apple() {
		x = 0;
		y = 0;
	}
	
	public Apple(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Apple(boolean[][] unavailable) {
		
		int nx = -1;
		int ny = -1;
		
		do {
			nx = rnd.nextInt(SnakeGame.rows);
			ny = rnd.nextInt(SnakeGame.cols);
		}while(unavailable[nx][ny]);
		
		x = nx;
		y = ny;
		
	}
	
	public void draw(Graphics g) {
		
		g.setColor(Color.red);
		g.fillRect(x*w, y*h, w, h);
		
	}
}
