import java.util.List;

public class myNode{
	public int value;
	public List<myEdge> next;
	
	public myNode(int startNode){
		value = startNode;
	}
	
	public addNext(myNode b, int cost){
		if (next == null){
			next = new ArrayList<myEdge>();
		}
		next.add(new myEdge(this, b, cost));
	}
	
	public int getValue(){
		return value;
	}
	
}

public class myEdge{
	public myNode start;
	public myNode end;
	public int cost;
	
	public myEdge(myNode a, myNode b, int cost){
		start = a;
		end = b;
		cost = cost;
	}
	
	public myNode getA(){
		return start;
	}
	
	public myNode getB(){
		return end;
	}
	
	public int getCost(){
		return cost;
	}
	
};


public class myPath{
	int totalCost;
	public List<myNode> nodes;
	
	public myPath(myNode first){
		nodes = new ArrayList<myNode>();
		nodes.add(first);
	}
	
	public void addNode(myNode n, int cost){
//		if (nodes == null){
//			nodes = new ArrayList<myNode>();
//		}
		nodes.add(n);
		totalCost += cost;
	}
	
	public void toString(){
		for (myNode n: nodes){
			System.out.print(n.value);
		}
		System.out.println("");
	}
	
	public int getCost(){
		return totalCost;
	}
	
}