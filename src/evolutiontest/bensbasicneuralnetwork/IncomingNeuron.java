package evolutiontest.bensbasicneuralnetwork;

public class IncomingNeuron {

    private Neuron neuron;
    private double weight;

    public IncomingNeuron(Neuron neuron, double weight){
        this.neuron = neuron;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public Neuron getNeuron() {
        return neuron;
    }

    public void setNeuron(Neuron neuron) {
        this.neuron = neuron;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String toString(){
        return ""+weight;
    }
}
