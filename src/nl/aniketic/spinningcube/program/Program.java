package nl.aniketic.spinningcube.program;

import nl.aniketic.spinningcube.display.DisplayManager;
import nl.aniketic.spinningcube.input.GameKey;
import nl.aniketic.spinningcube.input.KeyInput;
import nl.aniketic.spinningcube.math.Matrix3f;
import nl.aniketic.spinningcube.math.Vector3f;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Program {

    private final int centerX = DisplayManager.SCREEN_WIDTH / 2;
    private final int centerY = DisplayManager.SCREEN_HEIGHT / 2;

    private Vector3f[] points;
    private Vector3f[] projected;

    private Matrix3f projectionMatrix;
    private Matrix3f rotationMatrixX;
    private Matrix3f rotationMatrixY;
    private Matrix3f rotationMatrixZ;

    private float angle;
    private float speed;
    private float cubeLength;

    private Vector3f cubePosition;
    private Vector3f movement;

    public Program() {
        DisplayManager displayManager = new DisplayManager(this);
        displayManager.createWindow();

        displayManager.addKeyListener(new KeyInput());

        ProgramLoop programLoop = new ProgramLoop(this, displayManager);
        Thread thread = new Thread(programLoop);
        thread.start();
    }

    public void start() {
        angle = 0.0f;
        speed = 0.1f;
        cubeLength = 1.0f;
        float halfCube = cubeLength / 2;

        points = new Vector3f[]{
                new Vector3f(-halfCube, -halfCube, -halfCube),
                new Vector3f(halfCube, -halfCube, -halfCube),
                new Vector3f(halfCube, halfCube, -halfCube),
                new Vector3f(-halfCube, halfCube, -halfCube),

                new Vector3f(-halfCube, -halfCube, halfCube),
                new Vector3f(halfCube, -halfCube, halfCube),
                new Vector3f(halfCube, halfCube, halfCube),
                new Vector3f(-halfCube, halfCube, halfCube)
        };
        cubePosition = new Vector3f(1.0f, 0.0f, 0.0f);

        projected = new Vector3f[8];

        projectionMatrix = new Matrix3f();
        rotationMatrixX = new Matrix3f();
        rotationMatrixY = new Matrix3f();
        rotationMatrixZ = new Matrix3f();
    }

    public void update() {
        movement = new Vector3f(0, 0, 0);
        if (GameKey.getKey(KeyEvent.VK_W).isPressed()) {
            movement.y -= 1;
        }
        if (GameKey.getKey(KeyEvent.VK_S).isPressed()) {
            movement.y += 1;
        }
        if (GameKey.getKey(KeyEvent.VK_A).isPressed()) {
            movement.x -= 1;
        }
        if (GameKey.getKey(KeyEvent.VK_D).isPressed()) {
            movement.x += 1;
        }

        if (movement.length() > 0) {
            movement = movement.normalize();
            movement = movement.mul(speed);
            cubePosition = cubePosition.add(movement);
        }

        angle += 0.02f;

        rotationMatrixX.m11 = (float) Math.cos(angle);
        rotationMatrixX.m12 = (float) -Math.sin(angle);
        rotationMatrixX.m21 = (float) Math.sin(angle);
        rotationMatrixX.m22 = (float) Math.cos(angle);

        rotationMatrixY.m00 = (float) Math.cos(angle);
        rotationMatrixY.m02 = (float) -Math.sin(angle);
        rotationMatrixY.m20 = (float) Math.sin(angle);
        rotationMatrixY.m22 = (float) Math.cos(angle);

        rotationMatrixZ.m00 = (float) Math.cos(angle);
        rotationMatrixZ.m01 = (float) -Math.sin(angle);
        rotationMatrixZ.m10 = (float) Math.sin(angle);
        rotationMatrixZ.m11 = (float) Math.cos(angle);
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.WHITE);

        for (int i = 0; i < points.length; i++) {
            Vector3f p = points[i];

            float length = p.length();
            Vector3f normalized = p.normalize();

            Vector3f rotated = rotationMatrixY.mul(normalized);
            rotated = rotationMatrixX.mul(rotated);
            rotated = rotationMatrixZ.mul(rotated);

            rotated = rotated.mul(length);
            rotated = rotated.add(cubePosition);

            float distance = 2;
            float z = 1 / (distance - rotated.z);
            projectionMatrix.m00 = z;
            projectionMatrix.m11 = z;
            Vector3f projected2d = projectionMatrix.mul(rotated);

            projected2d = projected2d.mul(400);

            int x = (int) projected2d.x + centerX;
            int y = (int) projected2d.y + centerY;

            drawCenteredCircle(g2, x, y, 5);

            projected[i] = projected2d;
        }

        for (int i = 0; i < 4; i++) {
            g2.setColor(Color.RED);
            connect(g2, i, (i + 1) % 4);

            g2.setColor(Color.BLUE);
            connect(g2, i + 4, ((i + 1) % 4) + 4);

            g2.setColor(Color.GREEN);
            connect(g2, i, i + 4);
        }
    }

    public void connect(Graphics2D g2, int i, int j) {
        Vector3f a = projected[i];
        Vector3f b = projected[j];

        int x1 = (int) a.x + centerX;
        int y1 = (int) a.y + centerY;
        int x2 = (int) b.x + centerX;
        int y2 = (int) b.y + centerY;

        g2.drawLine(x1, y1, x2, y2);
    }

    public void drawCenteredCircle(Graphics2D g2, float x, float y, float r) {
        x = x - r;
        y = y - r;
        g2.fillOval((int) x, (int) y, (int) r * 2, (int) r * 2);
    }
}
