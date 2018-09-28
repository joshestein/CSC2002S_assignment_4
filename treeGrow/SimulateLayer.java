package treeGrow;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

import treeGrow.SunData;

class SimulateLayer extends RecursiveAction{
    static int SEQUENTIAL_CUTOFF = 10000;
    int start;
    int end;
    ArrayList<Tree> trees = new ArrayList<Tree>();
    Land land;

    SimulateLayer(int start, int end, ArrayList<Tree> trees, Land land){
        this.start = start;
        this.end = end;
        this.trees = trees;
        this.land = land;
    }

    public void compute(){
        double total = 0.0;
        if (end - start <= SEQUENTIAL_CUTOFF){
            for (int i = start; i < end; ++i){
                land.shadow(trees.get(i));
                trees.get(i).sungrow(land);
            }
        } else {
            SimulateLayer left = new SimulateLayer(start, (start+end)/2, trees, land);
            SimulateLayer right = new SimulateLayer((start+end)/2, end, trees, land);
            left.fork();
            right.compute();
            left.join();
        }
    }
}