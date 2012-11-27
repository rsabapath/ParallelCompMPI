import java.util.ArrayList;
import java.util.List;

public class Path{
	private int totalCost;
	private List<Node> nodes;
	
	public Path(Node first){
		nodes = new ArrayList<Node>();
		nodes.add(first);
	}
	
	public Path(Path old){
		totalCost = old.getCost();
		nodes = new ArrayList<Node>();
		for (Node n: old.getNodes()){
			nodes.add(n);
		}
	}
	public void addNode(Node n, int cost){
//		if (nodes == null){
//			nodes = new ArrayList<Node>();
//		}
		nodes.add(n);
		totalCost += cost;
	}
	
	public void printPath(){
		for (Node n: nodes){
			System.out.print(n.getValue() + "->");
		}
		System.out.println("XXX");
	}
	
	public Node getStart(){
		return nodes.get(0); //get first
	}
	
	public Node getLast(){
		if (nodes.size()>0){
			return nodes.get(nodes.size()-1);
		}else{
			return null;
		}
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	public int getCost(){
		return totalCost;
	}
}
	