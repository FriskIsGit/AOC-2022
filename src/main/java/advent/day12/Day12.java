package advent.day12;

import advent.AdventOfCode;

import java.util.*;

public class Day12{
    //'S' - start, 'E' - end,
    public static int ROWS, COLS;
    public static char[][] map;
    public static char[][] visual;
    public static int[][] accessibleAtAll;
    public static Point start, end;
    public static void main(String[] args){
        part1();
    }

    private static void part1(){
        List<String> lines = AdventOfCode.readDummy(12);
        setupEnvironment(lines);
        printDistances();
        System.out.println("ROWS:" + ROWS);
        System.out.println("COLS:" + COLS);
        System.out.println();
        int steps = 0;
        //set of hashcodes
        HashSet<Integer> visited = new HashSet<>();
        LinkedList<Point> path = new LinkedList<>();
        path.add(start);
        Point currentPoint = start;
        //map any route,
        while(!currentPoint.equals(end)){
            Point nextPoint;
            while(true){
                nextPoint = findCandidate(currentPoint, visited);
                if(nextPoint == null){
                    if(path.size() == 1){
                        throw new IllegalStateException("There's no way to reach the target");
                    }
                    Point last = path.removeLast();
                    currentPoint = path.getLast();
                    visual[last.row][last.col] = '.';
                    steps--;
                }else{
                    break;
                }
            }
            path.add(nextPoint);
            steps++;
            currentPoint = nextPoint;
            visited.add(currentPoint.hashCode());
            visual[currentPoint.row][currentPoint.col] = 'T';
            printMap(visual);
            System.out.println(steps);
            System.out.println();
        }
        System.out.println("steps: " + steps);
    }
    //find the closest traversable point which has not yet been visited
    //looks up, down, left and right
    //returns null if no valid candidate is found
    private static Point findCandidate(Point point, HashSet<Integer> visited){
        List<Point> candidates = new ArrayList<>(4);
        int upRow = point.row-1, upCol = point.col;
        int downRow = point.row+1, downCol = point.col;
        int leftRow = point.row, leftCol = point.col-1;
        int rightRow = point.row, rightCol = point.col+1;
        if(isWithin(upRow, upCol)){
            candidates.add(new Point(upRow, upCol));
        }
        if(isWithin(downRow, downCol)){
            candidates.add(new Point(downRow, downCol));
        }
        if(isWithin(leftRow, leftCol)){
            candidates.add(new Point(leftRow, leftCol));
        }
        if(isWithin(rightRow, rightCol)){
            candidates.add(new Point(rightRow, rightCol));
        }
        final int MAX_DISTANCE = Integer.MAX_VALUE;
        int min = MAX_DISTANCE, index = -1;
        int size = candidates.size();
        for (int i = 0; i < size; i++){
            Point p = candidates.get(i);
            if(visited.contains(p.hashCode())){
                continue;
            }
            if(canStep(point.row, point.col, p.row, p.col)){
                int distance = distance(end.row, end.col, p.row, p.col);
                if(min > distance){
                    min = distance;
                    index = i;
                }
            }
        }
        if(min == MAX_DISTANCE){
            System.out.println("No appropriate candidate could be found for: " + point);
            return null;
        }
        return candidates.get(index);
    }

    private static void printDistances(){
        int[][] distances = new int[ROWS][COLS];
        for (int i = 0; i < map.length; i++){
            char[] row = map[i];
            for (int j = 0; j < row.length; j++){
                if(map[i][j] != 'E' && map[i][j] != 'S'){
                    int dist = distance(end.row, end.col, i, j);
                    distances[i][j] = dist;
                }else if(map[i][j] == 'S'){
                    distances[i][j] = 0;
                }
            }
        }
        printMap(distances);
    }

    private static boolean isWithin(int row, int col){
        if(row >= ROWS || row < 0){
            return false;
        }
        return col < COLS && col > -1;
    }
    //f - from, t - to,
    public static boolean canStep(int fRow, int fCol, int toRow, int toCol){
        char fChar = map[fRow][fCol];
        if(fChar == 'S'){
            return true;
        }
        char toChar = map[toRow][toCol];
        if(toChar == 'E'){
            return fChar == 'z' || fChar =='y';
        }
        return toChar - fChar <= 1;
    }
    private static int distance(int row1, int col1, int row2, int col2){
        return Math.abs(col1 - col2) + Math.abs(row1 - row2);
    }
    private static void printMap(char[][] map){
        for(char[] row : map){
            System.out.println(Arrays.toString(row));
        }
    }
    private static void printMap(int[][] map){
        for(int[] row : map){
            System.out.println(Arrays.toString(row));
        }
    }
    private static void setupEnvironment(List<String> lines){
        ROWS = lines.size();
        COLS = lines.get(0).length();
        map = new char[ROWS][COLS];
        visual = new char[ROWS][COLS];
        for(char[] row : visual){
            Arrays.fill(row, '.');
        }
        accessibleAtAll = new int[ROWS][COLS];
        //map input
        for (int i = 0; i < lines.size(); i++){
            map[i] = lines.get(i).toCharArray();
        }
        //find S and E
        boolean scanningS = true;
        boolean scanningE = true;
        for (int r = 0; r < map.length; r++){
            char[] row = map[r];
            for (int c = 0; c < COLS; c++){
                if(!scanningS && !scanningE){
                    break;
                }
                if (scanningS && row[c] == 'S'){
                    scanningS = false;
                    start = new Point(r, c);
                }else if(scanningE && row[c] == 'E'){
                    scanningE = false;
                    end = new Point(r, c);
                }
            }
        }
    }
}
