package engine2D;

enum mType{
	rigid,liquid,solid,gas;
}

public class Material implements Resource{
	public mType glType; 
	public float ro = 0;
	public float cor = 0;
	public float Mus = 0,Muk = 0;
	public String texName = "";
	
	Material(float Mus,float Muk,float ro,float cor,String texName,mType glType){
		this.ro=ro;
		this.cor=cor;
		this.Mus = Mus;
		this.Muk = Muk;
		this.texName=texName;
		this.glType = glType;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}
}

