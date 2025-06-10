package gdd;

import gdd.scene.Scene1;
import javax.swing.JFrame;

public class Game extends JFrame  {

    public Game() {
        initUI();
    }

    private void initUI() {
        add(new Scene1());

        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
}