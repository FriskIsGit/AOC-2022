package advent.day12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GUI{
    public static void initializeContext(int ROWS, int COLS){
        GUI.ROWS = ROWS;
        GUI.COLS = COLS;
    }
    public static final boolean BLOCK_UPDATE = false;
    public volatile static int update = 0;
    public static int currentRow = 0;
    public static int currentCol = 0;
    public static int ROWS;
    public static int COLS;
    public static List<JPanel> panels = new ArrayList<>();
    public static HashMap<Integer, PathPoint> closed;
    public static JFrame frame;
    public static final boolean MONITOR_FRAME_SIZE = false;
    public static void main(String[] args){
        if(frame != null){
            //removes existing frame
            frame.dispose();
        }
        frame = new JFrame();
        addKeyboardListener();
        if(MONITOR_FRAME_SIZE){
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
                @Override
                public void run(){
                    System.out.println(frame.getWidth() + " x " + frame.getHeight());
                }
            };
            timer.scheduleAtFixedRate(task, 1000,500);
        }

        frame.setLocation(0, 0);
        frame.setTitle("Day12");
        frame.setSize(1900, 1060);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        frame.setLayout(layout);
        frame.getContentPane().setBackground(Color.black);
        for (int r = 0; r < ROWS; r++){
            for (int c = 0; c < COLS; c++){
                JPanel panel = new JPanel();
                panel.setBackground(Color.BLACK);
                char character = Day12.map[r][c];
                if(character == 'E' || character == 'S'){
                    panel.setBackground(Color.GREEN);
                }
                JLabel label = new JLabel(String.valueOf(character));
                label.setForeground(Color.white);

                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.gridx = c;
                constraints.gridy = r;

                label.setHorizontalAlignment(JLabel.CENTER);
                panel.add(label);
                //naming convention
                panel.setName(r + "+" + c);
                panel.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent click){
                        String name = panel.getName().replace('+', 'x');
                        System.out.println(name);
                        frame.setTitle(name);
                        doColorInteraction(panel);
                    }
                });
                panels.add(panel);
                frame.add(panel, constraints);
            }
        }
        frame.setVisible(true);
        System.out.println("SETUP");
    }
    public static void paintPanelColor(int row, int col, Color color){
        String target = row + "+" + col;
        for(JPanel panel : panels){
            if(panel.getName().equals(target)){
                panel.getComponent(0).setForeground(Color.black);
                panel.setBackground(color);
            }
        }
    }
    private static void doColorInteraction(JPanel panel){
        Color originalColor = panel.getBackground();
        if(originalColor == Color.yellow){
            return;
        }
        panel.setBackground(Color.yellow);
        Timer timer = new Timer();
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                panel.setBackground(originalColor);
            }
        };
        timer.schedule(task, 100);
    }
    public static void addKeyboardListener(){
        KeyAdapter keyAdapter = new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent typed){
            }

            @Override
            public void keyPressed(KeyEvent press){
                char key = press.getKeyChar();
                switch (key){
                    //backtrack point
                    case 'i':
                        String res = JOptionPane.showInputDialog("backtrack point: row,col");
                        if(res == null){
                            return;
                        }
                        int comma = res.indexOf(',');
                        if(comma == -1){
                            comma = res.indexOf('x');
                        }
                        int row, col;
                        try{
                            row = Integer.parseInt(res.substring(0, comma));
                            col = Integer.parseInt(res.substring(comma+1));
                        }catch (NumberFormatException nfExc){
                            JOptionPane.showMessageDialog(frame, "Number parsing error");
                            return;
                        }

                        int hash = new Point(row, col).hashCode();
                        if(!closed.containsKey(hash)){
                            JOptionPane.showConfirmDialog(null, "Point not found in the closed/visited list");
                            return;
                        }
                        backtrack(hash);
                        break;
                    case 't':
                        PathPoint end = closed.get(Day12.end.hashCode());
                        if(end == null){
                            return;
                        }
                        JOptionPane.showMessageDialog(frame, "DISTANCE:  " + end.pathDistance());
                        break;
                    case 'w':
                        if(currentRow - 1 < 0){
                            return;
                        }
                        currentRow--;
                        doLocationChangeEvents();
                        break;
                    case 'a':
                        if(currentCol - 1 < 0){
                            return;
                        }
                        currentCol--;
                        doLocationChangeEvents();
                        break;
                    case 's':
                        if(currentRow + 1 >= ROWS){
                            return;
                        }
                        currentRow++;
                        doLocationChangeEvents();
                        break;
                    case 'd':
                        if(currentCol + 1 >= COLS){
                            return;
                        }
                        currentCol++;
                        doLocationChangeEvents();
                        break;
                    //quick back-track
                    case 'b':
                        backtrack(new Point(currentRow, currentCol).hashCode());
                        break;
                    case 'n':
                        //next
                        update++;
                        break;
                    case 'm':
                        //mark all chars
                        String mark = JOptionPane.showInputDialog(frame, "Mark all(character):");
                        if(mark.length() != 1){
                            return;
                        }
                        GUI.colorAll(mark.charAt(0), Color.PINK);
                        break;
                }
            }

            private void doLocationChangeEvents(){
                JPanel panel = panels.stream()
                        .filter(p -> p.getName().equals(currentRow + "+" + currentCol))
                        .findAny().get();
                doColorInteraction(panel);
                String name = panel.getName().replace('+', 'x');
                frame.setTitle(name);
            }

            @Override
            public void keyReleased(KeyEvent e){
                e.consume();
            }
        };
        frame.addKeyListener(keyAdapter);
    }

    private static void backtrack(int hashPoint){
        PathPoint pp = closed.get(hashPoint);
        while(pp != null){
            String target = pp.row + "+" + pp.col;
            for(JPanel panel : panels){
                String name = panel.getName();
                if(name.equals(target)){
                    Color original = panel.getBackground();
                    Timer coloringTimer = new Timer();
                    TimerTask task = new TimerTask(){
                        @Override
                        public void run(){
                            if(panel.getBackground() == Color.red)
                                panel.setBackground(original);
                        }
                    };
                    coloringTimer.schedule(task, 10000);
                    panel.setBackground(Color.red);
                }
            }
            pp = pp.origin;
        }
    }

    public static void passClosedMap(HashMap<Integer, PathPoint> closed){
        GUI.closed = closed;
    }

    public static void colorAll(char c, Color color){
        for(JPanel panel : panels){
            String name = panel.getName();
            int plus = name.indexOf('+');
            int row, col;
            try{
                row = Integer.parseInt(name.substring(0, plus));
                col = Integer.parseInt(name.substring(plus + 1));
            }catch (NumberFormatException ignored){
                throw new RuntimeException();
            }
            if(Day12.map[row][col] == c){
                Color original = panel.getBackground();
                Timer coloringTimer = new Timer();
                TimerTask task = new TimerTask(){
                    @Override
                    public void run(){
                        if(panel.getBackground() == color)
                            panel.setBackground(original);
                    }
                };
                coloringTimer.schedule(task, 10000);
                panel.setBackground(color);
            }
        }
    }

    public static void setPanelLabel(int row, int col, String anything){
        String target = row + "+" + col;
        for(JPanel panel : panels){
            if(panel.getName().equals(target)){
                JLabel label = (JLabel) panel.getComponent(0);
                label.setText(anything);
            }
        }
    }

    //on closeds + availables
    public static void setFCosts(HashMap<Integer, PathPoint> available){
        for (PathPoint pp : available.values()){
            String target = pp.row + "+" + pp.col;
            for (JPanel panel : panels){
                if (panel.getName().equals(target)){
                    JLabel label = (JLabel) panel.getComponent(0);
                    label.setText(String.valueOf(pp.fCost));
                }
            }
        }
        for (PathPoint pp : closed.values()){
            String target = pp.row + "+" + pp.col;
            for (JPanel panel : panels){
                if (panel.getName().equals(target)){
                    JLabel label = (JLabel) panel.getComponent(0);
                    label.setText(String.valueOf(pp.fCost));
                }
            }
        }

    }

    public static void blockUpdate(int moves){
        if(!BLOCK_UPDATE){
            return;
        }
        while(update < moves){
        }
    }

    public static String determineOrigin(PathPoint pp){
        PathPoint origin = pp.origin;
        if(pp.origin == null){
            return "-";
        }
        if(origin.col < pp.col){
            return ">";
        }
        if(pp.col < origin.col){
            return "<";
        }
        if(pp.row < origin.row){
            return "^";
        }
        return "↓";
    }

    public static void paintOriginIndicators(HashMap<Integer, PathPoint> available){
        for (PathPoint pp : available.values()){
            String target = pp.row + "+" + pp.col;
            for (JPanel panel : panels){
                if (panel.getName().equals(target)){
                    JLabel label = (JLabel) panel.getComponent(0);
                    label.setText(determineOrigin(pp));
                }
            }
        }
        for (PathPoint pp : closed.values()){
            String target = pp.row + "+" + pp.col;
            for (JPanel panel : panels){
                if (panel.getName().equals(target)){
                    JLabel label = (JLabel) panel.getComponent(0);
                    label.setText(determineOrigin(pp));
                }
            }
        }
    }
}
