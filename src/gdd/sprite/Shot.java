package gdd.sprite;

import static gdd.Global.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Shot extends Sprite {

    private static final int H_SPACE = 6;
    private static final int V_SPACE = 1;
    private static final int BULLET_WIDTH = 2;
    private static final int BULLET_HEIGHT = 5;

    public Shot() {
    }

    public Shot(int x, int y) {
        initShot(x, y);
    }

    private void initShot(int x, int y) {
        // Create a custom colored bullet
        createCustomBullet();

        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }

    private void createCustomBullet() {
        // Create a custom bullet image with desired color and style
        BufferedImage bulletImage = new BufferedImage(
                BULLET_WIDTH * SCALE_FACTOR,
                BULLET_HEIGHT * SCALE_FACTOR,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bulletImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.CYAN); // Bright cyan bullet

        // Draw bullet shape
        g2d.fillRect(0, 0, BULLET_WIDTH * SCALE_FACTOR, BULLET_HEIGHT * SCALE_FACTOR);
        g2d.dispose();
        setImage(bulletImage);
    }
}
