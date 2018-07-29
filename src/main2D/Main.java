package main2D;

import static engine2D.Game.glMgr;
import engine2D.Game;
import engine2D.Util;

// Game file - use the "MiniEngine" however you want.
// The game/program itself.

public class Main implements Runnable{ // Game/program class
	
	static Game game = null; // The game object

	// Window size and consts
	static final int width = Util.getScreenWidth();
	static final int  height = Util.getScreenHeight();
	static final int  edge=2000;  
	static int n = 0; // Entity counter
	
	Main(){ // Constructor - Initializations
		game = new Game(this,width,height,"LWJGL 2D MiniEngine",true,true,60); // Init the Game
		// Set the textures path for the Materials
		glMgr.setMatTex("wood", "wood.png");
		glMgr.setMatTex("metal", "metal.png");
		glMgr.setMatTex("none", "sky.png");
		// Add the background (First entity is always the background)
		glMgr.getScene().createTexturedEntity(0-edge, 0-edge+500, width+edge, height+edge, "sky", true,true,"none",glMgr.getInterMethod("AABB")).setAlive(false); 
		// Adding entities
		glMgr.getScene().createAnimatedEntity(250,400,200,200,"Box",true,true,"metal",glMgr.getInterMethod("AABB"),130,130,60,false,false);
    	// Adding walls
    	glMgr.createWall("Box","metal",5);
    	//Changing whatever we want
	}
	
	public static void destroy() // Shutdown
	{
		game.destroy();
	}
	
	public void Scene(){ // A scene
		glMgr.dragEntity(new String[]{"Box"}); // Enable entities dragging
				
		if(glMgr.isKeyDown("RETURN")){
			glMgr.getScene().createTexturedEntity(glMgr.getMouseX(), glMgr.getMouseY(), 100, 100, "box"+n, true,false,"wood",glMgr.getInterMethod("AABB"));
			n++; // update entity counter
		}
		else if (glMgr.isKeyDown("SPACE")) { // Use keyboard
			// Space is the "Scrolling Locker"
			glMgr.updateScroller();
		}		
		else if(glMgr.isKeyDown("ESCAPE"))
		{
			glMgr.setGameState("DONE");
		}
	}
	
    public void run() // Run the program/game ("main loop"/"run loop")
    {
    	Scene(); //Code here
    }

    public static void main (String [] args) // Main - starting point
    {
        new Main(); // Create/Initialize a new game/program
        game.run(); // Run the game
        destroy(); // Shutdown
    }

}  
