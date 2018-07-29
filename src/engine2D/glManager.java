package engine2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;


public class glManager { // the "MiniEngine" - the manager

	public float translate_x = 0,translate_y=0; // Translations for scrolling
	public glVector<Scene> glScenes; // The entity manager
	public resourceManager resMgr; // Resource manager
	private glDisplay oglDisplay; // The display
	private GameState gameState; // The game state
	private static long lastFrame; // Last frame (used on timers and stuff)
	private int fps,curSceneID; // The fps,the current scene ID
	private Scene curScene; // The current scene instance
	
	glManager(int width, int height, String title,boolean fullScreen,boolean textures,int fps){ // Constructor of the "MiniEngine" - the manager. Make sure to init everything.
		this.fps = fps;
		oglDisplay=new glDisplay(width,height,title,fullScreen); // Creates a new display
		glMatrixMode(GL_PROJECTION); // Going on projection mode
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, width, height, 0, 1, -1); // Initializing the coordinate system
		glMatrixMode(GL_MODELVIEW); // Going on model view
		if(textures){ // If textures are used :
			glEnable(GL_TEXTURE_2D); // Enable textures
			glEnable(GL_BLEND); // Enable blend (alpha)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // Settings of blend
		}
		resMgr = new resourceManager(); // Creating the resource manager
		glScenes = new glVector<Scene>(); // Creating the entity manager
		
		gameState = GameState.INIT; // Setting game state to initialization
		// Setting some basic built-in materials
		resMgr.addMaterial("testBall",new Material(0.8f,0.8f,800*Util.Ro,0.5f,"ball.png",mType.rigid));
		resMgr.addMaterial("wood",new Material(0.35f,0.35f,750*Util.Ro, 0.2f, "",mType.rigid));
		resMgr.addMaterial("water",new Material(0,0,997.0479f*Util.Ro,0,"",mType.liquid));
		resMgr.addMaterial("metal",new Material(0.5f,0.5f,7874*Util.Ro,0.45f,"",mType.rigid));
		resMgr.addMaterial("clay",new Material(0,0,1600*Util.Ro,0,"",mType.rigid));
		resMgr.addMaterial("none",new Material(0,0,0,0,"",mType.rigid));
		lastFrame = Util.getTime(); // Setting the last frame
		glScenes.addObject(new Scene(textures,resMgr, fps));
		curSceneID = 0;
		curScene = glScenes.getObject(curSceneID);
	}
	
	public void translate(float x,float y, float z){ // Translating - moving
		glLoadIdentity(); // Loading identity matrix - to make it change only the object that is about to render
		glTranslatef(x,y,z); // Translate the vertices
	}
	
	public GameState getGameState(){ // Getter for the game state
		return gameState;
	}
	
	public void setGameState(String s)
	{
		gameState = GameState.valueOf(s);
	}
    
	public Scene getScene(int id) { // Getter
		return glScenes.getObject(id);
	}
	
	public Scene getScene() { // Getter
		return curScene;
	}
	
	private void updateCurScene(){ // Updater
		curScene = glScenes.getObject(curSceneID);
	}
	
	public void nextScene() { // Setter
		curSceneID++;
		updateCurScene();
	}
	
	public void prevScene() { // Setter
		curSceneID--;
		updateCurScene();
	}
	
	public void setScene(int id) { // Setter
		curSceneID=id;
		updateCurScene();
	}
	
    public static int getDelta() { // Getter for the delta
    	long currentTime = Util.getTime();
    	int delta = (int) (currentTime - lastFrame);
    	lastFrame = Util.getTime();
    	return delta; 
    }
    
    public float getMouseX(){ // Getter (fixed)
    	return Mouse.getX()-translate_x;
    }
    
    public boolean isMBDown(int b)
    {
    	return Mouse.isButtonDown(b);
    }
    
    public float getMouseY(){ // Getter (fixed)
    	return glDisplay.height-Mouse.getY()+translate_y;
    }
    
    public boolean isKeyDown(String key)
    {
    	return Keyboard.isKeyDown(Keyboard.getKeyIndex(key));
    }
    
    public interMethod getInterMethod(String methodName)
    {
		return interMethod.valueOf(methodName);
    }
    
    public void setMatTex(String matName, String texPath)
    {
    	resMgr.getMaterial(matName).texName = texPath;
    }
    
    public void dragEntity(String[] entitiesNames){ // Dragging an entity
    	
    	float x = getMouseX(),y = getMouseY(); // Will hold the fixed mouse x y
    	
    	for(int i = 0; i<entitiesNames.length; i++){ //For each entity that is dragable
    		Entity pTmp = curScene.getObject(entitiesNames[i]); 
        	if(pTmp.contains(x,y)){ // If mouse hovering
        		if(Mouse.isButtonDown(0)){
	        		// Make sure is in pressed mode and stop its' movement
	        		pTmp.setPressed(true); 
	        		pTmp.setDX(0);
	        		pTmp.setDY(0);
        		}
    		}
    		if(Mouse.isButtonDown(1)){ // If clicked
    			pTmp.setPressed(false);
    			double X = pTmp.getX()+pTmp.getWidth()/2;
    			double Y = pTmp.getY()+pTmp.getHeight()/2;
    			pTmp.setDX(0.3*(x-X)/fps);
    			pTmp.setDY(0.3*(y-Y)/fps);
    		}
    		if(!Mouse.isButtonDown(0)) // If not clicked than update its' state
    			pTmp.setPressed(false);
    		if(pTmp.getPressed()) // If it is being dragged than set the location accordingly
    			pTmp.setLocation(x-pTmp.getWidth()/2, y-pTmp.getHeight()/2);
    	}
    }
    
    public void updateEntity(String name){ // Update entity 
    	curScene.UpdateEntity(name,getDelta());
    }
    
    public void startRender(){ // Start render (this will draw quads). Problems with textured quads.
        glClear(GL_COLOR_BUFFER_BIT);
        glBegin(GL_QUADS);
    }
    
    public void endRender(){ // End render 
    	glEnd();
    }
    
    public void updateScroller(){ // Updater
		translate_x += Mouse.getDX();
		translate_y += Mouse.getDY();
    }
    
	public boolean Run(boolean auto){ // Run the manager or "MiniEngine"
		if(auto){ // If auto mode is chosen do the default stuff
			glClear(GL_COLOR_BUFFER_BIT); // Clear the screen
			curScene.glPhysics.update(); // Update physics
			curScene.Update(getDelta()); // Update the entities
			translate(translate_x, -translate_y, 0); // Translate (in this position - scroll)
			curScene.Render(); // Render
		}
		return oglDisplay.run(fps); // Update the display and handle the rest.
	}
	
	public void createWall(String name,String matName,int amount){ // Create a wall (Static objects in a row)
		Entity pEntity = curScene.getObject(name);
		for(int i=0; i<amount; i++){
			curScene.createTexturedEntity(pEntity.getX()+pEntity.getWidth()*(i+1), pEntity.getY(), pEntity.getWidth(), pEntity.getHeight(), name+String.valueOf(i), false, pEntity.getStatic(), matName,pEntity.getInterMethod());
		}
	}
	
	public void destroy(){ // Shutdown everything
		Keyboard.destroy();
		Mouse.destroy();
		glScenes.release();
		resMgr.destroy();
		oglDisplay.destroy();
		gameState = GameState.DONE;
	}
}
