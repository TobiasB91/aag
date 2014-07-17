package common;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private List<Vertex> vertices = null;
	private List<Edge> edges = null;
	
	public Graph(List<Vertex> vertices, List<Edge> edges) {
		this.vertices = vertices;
		this.edges = edges;
	}
	
	public Graph() {
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void deleteVertex(Vertex vertex) {
		for(int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			if(e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
				edges.remove(e);
				i--;
			}
		}
		vertices.remove(vertex);
	}
}
