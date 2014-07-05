package common;

public class Vertex {
	private int x;
	private int y;
	
	public Vertex(int x, int y) {
		this.setX(x);
		this.setY(y);
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
	
	@Override
	public boolean equals(Object v) {
		return v instanceof Vertex && x == ((Vertex)v).getX() && y == ((Vertex)v).getY();
	}
	
}
