import java.util.ArrayList;
import java.util.List;

public class Edge{
	public Node start;
	public Node end;
	public int value;
	
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