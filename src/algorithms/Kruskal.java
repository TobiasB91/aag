package algorithms;

import gui.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import common.Edge;
import common.Graph;
import common.Vertex;

public class Kruskal implements Algorithm {

	enum AlgorithmState {
		Initiating,
		SortingEdges,
		ChoosingEdges,
		Finished
	}
	
	Graph graph = null;
	boolean graphConnected = true;
	boolean emptyGraph = false;
	PriorityQueue<Edge> sortedEdges = null;  
	Set<Edge> minEdges = null;
	Set<Edge> circleEdges = null;
	int currentEdge = 0;
	AlgorithmState currentState = AlgorithmState.Initiating;
	
	public Kruskal(Graph graph) {
		if(!graph.getVertices().isEmpty()) {
			this.graph = graph;
			minEdges = new HashSet<Edge>();
			circleEdges = new HashSet<Edge>();
			currentEdge = 0;
			//Checking connectedness
			List<Edge> edges = new ArrayList<Edge>();
			
			for(Edge e : graph.getEdges()) {
				edges.add(e);
				edges.add(new Edge(e.getTarget(), e.getSource(), e.getWeight()));
			}
			
			FloydWarshall fw = new FloydWarshall(new Graph(graph.getVertices(), edges));
			fw.play();
			int[][] dist = fw.getDist();
			
	
			
			boolean isConnected = true;
			for(int j = 0; j < graph.getVertices().size(); j++) {
				int min = Integer.MAX_VALUE;
				for(int i = 0; i < graph.getVertices().size(); i++) {
					if(dist[i][j] < min) {
						min = dist[i][j];
					} else if(dist[i][j] >= Integer.MAX_VALUE){
						isConnected = false;
					}
				}
			}
			if(!isConnected) {
				graphConnected = false;
				currentState = AlgorithmState.Finished;
			} else {
				currentState = AlgorithmState.SortingEdges;
			}
		} else {
			emptyGraph = true;
			currentState = AlgorithmState.Finished;
		}
		//
	}
	
	@Override
	public boolean nextStep() {
		switch(currentState) {
		
		case SortingEdges: 
			if(!graph.getEdges().isEmpty()) {
				sortedEdges = new PriorityQueue<Edge>(graph.getEdges().size(), new WeightComparator());
			} else {
				sortedEdges = new PriorityQueue<Edge>();
			}
			for(Edge e : graph.getEdges()) {
				sortedEdges.add(e);
			}
			currentState = AlgorithmState.ChoosingEdges;
			return true;
			
		case ChoosingEdges:
			Edge e = sortedEdges.poll();
			if(e == null) {
				currentState = AlgorithmState.Finished;
			}
			else {
				List<Edge> minEdgesAsList = new ArrayList<Edge>();
				minEdgesAsList.addAll(minEdges);
				List<Edge> edges = new ArrayList<Edge>();
				
				for(Edge e2 : minEdgesAsList) {
					edges.add(e2);
					edges.add(new Edge(e2.getTarget(), e2.getSource()));
				}
				
				Dijkstra d = new Dijkstra(new Graph(graph.getVertices(), edges), e.getSource());
				d.play();
				HashMap<Vertex, Integer> distance = d.getDistance();
				if((distance.get(e.getTarget()) == Integer.MAX_VALUE)) {
					minEdges.add(e);
				} else {
					circleEdges.add(e);
				}
			}
			return true;
			
		case Finished:
			break;
		
		default: 
			break;
		}
		return false;
	}

	@Override
	public void play() {
		while(nextStep());
	}

	@Override
	public void print(Graphics g) {
		switch(currentState) {
		
		case Initiating:
			g.drawString("Checking if the graph is connected.", 10, 10);
			break;
		
		case SortingEdges: 
			g.drawString("Sorting the edges from lowest cost to highest cost.", 10, 10);
			break;
			
		case ChoosingEdges:
			g.drawString("Choosing edges from shortest to longest without creating a circle.", 10, 10);
			for(Edge e : minEdges) {
				Canvas.printLine(g, new Color(0,150,0), e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
			}
			for(Edge e : circleEdges) {
				Canvas.printLine(g, Color.RED, e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
			}
			break;
			
		case Finished:
			if(emptyGraph) {
				g.drawString("There is an empty graph.", 10, 10);
			} else if(!graphConnected) {
				g.drawString("The graph is not connected. Cannot use this algorithm.", 10, 10);
			} else {
				g.drawString("This is the minimal spanning tree. Green edges were token, red ones would create a circle.", 10, 10);
				for(Edge e : minEdges) {
					Canvas.printLine(g, new Color(0,150,0), e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
				}
				for(Edge e : circleEdges) {
					Canvas.printLine(g, Color.RED, e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
				}
			}
			break;
		}
	}
	
	private class WeightComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge e1, Edge e2) {
			if(e1.getWeight() < e2.getWeight()) return -1;
			else if(e1.getWeight() > e2.getWeight()) return 1;
			return 0;
		}
		
	}

}
