package treeGrow;

public class Land{

	float[][] grid;
	int[][] original_grid;
	float[][] shade;

	// to do
	// sun exposure data here

	static float shadefraction = 0.1f; // only this fraction of light is transmitted by a tree

	Land(int dx, int dy) {
		grid = new int[dx][dy];
		shade = new int[dx][dy];
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
		shade = new int[dx][dy];
	}

	float getFull(int x, int y) {
		return grid[x][y];
	}

	void setFull(int x, int y, float val) {
		// to do
		grid[x][y] = val;
	}

	float getShade(int x, int y) {
		// to do
		return shade[x][y];
	}

	void setShade(int x, int y, float val){
		shade[x][y] = val;
	}

	// reduce the
	void shadow(Tree tree){
		// to do
	}
}
