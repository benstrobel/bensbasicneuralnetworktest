package evolutiontest.bensbasicneuralnetwork;

import java.util.ArrayList;

public class Layer {

    private ArrayList<Neuron> neurons = new ArrayList<>();

    public Layer(int neuroncount){
        for(int i = 0; i < neuroncount; i++){
            neurons.add(new Neuron(null));
        }
    }

    public Layer(Layer previouslayer){
        if(previouslayer == null){return;}
        for(int i = 0; i < previouslayer.getNeurons().size(); i++){
            neurons.add(new Neuron(previouslayer));
        }
    }

    public Layer(Layer previouslayer, int outputlayerneuroncount){
        if(previouslayer == null){return;}
        for(int i = 0; i < outputlayerneuroncount; i++){
            neurons.add(new Neuron(previouslayer));
        }
    }

    public Layer cloneRawLayer(){
        Layer layer = new Layer(null);
        for(Neuron n : neurons){
            layer.addNeuron(n.cloneRawNeuron());
        }
        return layer;
    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    public boolean removeNeuron(Neuron n){
        return neurons.remove(n);
    }

    public void addNeuron(Neuron n){
        neurons.add(n);
    }

    public String toString(){
        String r = "";
        for(Neuron n : neurons){
            r += n.toString();
        }
        return r;
    }
}
