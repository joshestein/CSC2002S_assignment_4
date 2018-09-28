package treeGrow;

import java.util.concurrent.RecursiveAction;

class Reset extends RecursiveAction{
    static int SEQUENTIAL_THRESHOLD = 1000;
    int start;
    int end;
    Tree[] trees;

    Reset(int start, int end, Tree[] trees){
        this.start = start; this.end = end; this.trees = trees;
    }

    public void compute(){
        if (end - start  <= SEQUENTIAL_THRESHOLD){
            for (int i = start; i < end; i++){
                trees[i].setExt((float)0.4);
            }
        } else {
            Reset left = new Reset(start, (end+start)/2, trees);
            Reset right = new Reset((start+end)/2, end, trees);
            left.fork();
            right.compute();
            left.join();
        }
    }
}