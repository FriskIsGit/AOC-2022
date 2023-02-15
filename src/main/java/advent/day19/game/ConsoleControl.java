package advent.day19.game;

import advent.day19.Robot;
import advent.day19.State;

import java.util.Scanner;

public class ConsoleControl {
    private final GameFrame gf;
    private boolean wasRun = false;
    private final Scanner scanner = new Scanner(System.in);
    public ConsoleControl(GameFrame gf){
        this.gf = gf;
    }
    public void run(){
        if(wasRun){
            return;
        }
        wasRun = true;
        new Thread(() -> {
            String line = "";
            while(!line.equals("exit")){
                line = scanner.nextLine();
                switch (line){
                    case "pack":
                        System.out.println("Packing frame..");
                        gf.pack();
                        break;
                    case "scale":
                        System.out.println("Scaling icons..");
                        gf.rescaleIcons();
                        break;
                    case "unleashed":
                        GameState.MAX_MINUTES = 100;
                        gf.nextMinuteButton.setEnabled(true);
                        break;
                    case "reset":
                        gf.initGame();
                        gf.nextMinuteButton.setEnabled(true);
                        break;
                    case "next ore robot":
                        System.out.println(gf.getGameState().nextRobot(Robot.ORE));
                        break;
                    case "next clay robot":
                        System.out.println(gf.getGameState().nextRobot(Robot.CLAY));
                        break;
                    case "next obsidian robot":
                        System.out.println(gf.getGameState().nextRobot(Robot.OBSIDIAN));
                        break;
                    case "next geode robot":
                        System.out.println(gf.getGameState().nextRobot(Robot.GEODE));
                        break;
                }
            }
        }).start();
    }

}
