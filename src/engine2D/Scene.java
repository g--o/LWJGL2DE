package engine2D;


public class Scene extends glVector<Entity>{ // Entity manager - a vector that is well handled for entities
	
	protected resourceManager resMgr;
	protected Physics glPhysics; // The physics manager (Assisted by the entity manager)
	public float dt;
	
	Scene(boolean textures,resourceManager resMgr,int fps){
		if(textures&&resMgr!=null)
			this.resMgr=resMgr;
		dt = 1.0f/fps;
		glPhysics = new Physics(dt,this); // Creating the physics manager
	}
	
	public void createEntity(double x, double y, double width, double height, String name,boolean drawManually,boolean isStatic,String mat,interMethod imeth){ //Creates an entity
		simpleEntity pEntity = new simpleEntity(x, y, width, height, name, drawManually,isStatic,resMgr.getMaterial(mat),imeth);
		addObject(pEntity);
	}
	
	public void createEntity(double x, double y, double width, double height, String name,boolean drawManually,boolean isStatic,String mat,interMethod imeth,int r,int g, int b){ //Creates an entity
		simpleEntity pEntity = new simpleEntity(x, y, width, height, name, drawManually,isStatic,resMgr.getMaterial(mat),imeth,r,g,b);
		addObject(pEntity);
	}
	
	//Create an animated entity
	public animatedEntity createAnimatedEntity(double x, double y, double width, double height, String name,boolean drawManually,boolean isStatic,String mat,interMethod imeth,float rectWidth,float rectHeight,float spaceBetFrames,boolean pong,boolean Animate0){
		animatedEntity pEntity = new animatedEntity(x,y,width,height,name,drawManually,isStatic,resMgr.getMaterial(mat),imeth,rectWidth,rectHeight,spaceBetFrames,pong,Animate0);
		addObject(pEntity);
		try{
			Material pMat = resMgr.getMaterial(mat);
			if(!pMat.texName.isEmpty())
				setTexture(pEntity,pMat.texName);
				pEntity.pSpritesheet = resMgr.getSpritesheet(pMat.texName);
				pEntity.currentSprite = pEntity.pSpritesheet.getSprite(0);
				pEntity.numOfFrames = pEntity.pSpritesheet.getNumOf()-1;
		}
		catch(Exception exc){
			System.out.println("No such material: "+mat);
		}
		return pEntity;
	}
	
	public texturedEntity createTexturedEntity(double x, double y, double width, double height, String name,boolean drawManually,boolean isStatic,String mat,interMethod imeth){ //Creates a textured entity
		texturedEntity pEntity = new texturedEntity(x,y,width,height,name,drawManually,isStatic,resMgr.getMaterial(mat),imeth);
		addObject(pEntity);
		try{
		if(!resMgr.getMaterial(mat).texName.isEmpty())
			setTexture(pEntity,resMgr.getMaterial(mat).texName);
		}
		catch(Exception exc){
			System.out.println("No such material: "+mat);
		}
		return pEntity;
	}
	
	public void setTexture(texturedEntity pEntity,String path){ // Sets the texture
		pEntity.setTexture(resMgr.loadTexture(path)); 
	}
	
	public void Update(int delta){ // Update all entities
		for(int i=0;i<glVec.size();i++){
			getObject(i).update(delta);
		}
	}
	
	public void Render(){ // Render all entities
		for(int i=0;i<glVec.size();i++){
			getObject(i).draw();
		}
	}
	
    public boolean intersects(String ObjectA,String ObjectB){ // If intersects (for entities)
    	return ((Entity) getObject(ObjectA)).intersects((Entity) getObject(ObjectB));
    }
    
    public boolean intersects(int ObjectA,int ObjectB){ // If intersects (for entities)
    	return ((Entity) getObject(ObjectA)).intersects((Entity) getObject(ObjectB));
    }
    
	public void UpdateEntity(String name,int delta){ // Update a specific entity
		getObject(name).update(delta);
	}
	
	public void DrawEntity(String name){ // Draw a specific entity
		getObject(name).draw();
	}
    
	public texturedEntity getTexturedEntity(String name){ // Getter
		int id = getObjectId(name);
		if(id>=0)
			return (texturedEntity) getObject(id);
		return null;
	}
	
	public animatedEntity getAnimatedEntity(String name){ // Getter
		return (animatedEntity) getTexturedEntity(name);
	}
	
	public void destroy(){ // Shutdown
		glPhysics.destroy();
	}
}
