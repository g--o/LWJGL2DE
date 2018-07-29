package engine2D;

import main2D.Main;

// An example of a game architecture

public class Game implements Runnable{
	public static glManager glMgr = null; // The display object
	public int width, height;
	public Main pMain;
	
	public Game(Main pMain, int width, int height, String title, boolean fullScreen, boolean advanced, int fps){
		glMgr = new glManager(width,height,title,fullScreen,advanced,fps); // Init the manager/"MiniEngine"
		this.pMain = pMain;
	}

	public void run() { // Run the game
		do
		{
			pMain.run(); // User code
		} while(glMgr.Run(true) && glMgr.getGameState()!=GameState.DONE); // Running the "MiniEngine"
	}
	
	public void destroy() // Shutdown game
	{
		glMgr.destroy();
	}
	
}

