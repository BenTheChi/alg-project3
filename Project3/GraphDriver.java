package project3;
import java.util.*;

import java.io.*;

/*
 * This is the main class for the Graph drivers.  It takes a text file
 * of nodes and arcs and calculates various metrics for both directed
 * and undirected.
 * 
 * The methods:
 * build both directed and undirected graph objects while providing their associated metrics
 * builds vertices and arcs
 * traverse the graphs both breadth first and depth first
 * tracks visited nodes in a DFS with DFS visit
 * 
 * They also calculate:
 * reciprocity (directed), degree correlation (undirected),
 * clustering coefficient (undirected), geodesic distance (directed),
 * diameter(directed), vertex total (both), arc total (both), density (both)
 * min/max/avg in and out degrees (directed), and strongly connected components (directed)
 * 
 * Author:  Benjamin Chi
 * Class:  ICS 311/Algorithms
 */
public class GraphDriver{
	
	//An object for both directed and undirected graph
	static DirectedGraph dirGraph;
	static UndirectedGraph undirGraph;
	
	//Variables for DFS, SCC, filename, and diameter
	static int time = 0;
	static int scc = 0;
	static String fname;
	static double maxDistance = 0;
	
	public static void main(String[] args){
		try{
			
			//Read in file
			Scanner fileinput = new Scanner(System.in);
			System.out.print("Enter the filename: ");
			fname = fileinput.next();
			
			//Makes the directed graph
			File filename = new File(fname);
			Scanner filein = new Scanner(filename);
			directedGraph(filein);
			
			dirGraph = null;
			
			//Makes the undirected graph
			filein = new Scanner(filename);
			undirectedGraph(filein);
			
			undirGraph = null;
		}
		
		//If the given file is not valid return an error
		catch(FileNotFoundException e){
			System.out.println("FileNotFoundException: " + e.getMessage());
		}
	}
	
