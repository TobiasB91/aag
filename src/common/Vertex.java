package common;

public class Vertex {
	private int x;
	private int y;
	private char id;
	private static char nextID = 65;
	
	public Vertex(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.id = nextID++;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public char getID() {
		return id;
	}

	@Override
	public boolean equals(Object v) {
		return v instanceof Vertex && x == ((Vertex)v).getX() && y == ((Vertex)v).getY();
	}
	
}
