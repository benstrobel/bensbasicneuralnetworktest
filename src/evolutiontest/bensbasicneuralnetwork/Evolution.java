package evolutiontest.bensbasicneuralnetwork;

import bensbasicgameengine.Lib.Tools;
import evolutiontest.BoolWrapper;
import evolutiontest.EvolutionTestChamber;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Evolution {

    ArrayList<EvaluatedNetwork []> networks = new ArrayList<>();
    private static final int evosteps = 200, popsize = 25;
    private int probability, majorprobability;
    private EvolutionTestChamber evolutionTestChamber;
    private int cg = 0;

    //TODO find error and fix it, probably in network cloning or in mutation the references get fucked up. Later layers (first two are fine) dont properly point to previous ones
    public Evolution(int probability, int majorprobability){
        this.probability = probability;
        this.majorprobability = majorprobability;
        EvaluatedNetwork buff [] = {new EvaluatedNetwork(new Network(3,2,1),0)};
        networks.add(buff);
        evolutionTestChamber = new EvolutionTestChamber();
        //simDesignedNetwork();
        for(int i = 0; i < evosteps; i++){
            evolutionstep();
            cg++;
        }
        printEvaluatedNetworkArray(networks.get(networks.size()-1));
        System.out.println("Printing   Avg \t\t\t\tBst \t\tGeneration Fitness: ");
        for(int i = 0; i < networks.size(); i++){
            EvaluatedNetwork [] generation = networks.get(i);
            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println("Gen. " + i + " : " + df.format(getAverageGenerationFitness(generation)) + "\t\t\t\t" + df.format(getBestFitnessofGeneration(generation)));
        }
    }

    private Network getDesignedNetwork(){
        Network network = new Network(3,1,1);
        // Hiddenlayer
        network.getHiddenlayers().get(0).getNeurons().get(0).getIncominglinks().get(0).setWeight(-1);    //links
        network.getHiddenlayers().get(0).getNeurons().get(0).getIncominglinks().get(1).setWeight(0);
        network.getHiddenlayers().get(0).getNeurons().get(0).getIncominglinks().get(2).setWeight(1);

        network.getHiddenlayers().get(0).getNeurons().get(1).getIncominglinks().get(0).setWeight(1);    //rechts
        network.getHiddenlayers().get(0).getNeurons().get(1).getIncominglinks().get(1).setWeight(0);
        network.getHiddenlayers().get(0).getNeurons().get(1).getIncominglinks().get(2).setWeight(-1);

        network.getHiddenlayers().get(0).getNeurons().get(2).getIncominglinks().get(0).setWeight(0);    //nothing
        network.getHiddenlayers().get(0).getNeurons().get(2).getIncominglinks().get(1).setWeight(0);
        network.getHiddenlayers().get(0).getNeurons().get(2).getIncominglinks().get(2).setWeight(0);

        //OutputLayer
        network.getOutputlayer().getNeurons().get(0).getIncominglinks().get(0).setWeight(-1);
        network.getOutputlayer().getNeurons().get(0).getIncominglinks().get(1).setWeight(1);
        network.getOutputlayer().getNeurons().get(0).getIncominglinks().get(2).setWeight(0);

        return network;
    }

    public double getAverageGenerationFitness(EvaluatedNetwork [] generation){
        double fitnesssum = 0;
        for(EvaluatedNetwork evn : generation){
            fitnesssum += evn.getFitness();
        }
        return (fitnesssum/generation.length);
    }

    public double getAverageGenerationFitness(double [] generation){
        double fitnesssum = 0;
        for(double evn : generation){
            fitnesssum += evn;
        }
        return (fitnesssum/generation.length);
    }

    public double getBestFitnessofGeneration(double [] generation){
        double best = generation[0];
        for(double evn : generation){
            if(evn > best){
                best = evn;
            }
        }
        return best;
    }

    public double getBestFitnessofGeneration(EvaluatedNetwork [] generation){
        EvaluatedNetwork best = generation[0];
        for(EvaluatedNetwork evn : generation){
            if(evn.getFitness() > best.getFitness()){
                best = evn;
            }
        }
        return best.getFitness();
    }

    public void printEvaluatedNetworkArray(EvaluatedNetwork [] result){
        for(EvaluatedNetwork evn : result){
            System.out.println(evn);
        }
    }

    private void simDesignedNetwork(){
        Network network = getDesignedNetwork();

        BoolWrapper [] in, out;
        in = new BoolWrapper[15];
        out = new BoolWrapper[2];
        for(int i = 0; i < in.length; i++){in[i] = new BoolWrapper();}
        for(int i = 0; i < out.length; i++){out[i] = new BoolWrapper();}

        System.out.println(evolutionTestChamber.simulate(in,out,network));
    }

    private void evolutionstep(){
        EvaluatedNetwork [] currentgen = networks.get(networks.size()-1);
        Network [] nextgen = new Network[currentgen.length*popsize];
        for(int i = 0; i < currentgen.length; i++){
            for(int x = 0; x < popsize; x++){
                nextgen[i*popsize+x] = currentgen[i].getNetwork().weightmution(probability,majorprobability);
            }
        }
        for(int i = 0; i < currentgen.length; i++){
            nextgen[i] = currentgen[i].getNetwork().cloneNetwork();
        }
        double [] fitness = new double [nextgen.length];

        BoolWrapper [] in, out;
        in = new BoolWrapper[15];
        out = new BoolWrapper[2];
        for(int i = 0; i < in.length; i++){in[i] = new BoolWrapper();}
        for(int i = 0; i < out.length; i++){out[i] = new BoolWrapper();}

        for(int i = 0; i < nextgen.length; i++){
            fitness[i] = evolutionTestChamber.simulate(in,out,nextgen[i]);
            //System.out.println("Gen " + cg + " - " + "Id: " + i + " Fitness: " + fitness[i]);
            for(BoolWrapper bw : in){bw.setState(false);}
            for(BoolWrapper bw : out){bw.setState(false);}
        }
        System.out.println("Gen " + cg + " - " + " Avg. Fitness: " + getAverageGenerationFitness(fitness) + "\t\t" + "Bst: " + getBestFitnessofGeneration(fitness));
        networks.add(savebestandrandom(nextgen,fitness));
    }


    //TODO Clean up this mess (unefficient, unelegant)
    private EvaluatedNetwork [] savebestandrandom(Network [] nextgen, double [] fitness){
        EvaluatedNetwork buffer [] = new EvaluatedNetwork [6];
        int index0 = 0, index1 = 0, index2 = 0, index3 = 0, index4 = 0, index5 = 0;
        for(int i = 1; i <  nextgen.length; i++){
            if(fitness[index0] < fitness[i]){
                index0 = i;
            }
        }
        for(int i = 1; i <  nextgen.length; i++){
            if(fitness[index1] < fitness[i] && i != index0){
                index1 = i;
            }
        }
        for(int i = 1; i <  nextgen.length; i++){
            if(fitness[index2] < fitness[i] && i != index0 && i != index1){
                index2 = i;
            }
        }
        for(int i = 1; i <  nextgen.length; i++){
            if(fitness[index3] < fitness[i] && i != index0 && i != index1 && i != index2){
                index3 = i;
            }
        }
        for(int i = 1; i <  nextgen.length; i++){
            if(fitness[index4] < fitness[i] && i != index0 && i != index1 && i != index2 && i != index3){
                index4 = i;
            }
        }
        //best four networks
        buffer [0] = (new EvaluatedNetwork(nextgen[index0],fitness[index0]));
        buffer [1] = (new EvaluatedNetwork(nextgen[index1],fitness[index1]));
        buffer [2] = (new EvaluatedNetwork(nextgen[index2],fitness[index2]));
        buffer [3] = (new EvaluatedNetwork(nextgen[index3],fitness[index3]));
        buffer [4] = (new EvaluatedNetwork(nextgen[index4],fitness[index4]));
        //random three networks
        Random r = new Random();
        index5 = r.nextInt(fitness.length);
        buffer [5] = (new EvaluatedNetwork(nextgen[index5],fitness[index5]));
        return buffer;
    }

    private double evaluate(double [] is, double [] expected){
        double fitness = 1.0;
        for(int i = 0; i < expected.length; i++){
            fitness -= Tools.differance(expected[i],is[i]);
        }
        return fitness;
    }

    private class EvaluatedNetwork{

        private Network network;
        private double fitness;

        public EvaluatedNetwork(Network network, double fitness){
            this.network = network;
            this.fitness = fitness;
        }

        public double getFitness() {
            return fitness;
        }

        public Network getNetwork() {
            return network;
        }

        public String toString(){
            return "Fitness: " + fitness;
        }
    }
}
