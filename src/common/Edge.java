package common;

public class Edge {
	private Vertex source = null;
	private Vertex target = null;
	private int weight = 0;
	
	public Edge(Vertex source, Vertex target) {
		this.setSource(source);
		this.setTarget(target);
	}
	
	public Edge(Vertex source, Vertex target, int weight) {
		this(source,target);
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public Vertex getTarget() {
		return target;
	}

	public void setTarget(Vertex target) {
		this.target = target;
	}
	
	@Override
	public boolean equals(Object e) {
		return e instanceof Edge && source.equals(((Edge)e).getSource()) && target.equals(((Edge)e).getTarget());
	}
}
