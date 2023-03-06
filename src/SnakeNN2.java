import java.awt.*;
import java.io.*;
import java.util.*;

import neuralNet.NeuralNetwork;

public class SnakeNN2 extends NeuralNetwork{

	public SnakePlayer snake;
	public Apple apple;

	public SnakeNN2(int[] numPerLayer, SnakePlayer s, Apple a) {
		super(numPerLayer);
		
		snake = s;
		apple = a;
		
	}
	
	public SnakeNN2(int[] numPerLayer, SnakePlayer s, Apple a, File snakeNNFile) {
		super(numPerLayer);
		
		snake = s;
		apple = a;
		
		try {
			loadWeightsAndBiasesFromFile(snakeNNFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getDirection() {
		
		setInputNodes(getInputs());
		double[] out = calculateOutputs();
		
		int strongest = 0;
		
		for(int i = 0; i < out.length; i++) {
			if(out[i] > out[strongest]) strongest = i;
		}
		
		return strongest;
		
	}
	
	public int[] getInputs() {
		
		int[][] grid = new int[20][20];
		
		boolean[][] b = snake.bodyLocations();
		
		for(int i = 0; i < b.length; i++) {
			for(int j = 0; j < b[i].length; j++) {
				if(b[i][j]) grid[i][j] = 2;
			}
		}
		
		grid[snake.x][snake.y] = 1;
		
		grid[apple.x][snake.y] = 3;
		
		int[] r = new int[grid.length * grid[0].length];
		
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				r[i * grid[i].length + j] = grid[i][j];
			}
		}
		
		return r;
		
	}
	
	public void saveWeightsAndBiasesToFile(File snakeNNFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(snakeNNFile));
		
		writer.write("Fitness: "+fitness);
		
		writer.newLine();
		
		String weights = "";
		for(int i = 0; i < connections.length; i++) {
			weights += connections[i].weight + " ";
		}
		writer.write(weights);
		writer.newLine();
		
		for(int i = 0; i < network.length; i++) {
			String biases = "";
			for(int j = 0; j < network[i].length; j++) {
				biases += network[i][j].bias + " ";
			}
			biases += "\n";
			writer.write(biases);
		}
		writer.close();
	}
	
	public void loadWeightsAndBiasesFromFile(File snakeNNFile) throws IOException {
		Scanner in = new Scanner(snakeNNFile);
		in.nextLine();
		
		String[] weights = in.nextLine().split(" ");
		for(int i = 0; i < connections.length; i++) {
			connections[i].setWeight(Double.parseDouble(weights[i]));
		}
		
		for(int i = 0; i < network.length; i++) {
			String[] biases = in.nextLine().split(" ");
			for(int j = 0; j < network[i].length; j++) {
				network[i][j].setBias(Integer.parseInt(biases[j]));
			}
		}
		in.close();
	}
	
	@Override
	public SnakeNN2 copyOf() {
		
		SnakeNN2 copy = new SnakeNN2(npl, null, null);
		
		for(int i = 0; i < network.length; i++) {
			for(int j = 0; j < network[i].length; j++) {
				copy.network[i][j].bias = network[i][j].bias;
			}
		}
		
		for(int i = 0; i < connections.length; i++) {
			copy.connections[i].weight = connections[i].weight;
		}
		
		return copy;
		
	}
	
	public void drawOutputs(Graphics g) {
		int centerX = snake.x + snake.w/2;
		int centerY = snake.y + snake.h/2;
		
		double[] out = calculateOutputs();
		double largest = 0;
		for(int i = 0; i < out.length; i++) {
			if (out[i] > largest) largest = out[i];
		}
		
		double multiplier = 75 / largest;
		
		for(int i = 0; i < out.length; i++) {
			out[i] *= multiplier;
		}
		
		g.setColor(Color.white);
		g.drawLine(centerX, centerY, centerX, centerY - (int)out[0]);
		g.drawLine(centerX, centerY, centerX + (int)out[1], centerY);
		g.drawLine(centerX, centerY, centerX, centerY + (int)out[2]);
		g.drawLine(centerX, centerY, centerX - (int)out[3], centerY);
		
	}

}
