import java.util.ArrayList;
import java.util.List;

public class Path{
	int totalCost;
	public List<Node> nodes;
	
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
			System.out.print(n.value);
		}
		System.out.println("");
		return null;
	}
	
	public int getCost(){
		return totalCost;
	}
}
	