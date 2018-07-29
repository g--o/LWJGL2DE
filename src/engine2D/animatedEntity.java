package engine2D;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2i;

import org.newdawn.slick.opengl.Texture;

public class animatedEntity extends texturedEntity { // Animated entity
	
	public int curFrame,curAnim,numOfFrames,numOfAnims;
	public float spaceBetFrames,lastFrameTime;
	public boolean pong, isAnim;
	public Spritesheet pSpritesheet;
	public SpriteInfo currentSprite;
	
	animatedEntity(double x, double y, double width, double height,
			String name, boolean drawManually, boolean isStatic, Material mat,
			interMethod imeth,float rectWidth,float rectHeight,float spaceBetFrames,boolean pong,boolean Animate0) {
		super(x, y, width, height, name, drawManually, isStatic, mat, imeth);
		// TODO Auto-generated constructor stub
		this.curAnim = 0;
		this.curFrame = 0;
		this.isAnim = Animate0;
		this.currentSprite = new SpriteInfo("Default",0,0,rectWidth,rectHeight);
		this.spaceBetFrames = spaceBetFrames;
		lastFrameTime = Util.getTime();
	}
	
	public void startAnimation(){
		isAnim = true;
	}
	
	public void stopAnimation(){
		isAnim = false;
	}
	
	public void setTexture(Texture tex){
		this.pTexture = tex;
		this.numOfAnims = (int) (pTexture.getImageHeight()/currentSprite.h);
		this.numOfFrames = (int) (pTexture.getImageWidth()/currentSprite.w);
	}
	
	public void nextFrame(){
		if(!pong){
			if(curFrame>=numOfFrames)
				curFrame = 0;
			else
				curFrame++;
		}
		else{
		if(curFrame>=numOfFrames)
			curFrame = 0;
		else
			curFrame++;
		}
	}
	
	public void update(int delta){
		if(Util.getTime()-lastFrameTime>spaceBetFrames*delta && isAnim){
			nextFrame();
			lastFrameTime = Util.getTime();
			if(pSpritesheet == null){
				currentSprite.x = curFrame*currentSprite.w;
				currentSprite.y = curAnim*currentSprite.h;
			}
			else
				currentSprite = pSpritesheet.getSprite(curFrame);
		}
	}
	
	public void setSprite(String name){
		currentSprite = pSpritesheet.getSprite(name);
	}
	
	public void setSprite(SpriteInfo sprite){
		currentSprite = sprite;
	}
	
	public void draw(){
		
		float spriteX = currentSprite.x, spriteY = currentSprite.y, spriteX2 = currentSprite.x+currentSprite.w, spriteY2 = currentSprite.y + currentSprite.h,
				iWidth=pTexture.getImageWidth(), iHeight = pTexture.getImageHeight();
		pTexture.bind();
		glPushMatrix();
		glScaled(scale.x,scale.y,scale.z);
		glTranslated((float)x+width/2,(float)y+height/2,0);
		glRotated((float)rotAngle,0,0,1);
		glTranslated((float)-width/2, (float)-height/2, 0);
		glBegin(GL_QUADS);
        glTexCoord2f(spriteX/iWidth, spriteY/iHeight);
        glVertex2i((int)0, (int)0); // Upper-left
        glTexCoord2f(spriteX/iWidth, spriteY2/iHeight);
        glVertex2i(0, (int)height); // Bottom-left
        glTexCoord2f(spriteX2/iWidth, spriteY2/iHeight);
        glVertex2i((int)(width), (int)(height)); // Bottom-right
        glTexCoord2f(spriteX2/iWidth, spriteY/iHeight);
        glVertex2i((int)width, 0); // Upper-right
        glEnd();
        glPopMatrix();
	}
}
