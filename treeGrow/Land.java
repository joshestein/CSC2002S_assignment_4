package treeGrow;

import java.util.Arrays;

public class Land{

	float[][] grid;
	float[][] original_grid;

	// to do
	// sun exposure data here

	static float shadefraction = 0.1f; // only this fraction of light is transmitted by a tree

	Land(int dx, int dy) {
		grid = new float[dx][dy];
		original_grid = new float[dx][dy];
	}

	int getDimX() {
		return original_grid[0].length;
	}

	int getDimY() {
		return original_grid[1].length;
	}

	// Reset the shaded landscape to the same as the initial sun exposed landscape
	// Needs to be done after each growth pass of the simulator
	void resetShade() {
		//TODO: test that this works.
		for (int i = 0; i < Math.round(original_grid[0].length); i++){
			for (int j = 0; j < Math.round(original_grid[1].length); j++){
				grid[i][j] = original_grid[i][j];
			}
		}
	}

	float getFull(int x, int y) {
		return original_grid[x][y];
	}

	void setFull(int x, int y, float val) {
		// to do
		original_grid[x][y] = val;
		grid[x][y] = val;
	}

	void printGrid(){
		for (int i = 0; i < Math.round(grid[0].length); i++){
			for (int j = 0; j < Math.round(grid[1].length); j++){
				System.out.print(grid[i][j]+ " ");	
			}
			System.out.println();
		}
	}

	void printOriginalGrid(){
		for (int i = 0; i < Math.round(original_grid[0].length); i++){
			for (int j = 0; j < Math.round(original_grid[1].length); j++){
				System.out.print(original_grid[i][j]+ " ");	
			}
			System.out.println();
		}
	}

	synchronized float getShade(int x, int y) {
		// to do
		return grid[x][y];
	}

	synchronized void setShade(int x, int y, float val){
		grid[x][y] = val;
	}

	// reduce the
	synchronized void shadow(Tree tree){
		for (int i = getXLowerLimit(tree); i < getXUpperLimit(tree); i++){
			for (int j = getYLowerLimit(tree); j < getYUpperLimit(tree); j++){
				setShade(i, j, getShade(i, j)*shadefraction);
			}
		}
	}

	int getXLowerLimit(Tree tree) {
        if (Math.round(tree.getX() - tree.getExt()) < 0) {
			return 0;
        } else {
            return Math.round(tree.getX() - tree.getExt());
        }
	}

	int getXUpperLimit(Tree tree) {
        if (Math.round(tree.getX() + tree.getExt())+1 > getDimX()) {
            return getDimX();
        } else {
            return Math.round(tree.getX() + tree.getExt()+1);
        }
	}

	int getYLowerLimit(Tree tree) {
        if (Math.round(tree.getY() - tree.getExt()) < 0) {
			return 0;
        } else {
            return Math.round(tree.getY() - tree.getExt());
        }
	}

	int getYUpperLimit(Tree tree) {
        if (Math.round(tree.getY() + tree.getExt())+1 > getDimY()) {
            return getDimY();
        } else {
            return Math.round(tree.getY() + tree.getExt()+1);
        }
	}
}