import java.util.ArrayList;
import java.util.List;

public class Node{
	public int value;
	public List<Edge> next;
	
	public Node(int startNode){
		value = startNode;
	}
	
	public void addNext(Node b, int cost){
		if (next == null){
			next = new ArrayList<Edge>();
		}
		next.add(new Edge(this, b, cost));
	}
	
	public int getValue(){
		return value;
	}
	
}