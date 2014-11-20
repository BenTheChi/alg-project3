package project3;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/*
 * This class represents an Undirected Graph.  It uses 4 main structures.
 * The vertices are stored in a hashtable with the key as String (vertex ID) and a value of a Vertex
 * The adjacenct vertices are stored in a hashtable with the key as String (vertex ID) and value of an ArrayList of Vertex IDs
 * The pairs of vertices for each arc are stored in a hashtable with the key as a source vertex 
 * and the value as a hashtable with a key as a target vertex and a value of their shared Arc
 * The arcs themselves are stored in an ArrayList
 * 
 * The methods can:
 * provide iterators for arcs and vertices.
 * provide the amount of arcs and vertices
 * add, remove, view, or adjust data for both vertices and arcs.
 * add, remove, or view vertices and arcs themselves.
 * add, remove, or adjust annotations for both vertices and arcs
 * 
 * Author:  Benjamin Chi
 * Class:  ICS 311/Algorithms
 */

public class UndirectedGraph {
	private Hashtable <String, Vertex> vertices;
	private Hashtable <String, ArrayList<String>> adjacencyList;
	private Hashtable <Vertex, Hashtable<Vertex, Arc>> arcs;
	private ArrayList<Arc> arcList;
	
	//Creates objects for the structures above
	public UndirectedGraph(){
		vertices = new Hashtable<String, Vertex>();
		adjacencyList = new Hashtable <String, ArrayList<String>>();
		arcs = new Hashtable <Vertex, Hashtable<Vertex, Arc>>();
		arcList = new ArrayList<Arc>();
	}
	
	/*
	 * Returns the total number of vertices in the graph
	 */
	public int numVertices(){
		return vertices.size();
	}
	
	/*
	 * Returns the total number of arcs in the graph
	 */
	public int numArcs(){
		return arcList.size();
	}
	
	/*
	 * Returns an iterator object of type Map.Entry that lists all the vertices
	 */
	public java.util.Iterator <Map.Entry<String, Vertex>> vertices(){
		
		return vertices.entrySet().iterator();
	}
	
	/*
	 * Returns an iterator object of type Arc that lists all the vertices
	 */
	public java.util.Iterator <Arc> arcs(){

		return arcList.iterator();
	}
	
	/*
	 * Returns the vertex that matches to a particular key
	 */
	public Vertex getVertex(String key){
		return vertices.get(key);
	}
	
	/*
	 * Returns an arc object that matches a source and target vertex
	 */
	public Arc getArc(Vertex source, Vertex target){
		
		//Set variables
		Arc targetArc = null;
		Hashtable<Vertex, Arc> targetTable;
		
		if(arcs.containsKey(source)){
			//Get the sub-hashtable that is associated with source
			targetTable = arcs.get(source);
			
			//Get the arc that's associated with the target in the sub-hashtable
			targetArc = targetTable.get(target);
		}
		
		return targetArc;
	}
	
	/*
	 * Returns the data associated with a vertex
	 */
	public Object getVertexData(Vertex v){
		return v.getData();
	}
	
	/*
	 * Returns the weight of an arc
	 */
	public Object getArcData(Arc a){
		return a.getWeight();
	}
	
	/*
	 * Returns the number of arcs connected to a vertex
	 */
	public int degree(Vertex v){
		int deg = 0;
		
		if(adjacencyList.containsKey(v.getKey())){
			deg = adjacencyList.get(v.getKey()).size();
		}
		
		return deg;
	}
	
	/*
	 * Returns an iterator object of all the adjacentVertices to a vertex
	 */
	public java.util.Iterator <Vertex> adjacentVertices(Vertex v){
		ArrayList <String> adjKeys;
		ArrayList <Vertex> adjList = new ArrayList <Vertex>();

		if(adjacencyList.containsKey(v.getKey())){
			adjKeys = adjacencyList.get(v.getKey());
			
			for(int i = 0; i < adjKeys.size(); i++){
				adjList.add(vertices.get(adjKeys.get(i)));
			}
		}
		
		return adjList.iterator();
	}
	
	/*
	 * Returns the source of an arc
	 */
	public Vertex origin(Arc a){
		return a.getSource();
	}
	
	/*
	 * Returns the target of an arc
	 */
	public Vertex destination(Arc a){
		return a.getTarget();
	}
	
	/*
	 * Creates a new vertex and inserts it in the vertices table
	 */
	public Vertex insertVertex(String key){
		Vertex vertex = new Vertex(key);
		vertices.put(key, vertex);
		
		return vertex;
	}
	
