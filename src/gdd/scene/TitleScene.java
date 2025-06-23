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
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TitleScene extends JPanel {
    private boolean started = false;
    private ImageIcon titleImage;
    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private Timer timer;
    private Game parentGame;
    private Clip backgroundMusic;

    public TitleScene() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        // Load title image
        titleImage = new ImageIcon("src/images/title.png");

        // Load and prepare background music
        loadBackgroundMusic();

        // Don't start timer automatically
        timer = new Timer(DELAY, new GameCycle());
    }

    private void loadBackgroundMusic() {
        try {
            // Try to load the WAV file first, then MP3 as fallback
            File audioFile = new File("src/audio/title.wav");
            if (!audioFile.exists()) {
                audioFile = new File("src/audio/title.mp3");
            }

            if (audioFile.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioInputStream);
            } else {
                System.out.println("Audio file not found: title.wav or title.mp3");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading background music: " + e.getMessage());
            backgroundMusic = null;
        }
    }

    private void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0); // Reset to beginning
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void setParentGame(Game game) {
        this.parentGame = game;
    }

    public void startScene() {
        if (!started) {
            started = true;
            timer.start();
            playBackgroundMusic(); // Start playing music when scene starts
        }
    }

    public void stopScene() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        stopBackgroundMusic(); // Stop music when scene stops
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
            int scaledWidth = titleImage.getIconWidth() / 2 + 150;
            int scaledHeight = titleImage.getIconHeight() / 2 + 150;
            int x = (BOARD_WIDTH - scaledWidth) / 2;
            int y = (BOARD_HEIGHT - scaledHeight) / 2 - 50;

            g.drawImage(titleImage.getImage(), x, y, scaledWidth, scaledHeight, this);
        }

        // Draw pixel-style "Press SPACE to start" text
        drawPixelText(g, "Press 'SPACE' To Start", BOARD_WIDTH / 2, BOARD_HEIGHT - 120);

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawPixelText(Graphics g, String text, int centerX, int y) {
        // Create pixel-style font effect
        Graphics2D g2d = (Graphics2D) g;

        // Disable anti-aliasing for pixelated effect
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // Use a monospace font for pixel effect
        Font pixelFont = new Font(Font.MONOSPACED, Font.BOLD, 30);
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

        // Draw main text in bright orange (retro style)
        g2d.setColor(Color.ORANGE);
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

    // Clean up resources when the scene is destroyed
    public void cleanup() {
        stopBackgroundMusic();
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
    }
}
