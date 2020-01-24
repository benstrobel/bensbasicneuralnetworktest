package evolutiontest.bensbasicneuralnetwork;

import java.util.ArrayList;
import java.util.Random;

public class Neuron{

    private ArrayList<IncomingNeuron> incominglinks = new ArrayList<>();
    private double value;
    private double threshhold = 0.0;

    public Neuron(Layer previouslayer, double initvalue, double initthreshhold){
        value = initvalue;
        threshhold = initthreshhold;
        if(previouslayer == null){return;}
        Random r  = new Random();
        for(Neuron n : previouslayer.getNeurons()){
            incominglinks.add(new IncomingNeuron(n,(r.nextDouble()*2-1.0)));
        }
    }

    public Neuron(Layer previouslayer, double initvalue){
        value = initvalue;
        if(previouslayer == null){return;}
        Random r  = new Random();
        for(Neuron n : previouslayer.getNeurons()){
            incominglinks.add(new IncomingNeuron(n,(r.nextDouble()*2-1.0)));
        }
    }

    public Neuron(Layer previouslayer){
        if(previouslayer == null){return;}
        Random r  = new Random();
        for(Neuron n : previouslayer.getNeurons()){
            incominglinks.add(new IncomingNeuron(n,(r.nextDouble()*2-1.0)));
        }
    }

    public Neuron cloneRawNeuron(){
        Neuron neuron = new Neuron(null);
        neuron.setValue(value);
        neuron.setThreshhold(threshhold);
        return neuron;
    }

    public double getThreshhold() {
        return threshhold;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setThreshhold(double threshhold) {
        this.threshhold = threshhold;
    }

    public ArrayList<IncomingNeuron> getIncominglinks() {
        return incominglinks;
    }

    public void setIncominglinks(ArrayList<IncomingNeuron> incominglinks) {
        this.incominglinks = incominglinks;
    }

    public String toString(){
        String r = "";
        for(IncomingNeuron in : incominglinks){
            r += in.toString();
        }
        return r;
    }
}
