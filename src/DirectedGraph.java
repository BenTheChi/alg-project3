package project3;
import java.util.*;
import java.util.Map.Entry;

/*
 * This class represents a Directed Graph.  It uses 5 main structures.
 * The vertices are stored in a hashtable with the key as String (vertex ID) and a value of a Vertex
 * The adjacenct vertices are stored in two hashtables, one for in one for out (since it's directed),
 * with the key as String (vertex ID) and value of an ArrayList of Vertex IDs
 * The pairs of vertices for each arc are stored in a hashtable with the key as a source vertex 
 * and the value as a hashtable with a key as a target vertex and a value of their shared Arc
 * The arcs themselves are stored in an ArrayList
 * 
 * The methods can:
 * provide iterators for arcs, vertices, in adjacent vertices, and out adjacent vertices.
 * provide the amount of arcs, vertices, in degrees, and out degrees
 * add, remove, view, or adjust data for both vertices and arcs.
 * add, remove, or view vertices and arcs themselves.
 * add, remove, or adjust annotations for both vertices and arcs
 * reverse the direction of an arc
 * 
 * Author:  Benjamin Chi
 * Class:  ICS 311/Algorithms
 */
public class DirectedGraph {
	
	//Set attributes for DirectedGraph
	private Hashtable <String, Vertex> vertices;
	private Hashtable <String, ArrayList<String>> inAdjacencyList;
	private Hashtable <String, ArrayList<String>> outAdjacencyList;
	private Hashtable <Vertex, Hashtable<Vertex, Arc>> arcs;
	private ArrayList<Arc> arcList;
	
