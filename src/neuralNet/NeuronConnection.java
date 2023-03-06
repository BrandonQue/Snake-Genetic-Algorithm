package neuralNet;

public class NeuronConnection {
	
	public double weight;
	
	Neuron from;
	Neuron to;
	
	public NeuronConnection() {
		weight = 0;
		from = null;
		to = null;
	}

	public NeuronConnection(double w, Neuron f, Neuron t) {
		weight = w;
		from = f;
		to = t;
	}

	public NeuronConnection(Neuron f, Neuron t) {
		weight = 0;
		from = f;
		to = t;
	}
	
	public void setWeight(double w) {
		weight = w;
	}
	
}
