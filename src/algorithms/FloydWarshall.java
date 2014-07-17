package algorithms;

import gui.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

import common.Edge;
import common.Graph;
import common.Vertex;

@SuppressWarnings("unused")
public class FloydWarshall implements Algorithm {
	
	
	private Graph graph = null;
	private int[][] dist;
	private int k = 0;
	private int i = 0;
	private int j = 0;
	
	public FloydWarshall(Graph graph) {
		init(graph);
	}
	
	private void init(Graph graph) {
		this.graph = graph;
		dist = new int[graph.getVertices().size()][graph.getVertices().size()];
		for(int i = 0; i < graph.getVertices().size(); i++) {
			for(int j = 0; j < graph.getVertices().size(); j++) {
				if(i == j) {
					dist[i][j] = 0;
				}
				boolean existsEdge = false;
				for(Edge e : graph.getEdges()) {
					if(e.getSource().equals(graph.getVertices().get(i)) 
							&& e.getTarget().equals(graph.getVertices().get(j))) {
						dist[i][j] = e.getWeight();
						existsEdge = true;
					}
				}
				if(!existsEdge && i != j) {
					dist[i][j] = Integer.MAX_VALUE;
				}
			}
		}
		
		//for(int k = 1; k < graph.getVertices().size(); k++) {
		//	for(int i = 1; i < graph.getVertices().size(); i++) {
		//		for(int j = 1; j < graph.getVertices().size(); j++) {
		//			if(dist[i][j] > dist[i][k] + dist[k][j]) {
		//				dist[i][j] = dist[i][k] + dist[k][j];
		//			}
		//		}
		//	}
		//}
		
	}
	
	@Override
	public boolean nextStep() {
		if(j == graph.getVertices().size()) {
			j = 0;
			i++;
			if(i == graph.getVertices().size()) {
				i = 0;
				k++;
				if(k == graph.getVertices().size()) {
					//finished
				}
			}
			return true;
		} else if(k != graph.getVertices().size()) {
			if(dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][j] > dist[i][k] + dist[k][j]) {
				dist[i][j] = dist[i][k] + dist[k][j];
			}
			j++;
			return true;
		}
		return false;
	}
	
	@Override
	public void play() {
		while(nextStep());
	}
	
	@Override
	public void print(Graphics g) {
		

		g.setColor(new Color(0,0,0));
		if(i < graph.getVertices().size() && j < graph.getVertices().size() && k < graph.getVertices().size()) {
			g.drawString("Shortest path from " + graph.getVertices().get(i).getID() + " to " + graph.getVertices().get(j).getID() + " : " 
					+ (dist[i][j] == Integer.MAX_VALUE ? "Infinity" : dist[i][j]) +
					" - Calculating if path along vertex " + graph.getVertices().get(k).getID() + " is shorter.", 0, 15);
			if(i != j) {
				Canvas.printWeightArrow(g, new Integer(dist[i][j]).toString(), Color.BLUE, graph.getVertices().get(i).getX(), graph.getVertices().get(i).getY(),
						graph.getVertices().get(j).getX(), graph.getVertices().get(j).getY(), 10);
			}
			if(k < graph.getVertices().size() && k != i && k != j) {
				Canvas.printWeightArrow(g, new Integer(dist[i][k]).toString(), Color.MAGENTA, graph.getVertices().get(i).getX(), graph.getVertices().get(i).getY(),
						graph.getVertices().get(k).getX(), graph.getVertices().get(k).getY(), 10);
				Canvas.printWeightArrow(g, new Integer(dist[k][j]).toString(), Color.MAGENTA, graph.getVertices().get(k).getX(), graph.getVertices().get(k).getY(),
						graph.getVertices().get(j).getX(), graph.getVertices().get(j).getY(), 10);
			}
		}
		
		for(int n = 0; n < graph.getVertices().size(); n++) {
			Vertex v = graph.getVertices().get(n);
			if(i < graph.getVertices().size() && v.equals(graph.getVertices().get(i))) {
				g.setColor(Color.BLUE);
				g.drawOval(v.getX()-Canvas.VERTEX_SIZE/2, v.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
				g.setColor(new Color(0,0,0));
			}
			if(j < graph.getVertices().size() && v.equals(graph.getVertices().get(j))) {
				g.setColor(Color.BLUE);
				g.drawOval(v.getX()-Canvas.VERTEX_SIZE/2, v.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
				g.setColor(new Color(0,0,0));
			}
			if(k < graph.getVertices().size() && v.equals(graph.getVertices().get(k)) && k != i && k != j) {
				g.setColor(Color.MAGENTA);
				g.drawOval(v.getX()-Canvas.VERTEX_SIZE/2, v.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
				g.setColor(new Color(0,0,0));
			}
			g.drawString((dist[i][n] == Integer.MAX_VALUE? "Infinity"  : new Integer(dist[i][n]).toString()),
					v.getX() - Canvas.VERTEX_SIZE/2, v.getY() - Canvas.VERTEX_SIZE/4*3);
		}
	}
	
	public int[][] getDist() {
		return dist;
	}
}
