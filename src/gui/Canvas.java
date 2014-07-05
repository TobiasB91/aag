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
	
	public Canvas() {
		addMouseListener(this);
		graph = new Graph();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		
		((Graphics2D)g).setStroke(new BasicStroke(2));
		g.setColor(new Color(0,0,0));
		for(Vertex v : graph.getVertices()) {
			g.fillOval(v.getX()-VERTEX_SIZE/2, v.getY()-VERTEX_SIZE/2, VERTEX_SIZE, VERTEX_SIZE);
		}
		
		for(Edge e : graph.getEdges()) {
			g.drawLine(e.getSource().getX(),e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY());
		}
		
		if(markedVertex != null) {
			g.setColor(new Color(255,0,0));
			((Graphics2D)g).setStroke(new BasicStroke(5));
			g.drawOval(markedVertex.getX()-VERTEX_SIZE/2, markedVertex.getY()-VERTEX_SIZE/2, VERTEX_SIZE+1, VERTEX_SIZE+1);
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
		System.out.println("x: "+e.getX() + "  y: "+e.getY());
		int mouseX = e.getX();
		int mouseY = e.getY();
		Vertex collVertex = getCollidingVertex(mouseX,mouseY);

		if(collVertex == null && e.getButton() == MouseEvent.BUTTON1) {
			graph.addVertex(new Vertex(mouseX, mouseY));
		} else if(e.getButton() == MouseEvent.BUTTON1) {
			if(markedVertex == null) {
				markedVertex = collVertex;
			} else {
				Edge newEdge = new Edge(markedVertex, collVertex);
				if(!graph.getEdges().contains(newEdge)) {
					graph.addEdge(newEdge);
				}
				markedVertex = null;
			}
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			graph.deleteVertex(collVertex);
		}
		repaint();
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private Vertex getCollidingVertex(int x, int y) {
		for(Vertex v : graph.getVertices()) {
			if(x > v.getX() - VERTEX_SIZE/2 && x < v.getX() + VERTEX_SIZE/2 && y > v.getY() - VERTEX_SIZE/2 && y < v.getY() + VERTEX_SIZE/2) {
				return v;
			}
		}		
		return null;
	}

}
