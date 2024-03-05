import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

import neuralNet.NeuralNetwork;

public class SnakeNN extends NeuralNetwork{

	public SnakePlayer snake;
	public Apple apple;

	public SnakeNN(int[] numPerLayer, SnakePlayer s, Apple a) {
		super(numPerLayer);
		
		snake = s;
		apple = a;
		
	}
	
	public SnakeNN(int[] numPerLayer, SnakePlayer s, Apple a, File snakeNNFile) {
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
		
		int snakeX = snake.x;
		int snakeY = snake.y;
		int snakeDirection = snake.direction;
		
		int edgeUp = snakeY+1;
		int edgeDn = SnakeGame.rows-snakeY;
		int edgeLt = snakeX+1;
		int edgeRt = SnakeGame.cols-snakeX;
		
		int appleX = apple.x;
		int appleY = apple.y;
		
		int bodyUp = 0;
		int bodyDn = 0;
		int bodyRt = 0;
		int bodyLt = 0;
		
		int count = 1;
		while(!snake.head.overlapsWithBody(snakeX, snakeY-count)) {
			if(snakeY-count < 0) {
				count = 0;
				break;
			}
			count++;
		}
		bodyUp = count;
		
		count = 1;
		while(!snake.head.overlapsWithBody(snakeX, snakeY+count)) {
			if(snakeY+count > SnakeGame.rows) {
				count = 0;
				break;
			}
			count++;
		}
		bodyDn = count;
		
		count = 1;
		while(!snake.head.overlapsWithBody(snakeX-count, snakeY)) {
			if(snakeX-count < 0) {
				count = 0;
				break;
			}
			count++;
		}
		bodyLt = count;
		
		count = 1;
		while(!snake.head.overlapsWithBody(snakeX+count, snakeY)) {
			if(snakeX+count > SnakeGame.cols) {
				count = 0;
				break;
			}
			count++;
		}
		bodyRt = count;
		
		
		int[] r = {snakeX, snakeY, snakeDirection, edgeUp, edgeDn, edgeLt, edgeRt, appleX, appleY, bodyUp, bodyDn, bodyRt, bodyLt};
		return r;
		
	}
	
	public void saveWeightsAndBiasesToFile(File snakeNNFile) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(snakeNNFile.toPath());
		
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
	public SnakeNN copyOf() {
		
		SnakeNN copy = new SnakeNN(npl, null, null);
		
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
