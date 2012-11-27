import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mpi.*;

public class mpiSolution implements Runnable {
	static int n;
	static boolean exit = false;
	static int me;
	static int size;
	static int closedThreads = 1;
	static boolean[] threadComm;
	static int[][] next;
	static List<Node> nodes = new ArrayList<Node>();

	static Node start = null;
	static Node last = null;
	static List<Path> paths = new ArrayList<Path>(); //all of the paths found in this cluster

	public static void main(String args[]) throws Exception {
		MPI.Init(args);
		me = MPI.COMM_WORLD.Rank();
		size = MPI.COMM_WORLD.Size();
		threadComm = new boolean[size];
		threadComm[me] = true;
		for (int i = 0; i < size; i++) {
			if (i == me) {
				continue;
			}
			(new Thread(new mpiSolution())).start();
		}

		int[][] graph = create_graph();
		int[][] costGraph = new int[n][n];


		if (start != null) { //assuming that this cluster contains the start node
			graphStart();		
		}
		if (last != null){
//			graphEnd(); //TODO : RATHESH, this function can essentially be the opposite of graphStart? Add all of the final nodes from paths at this point to the queue and proceed!!
		}


		int divsionOfLabour = n; // size;
		if (me == 0) {
			int[] message = { 1, 2, 3, 4 };
			MPI.COMM_WORLD.Isend(message, 0, message.length, MPI.INT, 1, me);
		}

		int[] message = { 9999, 9999, 9999, 9999 };
		for (int i = 0; i < size; i++) {
			if (i == me) {
				continue;
			}
			MPI.COMM_WORLD.Isend(message, 0, message.length, MPI.INT, i, 99);
		}
		//		System.out.println("Hi from <" + me + ">" + "size=" + size);
		while (doClose());
		MPI.Finalize();
	}


	public static void graphStart() {
		paths.add(new Path(start)); //start off with 'path' 0

		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(start); //start off the queue with node 0

		while(!queue.isEmpty()){
			Node currentNode = queue.removeFirst();	

			List<Path> newPaths = new ArrayList<Path>(); //build up a list of new paths then add to master list
			List<Path> oldPaths = new ArrayList<Path>(); //to be removed after every iteration

			for (Path currentPath : paths){ //for all of the paths already implemented  
				//System.out.print("Size: " + paths.size() + "  Path: ");
				//currentPath.printPath();

				for(Edge e : currentNode.getConnections()){
					//System.out.println("# edges of " + current.getValue() + " : " + current.getConnections().size());
					if (currentPath.getLast().getValue() == e.getA().getValue()){
						if (nodes.contains(e.getB()) && !queue.contains(e.getB())){ //b is found in this cluster
							queue.addLast(e.getB()); //put on the queue
						}else{ //b is found in another cluster
							int[] message = {currentNode.getValue()}; //we need the nodes that are connected to current
							for (int j=0; j< size; j++){
								if (j==me){
									continue;
								}else{
									MPI.COMM_WORLD.Isend(message, 0, message.length, MPI.INT, j, 99); //"does anybody have the node I need?"
								}
							} 

						}
						Path temp = new Path(currentPath); //make a copy of the path until this point
						//System.out.print("TEMP: "); temp.printPath();
						temp.addNode(e.getB(), e.getCost()); //add the next edge to the temp path
						//System.out.print("TEMP2: "); temp.printPath();

						newPaths.add(temp); //add this path variation to the newPaths to be added
						oldPaths.add(currentPath);
					}
				}
			}
			paths.addAll(newPaths);
//			System.out.print("Before size: " + paths.size());
			paths.removeAll(oldPaths);
//			System.out.println("After size: " + paths.size());


		}
		for (Path p : paths) {
			p.printPath();
		}
	}
	
	public static void graphEnd(){
		LinkedList<Node> queue = new LinkedList<Node>();
		
		for (Path p : paths){
			queue.add(p.getLast()); //start off the queue with all incoming end of paths!

		while(!queue.isEmpty()){
			Node currentNode = queue.removeFirst();	

			List<Path> newPaths = new ArrayList<Path>(); //build up a list of new paths then add to master list
			List<Path> oldPaths = new ArrayList<Path>(); //to be removed after every iteration

			for (Path currentPath : paths){ //for all of the paths already implemented  
				//System.out.print("Size: " + paths.size() + "  Path: ");
				//currentPath.printPath();

				for(Edge e : currentNode.getConnections()){
					//System.out.println("# edges of " + current.getValue() + " : " + current.getConnections().size());
					if (currentPath.getLast().getValue() == e.getA().getValue()){
						if (nodes.contains(e.getB()) && !queue.contains(e.getB())){ //b is found in this cluster
							queue.addLast(e.getB()); //put on the queue
						}else{ //b is found in another cluster
							int[] message = {currentNode.getValue()}; //we need the nodes that are connected to current
							for (int j=0; j< size; j++){
								if (j==me){
									continue;
								}else{
									MPI.COMM_WORLD.Isend(message, 0, message.length, MPI.INT, j, 99); //"does anybody have the node I need?"
								}
							} 

						}
						Path temp = new Path(currentPath); //make a copy of the path until this point
						//System.out.print("TEMP: "); temp.printPath();
						temp.addNode(e.getB(), e.getCost()); //add the next edge to the temp path
						//System.out.print("TEMP2: "); temp.printPath();

						newPaths.add(temp); //add this path variation to the newPaths to be added
						oldPaths.add(currentPath);
					}
				}
			}
			paths.addAll(newPaths);
//			System.out.print("Before size: " + paths.size());
			paths.removeAll(oldPaths);
//			System.out.println("After size: " + paths.size());

		}
		}
	}

