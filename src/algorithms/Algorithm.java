package algorithms;

import java.awt.Graphics;

public interface Algorithm {

	public boolean nextStep();
	
	public void play();
	
	public void print(Graphics g);
	
}
