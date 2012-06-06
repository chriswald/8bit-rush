package pkg;

class GameLoop {	
	public static String RESDIR = "res/";
	public static String IMGDIR = "img/";
	public static String SNDDIR = "snd/";
	public static String LVLDIR = "lvl/";
	
	public static void main(String[] args) {		
		Camera c = new Camera();
		Level  l = new Level(1600, 900);
		Player p = new Player();
		
		c.setLevel(l);
		c.addDrawable(l);
		c.addDrawable(p);
		c.addKeyListener(p);
		
		c.setVisible(true);
		
		while (true) {
			p.update();
			c.repaint();
			
			try {
				Thread.sleep(1000 / 30);
			} catch (InterruptedException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
	}
}
