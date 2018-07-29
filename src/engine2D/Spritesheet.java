package engine2D;

import java.io.File;
import java.io.IOException;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

public class Spritesheet extends glVector<SpriteInfo> implements Resource{ // Spritesheet class
	
	public boolean loadSpritesheet(String xmlPath){ // Init the sprite sheet
		Document xmlFile = null;
	    	try {
				xmlFile =  Util.Builder.build(new File(xmlPath));
			} catch (JDOMException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		Element root = xmlFile.getRootElement();
		for(Object spriteObject : root.getChild("definitions").getChild("dir").getChildren()){ // For every element, get the info
			Element spriteElement = (Element) spriteObject;
			String name = spriteElement.getAttributeValue("name");
			try {
				float x = spriteElement.getAttribute("x").getIntValue();
				float y = spriteElement.getAttribute("y").getIntValue();
				float w = spriteElement.getAttribute("w").getIntValue();
				float h = spriteElement.getAttribute("h").getIntValue();
				addObject(new SpriteInfo(name,x,y,w,h)); // Add the sprite
			} catch (DataConversionException e) {
				e.printStackTrace();
			}			
		}
		
			return true;
	}
	
	public SpriteInfo getSprite(String name){ // Get a sprite by name
		for(int i=0; i<glVec.size(); i++){
			SpriteInfo tmpSpr = (SpriteInfo) getObject(i);
			if(tmpSpr.Name == name){
				return tmpSpr;
			}
		}
		return null;
	}
	
	public SpriteInfo getSprite(int id){ // Get a sprite by id
		return getObject(id);
	}
}
