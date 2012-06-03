package pkg;

class GameLoop {
	private static final String RESOURCEDIRid = "gameloop.resourcedir";
	private static final String IMAGEDIRid = "gameloop.imagedir";
	private static final String SOUNDDIRid = "gameloop.sounddir";
	private static final String LEVELDIRid = "gameloop.leveldir";
	
	public static int resourcedir;
	public static int imagedir;
	public static int sounddir;
	public static int leveldir;
	
	public static Database db;
	
	public static void main(String[] args) {
		db = new Database();
		
		resourcedir = db.getint(RESOURCEDIRid, "res/");
		imagedir = db.getint(IMAGEDIRid, "img/");
		sounddir = db.getint(SOUNDDIRid, "snd/");
		leveldir = db.getint(LEVELDIRid, "lvl/");
	}
}
