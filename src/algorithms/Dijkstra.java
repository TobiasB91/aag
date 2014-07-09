package algorithms;

import gui.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

import common.Edge;
import common.Graph;
import common.Vertex;

public class Dijkstra implements Algorithm {

	enum AlgorithmState {
		ChoosingNearestVertex,
		UpdatingNeighbourDistance,
		
	}
	
	Graph graph = null;
	Vertex startingPoint = null;
	HashMap<Vertex,Integer> distance = null;
	HashMap<Vertex,Vertex> predecessor = null;
	PriorityQueue<Vertex> q = null;
	
	AlgorithmState currentState = null;
	Vertex currentVertex = null;
	int currentNeighbour = 0;
	boolean markLastNeighbourUpdate = false;
	
	public Dijkstra(Graph graph, Vertex startingPoint) {
		init(graph, startingPoint);		
	}
	
	private void init(Graph graph, Vertex startingPoint) {
		this.startingPoint = startingPoint;
		this.graph = graph;
		distance = new HashMap<Vertex,Integer>();
		predecessor = new HashMap<Vertex,Vertex>();
		for(Vertex v : graph.getVertices()) {
			distance.put(v, Integer.MAX_VALUE);
			predecessor.put(v, null);
		}
		distance.put(startingPoint, 0);
		q = new PriorityQueue<Vertex>(graph.getVertices().size(), new DistanceComparator<Vertex>());
		q.addAll(graph.getVertices());
		currentState = AlgorithmState.ChoosingNearestVertex;
	}
	
	private boolean updateDistance(Vertex u, Vertex v) {
		int newDistance = distance.get(u) + dist(u,v);
		if(newDistance < distance.get(v)) {
			distance.put(v, newDistance);
			predecessor.put(v, u);
			return true;
		}
		return false;
	}

