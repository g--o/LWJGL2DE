package engine2D;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

//Using the slick lib.

public class texturedEntity extends simpleEntity{ // Textured entity that extends the simple one, adding more features.
	protected Texture pTexture;
	
	texturedEntity(double x, double y, double width, double height,String name,boolean drawManually,boolean isStatic,Material mat,interMethod imeth) {
		super(x, y, width, height,name, drawManually,isStatic,mat,imeth);	
	}
	
	public void draw(){
		//Texture drawing
		boolean isBinded = Util.lastBindedTexture == pTexture.getTextureID();
		if(!isBinded){
			pTexture.bind();
			Util.lastBindedTexture = pTexture.getTextureID();
		}
		// PUSHING A MATRIX - REMEBER: READ IT FLIPPED (OGL FILPS IT)
		glPushMatrix();
		glScaled(scale.x,scale.y,scale.z); // FOURTH
		glTranslated((float)x+width/2,(float)y+height/2,0); // THIRD
		glRotated((float)rotAngle,0,0,1); // SECOND
		glTranslated((float)-width/2, (float)-height/2, 0); // FIRST
		glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2i((int)0, (int)0); // Upper-left
        glTexCoord2f(1, 0);
        glVertex2i((int)(width), (int)0); // Upper-right
        glTexCoord2f(1, 1);
        glVertex2i((int)(width), (int)(height)); // Bottom-right
        glTexCoord2f(0, 1);
        glVertex2i((int)0, (int)(height)); // Bottom-left
        glEnd();
        glPopMatrix();
	}
	
	public void setTexture(Texture tex){ // Setter
		pTexture = tex;
	}

}
