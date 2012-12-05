import java.util.ArrayList;
import java.util.List;

public class Node{
	private int value;
	private List<Edge> next;
	
	public Node(int startNode){
		value = startNode;
		next = new ArrayList<Edge>();
	}
	
	public void addNext(Node b, int cost){
		
		
		next.add(new Edge(this, b, cost));
	}
	
	public List<Edge> getConnections(){
		return next;
	}
	
	public int getValue(){
		return value;
	}
	
}