package engine2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public final class resourceManager { // Texture manager that will handle the resources using slick lib
	HashMap<String, Resource> map = new HashMap<String, Resource>(); // Our map
	
	public void addTexture(glTexture tex,String path){ // Add a texture
		if(tex!=null&&path!="")
			map.put(path, tex);
	}
	
	public void addMaterial(String name,Material mat){ // Add a material
		if(name!=""&&mat!=null)
			if(getMaterial(name)==null)
				map.put(name, mat);
	}
	
	public Material getMaterial(String name){  // Getter
		if(map.containsKey(name))
			return (Material) map.get(name);
		
		return null;
	}
	
	public Spritesheet getSpritesheet(String xmlPath){ // Getter
		if(map.containsKey(xmlPath))
			return (Spritesheet) map.get(xmlPath);
		return null;
	}
	
	public Texture loadTexture(String name){ // Load a texture
			String frmt = name.substring(name.lastIndexOf(".")+1);
		try {
			Texture texture=getTexture(name);
			if(texture == null){
				 InputStream In = new FileInputStream(new File(name));
				 texture = TextureLoader.getTexture(frmt,In);
				 In.close();
				 String xmlPath = name.replace("."+frmt,"")+".xml";
				 if(getSpritesheet(xmlPath) == null){
					 Spritesheet spriteSheet = new Spritesheet();
					 if(spriteSheet.loadSpritesheet(xmlPath))
						 map.put(name, spriteSheet);
					 else
						 spriteSheet.release();
				 }
			}
			addTexture(new glTexture(texture),name);
			return texture;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public Texture getTexture(String path){ // Getter
		if(map.containsKey(path))
		{
			glTexture tex = (glTexture) map.get(path);
			return tex.getTexture();
		}
		
		return null;
	}

	public void destroy(){ // Shutdown
		//iterating over values only
		for (Resource res : map.values()) {
				res.release();
			}
		map.clear();
	}
	
	
}

