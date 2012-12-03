public class Edge{
	private Node start;
	private Node end;
	private int value;
	
	public Edge(Node a, Node b, int cost){
		start = a;
		end = b;
		value = cost;
	}
	
	public Node getA(){
		return start;
	}
	public Node getB(){
		return end;
	}
	
	public int getCost(){
		return value;
	}
	
}