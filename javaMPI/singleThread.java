import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class singleThread {

	/**
	 * @param args
	 * @throws IOException
	 */
	static int n;
	static int [][] next;
	public static void main(String[] args) throws IOException {
		int[][] graph;
		int[][] costGraph;

		/******************************************* Upload Data ****************************************/
		int first;
		int second;
		int third;

		Pattern pattern = Pattern.compile("\\d*");
		BufferedReader in = new BufferedReader(new FileReader("single.txt"));
		String line = in.readLine();
		String[] items = line.split(" ");
		first = Integer.parseInt(items[0]);
		second = Integer.parseInt(items[1]);
		graph = new int[first][first];
		costGraph = new int[first][first];
		next = new int[first][first];
		for (int i = 0; i < first; i++) {
			for (int j = 0; j < first; j++) {
				next[i][j] = 9999;
				if(i == j){
				graph[i][j] = 0;
				}else{
					graph[i][j] = 9999;
				}
			}
		}
		n = first;

		while ((line = in.readLine()) != null) {
			items = line.split(" ");
			first = Integer.parseInt(items[0]);
			second = Integer.parseInt(items[1]);
			third = Integer.parseInt(items[2]);
			// System.out.println("first:"+first+"second:"+second+"third:"+third);
			graph[first][second] = third;
			System.out.println(first + "--" + second + "--" + third);
		}
		/******************************************* Data uploaded ****************************************/
	costGraph = FloydWarshall(graph);
	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++) {
			path(i,j,costGraph);
		}
	}
	
	
	}
	public static void path(int i,int j,int[][] graph){
		if(graph[i][j] >9998){
			System.out.println("no path");
		}
		int intermidiate = next[i][j];
		if(intermidiate >9000){
			System.out.println("Cost of "+i+"->"+j+ "is "+ graph[i][j]);
			System.out.println(i+"->"+j);
		}else{
			System.out.println("Cost of "+i+"->"+j+ "is "+ graph[i][j]);
			System.out.println("intermidiate paths from: "+i+" to: "+j);
			path(i,intermidiate,graph);
			path(intermidiate,j,graph);
			System.out.println("End on intermidiates from: "+i+" to: "+j);
			
		}
	}

	public static int[][] FloydWarshall(int[][] graph) {
		for(int k = 0;k<n;k++){
			for(int i =0;i<n;i++){
				for(int j=0;j<n;j++){
					if(graph[i][k] + graph[k][j] < graph[i][j] ){
						graph[i][j] = graph[i][k]+graph[k][j];
						next[i][j] = k;
					}
				}
			}
		}
		return graph;

	}

}
