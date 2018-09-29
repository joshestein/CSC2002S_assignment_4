package treeGrow;

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
		return grid[0].length;
	}

	int getDimY() {
		return grid[1].length;
	}

	// Reset the shaded landscape to the same as the initial sun exposed landscape
	// Needs to be done after each growth pass of the simulator
	void resetShade() {
		//TODO: test that this works.
		grid = original_grid;
	}

	float getFull(int x, int y) {
		return original_grid[x][y];
	}

	void setFull(int x, int y, float val) {
		// to do
		original_grid[x][y] = val;
	}

	float getShade(int x, int y) {
		// to do
		return grid[x][y];
	}

	//synchronized
	void setShade(int x, int y, float val){
		grid[x][y] = val;
	}

	// reduce the
	//synchronized
	void shadow(Tree tree){
		// to do
		for (int i = getXLowerLimit(tree); i < getXLowerLimit(tree); i++){
			for (int j = getYLowerLimit(tree); j < getYUpperLimit(tree); j++){
				setShade(i, j, getShade(i, j)/(float)10.0);
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
        if (Math.round(tree.getX() + tree.getExt()) > getDimX()) {
            return getDimX();
        } else {
            return Math.round(tree.getX() + tree.getExt());
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
        if (Math.round(tree.getY() + tree.getExt()) > getDimY()) {
            return getDimY();
        } else {
            return Math.round(tree.getY() + tree.getExt());
        }
	}
}