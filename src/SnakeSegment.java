import java.awt.Color;
import java.awt.Graphics;

public class SnakeSegment {
	
	int x;
	int y;
	
	int w = 25;
	int h = w;
	
	SnakeSegment nextSegment;
	
	public SnakeSegment() {
		x = 0;
		y = 0;
	}
	
	public SnakeSegment(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(int x, int y) {
		
		if(nextSegment != null) {
			nextSegment.move(this.x, this.y);
		}
		
		this.x = x;
		this.y = y;
		
	}
	
	public void elongate(int x, int y) {
		
		if(nextSegment == null) {
			nextSegment = new SnakeSegment(this.x, this.y);
		} else {
			nextSegment.elongate(this.x, this.y);
		}
		
		this.x = x;
		this.y = y;
		
	}
	
	public boolean[][] bodyLocation(boolean[][] b) {
		
		if(nextSegment != null) {
			b = nextSegment.bodyLocation(b);
		}
		
		b[x][y] = true;
		
		return b;
		
	}
	
	public boolean overlapsWithBody(int x, int y) {
		
		if(this.x == x && this.y == y) {
			return true;
		} else if(nextSegment != null) {
			return nextSegment.overlapsWithBody(x, y);
		}
		
		return false;
		
	}
	
	public void draw(Graphics g, int n) {
		
		if(nextSegment != null) {
			nextSegment.draw(g, n+1);
		}
		
		int rb = n*10;
		if(rb > 255) {
			rb = 255;
		}
		g.setColor(new Color(rb, 255, rb));
		g.fillRect(x*w, y*h, w, h);
		
	}

}
