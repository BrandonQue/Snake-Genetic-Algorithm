package neuralNet;

import java.util.Random;

public class Genetics {
	
	static Random rnd = new Random();
	
	public static void advanceGeneration(NeuralNetwork[] nn) {
		mergeSortByFitness(nn);
		
		for(int i = 0; i < nn.length/2; i++) {
			nn[i] = null;
		}
		
		NeuralNetwork[] fit = new NeuralNetwork[nn.length/2];
		for(int i = nn.length/2; i < nn.length; i++) {
			fit[i-nn.length/2] = nn[i];
		}
		
		NeuralNetwork[] children = new NeuralNetwork[nn.length/2];
		for(int i = 0; i < fit.length; i++) {
			children[i] = selection(fit);
		}
		for(int i = 0; i < children.length/2; i++) {
			crossover(children[i], children[children.length-1-i]);
		}
		for(int i = 0; i < children.length; i++) {
			mutation(children[i]);
		}
		for(int i = 0; i < children.length; i++) {
			nn[i] = children[i];
		}
		
	}
	
	public static NeuralNetwork selection(NeuralNetwork[] nn) {
		
		NeuralNetwork[] sel = new NeuralNetwork[nn.length*(nn.length+1)/2];
		
		int selInd = 0;
		for(int i = 0; i < nn.length; i++) {
			for(int j = 0; j < i+1; j++) {
				sel[selInd++] = nn[i];
			}
		}
		
		return sel[rnd.nextInt(sel.length)].copyOf();
	}
	
	public static void crossover(NeuralNetwork nn1, NeuralNetwork nn2) {
		
		for(int i = 0; i < nn1.network.length; i++) {
			for(int j = 0; j < nn1.network[i].length; j++) {
				if(rnd.nextDouble() < 0.1) {
					int temp = nn1.network[i][j].bias;
					nn1.network[i][j].bias = nn2.network[i][j].bias;
					nn2.network[i][j].bias = temp;
				}
			}
		}
		
		for(int i = 0; i < nn1.connections.length; i++) {
			if(rnd.nextDouble() < 0.1) {
				double temp = nn1.connections[i].weight;
				nn1.connections[i].weight = nn2.connections[i].weight;
				nn2.connections[i].weight = temp;
			}
		}
		
	}
	
	public static void mutation(NeuralNetwork nn) {
		
		double mutationRate = 0.2;
		
		for(int i = 0; i < nn.network.length; i++) {
			for(int j = 0; j < nn.network[i].length; j++) {
				if(rnd.nextDouble() < mutationRate) {
					nn.network[i][j].bias += rnd.nextInt(5)-2;
				}
			}
		}
		
		for(int i = 0; i < nn.connections.length; i++) {
			if(rnd.nextDouble() < mutationRate) {
				nn.connections[i].weight += rnd.nextDouble()*4-2;
			}
		}
		
	}
	
	public static void mergeSortByFitness(NeuralNetwork[] nnArr) {
		
		if(nnArr.length > 1) {
			NeuralNetwork[] leftnnArr = new NeuralNetwork[nnArr.length/2];
			NeuralNetwork[] rightnnArr = new NeuralNetwork[nnArr.length-nnArr.length/2];
			split(nnArr, leftnnArr, rightnnArr);
			mergeSortByFitness(leftnnArr);
			mergeSortByFitness(rightnnArr);
			merge(nnArr, leftnnArr, rightnnArr);
		}
		
	}
	
	private static void split(NeuralNetwork[] big, NeuralNetwork[] left, NeuralNetwork[] right) {
		for(int i = 0; i < left.length; i++) {
			left[i] = big[i];
		}
		for(int i = 0; i < right.length; i++) {
			right[i] = big[left.length+i];
		}
	}
	
	private static void merge(NeuralNetwork[] big, NeuralNetwork[] left, NeuralNetwork[] right) {
		int bI = 0;
		int rI = 0;
		int lI = 0;
		
		while(lI < left.length && rI < right.length) {
			if(left[lI].fitness < right[rI].fitness) {
				big[bI] = left[lI];
				lI++;
			}else {
				big[bI] = right[rI];
				rI++;
			}
			bI++;
		}
		
		while(lI < left.length) {
			big[bI] = left[lI];
			lI++;
			bI++;
		}
		
		while(rI < right.length) {
			big[bI] = right[rI];
			rI++;
			bI++;
		}
		
		
	}

}
