package advent.day9;

import advent.day12.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class LineGUI{
    public static int GRID_ROWS;
    public static int GRID_COLUMNS;
    private static JFrame frame;
    private static JTextArea textArea;

    public static Point head;
    public static Point tail;
    private static List<Point> parts;
    private static char[][] map;
    private static boolean[][] visited;



    public static void show(){
        frame = new JFrame("Line");
        frame.setSize(700, 700);
        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setFocusable(false);
        textArea.setForeground(Color.white);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Consolas",Font.BOLD, 20));
        frame.add(textArea);

        addKeyboardControlToFrame();
        frame.setVisible(true);
        updateTextField();
    }

    public static void addKeyboardControlToFrame(){
        frame.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                char c = e.getKeyChar();
                switch (c){
                    case 'w':
                        doMove('U');
                        break;
                    case 'a':
                        doMove('L');
                        break;
                    case 's':
                        doMove('D');
                        break;
                    case 'd':
                        doMove('R');
                        break;
                    case 'r':
                        initializeContext(GRID_ROWS, GRID_COLUMNS);
                        updateTextField();
                        break;
                }
            }
        });
    }

    public static void initializeContext(int rows, int cols){
        GRID_ROWS = rows;
        GRID_COLUMNS = cols;
        map = new char[GRID_ROWS][GRID_COLUMNS];
        Day9.fillMapWithEmptyValues(map);
        visited = new boolean[GRID_ROWS][GRID_COLUMNS];
        //LINE - BEGIN
        head = new Point(GRID_ROWS / 2, GRID_COLUMNS / 2);
        tail = new Point(GRID_ROWS / 2, GRID_COLUMNS / 2);
        Day9.head = head;
        Day9.tail = tail;
        parts = new ArrayList<>();
        parts.add(head);
        for (int i = 1; i <= 8; i++){
            parts.add(new Point(head.row, head.col));
        }
        parts.add(tail);
        //LINE - END
    }

    private static void doMove(char dir){
        //update previous for each part
        switch (dir){
            case 'L':
                head.col = head.col - 1;
                break;
            case 'R':
                head.col = head.col + 1;
                break;
            case 'U':
                head.row = head.row - 1;
                break;
            case 'D':
                head.row = head.row + 1;
                break;
        }
        Day9.repositionParts(parts);
        visited[tail.row][tail.col] = true;
        System.out.println("Visited so far: " + Day9.getVisitedCount(visited));
        updateTextField();
    }
    private static void updateTextField(){
        String strRepresentation = Day9.ropePartsAsString(map, parts);
        //System.out.println(strRepresentation);
        textArea.setText(strRepresentation);
    }
}
