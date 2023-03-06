package neuralNet;

import java.io.File;
import java.util.Random;

public class NeuralNetwork {
	
	int hiddenLayers = 0;
	
	public Neuron[][] network;
	public NeuronConnection[] connections;
	
	public int [] npl;
	
	public int fitness = 0;
	
	Random rnd = new Random();
	
	public NeuralNetwork(int[] numPerLayer) {
		
		npl = numPerLayer;
		
		if (numPerLayer.length >= 2) hiddenLayers = numPerLayer.length - 2;
		
		network = new Neuron[npl.length][];
		for(int i = 0; i < numPerLayer.length; i++) {
			Neuron[] temp = new Neuron[numPerLayer[i]];
			for(int j = 0; j < numPerLayer[i]; j++) {
				temp[j] = new Neuron();
			}
			network[i] = temp;
		}
		
		int con = 0;
		for(int i = 0; i < numPerLayer.length - 1; i++) {
			con += numPerLayer[i]*numPerLayer[i+1];
		}
		connections = new NeuronConnection[con];
		
		int counter = 0;
		for(int i = 0; i < network.length-1; i++) {
			for(int j = 0; j < network[i].length; j++) {
				for(int k = 0; k < network[i+1].length; k++) {
					connections[counter++] = new NeuronConnection(network[i][j], network[i+1][k]);
				}
			}
		}
		
		for(int i = 0; i < network.length; i++) {
			for( int j = 0; j < network[i].length; j++) {
				
				Neuron n = network[i][j];
				
				NeuronConnection[] in = {};
				NeuronConnection[] out = {};
				int inCount = 0;
				int outCount = 0;
				
				if(i > 0) {
					in = new NeuronConnection[network[i-1].length];
				}
				if(i < network.length-1) {
					out = new NeuronConnection[network[i+1].length];
				}
				
				for(int k = 0; k < connections.length; k++) {
					NeuronConnection nc = connections[k];
					if(nc.from == n) {
						out[outCount++] = nc;
					} else if(nc.to == n) {
						in[inCount++] = nc;
					}
				}
				n.in = in;
				n.out = out;
			}
		}
		
		randomize();
		
	}
	
	public NeuralNetwork(int[] numPerLayer, String fileName) {
		
		npl = numPerLayer;
		
		if (numPerLayer.length >= 2) hiddenLayers = numPerLayer.length - 2;
		
		network = new Neuron[npl.length][];
		for(int i = 0; i < numPerLayer.length; i++) {
			Neuron[] temp = new Neuron[numPerLayer[i]];
			for(int j = 0; j < numPerLayer[i]; j++) {
				temp[j] = new Neuron();
			}
			network[i] = temp;
		}
		
		int con = 0;
		for(int i = 0; i < numPerLayer.length - 1; i++) {
			con += numPerLayer[i]*numPerLayer[i+1];
		}
		connections = new NeuronConnection[con];
		
		int counter = 0;
		for(int i = 0; i < network.length-1; i++) {
			for(int j = 0; j < network[i].length; j++) {
				for(int k = 0; k < network[i+1].length; k++) {
					connections[counter++] = new NeuronConnection(network[i][j], network[i+1][k]);
				}
			}
		}
		
		for(int i = 0; i < network.length; i++) {
			for( int j = 0; j < network[i].length; j++) {
				
				Neuron n = network[i][j];
				
				NeuronConnection[] in = {};
				NeuronConnection[] out = {};
				int inCount = 0;
				int outCount = 0;
				
				if(i > 0) {
					in = new NeuronConnection[network[i-1].length];
				}
				if(i < network.length-1) {
					out = new NeuronConnection[network[i+1].length];
				}
				
				for(int k = 0; k < connections.length; k++) {
					NeuronConnection nc = connections[k];
					if(nc.from == n) {
						out[outCount++] = nc;
					} else if(nc.to == n) {
						in[inCount++] = nc;
					}
				}
				n.in = in;
				n.out = out;
			}
		}
		
	}
	
	public void randomize() {
		
		for(int i = 0; i < network.length; i++) {
			for(int j = 0; j < network[i].length; j++) {
				network[i][j].bias = rnd.nextInt(10)-5;
			}
		}
		
		for(int i = 0; i < connections.length; i++) {
			connections[i].weight = rnd.nextDouble()*10-5;
		}
		
	}
	
	public void setInputNodes(int[] input) {
		
		if (input.length != npl[0]) return;
		
		for(int i = 0; i < npl[0]; i++) {
			network[0][i].value = input[i];
		}
		
	}
	
	public double[] getOutput() {
		
		double[] outputs = new double[npl[npl.length-1]];
		
		for(int i = 0; i < npl[npl.length-1]; i++) {
			outputs[i] = network[network.length-1][i].value;
		}
		
		return outputs;
		
	}
	
	public double[] calculateOutputs() {
		
		for(int i = 1; i < network.length; i++) {
			for(int j = 0; j < network[i].length; j++) {
				network[i][j].calculateValue();
			}
		}
		
		double[] outputs = new double[network[network.length-1].length];
		
		for(int i = 0; i < outputs.length; i++) {
			outputs[i] = network[network.length-1][i].value;
		}
		
		return outputs;
		
	}
	
	public NeuralNetwork copyOf() {
		
		NeuralNetwork copy = new NeuralNetwork(npl);
		
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

}
