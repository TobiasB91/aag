package algorithms;

import gui.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.Edge;
import common.Graph;
import common.Vertex;

public class FordFulkerson implements Algorithm {

	enum AlgorithmState {
		AssigningSource,
		AssigningTarget,
		CalculatingWayOnResidualGraph,
		Finished
		
	}
	
	AlgorithmState currentState = AlgorithmState.AssigningSource;
	Graph graph = null;
	HashMap<Edge,Integer> flow = null;
	static Vertex selectedVertex = null;
	int currentEdge = 0;
	private Graph residualGraph = null;
	private Vertex source = null;
	private Vertex target = null;
	private int lastMinCut = Integer.MAX_VALUE;
	
	public FordFulkerson(Graph graph) {
		init(graph);
	}
	
	private void init(Graph graph) {
		this.graph = graph;
		currentEdge = 0;
		selectedVertex = null;
		flow = new HashMap<Edge,Integer>();
		for(Edge e : graph.getEdges()) {
			flow.put(e, 0);
		}
	}
	
	@Override
	public boolean nextStep() {
		if(source != null && source==target) {
			currentState = AlgorithmState.Finished;
		}
		
		switch(currentState) {
		
		case AssigningSource:
			if(selectedVertex != null) {
				source = selectedVertex;
				currentState = AlgorithmState.AssigningTarget;
			}
			return false;
		
		
		case AssigningTarget:
			if(selectedVertex != null) {
				target = selectedVertex;
				currentState = AlgorithmState.CalculatingWayOnResidualGraph;
			}
			return false;
		
		case CalculatingWayOnResidualGraph:
			List<Edge> residualEdges = new ArrayList<Edge>();
			for(Edge e : graph.getEdges()) {
				if(flow.get(e) < e.getWeight()) {
					residualEdges.add(new Edge(e.getSource(), e.getTarget(), e.getWeight()-flow.get(e)));
				}
				if(flow.get(e) > 0) {
					residualEdges.add(new Edge(e.getTarget(), e.getSource(), flow.get(e)));
				}
			}
			residualGraph = new Graph(graph.getVertices(), residualEdges);
			Dijkstra dk = new Dijkstra(residualGraph, source);
			dk.play();
			HashMap<Vertex,Integer> distance = dk.getDistance();
			if(distance.get(target) != Integer.MAX_VALUE) {
				HashMap<Vertex,Vertex> predecessor = dk.getPredecessor();
				int minCut = Integer.MAX_VALUE;
				Vertex currentVertex = target;
				while(currentVertex != null) {
					for(Edge e : residualGraph.getEdges()) {
						if(e.equals(new Edge(predecessor.get(currentVertex),currentVertex))) {
							if(e.getWeight() < minCut) {
								minCut = e.getWeight();
							}
						}
					}
					currentVertex = predecessor.get(currentVertex);
				}
				
				currentVertex = target;
				while(currentVertex != null) {
					boolean inEdges = false;
					for(Edge e : graph.getEdges()) {
						if(e.equals(new Edge(predecessor.get(currentVertex),currentVertex))) {
							inEdges = true;
							flow.put(e, flow.get(e) + minCut); 
						} 
					}
					if(!inEdges) {
						for(Edge e : graph.getEdges()) {
							if(e.equals(new Edge(currentVertex, predecessor.get(currentVertex)))) {
								flow.put(e, flow.get(e) - minCut); 
							} 
						}
					}
					currentVertex = predecessor.get(currentVertex);
					lastMinCut = minCut;
				}
			} else {
				currentState = AlgorithmState.Finished;
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
			
		case AssigningSource:
			g.drawString("Assigning a source. Select a vertex by clicking and then press next.", 10, 10);
			break;
			
		case AssigningTarget:
			g.drawString("Assigning a target. Select a vertex by clicking and then press next.", 10, 10);
			break;
			
		case CalculatingWayOnResidualGraph:
			g.drawString("Creating residual graph and finding existing ways in it and creating flow-function. Last minimal cut: " +
					(lastMinCut == Integer.MAX_VALUE ? "None" : new Integer(lastMinCut).toString()), 10, 10);
			break;
			
		case Finished:
			g.drawString("Finished.", 10, 10);
			break;
			
		default:
			break;
		}
		
		if(source != null) {
			g.setColor(Color.BLUE);
			g.drawOval(source.getX()-Canvas.VERTEX_SIZE/2, source.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
			g.setColor(Color.BLACK);
		}
		
		if(target != null) {
			g.setColor(new Color(0,150,0));
			g.drawOval(target.getX()-Canvas.VERTEX_SIZE/2, target.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
			g.setColor(Color.BLACK);
		}
		
		for(Edge e : graph.getEdges()) {
			Canvas.printWeightArrow(g, "flow: " + flow.get(e).toString(), Color.BLACK,
					e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY(), 15);
		}
		
	}
	
	public static void setSelectedVertex(Vertex selectedVertex) {
		FordFulkerson.selectedVertex = selectedVertex;
	}
}