	/*
	 * This method determines strongly connected components, density,
	 * total arcs/nodes, reciprocity, geodesic distance, diameter, and
	 * in/out degree statistics.
	 */
	public static void directedGraph(Scanner filein){
		
		//Set the in/out counters
		int minIn = 0;
		int minOut = 0;
		int maxIn = 0;
		int maxOut = 0;
		
		//Determines whether it's node or arc data
		boolean connectGraph = false;

		//Create the directed graph object
		dirGraph = new DirectedGraph();
		
		//Get node information from the file
		String currLine = filein.nextLine();
		filein.nextLine();
		
		//Read in each file line by line
		while(filein.hasNextLine()){
		
		currLine = filein.nextLine();
			
			//Disregard property lines
			if(currLine.contains("*")){
				connectGraph = true;
				filein.nextLine();
				currLine = filein.nextLine();
			}
			
			//The arc data
			if(connectGraph){
				String source;
				String target;
				int weight;
				String [] line = new String [4];
				line = currLine.split(" +");
				source = line[0];
				target = line[1];
				
				//If no weight data is given set arc weight to 0
				if(line.length < 3)
					weight = 0;
				
				//Turns weight from string to double to int
				else if(line[2].contains(".")){
					weight = (int) Double.parseDouble(line[2]);
				}
				
				else
					//If the weight is already int
					weight = Integer.parseInt(line[2]);
				
				//Add the arc information to the graph
				connectGraph(source, target, weight, true);
			}
			
			//The vertex data
			else{
				String node;
				String data;
				String[] line = new String[3];
				
				line = currLine.split(" +");
				node = line[0];
				
				//No data associated with vertex key
				if(line.length < 2)
					data = null;
				
				else
					data = line[1];
				
				//Add the vertex information to the graph
				makeVertex(node, data, true);
			}
		}
		
		//Create an iterator of vertices
		Iterator <Map.Entry<String, Vertex>> vertices = dirGraph.vertices();
		
		//Create a hashtable of <String, Vertex> objects and iterate through the vertices
		Map.Entry<String, Vertex> hashTable = (Map.Entry<String, Vertex>) vertices.next();
		
		//Get the in and out degree statistics from the directed graph
		minIn = dirGraph.inDegree(hashTable.getValue());
		minOut = dirGraph.outDegree(hashTable.getValue());
		maxIn = dirGraph.inDegree(hashTable.getValue());
		maxOut = dirGraph.outDegree(hashTable.getValue());
		
		int currIn = 0;
		int currOut = 0;
		
		//Find the min and max in and out degrees by iterating over the vertices
		while(vertices.hasNext()){
			
			//Gets the next set of vertices
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			Vertex currV = hashT.getValue();
			
			
			currIn = dirGraph.inDegree(currV);
			currOut = dirGraph.outDegree(currV);
			
			//Update the current in/out to the max in/out
			if(currIn > maxIn)
				maxIn = currIn;
			
			if(currOut > maxOut)
				maxOut = currOut;
			
			if(currIn < minIn)
				minIn = currIn;
			
			if(currOut < minOut)
				minOut = currOut;
		}
		
		//Get the total vertices and arcs
		double totalVertices = (double) dirGraph.numVertices();
		double totalArcs = (double) dirGraph.numArcs();
		
		//Print the basic graph information
		System.out.println("Graph name: " + fname + " (Directed Graph)");
		System.out.println("Vertex total: " + totalVertices);
		System.out.println("Arc total: " + totalArcs);
		System.out.format("%s%.5f\n", "Graph density: ", totalArcs/(totalVertices * (totalVertices - 1)));
		System.out.println("Min/Avg/Max inDegree: " + minIn + " / " + (double) dirGraph.totalInVertices()/ (double) dirGraph.numVertices() + " / " + maxIn);
		System.out.println("Min/Avg/Max outDegree: " + minOut + " / " + (double) dirGraph.totalOutVertices()/ (double) dirGraph.numVertices() + " / " + maxOut);
		
		//Get reciprocity and geodesic distance
		double reciprocity = reciprocity();
		double geodesic = geodesicDistance();
		
		//Print out the reciprocity, geodesic distance, and diameter
		System.out.println("Reciprocity: " + reciprocity);
		System.out.println("Mean Geodesic Distance: " + geodesic);
		System.out.println("Diameter: " + maxDistance);
		
		//Get the strongly connected components
		List<ArrayList<Vertex>> finalList = SCC();
		
		System.out.println("Number of Strongly Connected Components: " + scc);
		
		
		Collections.sort(finalList, new Comparator<ArrayList<Vertex>>(){
			public int compare(ArrayList<Vertex> a1, ArrayList<Vertex> a2){
				return a2.size() - a1.size();
			}
		});
		
		//Print out the strongly connected components
		System.out.format("%s%.0f\n", "Percent Vertices in Largest Strongly Connected Component: ", (double) finalList.get(0).size()/dirGraph.numVertices() * 100);
		System.out.println();
	}
	
	/*
	 * This method determines density, total arcs/nodes, 
	 * clustering coefficient, degree correlation, and
	 * degree statistics.
	 */
	public static void undirectedGraph(Scanner filein){
		int min;
		int max;
		
		 //Create the undirected graph object
		 undirGraph = new UndirectedGraph();
		 
		 //Determines whether it's a vertex or an arc
		 boolean connectGraph = false;
			
		 	//Read in the file
			String currLine = filein.nextLine();
			filein.nextLine();
			
			//Read in each file line by line
			while(filein.hasNextLine()){
			
			currLine = filein.nextLine();
				
				//Disregard property lines
				if(currLine.contains("*")){
					connectGraph = true;
					filein.nextLine();
					currLine = filein.nextLine();
				}
				
				//The arc data
				if(connectGraph){
					String source;
					String target;
					int weight;
					String [] line = new String [4];
					line = currLine.split(" +");
					source = line[0];
					target = line[1];
					
					//If no weight is provided set it to 0
					if(line.length < 3)
						weight = 0;
					
					//Turns weight from string to double to int
					else if(line[2].contains(".")){
						weight = (int) Double.parseDouble(line[2]);
					}
					
					//If the weight is already int
					else
					weight = Integer.parseInt(line[2]);
					
					//Add the arc information to the graph
					connectGraph(source, target, weight, false);
				}
				
				//The vertex data
				else{
					String node;
					String data;
					String[] line = new String[3];
					
					line = currLine.split(" +");
					node = line[0];
					
					//No data associated with vertex key
					if(line.length < 2)
						data = null;
					
					else
						data = line[1];
					
					//Add vertex information to the graph
					makeVertex(node, data, false);
				}
			}
			
			//Create an iterator of the vertices
			Iterator <Map.Entry<String, Vertex>> vertices = undirGraph.vertices();
			min = 0;
			max = 0;
			
			int curr = 0;
			int totalCurr = 0;
			
			//Find the min and max in and out degrees
			while(vertices.hasNext()){
				
				//Gets the next set of vertices
				Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
				Vertex currV = hashT.getValue();
				
				curr = undirGraph.degree(currV);
				
				//Update the current min and max degrees
				if(curr > max)
					max = curr;
				
				if(curr < min)
					min = curr;
				
				//The total amount of degrees
				totalCurr += curr;
			}
			
			//Get the total vertices and arcs
			double totalVertices = (double) undirGraph.numVertices();
			double totalArcs = (double) undirGraph.numArcs();
			
			//Print out the basic information
			System.out.println("Graph name: " + fname + " (Undirected Graph)");
			System.out.println("Vertex total: " + totalVertices);
			System.out.println("Arc total: " + totalArcs);
			System.out.format("%s%.5f\n", "Graph density: ", totalArcs/(totalVertices * (totalVertices - 1)));
			System.out.println("Min/Avg/Max Degree: " + min + " / " + (double) totalCurr/totalVertices + " / " + max);
			double clusteringCo = clusteringCoefficient();
			double degreeCorr = degreeCorrelation();
			
			//Print out the clustering coefficient and degree correlation
			System.out.println("Clustering Coefficent: " + clusteringCo);
			System.out.println("Degree Correlation: " + degreeCorr);
	}
	
