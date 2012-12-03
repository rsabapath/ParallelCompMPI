import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mpi.*;

public class HelloWorld {
	static int n;

	public static void main(String args[]) throws Exception {
		MPI.Init(args);
		int me = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		int[][] graph;
		int[][] costGraph;

		/******************************************* Upload Data ****************************************/
		int first;
		int second;
		int third;

		Pattern pattern = Pattern.compile("\\d*");
		BufferedReader in = new BufferedReader(new FileReader(me + ".txt"));
		String line = in.readLine();
		String[] items = line.split(" ");
		first = Integer.parseInt(items[0]);
		second = Integer.parseInt(items[1]);
		n = first;
		graph = new int[first][first];
		costGraph = new int[first][first];
		while ((line = in.readLine()) != null) {
			items = line.split(" ");
			first = Integer.parseInt(items[0]);
			second = Integer.parseInt(items[1]);
			third = Integer.parseInt(items[2]);
			graph[first][second] = third;
			System.out.println("id" + me + "->content:" + first + "--" + second
					+ "--" + third);
		}
		/******************************************* Data uploaded ****************************************/
		int divsionOfLabour = n / size;
		if (me == 0) {
			int[] message = { 1, 2, 3 };

			MPI.COMM_WORLD.Send(message, 0, message.length, MPI.INT, 1, 99);
		} else {
			int[] message = new int[3];
			MPI.COMM_WORLD.Irecv(message, 0, 3, MPI.INT, 0, 99);
			// System.out.println("received:" + "--" + message[1] + "--"
			// + message[2]);

		}
		System.out.println("Hi from <" + me + ">" + "size=" + size);
		MPI.Finalize();
	}
	// public static
}
