package engine2D;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

enum interMethod{
	AABB,Circle;
}

//Creating an abstract entity class that implements entity and be the general entity class
public abstract class AbstractEntity implements Entity {
	public interMethod imeth;
	protected String name = "";
	protected double x=0,y=0,width=0,height=0,DX=0,DY=0,rotAngle=0;
	Vector3f scale = new Vector3f(1,1,1);
	protected boolean alive = true;
	protected Rectangle hitbox = new Rectangle();
	protected boolean pressed = false, solid = true,isStatic = false;
	protected Material mat;
	protected float mass, inertia;
	
	AbstractEntity(double x,double y,double width,double height,String name,boolean isStatic,Material mat,interMethod imeth){ // Constructor
		this.imeth = imeth;
		this.x=x;
		this.name = name;
		this.y=y;
		this.width=width;
		this.height=height;
		this.isStatic = isStatic;
		this.mat = mat;
		mass = 0;
		inertia = 0;
		updatePhysics();
	}
	
	//Getters and setters
	
	public void Scale(float x, float y, float z){
		scale.x*=x;
		scale.y*=y;
		scale.z*=z;
	}
	
	
	public void setRo(float ro){
		this.mat.ro = ro;
	}
	
	public void setPressed(boolean state){
		pressed = state;
	}
	
	public void setStatic(boolean state){
		this.isStatic = state;
	}
	
	public void setAlive(boolean state){
		alive=state;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setLocation(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public void setMass(float M)
	{
		this.mass = M;
	}
	
	public void setInertia(float I)
	{
		this.inertia = I;
	}
	
	public void Rotate(double rotAngle){
		this.rotAngle=rotAngle;
	}
	
	public void setMaterial(Material mat){
		this.mat=mat;
		updatePhysics();
	}
	
	public void setDX(double dx){
		DX=dx;
	}
	
	public void setDY(double dy){
		DY=dy;
	}
	
	public void setX(double x) {
		this.x=x;
	}

	public void setY(double y) {
		this.y=y;
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}

	public void setWidth(double width) {
		this.width=width;
		updatePhysics();
	}

	public void setHeight(double height) {
		this.height = height;
		updatePhysics();
	}
	
	public void setDimentions(double width,double height){
		this.width = width;
		this.height = height;
		updatePhysics();
	}
	
	public void setRect(double x,double y, double width, double height){
		setLocation(x,y);
		setDimentions(width,height);
	}

	public void setName(String name){
		this.name=name;
	}	
	
	public double getRotAngle(){
		return this.rotAngle;
	}
	
	public Vector3f getScale(){
		return scale;
	}
	
	public boolean getPressed(){
		return pressed;
	}
	
	public float getRo(){
		return this.mat.ro;
	}
	
	public float getMass()
	{
		return this.mass;
	}
	
	public float getAveRadius()
	{
		return (float) ((this.width+this.height)/4);
	}
	
	public float getInertia()
	{
		return this.inertia;
	}
	
	public Material getMaterial(){
		return this.mat;
	}
	
	public boolean getStatic(){
		return this.isStatic;
	}

	public interMethod getInterMethod() {
		return this.imeth;
	}
	
	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}
	
	public double getDX(){
		return DX;
	}
	
	public double getDY(){
		return DY;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public void updateRect(){
		hitbox.setBounds((int)x,(int)y,(int)width,(int)height);
		if(getInterMethod() != interMethod.Circle)
			hitbox.translate((int)Math.cos(rotAngle), (int)Math.sin(rotAngle));
	}
	
	public Rectangle getRect(){
		updateRect();
		return hitbox;
	}

	public String getName(){
		return name;
	}
	
	// Making operations more comfortable to use:
	
	public boolean intersects(Entity Other) {
		
		if(getInterMethod()==interMethod.Circle &&Other.getInterMethod() == interMethod.Circle){
			float r1 = (float) ((getWidth()+getHeight())/4);
			float r2 = (float) ((Other.getWidth()+Other.getHeight())/4);
			float Rs = r1+r2;
			Rs*=Rs;
			double x1 = getX()+getWidth()/2;
			double x2 = Other.getX()+Other.getWidth()/2;
			double y1 = getY()+getHeight()/2;
			double y2 = Other.getY()+Other.getHeight()/2;
			Vector2f n = new Vector2f();
			n.x = (float) (x2-x1);
			n.y = (float) (y2-y1);
			float Ns = (float) (n.x*n.x+n.y*n.y);
			if(Ns<=Rs)
				return true;
			return false;
		}
		else{
			updateRect();
			return hitbox.intersects(Other.getX(),Other.getY(),Other.getWidth(),Other.getHeight());
		}
	}
	
	public void updatePhysics() // Updates mass and inertia
	{    
	    this.mass = (float)(getRo()*height*Util.unit*width*Util.unit*(height+width)*Util.unit/2);
	    this.inertia = (float) (2/5*mass*Util.square(getAveRadius()));
	}
	
	public boolean contains(double x,double y){
		updateRect();
		return hitbox.contains(x,y);
	}

}