	//Figures out how many reciprocal edges (vertices that share arcs in both directions) there are
	public static double reciprocity(){
		
		//An Iterator for all the directed graph arcs
		Iterator <Arc> it = dirGraph.arcs();
		
		//The current arc and it's vertex source and 
		Arc currArc;
		Vertex source;
		Vertex target;
		double rec = 0;
		
		//Iterates through all the arcs
		while(it.hasNext()){
			currArc = it.next();
			source = currArc.getSource();
			target = currArc.getTarget();
			
			//If the reverse arc exists
			if(dirGraph.getArc(target, source) != null)
				rec++;
		}
		
		//The percentage of reciprocal edges
		rec = rec / dirGraph.numArcs();
		
		return rec;
	}
	
	//Figures out how many vertices share an adjacent vertex
	public static double clusteringCoefficient(){
		double closed = 0;
		double total = 0;
		
		//An Iterator for the vertices and the 3 vertices to compare
		Iterator <Map.Entry<String, Vertex>> vertices = undirGraph.vertices();
		Vertex firstV;
		Vertex secV;
		Vertex thirdV;
		
		//For each vertex
		while(vertices.hasNext()){
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			firstV = hashT.getValue();
			
			Iterator <Vertex> secIt = undirGraph.adjacentVertices(firstV);
				
				//For each adjacent vertex
				while(secIt.hasNext()){
					secV = secIt.next();
						
						//For each adjacent adjacent vertex 
						Iterator <Vertex> thirdIt = undirGraph.adjacentVertices(secV);
						while(thirdIt.hasNext()){
							thirdV = thirdIt.next();
							
							//Excludes the first vertex from the adjacency list
							if(thirdV != firstV){
								
								//If the arc exists
								if(undirGraph.getArc(firstV, thirdV) != null)
									closed++;
								
								//Reverse arc is the same
								else if(undirGraph.getArc(thirdV, firstV) != null)
									closed++;
								
								total++;
							}
						}
				}
		}
		
		//Prints the closed and total clusters.  The coefficient is their fraction.
		System.out.println("closed: " + closed);
		System.out.println("total: " + total);
		
		return closed/total;
	}
	
