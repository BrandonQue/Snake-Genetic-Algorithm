import java.awt.Graphics;
import java.util.Random;

public class SnakePlayer {
	
	public static final int UP = 0;
	public static final int RT = 1;
	public static final int DN = 2;
	public static final int LT = 3;
	
	int x;
	int y;
	
	int w = 25;
	int h = w;
	
	int prevDir;
	int direction;
	
	SnakeSegment head;
	
	public static int maxTTL = 40;
	int ttl = maxTTL;
	
	Random rnd = new Random();
	
	public SnakePlayer() {
		x = 0;
		y = 0;
		
		direction = 0;
	}
	
	public SnakePlayer(int x, int y, int dir) {
		this.x = x;
		this.y = y;
		
		direction = dir;
		
		head = new SnakeSegment(x, y);
	}
	
	public void changeDirection(int dir) {
		switch(prevDir) {
		case UP:
			if(dir != DN) direction = dir;
			break;
		case RT:
			if(dir != LT) direction = dir;
			break;
		case DN:
			if(dir != UP) direction = dir;
			break;
		case LT:
			if(dir != RT) direction = dir;
			break;
				
		}
	}
	
	public void move(Apple apple) {
		
		switch(direction) {
		case UP:
			--y;
			if(x == apple.x && y == apple.y) {
				ttl = maxTTL;
				head.elongate(x, y);
				apple.eaten = true;
			} else {
				head.move(x, y);
			}
			break;
			
		case DN:
			++y;
			if(x == apple.x && y == apple.y) {
				ttl = maxTTL;
				head.elongate(x, y);
				apple.eaten = true;
			} else {
				head.move(x, y);
			}
			break;
			
		case LT:
			--x;
			if(x == apple.x && y == apple.y) {
				ttl = maxTTL;
				head.elongate(x, y);
				apple.eaten = true;
			} else {
				head.move(x, y);
			}
			break;
			
		case RT:
			++x;
			if(x == apple.x && y == apple.y) {
				ttl = maxTTL;
				head.elongate(x, y);
				apple.eaten = true;
			} else {
				head.move(x, y);
			}
			break;
		}
		
		prevDir = direction;
		
		ttl--;
		
	}
	
	public boolean isDead() {
		if(ttl < 0 || x < 0 || x >= 20 || y < 0 || y >= 20) {
			return true;
		}
		
		if (head.nextSegment != null && head.nextSegment.overlapsWithBody(head.x, head.y)) {
			return true;
		}
		
		return false;
	}

	public boolean[][] bodyLocations() {
		
		boolean[][] snakeBody = new boolean[SnakeGame.rows][SnakeGame.cols];
		return head.bodyLocation(snakeBody);
		
	}
	
	public void draw(Graphics g) {
		head.draw(g, 0);
	}

}