	//Constructor for DirectedGraph
	public DirectedGraph (){
		vertices = new Hashtable<String, Vertex>();
		inAdjacencyList = new Hashtable <String, ArrayList<String>>();
		outAdjacencyList = new Hashtable <String, ArrayList<String>>();
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
		Hashtable<Vertex, Arc> targetTable = null;
		
		//Get the sub-hashtable that is associated with source
		targetTable = arcs.get(source);
		
		if(targetTable != null){
			if(targetTable.containsKey(target)){
				
				//Get the arc that's associated with the target in the sub-hashtable
				targetArc = targetTable.get(target);
			}
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
	 * Returns the number of arcs incoming to a vertex
	 */
	public int inDegree(Vertex v){
		int in = 0;
		
		if(inAdjacencyList.containsKey(v.getKey())){
			in = inAdjacencyList.get(v.getKey()).size();
		}
		
		return in;
	}
	
	/*
	 * Returns the number of arcs outgoing from a vertex
	 */
	public int outDegree(Vertex v){
		int out = 0;
		
		if(outAdjacencyList.containsKey(v.getKey())){
			out = outAdjacencyList.get(v.getKey()).size();
		}
		
		return out;
	}
	
	/*
	 * Returns an iterator object of all the inAdjacentVertices to a vertex
	 */
	public java.util.Iterator <Vertex> inAdjacentVertices(Vertex v){
		ArrayList <Vertex> inAdjacentList = new ArrayList <Vertex>();
		ArrayList <String> inAdjacentKeys = inAdjacencyList.get(v.getKey());
		
		for(int i = 0; i < inAdjacentKeys.size(); i++){
			inAdjacentList.add(vertices.get(inAdjacentKeys.get(i)));
		}
		
		return inAdjacentList.iterator();
	}
	
	/*
	 * Returns an iterator object of all the outAdjacentVertices from a vertex
	 */
	public java.util.Iterator <Vertex> outAdjacentVertices(Vertex v){
		
		ArrayList <Vertex> outAdjacentList = new ArrayList <Vertex>();
		ArrayList <String> outAdjacentKeys;
		
		outAdjacentKeys = outAdjacencyList.get(v.getKey());
				
		if(outAdjacentKeys == null)
			return outAdjacentList.iterator();
		
		else{
			for(int i = 0; i < outAdjacentKeys.size(); i++){
				outAdjacentList.add(vertices.get(outAdjacentKeys.get(i)));
			}
			
			return outAdjacentList.iterator();
		}
	}
	
	/*
	 * The total number of incoming arcs on the graph
	 */
	public int totalInVertices(){
		Set<Entry<String, ArrayList<String>>> adjEntry = inAdjacencyList.entrySet(); 
		Iterator<Entry<String, ArrayList<String>>> adjList = adjEntry.iterator();
		
		ArrayList<String> total = null;
		int totalIn = 0;
		
		while(adjList.hasNext()){
			Entry<String, ArrayList<String>> entry = adjList.next();
			total = entry.getValue();
			totalIn += total.size();
		}
		
		return totalIn;
	}
	
	/*
	 * The total number of outgoing arcs on the graph
	 */
	public int totalOutVertices(){
		Set<Entry<String, ArrayList<String>>> adjEntry = outAdjacencyList.entrySet(); 
		Iterator<Entry<String, ArrayList<String>>> adjList = adjEntry.iterator();
		
		ArrayList<String> total = null;
		int totalOut = 0;
		
		while(adjList.hasNext()){
			Entry<String, ArrayList<String>> entry = adjList.next();
			total = entry.getValue();
			totalOut += total.size();
		}
		
		return totalOut;
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
	 * Mutators
	 */
	
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
		if(outAdjacencyList.containsKey(sourceKey)){
			outAdjacencyList.get(sourceKey).add(targetKey);
		}
		
		/* If the Vertex key does not exist, it creates a new ArrayList,
		 * adds the Vertex to the Arraylist, and adds the ArrayList to the
		 * hashtable
		 */
		else{
			ArrayList <String> vertexArrayList = new ArrayList <String>();
				vertexArrayList.add(targetKey);
			
			outAdjacencyList.put(sourceKey, vertexArrayList);
		}
		
		/*
		 * Checks the hashtable to see if the Vertex key exists
		 */
		if(inAdjacencyList.containsKey(targetKey)){
			inAdjacencyList.get(targetKey).add(sourceKey);
		}
		
		/*
		 * If it does not exist it creates a new ArrayList,
		 * adds the Vertex to the ArrayList, and adds the ArrayList
		 * to the hashtable
		 */
		else{
			ArrayList <String> vertexArrayList = new ArrayList <String>();
			vertexArrayList.add(sourceKey);
			
			inAdjacencyList.put(targetKey, vertexArrayList);
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
	 * Removes the vertex from the vertices list.  Updates the in/out adjacency lists and arc list
	 */
	public String removeVertex(Vertex v){
		String vKey = v.getKey();
		int vOut = 0;
		vOut = outAdjacencyList.get(vKey).size();
		
		//No arcs attached to v
		if(vOut == 0 && vOut == 0){
			vertices.remove(vKey);
		}
		
		//Arcs out of v
		ArrayList <String> outList = outAdjacencyList.get(vKey);
			
		//For each outgoing vertex from v
		for(int i = 0; i < outList.size(); i++){
			
			String targetVKey = outList.get(i);
			
			//If v is not the only vertex incoming to targetV, delete only v
			if(inAdjacencyList.get(targetVKey).size() > 1){
				inAdjacencyList.get(targetVKey).remove(vKey);
				
				//Remove the arc with v as source and targetV as target
				arcs.get(v).remove(vertices.get(targetVKey));
			}
			
			//If v is the only vertex incoming to targetV, delete the key
			else{
				inAdjacencyList.remove(targetVKey);
				
				//Remove the only arc with v as source and targetV as target
				arcs.remove(v);
			}
		}
		
		//Remove v from outAdjacencyList
		outAdjacencyList.remove(vKey);
		
		//Arcs into vertex
		ArrayList <String> inList = inAdjacencyList.get(vKey);
			
		//For each incoming vertex to v
		for(int i = 0; i < inList.size(); i++){
			String targetVKey = inList.get(i);
			
			//If v is not the only vertex outgoing from targetV, delete only v
			if(outAdjacencyList.get(targetVKey).size() > 1){
				outAdjacencyList.get(targetVKey).remove(vKey);
				
				//Remove the arc with targetV as the source and v as the target
				arcs.get(vertices.get(targetVKey)).remove(v);
			}
			
			//If v is the only vertex outgoing from targetV, delete the key
			else{
				outAdjacencyList.remove(targetVKey);
				
				//Remove the only arc with targetV as the source and v as the target
				arcs.remove(vertices.get(targetVKey));
			}
		}
		
		inAdjacencyList.remove(vKey);
		
		//Remove the vertex from the vertices list
		vertices.remove(vKey);
		
		return v.getData();
	}
	
	/*
	 * Removes the arc from the arcList and arcTable.  
	 */
	public Object removeArc(Arc a){
		Vertex source = a.getSource();
		Vertex target = a.getTarget();
		String targetKey = target.getKey();
		String sourceKey = source.getKey();
		int targetIn = inAdjacencyList.get(targetKey).size();
		int sourceOut = outAdjacencyList.get(sourceKey).size();
		
		//Extracts the Hashtable of Arcs that matches a's source
		Hashtable <Vertex, Arc> arcList = arcs.get(source);
		
		//If the arrayList has multiple Arcs from the same source
		if(sourceOut > 1){
			arcList.remove(target);
			outAdjacencyList.get(sourceKey).remove(targetKey);
			
			if(targetIn > 1){
				inAdjacencyList.get(targetKey).remove(sourceKey);
			}
			
			else{
				inAdjacencyList.remove(targetKey);
			}
		}
		
		//Else remove the source and arc from the arc list
		else{
			arcs.remove(sourceKey);
			
			outAdjacencyList.remove(targetKey);
			
			if(targetIn > 1){
				inAdjacencyList.get(targetKey).remove(sourceKey);
			}
			
			else{
				inAdjacencyList.remove(targetKey);
			}
		}
		
		arcList.remove(a);
		
		return (String) a.getWeight();
	}
	
	/*
	 * Switches the source and target of an arc.  Updates the in/out adjacency list
	 */
	public void reverseDirection(Arc a){
		Vertex target = a.getTarget();
		Vertex source = a.getSource();
		String targetKey = target.getKey();
		String sourceKey = source.getKey();
		int targetIn = inAdjacencyList.get(targetKey).size();
		int sourceOut = outAdjacencyList.get(sourceKey).size();
		
		//If the target has multiple incoming arcs, remove only the sourceKey
		if(targetIn > 1)
			inAdjacencyList.get(targetKey).remove(sourceKey);
		
		//Else remove the key targetKey as it will have no incoming arcs after the switch
		else
			inAdjacencyList.remove(targetKey);
		
		//If the target has any outgoing arcs, add the source to the list
		if(outAdjacencyList.containsKey(targetKey))
			outAdjacencyList.get(targetKey).add(sourceKey);
		
		//Else create a new list and add the list to the new outAdjacencyList with target as the key
		else{
			ArrayList <String> vertexArrayList1 = new ArrayList <String>();
			vertexArrayList1.add(sourceKey);
			
			outAdjacencyList.put(targetKey, vertexArrayList1);
		}
		
		//If the source has multiple outgoing arcs, remove only the targetKey
		if(sourceOut > 1)
			outAdjacencyList.get(sourceKey).remove(targetKey);
		
		//Else remove the key sourceKey as it will have no outgoing arcs after the switch
		else
			outAdjacencyList.remove(sourceKey);
		
		//If the source has any outgoing arcs, add the target to the list
		if(inAdjacencyList.containsKey(sourceKey))
			inAdjacencyList.get(sourceKey).add(targetKey);
		
		//Else create a new list and add the list to the new inAdjacencyList with source as the key
		else{
			ArrayList <String> vertexArrayList2 = new ArrayList <String>();
			vertexArrayList2.add(targetKey);
			
			inAdjacencyList.put(sourceKey, vertexArrayList2);
		}
		
		//Switch direction of the arc
		a.setSource(target);
		a.setTarget(source);
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
