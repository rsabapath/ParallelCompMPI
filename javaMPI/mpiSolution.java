import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	
		costGraph = FloydWarshall(graph);
		
	/*	for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				path(i, j, costGraph);
			}
		}
	*/

		int divsionOfLabour = n / size;
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
		System.out.println("Hi from <" + me + ">" + "size=" + size);
		while (doClose())
			;
		MPI.Finalize();
	}

	// public static

	private static synchronized boolean doClose() {
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
	public void run() { // TODO Auto-generated method stub
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
				System.out.println("I am requested to close Comm is:" + me);
				closed();
				return;

			}
			System.out.println("received:" + message[0] + "--" + message[1]
					+ "--" + message[2]);
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
		int first;
		int second;
		int third;

		Pattern pattern = Pattern.compile("\\d*");
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(me + ".txt"));

			String line = in.readLine();
			String[] items = line.split(" ");
			first = Integer.parseInt(items[0]);
			second = Integer.parseInt(items[1]);
			n = first;
			graph = new int[first][first];
			next = new int[first][first];
			for (int i = 0; i < first; i++) {
				for (int j = 0; j < first; j++) {
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
				first = Integer.parseInt(items[0]);
				second = Integer.parseInt(items[1]);
				third = Integer.parseInt(items[2]);
				graph[first][second] = third;
				System.out.println("id" + me + "->content:" + first + "--" + second + "--" + third);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return graph;
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

	public static int[][] FloydWarshall(int[][] graph) {
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (graph[i][k] + graph[k][j] < graph[i][j]) {
						graph[i][j] = graph[i][k] + graph[k][j];
						next[i][j] = k;
					}
				}
			}
		}
		return graph;

	}

}
