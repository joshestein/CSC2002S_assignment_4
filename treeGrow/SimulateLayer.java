package treeGrow;

import java.util.concurrent.RecursiveAction;

import treeGrow.SunData;

class SimulateLayer extends RecursiveAction{
    static int SEQUENTIAL_CUTOFF = 1000;
    SunData sundata;
    SimulateLayer(int start, int end, SunData sundata){
        this.sundata = sundata;
        
    }
    public void compute(){
        if (end - start <= SEQUENTIAL_CUTOFF){
            for (int i = 0; i < end; i++){
            }
        }

    }
}