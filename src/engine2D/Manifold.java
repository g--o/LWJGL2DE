package engine2D;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;

public class Manifold {
	public Entity a,b;
	public interMethod imeth;

	Manifold(Entity a,Entity b){
		this.a=a;
		this.b=b;
		if(a.getInterMethod() == interMethod.Circle && b.getInterMethod() == interMethod.Circle)
			imeth = interMethod.Circle;
		else
			imeth = interMethod.AABB;
	}
	
    public boolean isSideCol(float y1,float y2,float h1,float h2){ // True for sides false for top and bottom
    	if((y2<=y1&&y1<=y2+h2)||(y1<=y2&&y2<=y1+h1))
    		return true;
    	return false;
    }
    
	public boolean isDouble(Manifold Other){
		return ((Other.a.equals(this.a)&&Other.b.equals(this.b))||(Other.b.equals(this.a)&&Other.a.equals(this.b)));
	}
	
	public Vector2f getNormal(){
		if(imeth == interMethod.Circle){ // Getting circle vs circle normal
			double x1 = a.getX()+a.getWidth()/2;
			double x2 = b.getX()+b.getWidth()/2;
			double y1 = a.getY()+a.getHeight()/2;
			double y2 = b.getY()+b.getHeight()/2;
			float absN = (float) Util.distance(x1, y1, x2, y2);
			return new Vector2f((float)(x2-x1)/absN,(float)(y2-y1)/absN);
		}
		else{ // Getting AABB vs AABB normal
			Rectangle test = a.getHitbox().intersection(b.getHitbox());
			if(test.getWidth()<test.getHeight()) // X TEST
				return new Vector2f((a.getHitbox().getCenterX()>b.getHitbox().getCenterX())?-1:1,0);
			else
				return new Vector2f(0,(a.getHitbox().getCenterY()>b.getHitbox().getCenterY())?-1:1);
		}
			//return new Vector2f(0,0);
	}
	
	public void fixPenetration(Vector2f normal,double massA, double massB,Physics psx)
	{
    	// Dealing with penetration using linear projection
    	// GET RADIUSES
    	final double r = a.getAveRadius(); // Radius 1 4 circles
    	final double R = b.getAveRadius(); // Radius 2 4 circles
    	
    	double penetration = 0; // Penetration depth

    	if(imeth == interMethod.Circle) // Circles collision
    		penetration = Math.abs( R+r-Util.distance(a.getX()+a.getWidth()/2,a.getY()+a.getHeight()/2,b.getX()+b.getWidth()/2,b.getY()+b.getHeight()/2));
    	else{
    		Rectangle test = a.getHitbox().intersection(b.getHitbox());
    		penetration = Math.min(test.getWidth(),test.getHeight());
    	}
        final float k_slop = 0.01f; // Penetration allowance (0.01-0.1)
        final float percent = 1.0f; // Penetration percentage to correct (0.2-0.8)  
        float correction = (float) (Math.max( penetration - k_slop, k_slop ) / (1/massA + 1/massB)) * percent; // Correction

        // MAKE A DISPOSITION
        if (!a.getStatic()) {
        	a.setX(a.getX()-1/massA*correction*normal.x); // Set final fix
            a.setY(a.getY()-1/massA*correction*normal.y); // Set final fix
        }
        if (!b.getStatic()) {
        	b.setX(b.getX()+1/massB*correction*normal.x); // Set final fix
            b.setY(b.getY()+1/massB*correction*normal.y); // Set final fix	
        }
        // INTRINSIC (RIGID-BODY STRUCTURE) FORCE 
        psx.applyForce(a,new Vector2f((float)-massA*correction*normal.x,(float)-massA*correction*normal.y));
        psx.applyForce(b,new Vector2f((float)massB*correction*normal.x,(float)massB*correction*normal.y));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((imeth == null) ? 0 : imeth.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manifold other = (Manifold) obj;
		
		// Members comparison
		// imeth
		/*if (imeth != other.imeth)
			return false;*/
		// a & b
		if ((a == other.a && b == other.b) || (a == other.b && b == other.a) ||
				(a.equals(other.a) && b.equals(other.b)) || (a.equals(other.b) && b.equals(other.a)))
			return true;
		
		return false;
	}

	public boolean update(){
		if(!a.intersects(b))
			return false;
		return true;
	}
}


