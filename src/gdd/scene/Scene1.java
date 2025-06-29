package gdd.scene;

import static gdd.Global.*;
import gdd.sprite.Enemy;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import gdd.sprite.Explosion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
// ADD: Audio imports
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Scene1 extends JPanel {
    private List<Enemy> enemies;
    private Player player;
    private List<Shot> shots;
    private List<Explosion> explosions;

    private int direction = -1;
    private int deaths = 0;
    private int currentWave = 0;
    private int totalEnemiesInCurrentWave = 0;
    private boolean waitingForNextWave = false;
    private Timer waveTimer;

    private boolean inGame = true;
    private boolean started = false;
    private String message = "Game Over";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();

    private Timer timer;

    // ADD: Audio clips for sound effects
    private Clip shootSound;
    private Clip explosionSound;
    private Clip backgroundMusic;

    public Scene1() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        // ADD: Load audio effects
        loadAudioEffects();

        // Don't start timer automatically
        timer = new Timer(DELAY, new GameCycle());
    }

    public void startScene() {
        if (!started) {
            started = true;
            gameInit();
            timer.start();
            // ADD: Start background music
            playBackgroundMusic();
        }
    }

    public void stopScene() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (waveTimer != null && waveTimer.isRunning()) {
            waveTimer.stop();
        }
        // ADD: Stop background music and clean up audio resources
        stopBackgroundMusic();
        if (shootSound != null) {
            shootSound.close();
        }
        if (explosionSound != null) {
            explosionSound.close();
        }
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
        started = false;
    }

    private void gameInit() {
        enemies = new ArrayList<>();
        player = new Player();
        shots = new ArrayList<>();
        explosions = new ArrayList<>();

        currentWave = 0;
        deaths = 0;
        waitingForNextWave = false;

        spawnWave();
    }

    private void spawnWave() {
        if (currentWave >= WAVE_ENEMY_COUNTS.length) {
            // All waves completed
            inGame = false;
            message = "All waves completed! You won!";
            return;
        }

        enemies.clear();
        int enemyCount = WAVE_ENEMY_COUNTS[currentWave];
        totalEnemiesInCurrentWave = enemyCount;
        deaths = 0;

        // Calculate grid dimensions based on enemy count
        int cols = Math.min(enemyCount, 6); // Max 6 columns
        int rows = (int) Math.ceil((double) enemyCount / cols);

        int enemiesSpawned = 0;
        for (int i = 0; i < rows && enemiesSpawned < enemyCount; i++) {
            for (int j = 0; j < cols && enemiesSpawned < enemyCount; j++) {
                var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
                        ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
                enemies.add(enemy);
                enemiesSpawned++;
            }
        }

        currentWave++;
        waitingForNextWave = false;
    }

    private void checkWaveCompletion() {
        if (deaths >= totalEnemiesInCurrentWave && !waitingForNextWave) {
            waitingForNextWave = true;

            // Start timer for next wave
            waveTimer = new Timer(WAVE_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    spawnWave();
                    waveTimer.stop();
                }
            });
            waveTimer.setRepeats(false);
            waveTimer.start();
        }
    }

    private void drawAliens(Graphics g) {
        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }
            if (enemy.isDying()) {
                enemy.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {
        // Changes: Render all shots in the list instead of single shot
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    private void drawBombing(Graphics g) {

        for (Enemy e : enemies) {

            Enemy.Bomb b = e.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {

            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
            drawExplosions(g); // ADD: Draw explosions

            g.drawString("Min Thet Naung ( 6530142 )", 10, 10);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    private void update() {
        // Check wave completion instead of fixed number
        checkWaveCompletion();

        // player
        player.act();

        // shots - handle multiple shots with proper cleanup
        List<Shot> toRemove = new ArrayList<>();

        for (Shot shot : shots) {
            if (shot.isVisible()) {
                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Enemy enemy : enemies) {
                    // Collision detection: shot and enemy
                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();

                    if (enemy.isVisible() && shot.isVisible()
                            && shotX >= (enemyX)
                            && shotX <= (enemyX + ALIEN_WIDTH)
                            && shotY >= (enemyY)
                            && shotY <= (enemyY + ALIEN_HEIGHT)) {

                        // CHANGE: Create explosion at enemy position before enemy dies
                        explosions.add(new Explosion(enemyX, enemyY));

                        // ADD: Play explosion sound effect
                        playSound(explosionSound);

                        var ii = new ImageIcon(IMG_EXPLOSION);
                        enemy.setImage(ii.getImage());
                        enemy.setDying(true);
                        deaths++;
                        shot.die();
                        toRemove.add(shot);
                    }
                }

                // Move shot upward
                int y = shot.getY();
                y -= 15; // increase bullet speed

                // Check if shot went off screen
                if (y < 0) {
                    shot.die();
                    // CHANGE: Add shot to removal list
                    toRemove.add(shot);
                } else {
                    shot.setY(y);
                }
            } else {
                // CHANGE: Add dead shots to removal list
                toRemove.add(shot);
            }
        }

        // CHANGE: Remove all dead shots from the active shots list
        // This prevents memory leaks and keeps the list clean
        shots.removeAll(toRemove);

        // enemies
        for (Enemy enemy : enemies) {

            int x = enemy.getX();

            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {

                direction = -1;

                for (Enemy e2 : enemies) {
                    e2.setY(e2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {

                direction = 1;

                for (Enemy e : enemies) {
                    e.setY(e.getY() + GO_DOWN);
                }
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {

                int y = enemy.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                enemy.act(direction);
            }
        }

        // bombs
        for (Enemy enemy : enemies) {

            int chance = randomizer.nextInt(15);
            Enemy.Bomb bomb = enemy.getBomb();

            if (chance == CHANCE && enemy.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(enemy.getX());
                bomb.setY(enemy.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()
                    && bombX >= (playerX)
                    && bombX <= (playerX + PLAYER_WIDTH)
                    && bombY >= (playerY)
                    && bombY <= (playerY + PLAYER_HEIGHT)) {

                var ii = new ImageIcon(IMG_EXPLOSION);
                player.setImage(ii.getImage());
                player.setDying(true);
                bomb.setDestroyed(true);
            }

            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 1);
                if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {
        update();
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
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            // Allow up to 4 bullets on screen
            if (key == KeyEvent.VK_SPACE && inGame && shots.size() < 4) {
                shots.add(new Shot(x, y));
                // ADD: Play shooting sound effect
                playSound(shootSound);
            }
        }
    }

    private void drawExplosions(Graphics g) {
        // Create list to track explosions that need to be removed
        List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {
            if (explosion.isVisible()) {
                // Draw the explosion
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);

                // Countdown the explosion's visible frames
                explosion.visibleCountDown();

                // If explosion is no longer visible, mark for removal
                if (!explosion.isVisible()) {
                    toRemove.add(explosion);
                }
            } else {
                // Add invisible explosions to removal list
                toRemove.add(explosion);
            }
        }

        // Remove all expired explosions from the list
        explosions.removeAll(toRemove);
    }

    private void loadAudioEffects() {
        // Load shooting sound
        loadSound("shootSound", "src/audio/funny_2.wav"); // Using existing file as placeholder

        // Load explosion sound
        loadSound("explosionSound", "src/audio/explosion.wav"); // Using existing file as placeholder

        // Load background music
        loadSound("backgroundMusic", "src/audio/funny.wav");
    }

    private void loadSound(String soundType, String filePath) {
        try {
            File audioFile = new File(filePath);
            if (audioFile.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                switch (soundType) {
                    case "shootSound":
                        shootSound = clip;
                        break;
                    case "explosionSound":
                        explosionSound = clip;
                        break;
                    case "backgroundMusic":
                        backgroundMusic = clip;
                        break;
                }
            } else {
                System.out.println("Audio file not found: " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading sound " + soundType + ": " + e.getMessage());
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            // Stop if already playing
            if (clip.isRunning()) {
                clip.stop();
            }
            // Reset to beginning and play
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
}
