package gdd;

import gdd.scene.TitleScene;
import gdd.scene.Scene1;
import javax.swing.JFrame;

public class Game extends JFrame {
    private TitleScene titleScene;
    private Scene1 gameScene;
    private boolean inTitle = true;

    public Game() {
        initUI();
    }

    private void initUI() {
        titleScene = new TitleScene();
        titleScene.setParentGame(this); // Set reference to this Game instance
        add(titleScene);
        titleScene.startScene();

        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void switchToGame() {
        if (inTitle) {
            titleScene.stopScene();
            remove(titleScene);
            
            gameScene = new Scene1();
            add(gameScene);
            gameScene.startScene();
            
            inTitle = false;
            revalidate();
            repaint();
            gameScene.requestFocusInWindow();
        }
    }
}