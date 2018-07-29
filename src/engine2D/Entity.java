package engine2D;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector3f;

public interface Entity { // Creating a general entity interface
	public void setRo(float ro);
	public void setAlive(boolean state);
	public void setPressed(boolean state);
	public void setStatic(boolean state);
	public void setDX(double dx);
	public void setDY(double dy);
	public void update(int delta);
	public void draw();
	public void setLocation(double x, double y);
	public void setX(double x);
	public void setY(double y);
	public void setWidth(double width);
	public void setHeight(double height);
	public void Scale(float x, float y, float z);
	public void Rotate(double rotAngle);
	public void setDimentions(double width,double height);
	public void setRect(double x,double y, double width, double height);
	public void setName(String name);
	public void setInertia(float I);
	public void setMass(float M);
	public void setColor(int R,int G,int B);
	public void setMaterial(Material mat);
	public void setInterMethod(interMethod imeth); // set intersection method
	public interMethod getInterMethod(); // get intersection method
	public Vector3f getScale();
	public Material getMaterial();
	public float getMass();
	public float getInertia();
	public float getAveRadius();
	public double getRotAngle();
	public float getRo();
	public double getX();
	public double getY();
	public double getWidth();
	public double getHeight();
	public Rectangle getRect();
	public Rectangle getHitbox();
	public double getDX();
	public double getDY();
	public String getName();
	public void updateRect();
	public void updatePhysics();
	public boolean getPressed();
	public boolean getStatic();
	public boolean isAlive();
	public boolean intersects(Entity other);
	public boolean contains(double x,double y);
	
}

