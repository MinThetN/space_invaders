package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Explosion extends Sprite {
    
    // Frame counter to control how long explosion is visible
    private int visibleFrames = 10;
    
    public Explosion(int x, int y) {
        initExplosion(x, y);
    }
    
    private void initExplosion(int x, int y) {
        // Load explosion image
        var ii = new ImageIcon(IMG_EXPLOSION);
        
        // Scale the image to use the global scaling factor
        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
        
        // Set position
        this.x = x;
        this.y = y;
        
        // Make sure explosion starts visible
        setVisible(true);
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