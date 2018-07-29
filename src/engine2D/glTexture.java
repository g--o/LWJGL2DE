package engine2D;

import org.newdawn.slick.opengl.Texture;

public class glTexture implements Resource{
	private static Texture pTexture;
	
	glTexture(Texture pTexture)
	{
		glTexture.pTexture = pTexture;
	}

	public Texture getTexture()
	{
		return pTexture;
	}
	
	@Override
	public void release() {
		pTexture.release();
		
	}
}
