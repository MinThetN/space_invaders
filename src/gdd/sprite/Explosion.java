package gdd.sprite;

import static gdd.Global.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion extends Sprite {

    // Frame counter to control how long explosion is visible
    private int visibleFrames = 10;
    private static final int EXPLOSION_SIZE = 12;

    public Explosion(int x, int y) {
        initExplosion(x, y);
    }

    private void initExplosion(int x, int y) {
        // Create custom explosion instead of using image
        createCustomExplosion();

        // Set position
        this.x = x;
        this.y = y;

        // Make sure explosion starts visible
        setVisible(true);
    }

    private void createCustomExplosion() {
        // Create a custom explosion image with desired color
        BufferedImage explosionImage = new BufferedImage(
                EXPLOSION_SIZE * SCALE_FACTOR,
                EXPLOSION_SIZE * SCALE_FACTOR,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = explosionImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw multiple circles with different colors for explosion effect
        // Outer circle - red/orange
        g2d.setColor(new Color(255, 100, 0, 180)); // Orange with transparency
        g2d.fillOval(0, 0, EXPLOSION_SIZE * SCALE_FACTOR, EXPLOSION_SIZE * SCALE_FACTOR);

        // Middle circle - yellow
        g2d.setColor(new Color(255, 255, 0, 200)); // Yellow
        int middleSize = (EXPLOSION_SIZE * SCALE_FACTOR) * 2 / 3;
        int middleOffset = (EXPLOSION_SIZE * SCALE_FACTOR - middleSize) / 2;
        g2d.fillOval(middleOffset, middleOffset, middleSize, middleSize);

        // Inner circle - white/bright
        g2d.setColor(new Color(255, 255, 255, 220)); // Bright white
        int innerSize = (EXPLOSION_SIZE * SCALE_FACTOR) / 3;
        int innerOffset = (EXPLOSION_SIZE * SCALE_FACTOR - innerSize) / 2;
        g2d.fillOval(innerOffset, innerOffset, innerSize, innerSize);
        g2d.dispose();
        setImage(explosionImage);
    }

    /**
     * Countdown method to control explosion visibility duration
     * Reduces visible frames and sets invisible when countdown reaches zero
     */
    public void visibleCountDown() {
        if (visibleFrames > 0) {
            visibleFrames--;
        } else {
            setVisible(false); // Hide explosion when countdown finishes
        }
    }

    // Getter for visible frames (optional, for debugging)
    public int getVisibleFrames() {
        return visibleFrames;
    }
}