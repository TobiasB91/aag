package algorithms;

import java.awt.Graphics;

public class FordFulkerson implements Algorithm {

	@Override
	public boolean nextStep() {
		
		return false;
	}

	@Override
	public void play() {
		while(nextStep());
	}

	@Override
	public void print(Graphics g) {
		
	}

}
