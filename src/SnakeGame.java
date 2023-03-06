import java.awt.*;
import java.io.*;
import java.util.*;

import neuralNet.Genetics;

public class SnakeGame extends GameBase{

	static Random rnd = new Random();
	
	static int rows = 20;
	static int cols = 20;
	
	int numOfGames = 1000;
	
	boolean[][][] snakeBody = new boolean[numOfGames][rows][cols];
	
	SnakePlayer[] snake = new SnakePlayer[numOfGames];
	Apple[] apple = new Apple[numOfGames];
	
	SnakeNN[] neuNet = new SnakeNN[numOfGames];
	SnakeNN smartestSnake;
	int[] nnLayers = {13, 10, 7, 4};
	
	int[][][] appleRecord = new int[numOfGames][rows*cols-1][2];
	int[] appRecCount = new int[numOfGames];
	int[][] snakeStart = new int[numOfGames][2];
	int[][] dirRecord = new int[numOfGames][rows*cols*SnakePlayer.maxTTL];
	int dirRecCount = 0;
	
	int[][] prevBestApple = new int[rows*cols-1][2];
	int[] prevBestStart = new int[2];
	int[] prevBestDir = new int[rows*cols*SnakePlayer.maxTTL];
	
	boolean isInPlayback = false;
	int pbFrame = 0;
	int pbAppleInd = 0;
	SnakePlayer pbSnake;
	Apple pbApple;
	
	int startTime = 2;
	int timer = startTime;
	int endTime = 0;
	
	private Image doubleBuffer;
	private File snakeNNFile = new File("BestSnakeNN.txt");

	@Override
	public void initialize() {
		
		setFocusable(true);
		setBackground(Color.black);
		
		for(int i = 0; i < numOfGames; i++) {
			
			snake[i] = new SnakePlayer(rnd.nextInt(rows), rnd.nextInt(cols), rnd.nextInt(4));
			apple[i] = new Apple(snakeBody[i]);
			
			neuNet[i] = new SnakeNN(nnLayers, snake[i], apple[i]);
			
			for(int j = 0; j < appleRecord[i].length; j++) {
				for(int k = 0; k < appleRecord[i][j].length; k++) {
					appleRecord[i][j][k] = -1;
				}
			}
			
			appleRecord[i][0][0] = apple[i].x;
			appleRecord[i][0][1] = apple[i].y;
			
			snakeStart[i][0] = snake[i].x;
			snakeStart[i][1] = snake[i].y;
			
			for(int j = 0; j < dirRecord[i].length; j++) {
				dirRecord[i][j] = -1;
			}
			
		}
		
		for(int i = 0; i < numOfGames/2; i++) {
			neuNet[i] = new SnakeNN(nnLayers, snake[i], apple[i], snakeNNFile);
		}
		
	}

