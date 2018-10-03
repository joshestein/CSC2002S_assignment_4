package treeGrow;

// Trees define a canopy which covers a square area of the landscape
public class Tree{

private
	int xpos;	// x-coordinate of center of tree canopy
	int ypos;	// y-coorindate of center of tree canopy
	float ext;	// extent of canopy out in vertical and horizontal from center

	static float growfactor = 1000.0f; // divide average sun exposure by this amount to get growth in extent

public
	Tree(int x, int y, float e){
		xpos=x; ypos=y; ext=e;
	}

	int getX() {
		return xpos;
	}

	int getY() {
		return ypos;
	}

	float getExt() {
		return ext;
	}

	void setExt(float e) {
		ext = e;
	}

	// return the average sunlight for the cells covered by the tree
	synchronized float sunexposure(Land land){
		// to do
		float total = 0.0f;
		int num_squares = 0;
		for (int i = land.getXLowerLimit(this); i < land.getXUpperLimit(this); i++){
			for (int j = land.getYLowerLimit(this); j < land.getYUpperLimit(this); j++){
				float current_sun_value = land.getShade(i, j);
				total += current_sun_value;
				num_squares++;
				//for some reason, this is the only way to prevent interleavings
				//calling this method here means that I don't have to call land.shadow()
				land.setShade(i, j, current_sun_value*0.1f);
			}
		}
		//Uncomment to test for interleavings
		//System.out.println("Total sunlight: "+total);
		return total/(num_squares);
	}

	// is the tree extent within the provided range [minr, maxr)
	boolean inrange(float minr, float maxr) {
		return (ext >= minr && ext < maxr);
	}

	// grow a tree according to its sun exposure
	synchronized void sungrow(Land land) {
		float newExtent = ext + sunexposure(land)/growfactor;
		ext = newExtent;
		//Once again, moving this call to sunexposure because of interleavings
		//land.shadow(this);
	}
}
