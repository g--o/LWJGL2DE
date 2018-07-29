package engine2D;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.util.vector.Vector2f;

public class Physics {
	
	static Set<Manifold> sManifolds; // Manifold vector
	static Scene pScene; // Entity manager	
	
	Physics(float dt,Scene pScene){ // Constructor - setting stuff and getting required stuff
		Physics.pScene = pScene;
		sManifolds = new HashSet<Manifold>();
	}
	
	public void applyForce(Entity e, Vector2f f) // Apply force to an object
	{
		if(!e.getStatic()){
			e.setDX(e.getDX()+f.x*pScene.dt/e.getMass());
			e.setDY(e.getDY()+f.y*pScene.dt/e.getMass());
		}
	}

	public void addVelocity(Entity e, Vector2f vel)
	{
		e.setDX(e.getDX() + vel.x);
		e.setDY(e.getDY() + vel.y);
	}

    public void calCollision(Manifold man){ // A function that calculates a collision node.
    	// BASIC
    	Entity a = man.a;
    	Entity b = man.b;
    	Vector2f normal = man.getNormal(); // Normal
    	float massA = a.getMass(), massB = b.getMass(); // Masses
    	double fCor = (a.getMaterial().cor + b.getMaterial().cor)/2; // Final coefficient of restitution (average)
    	
    	if(a.getStatic())
    		massA = Float.MAX_VALUE; // Infinite mass!
    	if(b.getStatic())
    		massB = Float.MAX_VALUE; // ...
    	
    	// VECTORS DEFINITIONS:
    	Vector2f ut = new Vector2f(-normal.y,normal.x); 							// Tangent
    	Vector2f v1 = new Vector2f((float)a.getDX(),(float)a.getDY()); 				// Original velocity
    	Vector2f v2 = new Vector2f((float)b.getDX(),(float)b.getDY()); 				// Original velocity
    	
    	double v1n = Util.dotProduct(v1,normal); 									// Velocity in normal direction
    	double v1t = Util.dotProduct(ut,v1); 										// Velocity in tangent direction
    	double v2n = Util.dotProduct(normal,v2); 									// Velocity in normal direction
    	double v2t = Util.dotProduct(ut,v2); 										// Velocity in tangent direction

		// APPLY MOMENTUM CONSERVATION:

    	double V1t = v1t, V2t = v2t;
    	double V1n = (v1n*(massA-fCor*massB)+(1+fCor)*massB*v2n)/(massA+massB); 	// Velocity size using my custom equation
    	double V2n = (v2n*(massB-fCor*massA)+(1+fCor)*massA*v1n)/(massA+massB); 	// Velocity size using my custom equation
    	
    	Vector2f vV1n = new Vector2f((float)V1n*normal.x,(float)V1n*normal.y); 		// Getting the velocity for the normal direction
    	Vector2f vV1t = new Vector2f((float)V1t*ut.x,(float)V1t*ut.y); 				// Getting the velocity for the tangent direction
    	Vector2f vV2n = new Vector2f((float)V2n*normal.x,(float)V2n*normal.y); 		// Getting the velocity for the normal direction
    	Vector2f vV2t = new Vector2f((float)V2t*ut.x,(float)V2t*ut.y); 				// Getting the velocity for the tangent direction

    	Vector2f fV1 = new Vector2f(vV1n.x+vV1t.x,vV1n.y+vV1t.y); 					// Getting final velocity
    	Vector2f fV2 = new Vector2f(vV2n.x+vV2t.x,vV2n.y+vV2t.y); 					// Getting final velocity

    	// OTHER INTERACTIONAL FORCES

    	// FRICTION
    	//Get the static and kinetic friction coefficient
    	final double fMuk = (a.getMaterial().Muk+b.getMaterial().Muk)/2;
    	final double fMus = (a.getMaterial().Mus+b.getMaterial().Mus)/2;
    	// GET NORMAL FORCES
    	final double fNa = Math.abs(a.getMass()*(v1n-v2n));
    	final double fNb = Math.abs(b.getMass()*(v2n-v1n));
    	//GET TANGENTIAL FORCES
    	final double fTa = Math.abs(a.getMass()*(V1t-V2t));
    	final double fTb = Math.abs(b.getMass()*(V2t-V1t));
    	//CALCULATE FRICTION FORCES
    	double f1 = -Math.abs((fTa > fMus*fNb)? fMuk*(v1n-v2n) : V1t);
    	double f2 = -Math.abs((fTb > fMus*fNa)? fMuk*(v2n-v1n) : V2t);
    	// CALCULATE FINAL FORCE
    	Util.normalize(vV1t);
    	Util.normalize(vV2t);

    	if (Double.isNaN(f1))
    		f1 = 0.0f;
    	if (Double.isNaN(f2))
    		f2 = 0.0f;

    	Vector2f F1 = new Vector2f((float)(vV1t.x*f1),(float)(vV1t.y*f1));
    	Vector2f F2 = new Vector2f((float)(vV2t.x*f2),(float)(vV2t.y*f2));

    	//APPLY FORCE
		fV1.x += F1.x;
		fV1.y += F1.y;
		fV2.x += F2.x;
		fV2.y += F2.y;

		// set new velocities (including friction) ; dv = f*dt/m
		if (!a.getStatic())
		{
			a.setDX(fV1.x); // Set the new velocity
			a.setDY(fV1.y); // Set the new velocity
		}

		if (!b.getStatic())
		{
			b.setDX(fV2.x); // Set the new velocity
			b.setDY(fV2.y); // Set the new velocity
		}

		// PENETRATION
    	man.fixPenetration(normal,massA,massB,this);
        
		// WAY #2 (Trigonometry):
    	/*
    	double fi = getAngle(normal,new Vector2f(1,0));
    	double theta1 = Math.atan(a.getDY()/a.getDX());
    	double theta2 = Math.atan(b.getDY()/b.getDX());
    	if(a.getDX() == 0)
    		theta1=Math.PI/2;
    	if(b.getDX() == 0)
    		theta2=Math.PI/2;
    	
	    a.setDX((a.getDX()*Math.cos(theta1-fi)*(massA-fCor*massB)+(1+fCor)*massB*b.getDX()*Math.cos(theta1-fi))/(massA+massB)*Math.cos(fi)+a.getDX()*Math.sin(theta1-fi)*Math.cos(fi+Math.PI/2));
	    a.setDY(((a.getDY()*Math.cos(theta1-fi)*(massA-fCor*massB)+(1+fCor)*massB*b.getDY()*Math.cos(theta1-fi))/(massA+massB)*Math.sin(fi)+a.getDY()*Math.sin(theta1-fi)*Math.sin(fi+Math.PI/2)));
    		
	    b.setDX((b.getDX()*Math.cos(theta2-fi)*(massB-fCor*massA)+(1+fCor)*massA*a.getDX()*Math.cos(theta2-fi))/(massA+massB)*Math.cos(fi)+b.getDX()*Math.sin(theta2-fi)*Math.cos(fi+Math.PI/2));
	    b.setDY(((b.getDY()*Math.cos(theta2-fi)*(massB-fCor*massA)+(1+fCor)*massA*a.getDY()*Math.cos(theta2-fi))/(massA+massB)*Math.sin(fi)+b.getDY()*Math.sin(theta2-fi)*Math.sin(fi+Math.PI/2)));
    	
    	System.out.println(a.getDY());
    	*/
    }
    