	//Finds the coefficient that measures the correlation of
	//high degree vertices to low degree vertices.
	//The formula is ((s1 * (se*2)) - s2^2)/((s1 * s3) - s2^2)
	//With the s variables representing calculated sums to various degrees
	public static double degreeCorrelation(){
		
		//The 4 variables used for the fomula
		double s1 = 0;
		double s2 = 0;
		double s3 = 0;
		double se = 0;
		
		//The degree variables for the examined vertex
		double vDegree = 0;
		double sourceDegree = 0; 
		double targetDegree = 0;
		double r = 0;
		
		//The target vertex, it's adjacent vertices, and the arc(s) between them
		Vertex currV;
		Vertex source;
		Vertex target;
		Arc currArc;
		
		//An Iterator for the vertices
		Iterator <Map.Entry<String, Vertex>> vertices = undirGraph.vertices();
		
		//Iterate through all vertices.  Record their degrees as vDegree.
		while(vertices.hasNext()){
			
			//The hashtable associated with the Vertex needed to get the vertex/currV
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			currV = hashT.getValue();
			
			//The degree of the current vertex
			vDegree = undirGraph.degree(currV);
			
			//s1 is the sum of the degrees
			s1 += vDegree;
			
			//s2 is the sum of the degrees squared
			s2 += Math.pow(vDegree, 2);
			
			//s3 is the sum of the degrees cubed
			s3 += Math.pow(vDegree, 3);
		}
		
		//An Iterator for all the arcs
		Iterator <Arc> arcs = undirGraph.arcs();
			
		//Iterate through all the arcs.  Record the source times the target.
		while(arcs.hasNext()){
			currArc = arcs.next();
			
			source = currArc.getSource();
			target = currArc.getTarget();
			
			sourceDegree = undirGraph.degree(source);
			targetDegree = undirGraph.degree(target);
			
			//se is the sum of the source times the degrees for each arc
			se += (sourceDegree * targetDegree);
		}
		
		//The formula needs se to be doubled
		se = se * 2;
		
		//The formula for degree correlation
		r = ((s1 * se) - Math.pow(s2, 2))/((s1 * s3) - Math.pow(s2, 2));
		
		return r;
	}
	
	//Mean geodesic distance is the average path length between all pairs of vertices
	public static double geodesicDistance(){
		
		//The current vertex being examined
		Vertex currV = null;
		
		//The sum of all BFS results
		double[] total = {0, 0};
		
		//The result of each BFS for that vertex
		double[] temp = {0, 0};
		
		//An Iterator for all vertices
		Iterator <Map.Entry<String, Vertex>> vertices = dirGraph.vertices();
		
		
		//For all vertices call BFS, get the sum of their distances and total distance
		while(vertices.hasNext()){
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			currV = hashT.getValue();
			
			temp = BFS(currV);
			
			total[0] += temp[0];
			total[1] += temp[1];
		}
		
		//The average geodesic distance
		return total[0]/total[1];
	}
	
	//Does a breadth first search on the graph
	public static double[] BFS(Vertex s){
		double[] total = {0, 0};

		Vertex currV = null;
		Vertex adjV = null;
		double distance = 0;
		Iterator <Vertex> adjVertices;
		Iterator <Map.Entry<String, Vertex>> vertices = dirGraph.vertices();
		
		//Initialize all the vertices
		while(vertices.hasNext()){
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			currV = hashT.getValue();
			
			//Initialize all the vertices as white
			dirGraph.setAnnotation(currV, "color", "white");
			dirGraph.setAnnotation(currV, "distance", "inf");
			dirGraph.setAnnotation(currV, "parent", "none");	
		}
		
		//Initialize s as gray
		dirGraph.setAnnotation(s, "color", "gray");
		dirGraph.setAnnotation(s, "distance", "0");
		dirGraph.setAnnotation(s, "parent", "none");
		
		//Make a queue and add s to it
		Queue<Vertex> Q = new LinkedList<Vertex>();
		Q.add(s);
		
		//While the queue is not empty 
		while(Q.size() != 0){
			currV = Q.poll();
			
			adjVertices = dirGraph.outAdjacentVertices(currV);
			
			//For each adjacent vertex to currV
			while(adjVertices.hasNext()){
				adjV = adjVertices.next();
				
				//If it's unvisited give the node a parent and distance
				if(dirGraph.getAnnotation(adjV, "color") == "white"){
					
					//Get the distance of currV and increment it by 1 to make it
					distance = Double.valueOf((String)dirGraph.getAnnotation(currV, "distance"));
					distance++;
					
					//Set the adjacent vertex as gray and make it's parent the current vertex
					dirGraph.setAnnotation(adjV, "color", "gray");
					dirGraph.setAnnotation(adjV, "distance", String.valueOf(distance));
					dirGraph.setAnnotation(adjV, "parent", currV);
					
					//Add the newly edited adjacent vertex to the queue
					Q.add(adjV);
					
					//Add the distance to the total distance
					total[0] = total[0] + (distance);
					
					//The biggest distance traversed is the diameter
					if(distance > maxDistance)
						maxDistance = distance;
				}
			}
			
			//Set node as visited all adjacent vertices
			dirGraph.setAnnotation(currV, "color", "black");
			total[1]++;
		}
		
		//Don't count the starting vertex
		total[1] --;
		return total;
	}
	
