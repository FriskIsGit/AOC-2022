package advent.day19.game;

import advent.day19.Blueprint;
import advent.day19.State;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameFrame extends JFrame{

    private JPanel gamePanel;
    private JPanel topPanel;
    private JTextArea bluePrintTextArea;
    private JLabel materialsLabel;
    private JLabel amount1;
    private JLabel minuteLabel;
    private JLabel amount2;
    private JLabel robotsLabel;
    private JLabel oreIcon;
    private JLabel oreAmount;
    private JLabel clayIcon;
    protected JButton nextMinuteButton;
    private JLabel minuteCount;
    private JLabel obsidianIcon;
    private JLabel geodesIcon;
    private JLabel oreRobotAmount;
    private JLabel clayAmount;
    private JLabel obsidianAmount;
    private JLabel geodeAmount;
    private JLabel clayRobotAmount;
    private JLabel obsidianRobotAmount;
    private JLabel geodeRobotAmount;
    private JButton buyOreBotButton;
    private JButton buyClayBotButton;
    private JButton buyObsidianBotButton;
    private JButton buyGeodeCrackerButton;
    private JComboBox blueprintBox;
    private JTextArea stateArea;

    private GameState gameState;
    public boolean loadIcons = true, limitRatio = false, printSize = true;
    private static final double frameRatio = 1.275;
    public HashMap<GameIcon, JLabel> labelIcons;
    public HashMap<GameIcon, JButton> buttonIcons;
    static{
        //anti-aliasing
        System.setProperty("awt.useSystemAAFontSettings","on");
    }

    public GameFrame(String title) {
        setTitle(title);
        System.out.println("Loading frame..");
        setupFrame();

        colorGridlines();
        customConfigureComponents();
        //maintain ratio
        spawnFrameSizeMonitor();

        initGame();
        gamePanel.setFocusable(false);
        new ConsoleControl(this).run();
        super.addKeyListener(new KeyControl(this));
        setVisible(true);
        //must be visible to request focus successfully
        super.requestFocusInWindow();

        if(loadIcons){
            loadIcons();
            rescaleIcons();
        }
        addActions();
    }
    public void rescaleIcons(){
        for(Map.Entry<GameIcon, JLabel> pair : labelIcons.entrySet()){
            GameIcon icon = pair.getKey();
            JLabel label = pair.getValue();
            label.setIcon(icon.scale());
        }
        for(Map.Entry<GameIcon, JButton> pair : buttonIcons.entrySet()){
            GameIcon icon = pair.getKey();
            JButton button = pair.getValue();
            button.setIcon(icon.scale());
        }
    }

    public GameState getGameState(){
        return gameState;
    }
    public boolean hasExceededRatio(){
        return this.getWidth()/(double)this.getHeight() > frameRatio;
    }

    private void setupFrame(){
        add(gamePanel);
        getContentPane().setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        setLocation(400, 20);
        setMinimumSize(new Dimension(950, 720));
    }

    private void loadIcons(){
        labelIcons = new HashMap<>();
        labelIcons.put(new GameIcon("/icons/ORE.jpg", 0.35f, 0.15f, gamePanel), oreIcon);
        labelIcons.put(new GameIcon("/icons/CLAY.jpg", 0.3f, 0.15f, gamePanel), clayIcon);
        labelIcons.put(new GameIcon("/icons/OBSIDIAN.jpg", 0.3f, 0.15f, gamePanel), obsidianIcon);
        labelIcons.put(new GameIcon("/icons/GEODE.jpg", 0.3f, 0.15f, gamePanel), geodesIcon);

        buttonIcons = new HashMap<>();
        GameIcon oreRobot = new GameIcon("/icons/ORE_ROBOT.jpg", 0.3f, 0.15f, gamePanel);
        GameIcon clayRobot = new GameIcon("/icons/CLAY_ROBOT.jpg", 0.3f, 0.15f, gamePanel);
        GameIcon obsidianRobot = new GameIcon("/icons/OBSIDIAN_ROBOT.jpg", 0.3f, 0.15f, gamePanel);
        GameIcon geodeCracker = new GameIcon("/icons/GEODE_CRACKER.jpg", 0.3f, 0.15f, gamePanel);
        buttonIcons.put(oreRobot, buyOreBotButton);
        buttonIcons.put(clayRobot, buyClayBotButton);
        buttonIcons.put(obsidianRobot, buyObsidianBotButton);
        buttonIcons.put(geodeCracker, buyGeodeCrackerButton);

        GameIcon buyIcon = new GameIcon("/icons/BUY.jpg", 0.25f, 0.15f, this);
        buyOreBotButton.addMouseListener(new HoverDetector(buyIcon, buyOreBotButton));
        buyClayBotButton.addMouseListener(new HoverDetector(buyIcon, buyClayBotButton));
        buyObsidianBotButton.addMouseListener(new HoverDetector(buyIcon, buyObsidianBotButton));
        buyGeodeCrackerButton.addMouseListener(new HoverDetector(buyIcon, buyGeodeCrackerButton));
        rescaleIcons();
    }

    public void initGame(){
        System.out.println("Initializing game state..");
        //List<String> lines = AdventOfCode.readDummy(19);
        //String bp = "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.\n";
        //String bp = "Blueprint 7: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 20 obsidian.\n";
        String bp = "Blueprint 13: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 13 clay. Each geode robot costs 2 ore and 20 obsidian.\n";
        gameState = new GameState(Blueprint.parseBlueprint(bp));
        bluePrintTextArea.setText(Blueprint.toPrettyString(bp));
        updateValues();
    }

    private void addActions(){
        buyOreBotButton.addActionListener(press -> {
            if(!gameState.makeOreRobot()){
                showPurchaseFailure();
                return;
            }
            updateResources();
            stateArea.setForeground(Color.GREEN);
            stateArea.setText("Ordered an ore bot");
        });
        buyClayBotButton.addActionListener(press -> {
            if(!gameState.makeClayRobot()){
                showPurchaseFailure();
                return;
            }
            updateResources();
            stateArea.setForeground(Color.GREEN);
            stateArea.setText("Ordered a clay bot");
        });
        buyObsidianBotButton.addActionListener(press -> {
            if(!gameState.makeObsidianRobot()){
                showPurchaseFailure();
                return;
            }
            updateResources();
            stateArea.setForeground(Color.GREEN);
            stateArea.setText("Ordered an obsidian bot");
        });
        buyGeodeCrackerButton.addActionListener(press -> {
            if(!gameState.makeGeodeRobot()){
                showPurchaseFailure();
                return;
            }
            updateResources();
            stateArea.setForeground(Color.GREEN);
            stateArea.setText("Ordered a geode cracker");
        });

        nextMinuteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(!gameState.nextMinute()){
                    nextMinuteButton.setEnabled(false);
                    JOptionPane.showMessageDialog(
                            null,
                            "The game is over you've collected: " + gameState.geodes + " geodes");
                    return;
                }
                updateValues();
                stateArea.setText("");
            }


        });
    }

    private void updateValues(){
        updateResources();

        oreRobotAmount.setText(String.valueOf(gameState.oreRobots));
        clayRobotAmount.setText(String.valueOf(gameState.clayRobots));
        obsidianRobotAmount.setText(String.valueOf(gameState.obsidianRobots));
        geodeRobotAmount.setText(String.valueOf(gameState.geodeRobots));

        minuteCount.setText(String.valueOf(gameState.minute));
    }
    private void updateResources(){
        oreAmount.setText(String.valueOf(gameState.ore));
        clayAmount.setText(String.valueOf(gameState.clay));
        obsidianAmount.setText(String.valueOf(gameState.obsidian));
        geodeAmount.setText(String.valueOf(gameState.geodes));
    }

    private void spawnFrameSizeMonitor(){
        new Thread(() -> {
            while(true){
                if(printSize){
                    int fWidth = getWidth(), fHeight = getHeight();
                    System.out.println("frame: " + fWidth + "x" + fHeight + " ratio: " + fWidth/(double)fHeight);
                }
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
            }

        }).start();
    }

    private void showPurchaseFailure(){
        if(gameState.canBuy){
            stateArea.setForeground(Color.red);
            stateArea.setText("Not enough\n resources");
            return;
        }
        stateArea.setText("You cannot\n buy now");
    }

    private void colorGridlines(){
        Border whiteBorder = BorderFactory.createLineBorder(Color.lightGray);
        materialsLabel.setBorder(whiteBorder);
        amount1.setBorder(whiteBorder);
        minuteLabel.setBorder(whiteBorder);
        amount2.setBorder(whiteBorder);
        robotsLabel.setBorder(whiteBorder);

        minuteCount.setBorder(whiteBorder);

        oreIcon.setBorder(whiteBorder);
        clayIcon.setBorder(whiteBorder);
        obsidianIcon.setBorder(whiteBorder);
        geodesIcon.setBorder(whiteBorder);

        oreAmount.setBorder(whiteBorder);
        clayAmount.setBorder(whiteBorder);
        obsidianAmount.setBorder(whiteBorder);
        geodeAmount.setBorder(whiteBorder);

        oreRobotAmount.setBorder(whiteBorder);
        clayRobotAmount.setBorder(whiteBorder);
        obsidianRobotAmount.setBorder(whiteBorder);
        geodeRobotAmount.setBorder(whiteBorder);


        //bluePrintTextArea.setBorder(border);
    }

    private void customConfigureComponents(){
        UIManager.LookAndFeelInfo[] feels = UIManager.getInstalledLookAndFeels();
        System.out.println("feels: " + feels.length);
        for(UIManager.LookAndFeelInfo look : feels){
            System.out.println(look.getClassName());
        }
        try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException e){
        }
    }
}