	@Override
	public void inTheGameLoop() {
		
//			if(pressed[UP]) snake.changeDirection(SnakePlayer.UP);
//			if(pressed[DN]) snake.changeDirection(SnakePlayer.DN);
//			if(pressed[LT]) snake.changeDirection(SnakePlayer.LT);
//			if(pressed[RT]) snake.changeDirection(SnakePlayer.RT);
		

		if(!isInPlayback) {
			
			boolean allDead = true;
			
			for(int i = 0; i < numOfGames; i++) {
				
				if(!snake[i].isDead()) {
					
					dirRecord[i][dirRecCount] = neuNet[i].getDirection();
					snake[i].changeDirection(dirRecord[i][dirRecCount]);
					
					snake[i].move(apple[i]);
					
					if(apple[i].eaten) {
						snakeBody[i] = snake[i].bodyLocations();
						apple[i] = new Apple(snakeBody[i]);
						neuNet[i].apple = apple[i];
						appleRecord[i][appRecCount[i]][0] = apple[i].x;
						appleRecord[i][appRecCount[i]++][1] = apple[i].y;
						
						neuNet[i].fitness+=800;
						
					}
					
					allDead = false;
				}
				
			}
			
			dirRecCount++;
			
			if(allDead) {
				
				for(int i = 0; i < numOfGames; i++) {
					int dx = snake[i].x - apple[i].x;
					int dy = snake[i].y - apple[i].y;
					int d = dx*dx + dy*dy;
					neuNet[i].fitness += 722 - d;
				}
				
				Genetics.mergeSortByFitness(neuNet);
				smartestSnake = neuNet[neuNet.length-1];

				if(snakeNNFile == null) {
					File file = new File("BestSnakeNN.txt");
					snakeNNFile = file;
				}
				
				if(smartestSnake.fitness > fitnessOfSavedNN()) {
					try {
						smartestSnake.saveWeightsAndBiasesToFile(snakeNNFile);
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
				
				for(int i = 0; i < numOfGames; i++) {
					if(smartestSnake.snake == snake[i]) {

						for(int j = 0; j < prevBestApple.length; j++) {
							prevBestApple[j][0] = appleRecord[i][j][0];
							prevBestApple[j][1] = appleRecord[i][j][1];
						}
						
						prevBestStart[0] = snakeStart[i][0];
						prevBestStart[1] = snakeStart[i][1];
						
						
						for(int j = 0; j < prevBestDir.length; j++) {
							prevBestDir[j] = dirRecord[i][j];
						}
					}
				}
				
				Genetics.advanceGeneration(neuNet);
				
				for(int i = 0; i < numOfGames; i++) {
					snake[i] = new SnakePlayer(rnd.nextInt(rows), rnd.nextInt(cols), rnd.nextInt(4));
					snakeBody[i] = snake[i].bodyLocations();
					apple[i] = new Apple(snakeBody[i]);
					neuNet[i].snake = snake[i];
					neuNet[i].apple = apple[i];
					neuNet[i].fitness = 0;
					
					for(int j = 0; j < appleRecord[i].length; j++) {
						for(int k = 0; k < appleRecord[i][j].length; k++) {
							appleRecord[i][j][k] = -1;
						}
					}
					
					appleRecord[i][0][0] = apple[i].x;
					appleRecord[i][0][1] = apple[i].y;
					
					snakeStart[i][0] = snake[i].x;
					snakeStart[i][1] = snake[i].y;
					
					for(int j = 0; j < dirRecord[i].length; j++) {
						dirRecord[i][j] = -1;
					}
				}
				
				appRecCount = new int[numOfGames];
				dirRecCount = 0;
				
				pbSnake = new SnakePlayer(prevBestStart[0], prevBestStart[1], prevBestDir[0]);
				pbApple = new Apple(prevBestApple[0][0], prevBestApple[0][1]);
				
				isInPlayback = true;
				
			}
			
		} else {

			if(timer == endTime) {
				
				if(prevBestDir[pbFrame] > -1) {
					
					handlePlayback();
					
				} else {
					
					prevBestApple = new int[rows*cols-1][2];
					prevBestStart = new int[2];
					prevBestDir = new int[rows*cols*SnakePlayer.maxTTL];
					
					pbFrame = 0;
					pbAppleInd = 0;
					
					isInPlayback = false;
					
				}
				
				timer = startTime;
				
			} else {
				timer--;
			}
		}
		
	}
	
	public int fitnessOfSavedNN() {
		try {
			Scanner in = new Scanner(snakeNNFile);
			
			if(in.hasNextLine()) {
				String s = in.nextLine();
				
				in.close();
				return Integer.parseInt(s.split(" ")[1]);	
				
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public void handlePlayback() {
		
		pbSnake.changeDirection(prevBestDir[pbFrame++]);
		
		pbSnake.move(pbApple);
		
		if(pbApple.eaten) {
			pbApple = new Apple(prevBestApple[++pbAppleInd][0], prevBestApple[pbAppleInd][1]);
		}
		
	}
	
	public void update(Graphics g) {
		Dimension size = getSize();
		
		if (doubleBuffer == null || doubleBuffer.getWidth(this) != size.width || doubleBuffer.getHeight(this) != size.height) {
			doubleBuffer = createImage(size.width, size.height);
		}
		
		if (doubleBuffer != null) {
			Graphics g2 = doubleBuffer.getGraphics();
			paint(g2);
			g2.dispose();
			
			g.drawImage(doubleBuffer, 0, 0, null);
		} else {
			paint(g);
		}
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, 500, 500);
		
		if(pbSnake != null && pbApple != null) {
			
			pbSnake.draw(g);
			pbApple.draw(g);
			
		}
		
	}

}
