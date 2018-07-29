package engine2D;

import java.util.Vector;

@SuppressWarnings("hiding")
public class glVector<Object> { // Handled vector container
	protected Vector<Object> glVec = new Vector<Object>(); // Create the vector
	
	public int getNumOf(){ // Getter
		return glVec.size();
	}
	
	public void addObject(Object pObject){ // Add an object
		glVec.add(pObject);
	}
	
	public void addObject(Object pObject,int pos){ // Add an object at a position
		glVec.add(pos,pObject);
	}
	
	public void removeObject(int pos){ // Remove an object
		glVec.remove(pos);
	}
	
	public void removeObject(String name){ // Remove an object
		int id = getObjectId(name);
		if(id>=0)
			removeObject(id);
	}
	
	public Object getObject(int id){ //Get an object (by id)
		if(id>=0 && id<glVec.size()) 
			return glVec.elementAt(id);
		return null;
	}
	
	public Object getObject(String name){ // //Get an object (by name)
		int id = getObjectId(name);
		if(id>=0)
			return getObject(id);
		return null;
	}
    
	public int getObjectId(String name){ // Get an object id by its name
		boolean found = false;
		int i;
		for(i=0; i<glVec.size(); i++){
			if(((Entity) getObject(i)).getName() == name){
				found = true;
				break;
			}
		}
		if(found)
			return i;
		else
			return -1;
	}
	
	public void release(){ // Shutdown
		glVec.clear();
	}

}
