package advent.day19.game;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverDetector extends MouseAdapter{

    public GameIcon hoverIconOriginal;
    //state switching icons
    public Icon mainIcon, hoverIcon;
    public JButton button;
    public final int delayMs = 100;
    public boolean isIn = false;

    public HoverDetector(GameIcon hoverIcon, JButton button){
        super();
        this.hoverIconOriginal = hoverIcon;
        this.button = button;
    }
    @Override
    public void mouseEntered(MouseEvent e){
        if(isIn){
            return;
        }
        isIn = true;
        long st = System.currentTimeMillis();

        Icon freshIcon = button.getIcon();
        if(freshIcon == null){
            return;
        }
        boolean identical = mainIcon != null && mainIcon.equals(freshIcon);
        if(!identical){
            mainIcon = freshIcon;
            int prvsWidth = mainIcon.getIconWidth();
            int prvsHeight = mainIcon.getIconHeight();
            //height is the base for scaling
            hoverIcon = hoverIconOriginal.scale(prvsWidth, prvsHeight);
        }

        long diff = System.currentTimeMillis() - st;
        try{
            Thread.sleep(Math.max(0, delayMs-diff));
            button.setIcon(hoverIcon);
        }catch (InterruptedException ignored){}
    }

    @Override
    public void mouseExited(MouseEvent e){
        isIn = false;
        button.setIcon(mainIcon);
    }
}
