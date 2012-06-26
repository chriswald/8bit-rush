class GameLoop {

    public static void main(String[] args) {
        G.camera = new Camera();
        G.camera.setVisible(true);

        boolean levelset = false;
        boolean startset = false;
        boolean addedlistener = false;

        while (true) {
            long starttime = System.currentTimeMillis();
            switch (G.GAMESTATE) {
            case STARTUP:
                if (!startset) {
                    G.ss = new StartScreen();
                    G.camera.addKeyListener(G.ss);
                    G.camera.setBufferSize(G.WINDOWW, G.WINDOWH);
                    startset = true;
                }
                G.ss.update();
                G.camera.update(G.ss.getArtifacts());
                G.camera.repaint();
                break;
            case METAMAP:
                break;
            case SELECT:
                break;
            case PLAY:
                if (!levelset) {
                    G.l = new Level("lvl1.lvl");
                    G.camera.setLevel(G.l);
                    if (!addedlistener) {
                        G.camera.addMouseListener(G.l);
                        addedlistener = true;
                    }
                    levelset = true;
                }
                G.l.update();
                G.l.checkCollide();

                G.camera.setPostition(G.l.player);
                G.camera.update(G.l.getArtifacts());
                G.camera.repaint();
                break;
            case PAUSE:
                break;
            case DEATH:
                startset = levelset = false;
                G.l.player.die();
                G.GAMESTATE = G.State.STARTUP;
                break;
            case CREDITS:
                break;
            case END:
                break;
            }
            long endtime = System.currentTimeMillis();

            try {
                // To make sure updates are consistent subtract the time it took
                // for the code to run from 1/30th of a second.
                long sleeptime = (1000 / 30) - (endtime - starttime);
                if (sleeptime > 0)
                    Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
