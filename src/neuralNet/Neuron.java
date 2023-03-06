package neuralNet;

public class Neuron {

	double value;
	public int bias;
	
	NeuronConnection[] in;
	NeuronConnection[] out;
	
	public Neuron() {
		value = 0;
		bias = 0;
	}
	
	public Neuron(double v, int b) {
		value = v;
		bias = b;
	}
	
	public void setValue(double v) {
		value = v;
	}
	
	public void setBias(int b) {
		bias = b;
	}
	
	public void calculateValue() {
		
		double sum = bias;
		for(int i = 0; i < in.length; i++) {
			sum += in[i].from.value * in[i].weight;
		}
		
		value = sigmoid(sum);
			
	}
	
	public double sigmoid(double n) {
		double e = Math.exp(n);
		
		return e / (e+1);
	}
	
}
