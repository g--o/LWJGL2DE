package engine2D;

public class SpriteInfo implements Resource{ // A neat structure for sprite info
	
	public String Name;
	public float x,y,w,h;
	
	public SpriteInfo(String Name,float x, float y, float w ,float h){
		this.Name = Name;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}
}
