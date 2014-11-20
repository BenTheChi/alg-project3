package project3;
import java.util.HashMap;
/*
 * This class represents a vertex object
 * 
 * The methods can:
 * set and get both data and key/ID
 * 
 * Author:  Benjamin Chi
 * Class:  ICS 311/Algorithms
 */
public class Vertex{
		
		private String key = null;
		private String data = null;
		public HashMap <Object, Object> annotation = new HashMap <Object, Object>();
		
		//Empty constructor
		public Vertex (){
		
		}
		
		//Overloaded constructor with a key provided
		public Vertex (String newkey){
			key = newkey;
		}
		
		//Set the key/ID for this vertex
		public void setKey (String newkey){
			key = newkey;
		}
		
		//Set the data for this vertex
		public void setData (String newdata){
			data = newdata;
		}
		
		//Get the key/ID for this vertex
		public String getKey(){
			return key;
		}
		
		//Get the data for this vertex
		public String getData(){
			return data;
		}
		
	}