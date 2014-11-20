package project3;

import java.util.HashMap;
/*
 * This class represents an arc object
 * 
 * The methods can:
 * set and get both target and source 
 * 
 * Author:  Benjamin Chi
 * Class:  ICS 311/Algorithms
 */

public class Arc{
	
	private String weight = null;
	private Vertex source = null;
	private Vertex target = null;
	public HashMap <Object, Object> annotation = new HashMap <Object, Object>();
	
	//Empty constructor
	public Arc(){
		
	}
	
	//Overloaded constructor with a provided target and source vertex
	public Arc(Vertex newsource, Vertex newtarget){
		source = newsource;
		target = newtarget;
	}
	
	//Get the vertex corresponding to arc's source
	public Vertex getSource(){
		return source;
	}
	
	//Get the vertex corresponding to arc's target
	public Vertex getTarget(){
		return target;
	}
	
	//Set a new source vertex
	public void setSource(Vertex newsource){
		source = newsource;
	}
	
	//Set a new target vertex
	public void setTarget(Vertex newtarget){
		target = newtarget;
	}
	
	//Get the weight for the arc
	public String getWeight(){
		return weight;
	}
	
	//Set the weight for the arc
	public void setWeight(String data){
		weight = data;
	}
}