	private int dist(Vertex u, Vertex v) {
		if(u.equals(v)) return 0;
		for(Edge e : graph.getEdges()) {
			if(e.getSource().equals(u) && e.getTarget().equals(v)) {
				return e.getWeight();
			}
		}
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean nextStep() {
		switch(currentState) {
			case ChoosingNearestVertex:
				if(!q.isEmpty()) {
					currentVertex = q.poll();
					currentState = AlgorithmState.UpdatingNeighbourDistance;
					return true;
				} else {
					return false;
				}
				
			case UpdatingNeighbourDistance:
				SortedSet<Vertex> neighbours = getNeighbours(currentVertex);
				if(currentNeighbour < neighbours.size()) {
					Object[] neighbourArray;
					neighbourArray = neighbours.toArray();
					Vertex v = (Vertex) neighbourArray[currentNeighbour++];
					if(q.contains(v)) {
						markLastNeighbourUpdate = updateDistance(currentVertex, v);
					}
				} else {
					//Removing and reinserting for ordering to happen
					List<Vertex> p = new ArrayList<Vertex>();
					Vertex v;
					while((v = q.poll()) != null) {
						p.add(v);
					}
					q.addAll(p);
					
					currentState = AlgorithmState.ChoosingNearestVertex;
					currentNeighbour = 0;
				}
				return true;
		}
		return true;
	}

	@Override
	public void play() {
		while(nextStep());
	}

	@Override
	public void print(Graphics g) {
		SortedSet<Vertex> neighbours = getNeighbours(currentVertex);
		Vertex updatedVertex = null;
		if(currentNeighbour-1 < neighbours.size() && currentNeighbour - 1 >= 0) {
			Object[] neighbourArray;
			neighbourArray = neighbours.toArray();
			updatedVertex = (Vertex) neighbourArray[currentNeighbour-1];
		}
		
		g.setColor(new Color(0,0,255));
		for(Vertex v : graph.getVertices()) {
			if(distance.get(v) != null) {
				if(v.equals(updatedVertex)) {
					g.setColor(new Color(0,150,0));
					g.drawString((distance.get(v) == Integer.MAX_VALUE? "Infinity"  : distance.get(v).toString()),
						v.getX() - Canvas.VERTEX_SIZE/2, v.getY() - Canvas.VERTEX_SIZE/4*3);
					g.setColor(new Color(0,0,0));
				} else {
					g.drawString((distance.get(v) == Integer.MAX_VALUE? "Infinity"  : distance.get(v).toString()),
							v.getX() - Canvas.VERTEX_SIZE/2, v.getY() - Canvas.VERTEX_SIZE/4*3);
				}
			}
		}
		g.setColor(new Color(0,0,0));
		
		if(currentVertex != null && updatedVertex != null) {
			g.setColor(new Color(0,150,0));
			double targetX = updatedVertex.getX(), targetY = updatedVertex.getY(), sourceX = currentVertex.getX(), sourceY = currentVertex.getY();
			int VERTEX_SIZE = Canvas.VERTEX_SIZE;
			double deltaX = targetX - sourceX;
			double deltaY = targetY - sourceY;
			double length = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			deltaX = deltaX/length;
			deltaY = deltaY/length;
			int[] arrowPointsX = new int[3];
			int[] arrowPointsY = new int[3];
			arrowPointsX[0] = (int)(targetX-VERTEX_SIZE/2*deltaX);
			arrowPointsY[0] = (int)(targetY-VERTEX_SIZE/2*deltaY);
			g.drawLine((int)(sourceX+VERTEX_SIZE/2*deltaX),(int)(sourceY+VERTEX_SIZE/2*deltaY),arrowPointsX[0],arrowPointsY[0]);
			
			double lineX = arrowPointsX[0] - VERTEX_SIZE/3*deltaX;
			double lineY = arrowPointsY[0] - VERTEX_SIZE/3*deltaY;
			double normalX = 1;
			double normalY = -deltaX / deltaY;
			double normalLength = Math.sqrt(1 + normalY*normalY);
			normalX = normalX / normalLength;
			normalY = normalY / normalLength;
			arrowPointsX[1] = (int)(lineX + normalX*VERTEX_SIZE/3);
			arrowPointsX[2] = (int)(lineX - normalX*VERTEX_SIZE/3);
			
			arrowPointsY[1] = (int)(lineY + normalY*VERTEX_SIZE/3);
			arrowPointsY[2] = (int)(lineY - normalY*VERTEX_SIZE/3);
			
			g.fillPolygon(arrowPointsX, arrowPointsY, 3);
			g.setColor(new Color(0,0,0));
		}
		
		if(currentVertex != null) {
			g.setColor(new Color(0,150,0));
			g.drawOval(currentVertex.getX()-Canvas.VERTEX_SIZE/2, currentVertex.getY()-Canvas.VERTEX_SIZE/2, Canvas.VERTEX_SIZE, Canvas.VERTEX_SIZE);
			g.setColor(new Color(0,0,0));
		}
		
	}
	
	private SortedSet<Vertex> getNeighbours(Vertex u) {
		SortedSet<Vertex> neighbours = new TreeSet<Vertex>(new NeighbourComparator<Vertex>());
		for(Edge e : graph.getEdges()) {
			if(e.getSource().equals(u)) {
				neighbours.add(e.getTarget());
			}
		}
		return neighbours;
	}

	public class DistanceComparator<T extends Vertex> implements Comparator<Object> {

		@Override
		public int compare(Object u, Object v) {
			if(distance.get((Vertex) u) < distance.get((Vertex) v)) {
				return -1;
			} else if(distance.get((Vertex) u) > distance.get((Vertex) v)) {
				return 1;
			} 
			return 0;
		}
	}

	public class NeighbourComparator<T extends Vertex> implements Comparator<Object> {

		@Override
		public int compare(Object u, Object v) {
			if(dist(currentVertex, (Vertex) u) <= dist(currentVertex, (Vertex) v)) {
				return -1;
			} else if(dist(currentVertex, (Vertex) u) > dist(currentVertex, (Vertex) v)) {
				return 1;
			}
			return 0;
		}
		
	}
}
