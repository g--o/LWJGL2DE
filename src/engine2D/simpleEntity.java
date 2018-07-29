package engine2D;

import static org.lwjgl.opengl.GL11.*;

public class simpleEntity extends AbstractEntity { // A simple entity that extends the abstract definition of it

	public boolean drawManually = false;
	public int r=255,g=255,b=255;
	simpleEntity(double x, double y, double width, double height, String name,boolean drawmanually,boolean isStatic,Material mat,interMethod imeth) {
		super(x, y, width, height, name,isStatic,mat,imeth);
		// TODO Auto-generated constructor stub
		this.drawManually = drawmanually;
		this.isStatic = isStatic;
	}
	
	simpleEntity(double x, double y, double width, double height, String name,boolean drawmanually,boolean isStatic,Material mat,interMethod imeth,int r,int g,int b) {
		super(x, y, width, height, name,isStatic,mat,imeth);
		// TODO Auto-generated constructor stub
		this.drawManually = drawmanually;
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	// Setters and getters
	
	public void setColor(int R,int G,int B){
		r=R;
		g=G;
		b=B;
	}
	
	public void setInterMethod(interMethod imeth){
		this.imeth = imeth;
	}
	
	public void update(int delta){ // Update
		x+=delta*DX;
		y+=delta*DY;
		updateRect();
	}
	
	public void draw(){ // Draw
		if(!drawManually){
	        glColor3f(r,g,b);
	        glRectf((float)x,(float)y,(float)(x+width),(float)(y+height));
		}		
	}
	
	
}
