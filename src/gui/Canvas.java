package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import common.Edge;
import common.Graph;
import common.Vertex;

public class Canvas extends JPanel implements MouseListener{

	public static final int VERTEX_SIZE = 50;
	
	private Graph graph = null;
	private Vertex markedVertex = null;
	private Edge markedEdge = null;
	
	public Canvas() {
		addMouseListener(this);
		graph = new Graph();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		
		((Graphics2D)g).setStroke(new BasicStroke(6));
		g.setColor(new Color(0,0,0));
		for(Vertex v : graph.getVertices()) {
			g.fillOval(v.getX()-VERTEX_SIZE/2, v.getY()-VERTEX_SIZE/2, VERTEX_SIZE, VERTEX_SIZE);
		}
		
		for(Edge e : graph.getEdges()) {
			int sourceX = e.getSource().getX(), sourceY = e.getSource().getY(), targetX = e.getTarget().getX(), targetY = e.getTarget().getY();
			g.drawLine(sourceX,sourceY,targetX,targetY);
			boolean isHorizontal = Math.abs(sourceX - targetX) > Math.abs(sourceY - targetY);
			g.drawString(new Integer(e.getWeight()).toString(), Math.min(sourceX, targetX) + Math.abs(targetX - sourceX)/2 + (isHorizontal ? 0 : 20),
					Math.min(sourceY, targetY) + Math.abs(targetY - sourceY)/2 + (isHorizontal? -20 : 0));
		}
		
		if(markedVertex != null) {
			g.setColor(new Color(255,0,0));
			((Graphics2D)g).setStroke(new BasicStroke(5));
			g.drawOval(markedVertex.getX()-VERTEX_SIZE/2, markedVertex.getY()-VERTEX_SIZE/2, VERTEX_SIZE+1, VERTEX_SIZE+1);
		}
		
		if(markedEdge != null) {
			g.setColor(new Color(255,0,0));
			((Graphics2D)g).setStroke(new BasicStroke(7));
			g.drawLine(markedEdge.getSource().getX(), markedEdge.getSource().getY(), markedEdge.getTarget().getX(), markedEdge.getTarget().getY());
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		Vertex collVertex = getCollidingVertex(mouseX,mouseY);

		if(collVertex == null && e.getButton() == MouseEvent.BUTTON1) {
			Edge edge = getCollidingEdge(mouseX, mouseY);
			if(edge == null) {
				graph.addVertex(new Vertex(mouseX, mouseY));
			} else {
				markedEdge = edge;
				markedVertex = null;
			}
		} else if(e.getButton() == MouseEvent.BUTTON1) {
			if(markedVertex == null) {
				markedVertex = collVertex;
				markedEdge = null;
			} else {
				Edge newEdge = new Edge(markedVertex, collVertex);
				if(!graph.getEdges().contains(newEdge)) {
					graph.addEdge(newEdge);
				}
				markedVertex = null;
			}
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			graph.deleteVertex(collVertex);
			markedVertex = null;
			markedEdge = null;
		}
		repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateWeight(int weight) {
		if(markedEdge != null) {
			markedEdge.setWeight(weight);
		}
		repaint();
	}
	
	private Vertex getCollidingVertex(int x, int y) {
		for(Vertex v : graph.getVertices()) {
			if(x > v.getX() - VERTEX_SIZE/2 && x < v.getX() + VERTEX_SIZE/2 && y > v.getY() - VERTEX_SIZE/2 && y < v.getY() + VERTEX_SIZE/2) {
				return v;
			}
		}		
		return null;
	}

	
	private Edge getCollidingEdge(int x, int y) {
		for(Edge e : graph.getEdges()) {
			double deltaX = e.getTarget().getX() - e.getSource().getX();
			double deltaY = e.getTarget().getY() - e.getSource().getY();
			double normalY = -deltaX / deltaY;
			double normalLength = Math.sqrt((Math.pow(normalY,2)+1));
			double normalizedX = 1/normalLength;
			double normalizedY = normalY/normalLength;
			double c = e.getSource().getX() * normalizedX + e.getSource().getY() * normalizedY;
			double distance = Math.abs(x * normalizedX + y * normalizedY - c);
			
			if(distance <= 20 && x > Math.min(e.getTarget().getX(), e.getSource().getX()) && x < Math.max(e.getTarget().getX(),e.getSource().getX()) 
					&& y > Math.min(e.getTarget().getY(), e.getSource().getY()) && y < Math.max(e.getTarget().getY(), e.getSource().getY())) {
				return e;
			}
		}
		return null;
	}
}
