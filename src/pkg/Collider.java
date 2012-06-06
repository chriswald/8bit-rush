package pkg;

import java.awt.Point;

public abstract class Collider {
	public double posx;
	public double posy;
	public int width;
	public int height;
	public double velx;
	public double vely;
	
	protected abstract void onCollide(CollidedSide side, Collider c);
	
	public synchronized void checkCollide(Collider c) {
		boolean collided = ((posx + width) > c.posx &&
							posx < (c.posx + c.width) &&
							(posy + height) > c.posy &&
							posy < (c.posy - c.height));
		
		if (collided) {
			Point p0 = new Point((int) c.posx, (int) c.posy);
			Point p1 = new Point((int) posx, (int) posy);
			
			if (velx >= 0)
				p1.x += width;
			else
				p0.x += c.width;
			if (vely >= 0)
				p1.y += height;
			else
				p0.y += c.height;
			
			int w = Math.abs(p0.x - p1.x);
			int h = Math.abs(p0.y - p1.y);
			
			if (w > h) {
				if (vely > 0) {
					c.onCollide(CollidedSide.TOP, this);
					this.onCollide(CollidedSide.BOTTOM, c);
				} else {
					c.onCollide(CollidedSide.BOTTOM, this);
					this.onCollide(CollidedSide.TOP, c);
				}
			} else {
				if (velx > 0) {
					c.onCollide(CollidedSide.LEFT, this);
					this.onCollide(CollidedSide.RIGHT, c);
				} else {
					c.onCollide(CollidedSide.RIGHT, this);
					this.onCollide(CollidedSide.LEFT, c);
				}
			}
		}
	}
}
