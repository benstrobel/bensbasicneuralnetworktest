package evolutiontest.bensbasicneuralnetwork;


import java.util.ArrayList;
import java.util.Random;

public class Network {

    private Layer inputlayer;
    private ArrayList<Layer> hiddenlayers = new ArrayList<>();
    private Layer outputlayer;
    private static double slightmutationmax = 0.2, majormutationmax = 0.6, extrememutationmax = 2;
    public static int neg = 0, pos = 0;

    public Network(int inputlayerneuroncount, int hiddenlayercount, int outputlayerneuroncount){
        inputlayer = new Layer(inputlayerneuroncount);
        Layer previouslayer = inputlayer;
        for(int i = 0; i < hiddenlayercount; i++){
            hiddenlayers.add(new Layer(previouslayer));
            previouslayer = hiddenlayers.get(hiddenlayers.size()-1);
        }
        outputlayer = new Layer(previouslayer,outputlayerneuroncount);
    }

    public Layer getInputlayer() {
        return inputlayer;
    }

    public ArrayList<Layer> getHiddenlayers() {
        return hiddenlayers;
    }

    public Layer getOutputlayer() {
        return outputlayer;
    }

    public Network cloneNetwork(){
        Network network = new Network(inputlayer.getNeurons().size(),hiddenlayers.size(),outputlayer.getNeurons().size());
        network.setInputlayer(inputlayer.cloneRawLayer());
        network.getHiddenlayers().clear();
        network.setOutputlayer(outputlayer.cloneRawLayer());
        Layer previouslayer = network.getInputlayer();
        for(int i = 0; i < hiddenlayers.size(); i++){
            previouslayer = fixLayer(network, previouslayer, hiddenlayers.get(i),i);
        }
        fixLayer(network, previouslayer, outputlayer,-1);
        return network;
    }

    private Layer fixLayer(Network network, Layer previouslayer, Layer l,int layerid) {
        Layer nlayer = l.cloneRawLayer();
        for(int x = 0; x < nlayer.getNeurons().size(); x++){
            Neuron n = nlayer.getNeurons().get(x);
                for(int i = 0; i < previouslayer.getNeurons().size(); i++){
                    if(layerid == -1){
                        n.getIncominglinks().add(new IncomingNeuron(previouslayer.getNeurons().get(i),outputlayer.getNeurons().get(x).getIncominglinks().get(i).getWeight()));
                    }else{
                        n.getIncominglinks().add(new IncomingNeuron(previouslayer.getNeurons().get(i),getHiddenlayers().get(layerid).getNeurons().get(x).getIncominglinks().get(i).getWeight()));
                    }

                }
        }
        if(layerid == -1){
            network.setOutputlayer(nlayer);
        }else{
            network.getHiddenlayers().add(nlayer);
        }
        return nlayer;
    }

    //TODO Add mutation on layer and network level (creating/destorying neurons/layers)

    private void mutateLayer(Layer l, double probability, double majorprobability){
        Random random = new Random();
        for(Neuron n : l.getNeurons()){
            for(IncomingNeuron in : n.getIncominglinks()){
                int r = random.nextInt(100);
                if(r < probability){
                    r = random.nextInt(100);
                    if(r < majorprobability){
                        double delta = (random.nextDouble()-0.5)*2*majormutationmax;
                        in.setWeight(in.getWeight()+delta);
                        if(delta < 0 ){neg++;}else{pos++;}
                    }else{
                        r = random.nextInt(100);
                        if(r < 10){
                            double delta = (random.nextDouble()-0.5)*2*extrememutationmax;
                            in.setWeight(in.getWeight()+ delta);
                            if(delta < 0 ){neg++;}else{pos++;}
                        }else{
                            double delta = (random.nextDouble()-0.5)*2*slightmutationmax;
                            in.setWeight(in.getWeight()+ delta);
                            if(delta < 0 ){neg++;}else{pos++;}
                        }
                    }
                }
            }
        }
    }

    public boolean check(){
        Layer previouslayer = inputlayer;
        for(Layer l : hiddenlayers){
            for(Neuron n : l.getNeurons()){
                for(int i = 0; i < n.getIncominglinks().size(); i++){
                    if(previouslayer.getNeurons().get(i) != n.getIncominglinks().get(i).getNeuron()){
                        System.out.println("Hidden");
                        return false;
                    }
                }
            }
            previouslayer = l;
        }
        previouslayer = hiddenlayers.get(hiddenlayers.size()-1);
        for(Neuron n : outputlayer.getNeurons()){
            for(int i = 0; i < n.getIncominglinks().size(); i++){
                if(previouslayer.getNeurons().get(i) != n.getIncominglinks().get(i).getNeuron()){
                    System.out.println("Output");
                    return false;
                }
            }
        }
        return true;
    }

    public Network weightmution(double probability, double majorprobability){
        Network network = cloneNetwork();
        for(Layer l : network.getHiddenlayers()){
            mutateLayer(l,probability,majorprobability);
        }
        mutateLayer(network.getOutputlayer(),probability,majorprobability);
        return network;
    }

    public void simulateandoutput(double [] inputlayervalues){
        double [] result = simulate(inputlayervalues);
        for(int i = 0; i < result.length; i++){
            System.out.println("Neuron " + i+ ": " + result[i]);
        }
    }

    public double[] simulate(double [] inputlayervalues){
        if(!setInputlayerValues(inputlayervalues)){return new double [0];}
        for(int i = 0; i < hiddenlayers.size(); i++){
            simulateLayer(hiddenlayers.get(i));
        }
        simulateLayer(outputlayer);
        double [] outlayervalues = new double [outputlayer.getNeurons().size()];
        for(int i = 0; i < outlayervalues.length; i++){
            outlayervalues[i] = outputlayer.getNeurons().get(i).getValue();
        }
        return outlayervalues;
    }

    private void simulateLayer(Layer layer){
        for(Neuron n : layer.getNeurons()){
            double result = 0.0;
            for(IncomingNeuron incomingNeuron : n.getIncominglinks()){
                result += incomingNeuron.getWeight()*incomingNeuron.getNeuron().getValue();
            }
            //n.setValue(result);
            n.setValue(result/n.getIncominglinks().size());
        }
    }

    private boolean setInputlayerValues(double [] inputlayervalues){
        if(inputlayervalues.length != inputlayer.getNeurons().size()){return false;}
        for(int i = 0; i < inputlayervalues.length; i++){
            inputlayer.getNeurons().get(i).setValue(inputlayervalues[i]);
        }
        return true;
    }

    private void setInputlayer(Layer inputlayer) {
        this.inputlayer = inputlayer;
    }

    private void setHiddenlayers(ArrayList<Layer> hiddenlayers) {
        this.hiddenlayers = hiddenlayers;
    }

    private void setOutputlayer(Layer outputlayer) {
        this.outputlayer = outputlayer;
    }

    public String toString(){
        String r = "";
        r += getInputlayer().toString();
        for(Layer l : getHiddenlayers()){
            r += l.toString();
        }
        r += getOutputlayer().toString();
        return r;
    }
}
