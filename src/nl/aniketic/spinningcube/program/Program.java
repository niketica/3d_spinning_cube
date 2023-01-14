package nl.aniketic.spinningcube.program;

import nl.aniketic.spinningcube.display.DisplayManager;
import nl.aniketic.spinningcube.math.Matrix3f;
import nl.aniketic.spinningcube.math.Vector3f;

import java.awt.Color;
import java.awt.Graphics2D;

public class Program {

    private final int centerX = DisplayManager.SCREEN_WIDTH / 2;
    private final int centerY = DisplayManager.SCREEN_HEIGHT / 2;

    private Vector3f[] points;
    private Vector3f[] projected;

    private Matrix3f projectionMatrix;
    private Matrix3f rotationMatrixX;
    private Matrix3f rotationMatrixY;
    private Matrix3f rotationMatrixZ;

    float angle;

    public Program() {
        DisplayManager displayManager = new DisplayManager(this);
        displayManager.createWindow();

        ProgramLoop programLoop = new ProgramLoop(this, displayManager);
        Thread thread = new Thread(programLoop);
        thread.start();
    }

    public void start() {
        angle = 0.0f;

        points = new Vector3f[]{
                new Vector3f(-0.5f, -0.5f, -0.5f),
                new Vector3f(+0.5f, -0.5f, -0.5f),
                new Vector3f(+0.5f, +0.5f, -0.5f),
                new Vector3f(-0.5f, +0.5f, -0.5f),

                new Vector3f(-0.5f, -0.5f, 0.5f),
                new Vector3f(+0.5f, -0.5f, 0.5f),
                new Vector3f(+0.5f, +0.5f, 0.5f),
                new Vector3f(-0.5f, +0.5f, 0.5f)
        };

        projected = new Vector3f[8];

        projectionMatrix = new Matrix3f();
        rotationMatrixX = new Matrix3f();
        rotationMatrixY = new Matrix3f();
        rotationMatrixZ = new Matrix3f();
    }

    public void update() {
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

            Vector3f rotated = rotationMatrixY.mul(p);
            rotated = rotationMatrixX.mul(rotated);
            rotated = rotationMatrixZ.mul(rotated);

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
