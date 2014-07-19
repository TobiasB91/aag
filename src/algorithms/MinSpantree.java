package algorithms;

import gui.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Edge;
import common.Graph;
import common.Vertex;

public class MinSpantree implements Algorithm {

	enum AlgorithmState {
		Initiating,
		CalculatingBridges,
		ChoosingEdges,
		Finished
	}
	
	private AlgorithmState currentState = null;
	private Graph graph = null;
	private boolean graphConnected = true, emptyGraph = false;
	private Set<Edge> eWithoutBridges = null, bridges = null, edges = null, chosenEdges = null;
	
	
	public MinSpantree(Graph graph) {
		init(graph);
	}
	
	private void init(Graph graph) {
		if(!graph.getVertices().isEmpty()) {
			this.graph = graph;
			eWithoutBridges = new HashSet<Edge>();
			chosenEdges = new HashSet<Edge>();
			bridges = new HashSet<Edge>();
			edges = new HashSet<Edge>();
			edges.addAll(graph.getEdges());
			currentState = AlgorithmState.Initiating;
			
			//Checking for connectedness
			
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
				currentState = AlgorithmState.CalculatingBridges;
			}
		} else {
			emptyGraph = true;
			currentState = AlgorithmState.Finished;
		}
	}
	
	@Override
	public boolean nextStep() {
		switch(currentState) {
		case Initiating:
			return true;
		case CalculatingBridges:
			bridges = new HashSet<Edge>();
			eWithoutBridges = new HashSet<Edge>();
			for(Edge e : edges) {
				List<Edge> withoutE = new ArrayList<Edge>();
				for(Edge e2 : edges) {
					if(!e2.equals(e)) {
						withoutE.add(e2);
						withoutE.add(new Edge(e2.getTarget(), e2.getSource()));
					}
				}
				Dijkstra dk = new Dijkstra(new Graph(graph.getVertices(), withoutE), e.getSource());
				dk.play();
				HashMap<Vertex,Integer> distance = dk.getDistance();
				boolean isBridge = false;
				for(Vertex v : graph.getVertices()) {
					if(distance.get(v) == Integer.MAX_VALUE) {
						isBridge = true;
					}
				}
				if(isBridge) {
					bridges.add(e);
				}
			}
			eWithoutBridges.addAll(edges);
			eWithoutBridges.removeAll(bridges);
			currentState = AlgorithmState.ChoosingEdges;
			return true;
		case ChoosingEdges:
			Edge largestEdge = null;
			for(Edge e : eWithoutBridges) {
				if(largestEdge == null) {
					largestEdge = e;
				}
				else if(e.getWeight() >= largestEdge.getWeight()) {
					largestEdge = e;
				}
			}
			if(largestEdge == null) {
				currentState = AlgorithmState.Finished;
			} else {
				chosenEdges.add(largestEdge);
				edges.remove(largestEdge);
				currentState = AlgorithmState.CalculatingBridges;
			}
			return true;
		case Finished:
			return false;
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
		case CalculatingBridges:
			g.drawString("Calculating the bridges.", 10, 10);
			break;
		case ChoosingEdges:
			g.drawString("Deleting the largest non-bridge. Blue edges are non-briges. Green edges are bridges.", 10, 10);
			break;
		case Finished:
			if(emptyGraph) {
				g.drawString("There is an empty graph.", 10, 10);
			} else if(!graphConnected) {
				g.drawString("The graph is not connected. Cannot use this algorithm.", 10, 10);
			} else {
				g.drawString("Finished. Green edges are bridges. Red edges are deleted edges.", 10, 10);
			}
			break;
		}
		
		if(bridges != null && !bridges.isEmpty()) {
			for(Edge e : bridges) {
				Canvas.printLine(g, new Color(0,150,0), e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
			}
		}
		if(eWithoutBridges != null && !eWithoutBridges.isEmpty()) {
			for(Edge e : eWithoutBridges) {
				Canvas.printLine(g, Color.BLUE, e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
			}
		}
		if(chosenEdges != null && !chosenEdges.isEmpty()) {
			for(Edge e : chosenEdges) {
				Canvas.printLine(g, Color.RED, e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
			}
		}
	}

}