	private static synchronized boolean doClose() {	// public static
		if (closedThreads == size) {
			return false;
		} else {
			return true;
		}

	}

	private static synchronized void closed() {

		closedThreads++;
	}

	@Override
	public void run() {
		int myDuty = assignment();
		if (myDuty > 9000) {
			// not needed, In event it is created
			return;
		}
		while (true) {

			int[] message = new int[4];
			MPI.COMM_WORLD.Recv(message, 0, 4, MPI.INT, myDuty, 99);
			if (message[0] > 9000 && message[1] > 9000 && message[2] > 9000
					&& message[3] > 9000) {
//				System.out.println("I am requested to close Comm is:" + me);
				closed();
				return;

			}
//			System.out.println("received:" + message[0] + "--" + message[1] + "--" + message[2]);
		}
	}

	public static synchronized int assignment() {
		for (int i = 0; i < threadComm.length; i++) {
			if (threadComm[i] == false) {
				threadComm[i] = true;
				return i;
			}

		}
		return 9999;

	}

	/*
	 * This method will create the graph to work on based off the data file
	 * Note: the file is read based off its rank. So process of Rank 0 reads
	 * 0.txt, and
	 */

	public static int[][] create_graph() {
		int[][] graph = null;

		Pattern pattern = Pattern.compile("\\d*");
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(me + ".txt"));

			String line = in.readLine();
			String[] items = line.split(" ");
			int startingNode = Integer.parseInt(items[0]);
			int endingNode = Integer.parseInt(items[1]);

			line = in.readLine();
			items = line.split(" ");
			int numNodes = Integer.parseInt(items[0]);
			int numEdges = Integer.parseInt(items[1]);
			n = numNodes;

			line = in.readLine();
			items = line.split(" ");

			for (int i = 0; i < items.length; i++) {
				int currentValue = Integer.parseInt(items[i]);
				Node node = new Node(currentValue);
				nodes.add(node);
				if (currentValue == startingNode){
					start = node;
				}else if (currentValue == endingNode){
					last = node;
				}
			}

			//			if (me * (n / size) <= startingNode && startingNode < (me + 1) * (n / size)) {
			//				start = nodes.get(startingNode % (n / size));
			//			}
			//			if (me * (n / size) <= endingNode && endingNode < (me + 1) * (n / size)) {
			//				last = nodes.get(endingNode % (n / size));
			//			}

			graph = new int[numNodes][numNodes];
			next = new int[numNodes][numNodes];
			for (int i = 0; i < numNodes; i++) {
				for (int j = 0; j < numNodes; j++) {
					next[i][j] = 9999;
					if (i == j) {
						graph[i][j] = 0;
					} else {
						graph[i][j] = 9999;
					}
				}
			}


			while ((line = in.readLine()) != null) {
				items = line.split(" ");
				int nodeA = Integer.parseInt(items[0]);
				Node node = null;
				int nodeB = Integer.parseInt(items[1]);
				Node node2 = null;
				int edgeWeight = Integer.parseInt(items[2]);

				if (me==0){ //cluster with start node
					node = getNode(nodeA);

					if (isAccessible(nodeB)){
						node2 = getNode(nodeB);
					}else{
						node2 = new Node(nodeB);
					}
				}else{ //cluster with end node
					if (isAccessible(nodeA)){
						node = getNode(nodeA);
					}else{
						node = new Node(nodeA);
					}

					node2 = getNode(nodeB);
				}

				node.addNext(node2, edgeWeight);

				graph[nodeA][nodeB] = edgeWeight;
//				System.out.println("id" + me + "->content:" + nodeA + "--" + nodeB + "--" + graph[nodeA][nodeB]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return graph;
	}

	public static boolean isAccessible(int nodeValue){
		for(Node n: nodes){
			if (n.getValue() == nodeValue){
				return true;
			}
		}
		return false;
	}

	public static Node getNode(int nodeValue){
		for(Node n: nodes){
			if (n.getValue() == nodeValue){
				return n;
			}
		}
		return null;
	}

	public static void path(int i, int j, int[][] graph) {
		if (graph[i][j] > 9000) {
			System.out.println("no path");
		}
		int intermidiate = next[i][j];
		if (intermidiate > 9000) {
			System.out.println("Cost of " + i + "->" + j + "is " + graph[i][j]);
			System.out.println(i + "->" + j);
		} else {
			System.out.println("Cost of " + i + "->" + j + "is " + graph[i][j]);
			System.out.println("intermidiate paths from: " + i + " to: " + j);
			path(i, intermidiate, graph);
			path(intermidiate, j, graph);
			System.out.println("End on intermidiates from: " + i + " to: " + j);

		}
	}

}
