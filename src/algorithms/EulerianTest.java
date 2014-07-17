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

public class EulerianTest implements Algorithm {

	private Graph graph = null;
	private AlgorithmState currentState = null;
	private int currentEdge = 0;
	private int currentVertex = 0;
	private HashMap<Vertex,Integer> vertexDegrees = null;
	private Vertex updatedSource = null, updatedTarget = null;
	private boolean result = false;
	private List<Vertex> deletedVertices = null;
	private List<Vertex> notDeletedVertices = null;
	private int[][] dist = null;
	
	enum AlgorithmState {
		CalculatingVertexDegree,
		CheckingVertexDegree,
		DeletingIsolatedVertices,
		CheckingConnectedness,
		Finished
	}
	
	public EulerianTest(Graph graph) {
		init(graph);
	}
	
	private void init(Graph graph) {
		dist = null;
		this.graph = graph;
		currentEdge = 0;
		currentVertex = 0;
		vertexDegrees = new HashMap<Vertex,Integer>();
		for(Vertex v : graph.getVertices()) {
			vertexDegrees.put(v, 0);
		}
		currentState = AlgorithmState.CalculatingVertexDegree;
		deletedVertices = new ArrayList<Vertex>();
		notDeletedVertices = new ArrayList<Vertex>();
	}
	
	@Override
	public boolean nextStep() {
		switch(currentState) {
		
		case CalculatingVertexDegree: 
			if(currentEdge < graph.getEdges().size()) {
				Vertex s = graph.getEdges().get(currentEdge).getSource();
				Vertex t = graph.getEdges().get(currentEdge).getTarget();
				vertexDegrees.put(s, vertexDegrees.get(s)+1);
				updatedSource = s;
				vertexDegrees.put(t, vertexDegrees.get(t)+1);
				updatedTarget = t;
				currentEdge++;
			} else {
				currentState = AlgorithmState.CheckingVertexDegree;
				updatedTarget = null;
				updatedSource = null;
			}
			return true;
			
		case CheckingVertexDegree:
			if(currentVertex < graph.getVertices().size()) {
				if(vertexDegrees.get(graph.getVertices().get(currentVertex)) % 2 == 0) {
					currentVertex++;
				} else {
					currentState = AlgorithmState.Finished;
				}
			} else {
				currentState = AlgorithmState.DeletingIsolatedVertices;
				currentVertex = 0;
			}
			return true;
			
		case DeletingIsolatedVertices:
			if(currentVertex < graph.getVertices().size()) {
				if(vertexDegrees.get(graph.getVertices().get(currentVertex)) == 0) {
					deletedVertices.add(graph.getVertices().get(currentVertex));
				}
				currentVertex++;
			} else {
				currentState = AlgorithmState.CheckingConnectedness;
			}
			return true;
			
		case CheckingConnectedness:
			if(!graph.getVertices().isEmpty()) {
				List<Edge> edges = new ArrayList<Edge>();
				for(Vertex v : graph.getVertices()) {
					if(!(deletedVertices.contains(v))) {
						notDeletedVertices.add(v);
					}
				}
				for(Edge e : graph.getEdges()) {
					edges.add(e);
					edges.add(new Edge(e.getTarget(), e.getSource()));
				}
				
				FloydWarshall fw = new FloydWarshall(new Graph(notDeletedVertices, edges));
				fw.play();
				dist = fw.getDist();
				currentState = AlgorithmState.Finished;
				
				
				boolean isConnected = true;
				for(int j = 0; j < notDeletedVertices.size(); j++) {
					int min = Integer.MAX_VALUE;
					for(int i = 0; i < notDeletedVertices.size(); i++) {
						if(dist[i][j] < min) {
							min = dist[i][j];
						} else if(dist[i][j] >= Integer.MAX_VALUE){
							isConnected = false;
						}
					}
				}
				if(isConnected) {
					result = true;
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
		
		case CalculatingVertexDegree: 
			g.drawString("Calculating the degree of every vertex.", 10, 10);
			
			if(currentEdge < graph.getEdges().size()) {
				Edge e = graph.getEdges().get(currentEdge);
				Canvas.printLine(g, new Color(0,150,0), e.getSource().getX(), e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
			}
			break;
			
		case CheckingVertexDegree:
			g.drawString("Checking if every vertex degree is an even number.", 10, 10);
			if(currentVertex < graph.getVertices().size()) {
				Vertex v = graph.getVertices().get(currentVertex);
				g.setColor(new Color(0,150,0));
				g.drawOval(v.getX()-Canvas.VERTEX_SIZE/2, v.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
				g.setColor(new Color(0,0,0));
			}
			
			break;
			
		case DeletingIsolatedVertices:
			g.drawString("Deleting isolated vertices.", 10, 10);
			if(currentVertex < graph.getVertices().size()) {
				Vertex v = graph.getVertices().get(currentVertex);
				g.setColor(new Color(0,150,0));
				g.drawOval(v.getX()-Canvas.VERTEX_SIZE/2, v.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
				g.setColor(new Color(0,0,0));
			}
			break;
			
		case CheckingConnectedness:
			g.drawString("Checking for connectedness. Using Floyd-Warshall.", 10, 10);
			break;
			
		case Finished:
			g.drawString("This graph is " + (result? "" : "not ") + "eulerian" + (!result && dist == null? " because at least one vertex does not have an even degree or there is no edge" : "") +
					(!result && dist != null? " because it is not connected" : "")  + "!", 10, 10);
			break;
		}
		
		
		for(Vertex v : graph.getVertices()) {
			if(v.equals(updatedSource) || v.equals(updatedTarget)) {
				g.setColor(Color.RED);
				g.drawString(vertexDegrees.get(v).toString(), v.getX() - Canvas.VERTEX_SIZE/2, v.getY() - Canvas.VERTEX_SIZE/4*3);
				g.setColor(Color.BLACK);
			} else {
				g.drawString(vertexDegrees.get(v).toString(), v.getX() - Canvas.VERTEX_SIZE/2, v.getY() - Canvas.VERTEX_SIZE/4*3);
			}
		}
		
		for(Vertex v : deletedVertices) {
			Canvas.printX(g, v.getX()-Canvas.VERTEX_SIZE/2, v.getY()-Canvas.VERTEX_SIZE/2, v.getX()+Canvas.VERTEX_SIZE/2, v.getY()+Canvas.VERTEX_SIZE/2);
		}
	}

}