	//Adds a vertex to either directed or undirected graph
	public static void makeVertex(String node, String data, boolean directed){
		
		//Use the DirectedGraph methods to add a new vertex
		if(directed){
			Vertex realNode = dirGraph.insertVertex(node);
			dirGraph.setVertexData(realNode, data);
		}
		
		//Use the UndirectedGraph methods to add a new vertex
		else{
			Vertex realNode = undirGraph.insertVertex(node);
			undirGraph.setVertexData(realNode, data);
		}
	}
	
	//Sets arcs given two vertices for either directed or undirected graphs
	public static void connectGraph(String source, String target, int weight, boolean directed){
		
		//Set arcs using the DirectedGraph methods
		if(directed){
			Vertex sourceV = dirGraph.getVertex(source);
			Vertex targetV = dirGraph.getVertex(target);
			Arc newArc = dirGraph.insertArc(sourceV, targetV);
			dirGraph.setArcData(newArc, String.valueOf(weight));
		}
		
		//Set arcs using the UndirectedGraph methods
		else{
			Vertex sourceV = undirGraph.getVertex(source);
			Vertex targetV = undirGraph.getVertex(target);
			Arc newArc = undirGraph.insertArc(sourceV, targetV);
			undirGraph.setArcData(newArc, String.valueOf(weight));
		}
	}
	
	//Uses DFS and DFS-visit to figure out Strongly Connected Components
	public static List<ArrayList<Vertex>> SCC(){
		
		//Make a new arrayList that holds all the vertices in finish time order
		ArrayList <Vertex> finishArray = new ArrayList <Vertex> (dirGraph.numVertices());
		
		//The first pass of DFS
		finishArray = DFS(finishArray);
		
		//Transpose all the arcs in dirGraph
		Iterator <Arc> arcList = dirGraph.arcs();
		Arc currArc;
		
		//Reverse all the arcs in the directed graph
		while(arcList.hasNext()){
			currArc = arcList.next();
			
			dirGraph.reverseDirection(currArc);
		}
		
		//The second pass of DFS
		DFS(finishArray);
		
		//Create a Hashtable list of vertices
		Hashtable <String, ArrayList<Vertex>> sccList = new Hashtable <String, ArrayList<Vertex>>();
		
		//An Iterator for all vertices
		Iterator <Map.Entry<String, Vertex>> vertices = dirGraph.vertices();
		String currSCC;
		
		//Iterate through all the vertices 
		while(vertices.hasNext()){
			
			//Get the current vertex
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			Vertex currV = hashT.getValue();
			
			//Get the vertex associated with this strongly connected component
			currSCC = (String) dirGraph.getAnnotation(currV, "scc");
			
			//Add this vertex to these strongly connected components
			if(sccList.containsKey(currSCC))
				sccList.get(currSCC).add(currV);
			
			//If it isn't create a new array to store new strongly connected components
			else{
				ArrayList<Vertex> sccArray = new ArrayList<Vertex>();
				sccArray.add(currV);
				sccList.put(currSCC, sccArray);
			}
		}
		
		//Create an arraylist to store each SCC
		List<ArrayList<Vertex>> sccListFinal = new ArrayList<ArrayList<Vertex>>();
		
		//Add each SCC to the final list
		for(int i = 1; i <= scc; i++){
			sccListFinal.add(sccList.get(Integer.toString(i)));
			sccList.remove(Integer.toString(i));
		}
		
		return sccListFinal;
	}
	
