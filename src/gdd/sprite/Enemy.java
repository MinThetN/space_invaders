package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Enemy extends Sprite {

    private Bomb bomb;
    private int directionX = -1; // Horizontal direction (-1 for left, 1 for right)
    private int directionY = 1; // Vertical direction (1 for down)

    public Enemy(int x, int y) {
        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);

        var ii = new ImageIcon(IMG_ENEMY);

        // Scale the image to use the global scaling factor
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    public void act(int direction) {
        // Move diagonally towards corner
        this.x += directionX; // Move horizontally
        this.y += directionY; // Move vertically down

        // Reverse horizontal direction when hitting screen borders
        if (this.x >= BOARD_WIDTH - BORDER_RIGHT) {
            directionX = -1; // Move left
        } else if (this.x <= BORDER_LEFT) {
            directionX = 1; // Move right
        }

        // Continue moving down (towards bottom corner)
        // You can add logic here to reverse Y direction if needed
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            var bombImg = "src/images/bomb.png";
            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