	public void udpateManifolds(){ // Updating manifolds and making sure no doubles exists
		Iterator<Manifold> iter = sManifolds.iterator();
		while (iter.hasNext()) {
			Manifold man = iter.next();
			if(!man.update())
				iter.remove();	   // Remove finished collision
			else
				calCollision(man); // Calculate the collision	
		}
	}
	
	public void detectCollisions(){ // Handles the collisions
		// First, find all collisions and make manifolds
		for(int i = 0; i < pScene.getNumOf(); i++){
			Entity a =  pScene.getObject(i);
			if (!a.isAlive())
				continue;
			for(int j = i + 1; j < pScene.getNumOf(); j++){
				Entity b =  pScene.getObject(j);
				if(a.intersects(b)&&(!(a.getStatic()&&b.getStatic()))){
					sManifolds.add(new Manifold(a,b)); // For regular objects
				}
			}
		}
	}
	
    public void setGravity(){ // Setting gravity
    	for(int i = 1; i<pScene.getNumOf(); i++){
    		Entity pTmp = (Entity) pScene.getObject(i);
    		if(!pTmp.getStatic()&&!pTmp.getPressed())
    			applyForce(pTmp,new Vector2f(0.0f,pTmp.getMass()*Util.g*Util.unit)); //pTmp.setDY(((Entity) pScene.getObject(i)).getDY()+);
    	}
    }
    
	public void update(){ // Update the physics.
    	setGravity();
		detectCollisions(); // Detecting collisions
		udpateManifolds(); // Updating manifolds
		System.out.println("Number of manifolds:"+sManifolds.size()+"\t Number of Entities: "+pScene.getNumOf()); // Printing info to the console
	}
	
	public void destroy()
	{
		sManifolds.clear();
	}
	
}
