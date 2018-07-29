package engine2D;

import org.lwjgl.util.Point;

public class AABB2D {
	public Point a,b,c,d;
	public Point origin;
	public double angle;
	
	AABB2D(Entity e,Point origin,double angle){
		a = new Point((int)e.getX(),(int)e.getY());
		b = new Point((int)(e.getX()+e.getWidth()),(int)e.getY());
		c = new Point((int)(e.getX()+e.getWidth()),(int)(e.getY()+e.getHeight()));
		d = new Point((int)e.getX(),(int)(e.getY()+e.getHeight()));
		this.origin = origin;
		this.angle = angle;
		
	}
}


/*
*(x,y)---------------*(x+w,y)
|                    |
|                    |
|                    |
|                    |
|                    |
|                    |
*(x,y+h)-------------*(x+w,y+h)
*/