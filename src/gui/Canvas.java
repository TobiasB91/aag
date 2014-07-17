package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import common.Edge;
import common.Graph;
import common.Vertex;

@SuppressWarnings("serial")
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
			g.drawOval(v.getX()-VERTEX_SIZE/2, v.getY()-VERTEX_SIZE/2, VERTEX_SIZE, VERTEX_SIZE);
			g.drawString("" + v.getID(), v.getX()-VERTEX_SIZE/10, v.getY()+VERTEX_SIZE/10);
		}
		
		for(Edge e : graph.getEdges()) {
			if(MainFrame.directed) {
				printWeightArrow(g, new Integer(e.getWeight()).toString(), Color.BLACK, e.getSource().getX(),
						e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY(), 0);
			} else {
				printWeightLine(g, new Integer(e.getWeight()).toString(), Color.BLACK, e.getSource().getX(),
						e.getSource().getY(), e.getTarget().getX(), e.getTarget().getY(), 0);
			}
					
		}
		
		if(markedVertex != null && MainFrame.currentAlgorithm == null) {
			g.setColor(new Color(255,0,0));
			((Graphics2D)g).setStroke(new BasicStroke(5));
			g.drawOval(markedVertex.getX()-VERTEX_SIZE/2, markedVertex.getY()-VERTEX_SIZE/2, VERTEX_SIZE+1, VERTEX_SIZE+1);
		}
		
		if(markedEdge != null) {
			((Graphics2D)g).setStroke(new BasicStroke(7));
			if(MainFrame.directed) {
				printArrow(g, Color.RED, markedEdge.getSource().getX(), markedEdge.getSource().getY(),
					markedEdge.getTarget().getX(), markedEdge.getTarget().getY());
			} else {
				printLine(g, Color.RED, markedEdge.getSource().getX(), markedEdge.getSource().getY(),
						markedEdge.getTarget().getX(), markedEdge.getTarget().getY());
			}
		}
		
		if(MainFrame.currentAlgorithm != null) {
			MainFrame.currentAlgorithm.print(g);
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

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
	}
	
	public void updateWeight(int weight) {
		if(markedEdge != null) {
			markedEdge.setWeight(weight);
		}
		repaint();
	}
	
	public Vertex getMarkedVertex() {
		return markedVertex;
	}
	
	public Graph getGraph() {
		return graph;
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
	
	public static void printLine(Graphics g, Color color, double sourceX, double sourceY, double targetX, double targetY) {
		Color clrTmp = g.getColor();
		g.setColor(color);
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
		g.setColor(clrTmp);
	}
	
	public static void printWeightLine(Graphics g, String weight, Color color, double sourceX, double sourceY, double targetX, double targetY, int offset) {
		printLine(g, color, sourceX, sourceY, targetX, targetY);
		boolean isHorizontal = Math.abs(sourceX - targetX) > Math.abs(sourceY - targetY);
		Color tmpClr = g.getColor();
		g.setColor(color);
		g.drawString((new Integer(Integer.MAX_VALUE).toString().equals(weight)? "Infinity" : weight), (int) (Math.min(sourceX, targetX) + Math.abs(targetX - sourceX)/2 + (isHorizontal ? 0 : 20 + offset)),
				(int) (Math.min(sourceY, targetY) + Math.abs(targetY - sourceY)/2 + (isHorizontal? -20 - offset : 0)));
		g.setColor(tmpClr);
	}
	
	public static void printArrow(Graphics g, Color color, double sourceX, double sourceY, double targetX, double targetY) {
		
		Color clrTmp = g.getColor();
		g.setColor(color);
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
		g.setColor(clrTmp);
	}
	
	public static void printWeightArrow(Graphics g, String weight, Color color, double sourceX, double sourceY, double targetX, double targetY, int offset) {
		printArrow(g, color, sourceX, sourceY, targetX, targetY);
		boolean isHorizontal = Math.abs(sourceX - targetX) > Math.abs(sourceY - targetY);
		Color tmpClr = g.getColor();
		g.setColor(color);
		g.drawString((new Integer(Integer.MAX_VALUE).toString().equals(weight)? "Infinity" : weight), (int) (Math.min(sourceX, targetX) + Math.abs(targetX - sourceX)/2 + (isHorizontal ? 0 : 20 + offset)),
				(int) (Math.min(sourceY, targetY) + Math.abs(targetY - sourceY)/2 + (isHorizontal? -20 - offset : 0)));
		g.setColor(tmpClr);
	}
	
	public static void printX(Graphics g, double sourceX, double sourceY, double targetX, double targetY) {
		g.drawLine((int) Math.min(sourceX, targetX), (int) Math.min(sourceY, targetY), (int) Math.max(sourceX, targetX), (int) Math.max(sourceY, targetY));
		g.drawLine((int) Math.min(sourceX, targetX), (int) Math.max(sourceY, targetY), (int) Math.max(sourceX, targetX), (int) Math.min(sourceY, targetY));
	}

}