	/*
	 * Does a DFS on the dirGraph and passes an arrayList that stores finish time.
	 * If the arraylist is empty it bases the DFS on dirGraph.vertices()
	 * If it is not empty it bases it on the arrayList
	 */
	public static ArrayList <Vertex> DFS(ArrayList <Vertex> finishArray){
		
		scc = 0;
		time = 0;
		
		//If the arrayList is empty
		if(finishArray.isEmpty()){
			
			//Creates an iterator object
			Iterator <Map.Entry<String, Vertex>> vertices = dirGraph.vertices();
			
			//Loops through the iterator
			while(vertices.hasNext()){
				Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
				Vertex currV = hashT.getValue();
				
				//Sets all vertices to white and null parents
				dirGraph.setAnnotation(currV, "color", "white");
				dirGraph.setAnnotation(currV, "parent", null);
			}
			
			//Reloads the iterator object into vertices
			vertices = dirGraph.vertices();
			
			//Loops through the iterator object
			while(vertices.hasNext()){
				Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
				Vertex currV = hashT.getValue();
				
				//Visits all the unvisited nodes
				if(dirGraph.getAnnotation(currV, "color") == "white"){
					DFSVisit(currV, finishArray);
				}
			}
		}
		
		//If the arrayList is not empty.  The dirGraph should be transposed
		else{
			Vertex currV;
			
			//Iterates through the arrayList
			for(int i = 0; i < finishArray.size(); i++){
				currV = finishArray.get(i);
				
				//Resets all vertices to white and null parents
				dirGraph.setAnnotation(currV, "color", "white");
				dirGraph.setAnnotation(currV, "parent", null);
			}
			
			//Iterates through the arrayList
			for(int i = 0; i < finishArray.size(); i++){
				currV = finishArray.get(i);
				
				//Visits all the unvisited nodes
				if(dirGraph.getAnnotation(currV, "color") == "white"){
					
					//Each loop represents a different strongly connected component group
					scc++;
					DFSVisit(currV);
				}
			}
		}
		
		return finishArray;
	}
	
	/*
	 * This visits each vertex and loads them into the finishArray marked by
	 * their finish time
	 */
	public static void DFSVisit(Vertex u, ArrayList <Vertex> finishArray){		
		time++;
		
		//Marks the discovery time and the color as gray
		dirGraph.setAnnotation(u, "discovery", time);
		dirGraph.setAnnotation(u, "color", "gray");
		
		//Loads an iterator object from the outAdjacencylist of the current vertex
		Iterator <Vertex> outV = dirGraph.outAdjacentVertices(u);
		
		//Loops through each adjacent vertex
		while(outV.hasNext()){
			Vertex currV = (Vertex) outV.next();
			
			//If the vertex is unvisited
			if(dirGraph.getAnnotation(currV, "color") == "white"){
				
				//Mark the u vertex as the parent of the adjacent vertex being looked at
				dirGraph.setAnnotation(currV, "parent", u);
				
				//Look at the adjacent vertices of the current adjacent vertex being looked at
				DFSVisit(currV, finishArray);
			}
		}
		
		//Once all adjacent vertices have been examined mark it black and load it into beginning of the arrayList
		dirGraph.setAnnotation(u, "color", "black");
		finishArray.add(0, u);
		
		//Increment time and mark it as the finish time of u Vertex
		time++;
		dirGraph.setAnnotation(u, "finish", time);
		
	}
	
	/*
	 * On the second pass we are examining each adjacent vertex of the 
	 * transposed dirGraph
	 */
	public static void DFSVisit(Vertex u){

		//Mark it gray, and mark it in the current scc group
		dirGraph.setAnnotation(u, "color", "gray");
		dirGraph.setAnnotation(u, "scc", Integer.toString(scc));
		
		//Make a new iterator object for the outAdjacentVertices
		Iterator <Vertex> outV = dirGraph.outAdjacentVertices(u);
		
		//Loop through each adjacent vertex
		while(outV.hasNext()){
			Vertex currV = (Vertex) outV.next();
			
			//If the vertex is unvisited
			if(dirGraph.getAnnotation(currV, "color") == "white"){
				
				//Mark the u vertex as the parent of the adjacent vertex being looked at
				dirGraph.setAnnotation(currV, "parent", u);
				
				//Look at the adjacent vertices of the current adjacent vertex being looked at
				DFSVisit(currV);
			}
		}
		
		//Once all adjacent vertices have been examined mark it black
		dirGraph.setAnnotation(u, "color", "black");
	}
}