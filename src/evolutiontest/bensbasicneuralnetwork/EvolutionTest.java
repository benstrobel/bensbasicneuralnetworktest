package evolutiontest.bensbasicneuralnetwork;

import bensbasicgameengine.Lib.Tools;
import evolutiontest.Example;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EvolutionTest {

    ArrayList<EvaluatedNetwork []> networks = new ArrayList<>();
    private static final int evosteps = 200, popsize = 100;
    private int probability, majorprobability;
    private Example example;

    //TODO find error and fix it, probably in network cloning or in mutation the references get fucked up. Later layers (first two are fine) dont properly point to previous ones
    public EvolutionTest(int probability, int majorprobability){
        this.probability = probability;
        this.majorprobability = majorprobability;
        EvaluatedNetwork buff [] = {new EvaluatedNetwork(new Network(3,2,3),0)};
        networks.add(buff);
        for(int i = 0; i < evosteps; i++){
            evolutionstep();
        }
        printEvaluatedNetworkArray(networks.get(networks.size()-1));
        System.out.println("Printing   Avg \t\t\t\tBst \t\tGeneration Fitness: ");
        for(int i = 0; i < networks.size(); i++){
            EvaluatedNetwork [] generation = networks.get(i);
            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println("Gen. " + i + " : " + df.format(getAverageGenerationFitness(generation)) + "\t\t\t\t" + df.format(getBestFitnessofGeneration(generation)));
        }
        example = new Example();
    }

    public double getAverageGenerationFitness(EvaluatedNetwork [] generation){
        double fitnesssum = 0;
        for(EvaluatedNetwork evn : generation){
            fitnesssum += evn.getFitness();
        }
        return (fitnesssum/generation.length);
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
        for(int i = 0; i < nextgen.length; i++){
            double fit = 0.0;

            fitness[i] = fit;
        }
        networks.add(savebestfive(nextgen,fitness));
    }


    //TODO Clean up this mess (unefficient, unelegant)
    private EvaluatedNetwork [] savebestfive(Network [] nextgen, double [] fitness){
        EvaluatedNetwork buffer [] = new EvaluatedNetwork [5];
        int index0 = 0, index1 = 0, index2 = 0, index3 = 0, index4 = 0;
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
        buffer [0] = (new EvaluatedNetwork(nextgen[index0],fitness[index0]));
        buffer [1] = (new EvaluatedNetwork(nextgen[index1],fitness[index1]));
        buffer [2] = (new EvaluatedNetwork(nextgen[index2],fitness[index2]));
        buffer [3] = (new EvaluatedNetwork(nextgen[index3],fitness[index3]));
        buffer [4] = (new EvaluatedNetwork(nextgen[index4],fitness[index4]));
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
