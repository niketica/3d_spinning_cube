package nl.aniketic.asteroids.game;

import nl.aniketic.asteroids.display.DisplayManager;

public class ProgramLoop implements Runnable {

    private static final int FPS = 60;

    private Program game;
    private DisplayManager displayManager;

    private boolean running;

    public ProgramLoop(Program game, DisplayManager displayManager) {
        this.game = game;
        this.displayManager = displayManager;
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS; // 0.016666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        running = true;

        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {

                game.update();
                displayManager.render();

                delta--;
                drawCount++;
            }

            if (timer >= 1_000_000_000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
}