	/*
	 * Creates a new arc and inserts it into the arc table.  Updates the in and out adjacency list
	 */
	public Arc insertArc(Vertex source, Vertex target){
		
		//Checks to see if the arc exists
		if(arcs.containsKey(source))
			if(arcs.get(source).containsKey(target))
				return arcs.get(source).get(target);
		
		if(arcs.containsKey(target))
			if(arcs.get(target).containsKey(source))
				return arcs.get(target).get(source);
		
		Arc arc = new Arc(source, target);
		String sourceKey = source.getKey();
		String targetKey = target.getKey();
		
		//Checks the hashtable to see if the Vertex already has an arc
		if(arcs.containsKey(source)){
			arcs.get(source).put(target, arc);
		}
		
		/*If the Vertex does not exist in the hashtable,
		 * It creates a new sub-hashtable, puts the Arc in the table, and 
		 * puts the sub-hashtable in the hashtable
		 */
		else{
			Hashtable<Vertex, Arc> arcTable = new Hashtable<Vertex, Arc>();
			arcTable.put(target, arc);
			
			arcs.put(source, arcTable);
		}
		
		//Checks the hashtable to see if the Vertex key exists
		if(adjacencyList.containsKey(sourceKey)){
			adjacencyList.get(sourceKey).add(targetKey);
		}
		
		/* If the Vertex key does not exist, it creates a new ArrayList,
		 * adds the Vertex to the Arraylist, and adds the ArrayList to the
		 * hashtable
		 */
		else{
			ArrayList <String> vertexArrayList = new ArrayList <String>();
				vertexArrayList.add(targetKey);
			
			adjacencyList.put(sourceKey, vertexArrayList);
		}
		
		//Checks the hashtable to see if the Vertex key exists
		if(adjacencyList.containsKey(targetKey)){
			adjacencyList.get(targetKey).add(sourceKey);
		}
		
		/* If the Vertex key does not exist, it creates a new ArrayList,
		 * adds the Vertex to the Arraylist, and adds the ArrayList to the
		 * hashtable
		 */
		else{
			ArrayList <String> vertexArrayList = new ArrayList <String>();
				vertexArrayList.add(sourceKey);
			
			adjacencyList.put(targetKey, vertexArrayList);
		}
		
		//Adds the arc to arcList array
		arcList.add(arc);
		
		return arc;
	}
	
	/*
	 * Sets the data on a vertex
	 */
	public void setVertexData(Vertex v, String data){
		v.setData((String) data);
	}
	
	/*
	 * Sets the weight of an arc
	 */
	public void setArcData(Arc a, String data){
		a.setWeight(data);
	}
	
	/*
	 * Adds a new key and value to the hash map annotation for the vertex
	 */
	public void setAnnotation(Vertex v, Object k, Object o){
		v.annotation.put(k, o);
	}
	
	/*
	 * Adds a new key and value to the hash map annotation for the arc
	 */
	public void setAnnotation(Arc a, Object k, Object o){
		a.annotation.put(k, o);
	}
	
	/*
	 * Returns the corresponding value of a particular key for a vertex from the hash map annotation
	 */
	public Object getAnnotation(Vertex v, Object k){
		return v.annotation.get(k);
	}
	
	/*
	 * Returns the corresponding value of a particular key for an arc from the hash map annotation
	 */
	public Object getAnnotation(Arc a, Object k){
		return a.annotation.get(k);
	}
	
	/*
	 * Removes the key and it's corresponding value for a vertex from the hash map annotation
	 */
	public Object removeAnnotation(Vertex v, Object k){
		Object obj = v.annotation.get(k);
		v.annotation.remove(k);
		
		return obj;
	}
	
	/*
	 * Removes the key and it's corresponding value for a arc from the hash map annotation
	 */
	public Object removeAnnotation(Arc a, Object k){
		Object obj = a.annotation.get(k);
		a.annotation.remove(k);
		
		return obj;
	}
		
	/*
	 * Removes all of a particular key on both arcs and vertices
	 */
	public void clearAnnotations(Object k){
		Iterator <Map.Entry<String, Vertex>> vertices = vertices();
		
		while(vertices.hasNext()){
			Map.Entry<String, Vertex> hashT = (Map.Entry<String, Vertex>) vertices.next();
			
			Vertex currVertex = hashT.getValue();
			
			if(currVertex.annotation.containsKey(k))
				removeAnnotation(currVertex, k);
		}
		
		for(int i = 0; i < arcList.size(); i++){
			Arc currArc = arcList.get(i);
			if(currArc.annotation.containsKey(k))
				removeAnnotation(currArc, k);
		}
	}
}
