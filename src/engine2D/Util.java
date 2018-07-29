package engine2D;

import java.awt.Dimension;
import java.awt.Toolkit;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;
import org.jdom2.input.SAXBuilder;

public final class Util { // Basic utilities
	
	public final static float g=9.81f; // On earth acceleration
	public final static float unit = 0.1f; // 1 meter:10 = 10 centimeters per unit		  | 10cm:unit (unit = 10cm) | 10 pixels = meter
	public final static float Ro = 0.000001f; //ro = 1kg:1*unit = 1/100*100          	  | notice it's unit optimized
	public static int lastBindedTexture; // Last Binded Texture Name
	
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
	public static SAXBuilder Builder = new SAXBuilder(); // For xml files (jdom)
	
    public static long getTime() { // Getter for the time
    	return (Sys.getTime() * 1000) / Sys.getTimerResolution(); 
    	}
    
    public static double dotProduct(Vector2f a,Vector2f b){ // Getting the dot product of two vectors
		return a.x*b.x+a.y*b.y;
	}
    
    public static float getSgn(float x)
    {
    	return x/Math.abs(x);
    }
    
	public static double getAngle(Vector2f a,Vector2f b){ // Getting the angle between two vectors
		return Math.acos(dotProduct(a,b)/(Math.sqrt(a.getX()*a.getX()+a.getY()*a.getY())*Math.sqrt(b.getX()*b.getX()+b.getY()*b.getY())));
	}
	
	public static double square(double x){ // ^2
		return x*x;
	}
	
	public static double distance(double x1,double y1,double x2,double y2){ // Get distance between points
		return (double) Math.sqrt(square(x1-x2)+square(y1-y2));
	}
	
	public static int getScreenWidth(){
		return (int) screenSize.getWidth();
	}
	
	public static int getScreenHeight(){
		return (int) screenSize.getHeight();
	}
	
	public static void getAcceleration(Entity entity,float oldDX, float oldDY,int delta,Vector2f out)
	{
		out.x = (float) ((entity.getDX()-oldDX)/delta);
		out.y = (float) ((entity.getDY()-oldDY)/delta);
	}
	
	public static void normalize(Vector2f normal,double distance){ // Normalize vector
		normal.x = (float) (normal.x/distance);
		normal.y = (float) (normal.y/distance);
	}
	
	public static void normalize(Vector2f v)
	{
		if (v.length() != 0)
			normalize(v,distance(v.x,v.y,0,0));
	}

	public static Vector2f getNormalized(Vector2f vec)
	{
		Vector2f n = vec;
		n.normalise();
		return n;
	}
}
