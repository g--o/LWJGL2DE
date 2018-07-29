package engine2D;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class glDisplay { // display class

	static boolean update = true;
	static int width=0,height = 0;
	
	public void updateManually(){ // Update manually
		update = false;
	}
	
	/**
	 * Set the display mode to be used 
	 * 
	 * @param Width The width of the display required
	 * @param Height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int Width, int Height, boolean fullscreen) {

		if(fullscreen){
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double w = screenSize.getWidth();
			double h = screenSize.getHeight();	
			height=(int)h;
			width=(int)w;
		}
		width = Width;
		height = Height;
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
		    return;
	    }
	 
	    try {
	        DisplayMode targetDisplayMode = null;
	 
		if (fullscreen) {
		    DisplayMode[] modes = Display.getAvailableDisplayModes();
		    int freq = 0;
	 
		    for (int i=0;i<modes.length;i++) {
		        DisplayMode current = modes[i];
	 
			if ((current.getWidth() == width) && (current.getHeight() == height)) {
			    if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
			        if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
				    targetDisplayMode = current;
				    freq = targetDisplayMode.getFrequency();
	                        }
	                    }
	 
			    // if we've found a match for bpp and frequence against the 
			    // original display mode then it's probably best to go for this one
			    // since it's most likely compatible with the monitor
			    if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }
	 
	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }
	 
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	 
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
	
	// Running the display
	public boolean run(int fps){
		if(fps!=0)
			Display.sync(fps);
		if(update)
			Display.update(); 
		return !Display.isCloseRequested();
	}
	
	/**
	 * Initialize the display
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param title The title of the display required
	 */
		glDisplay(int width, int height, String title,boolean fullScreen){
			 try
		        {
				 	setDisplayMode(width, height,fullScreen); 
		            Display.setTitle(title); 
		            Display.create(); 
		        }catch (LWJGLException e) 
		        {
		            e.printStackTrace();
		        }
		}
		
		public void destroy(){
			Display.destroy(); 
		}
}


