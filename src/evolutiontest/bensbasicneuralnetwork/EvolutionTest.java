package evolutiontest.bensbasicneuralnetwork;

import bensbasicgameengine.Lib.Tools;

import java.util.ArrayList;

public class EvolutionTest {

    ArrayList<EvaluatedNetwork []> networks = new ArrayList<>();
    private static final int evosteps = 10, popsize = 5;


    //TODO find error and fix it, probably in network cloning or in mutation the references get fucked up. Later layers (first two are fine) dont properly point to previous ones
    public EvolutionTest(){
        EvaluatedNetwork buff [] = {new EvaluatedNetwork(new Network(3,2,3),0)};
        networks.add(buff);
        for(int i = 0; i < evosteps; i++){
            evolutionstep();
        }
        System.out.println();
    }

    private void evolutionstep(){
        EvaluatedNetwork [] currentgen = networks.get(networks.size()-1);
        Network [] nextgen = new Network[currentgen.length*popsize];
        for(int i = 0; i < currentgen.length; i++){
            for(int x = 0; x < popsize; x++){
                nextgen[i+x] = currentgen[i].getNetwork().weightmution(0,0);
            }
        }
        double [] fitness = new double [nextgen.length];
        for(int i = 0; i < nextgen.length; i++){
            double fit = 0.0;
            fit += evaluate(nextgen[i].simulate(new double[] {1,1,1} ),new double [] {0,0,1});
            fit += evaluate(nextgen[i].simulate(new double[] {0,1,0} ),new double [] {0,1,0});
            fit += evaluate(nextgen[i].simulate(new double[] {1,0,0} ),new double [] {1,0,0});
            fitness[i] = fit;
        }
        networks.add(savebestfive(nextgen,fitness));
    }

    //TODO Clean up this mess
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
    }
}
