package gdd.scene;

import static gdd.Global.*;
import gdd.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TitleScene extends JPanel {
    private boolean started = false;
    private ImageIcon titleImage;
    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private Timer timer;
    private Game parentGame;

    public TitleScene() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        // Load title image
        titleImage = new ImageIcon("src/images/title.png");

        // Don't start timer automatically
        timer = new Timer(DELAY, new GameCycle());
    }

    public void setParentGame(Game game) {
        this.parentGame = game;
    }

    public void startScene() {
        if (!started) {
            started = true;
            timer.start();
        }
    }

    public void stopScene() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        started = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        // Draw smaller title image centered
        if (titleImage != null) {
            // Scale the image to be smaller (50% of original size)
            int scaledWidth = titleImage.getIconWidth() / 2;
            int scaledHeight = titleImage.getIconHeight() / 2;
            int x = (BOARD_WIDTH - scaledWidth) / 2;
            int y = (BOARD_HEIGHT - scaledHeight) / 2 - 80;

            g.drawImage(titleImage.getImage(), x, y, scaledWidth, scaledHeight, this);
        }

        // Draw pixel-style "Press SPACE to start" text
        drawPixelText(g, "PRESS 'SPACE' TO START", BOARD_WIDTH / 2, BOARD_HEIGHT - 120);

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawPixelText(Graphics g, String text, int centerX, int y) {
        // Create pixel-style font effect
        Graphics2D g2d = (Graphics2D) g;

        // Disable anti-aliasing for pixelated effect
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // Use a monospace font for pixel effect
        Font pixelFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
        g2d.setFont(pixelFont);

        // Draw text with pixel-style border effect
        var fontMetrics = g2d.getFontMetrics(pixelFont);
        int textWidth = fontMetrics.stringWidth(text);
        int textX = centerX - textWidth / 2;

        // Draw black border (pixel effect)
        g2d.setColor(Color.BLACK);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, textX + dx, y + dy);
                }
            }
        }

        // Draw main text in bright green (retro style)
        g2d.setColor(Color.GREEN);
        g2d.drawString(text, textX, y);

        // Add blinking effect
        long time = System.currentTimeMillis();
        if ((time / 500) % 2 == 0) { // Blink every 500ms
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, textX, y);
        }
    }

    private void doGameCycle() {
        repaint();
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                // Transition to Scene1
                stopScene();
                if (parentGame != null) {
                    parentGame.switchToGame();
                }
            }
        }
    }
}
