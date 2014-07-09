package algorithms;

import java.awt.Graphics;

import common.Graph;

@SuppressWarnings("unused")
public class FloydWarshall implements Algorithm {
	
	private Graph graph = null;
	private int[][] dist;
	private int k;
	private int i;
	private int j;
	
	@Override
	public boolean nextStep() {
		return false;
	}
	
	@Override
	public void play() {
	}
	
	@Override
	public void print(Graphics g) {
	}
}
