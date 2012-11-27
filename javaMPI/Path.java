import java.util.ArrayList;
import java.util.List;

public class Path{
	private int totalCost;
	private List<Node> nodes;
	
	public Path(Node first){
		nodes = new ArrayList<Node>();
		nodes.add(first);
	}
	
	public void addNode(Node n, int cost){
//		if (nodes == null){
//			nodes = new ArrayList<Node>();
//		}
		nodes.add(n);
		totalCost += cost;
	}
	
	public String toString(){
		for (Node n: nodes){
			System.out.print(n.getValue());
		}
		System.out.println("");
		return null;
	}
	
	public Node getStart(){
		return nodes.get(0); //get first
	}
	
	public int getCost(){
		return totalCost;
	}
}
	