package pkg;

import java.awt.image.BufferedImage;

public class Block extends Collider{
	//Sides to collide with
	public boolean top, right, bottom, left;
	public BufferedImage img;
	public int x, y;
	
	public Block(BufferedImage img, int posx, int posy, String collide) {
		this.img = img;
		this.x = posx;
		this.y = posy;
		this.posx = this.x*50;
		this.posy = this.y*50;
		this.width = 50;
		this.height = 50;
		
		if (collide.charAt(0) == '1')	top = true;
		else							top = false;
		
		if (collide.charAt(1) == '1')	right = true;
		else							right = false;
		
		if (collide.charAt(2) == '1')	bottom = true;
		else							bottom = false;
		
		if (collide.charAt(3) == '1')	left = true;
		else							left = true;
	}

	@Override
	protected void onCollide(CollidedSide side, Collider c) {
		// TODO Auto-generated method stub
		
	}
}
