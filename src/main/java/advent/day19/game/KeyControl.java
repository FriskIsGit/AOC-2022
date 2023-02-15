package advent.day19.game;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyControl extends KeyAdapter{
    private final GameFrame gameFrame;
    public KeyControl(GameFrame gameFrame){
        this.gameFrame = gameFrame;
    }

    @Override
    public void keyPressed(KeyEvent key){
        char pressed = key.getKeyChar();
        switch (pressed){
            case 'b':
                String path = JOptionPane.showInputDialog(null, "Import blueprints");
                System.out.println("Path: " + path);
                break;
            case 'i':
                gameFrame.printSize = !gameFrame.printSize;
                break;
        }
        super.keyPressed(key);
    }
}
