package advent.day12;

import advent.AdventOfCode;

import java.awt.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class Day12{
    public static final boolean USE_MIN_H_COST = false;
    public static final boolean USE_MAX_H_COST = true;
    public static final boolean USE_MAX_G_COST = false;
    public static final boolean USE_GUI = true;
    //'S' - start, 'E' - end,
    public static int ROWS, COLS;
    public static char[][] map;
    public static HashMap<Integer, PathPoint> closed = new HashMap<>(128);
    public static HashMap<Integer, PathPoint> available = new HashMap<>(128);
    public static Point start, end;
    public static int speedMs = 10;
    public static void main(String[] args){
        part1();
        //part2();
    }

    private static void part1() {
        List<String> lines = AdventOfCode.readDay(12);

        setupEnvironment(lines);
        if(USE_GUI){
            GUI.main(null);
        }
        System.out.println("ROWS:" + ROWS);
        System.out.println("COLS:" + COLS);
        int distance = enterSearchLoop();
        System.out.println("Least steps: " + distance);
    }
    private static void part2(){
        List<String> lines = AdventOfCode.readDay(12);

        setupEnvironment(lines);
        int minFromA = Integer.MAX_VALUE;
        Point minA = null;
        //assuming that all valid A's are at column 0
        final int col = 0;
        for(int row = 0; row < ROWS; row++){
            int d = runFor(row, col);
            if(d < minFromA){
                minFromA = d;
                minA = new Point(row, col);
            }
        }
        //+-2
        System.out.println("Shortest path from A: " + minFromA + "; point: " + minA);
    }

    private static int runFor(int row, int col){
        start = new Point(row,col);
        System.out.println("FOR: " + start);
        closed = new HashMap<>(128);
        available = new HashMap<>(128);
        if(USE_GUI){
            GUI.passClosedMap(closed);
            GUI.main(null);
        }
        return enterSearchLoop();

    }

    public static void sleep(int sleep){
        try{
            Thread.sleep(sleep);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static int enterSearchLoop(){
        int moves = 0;
        PathPoint currentPoint = new PathPoint(start.row, start.col);

        closed.put(start.hashCode(), currentPoint);
        long st = System.currentTimeMillis();
        //map shortest route,
        while(!currentPoint.equals(end)){
            currentPoint = findNext(currentPoint);
            moves++;
            int hash = currentPoint.hashCode();
            closed.put(hash, currentPoint);
            available.remove(hash);
            if(USE_GUI){
                GUI.paintPanelColor(currentPoint.row, currentPoint.col, Color.lightGray);
                //GUI.paintOriginIndicators(available);
                GUI.blockUpdate(moves);
                //GUI.setPanelLabel(currentPoint.row, currentPoint.col, currentPoint.fCost);
            }
        }
        long en = System.currentTimeMillis();
        //GUI.setFCosts(available);
        System.out.println("moves: " + moves);
        System.out.println("time taken to explore: " + (en-st) + " ms");
        int pathDistance = currentPoint.pathDistance();

        if(USE_GUI){
            PathPoint back = currentPoint;
            while(back != null){
                sleep(speedMs);
                GUI.paintPanelColor(back.row, back.col, Color.cyan);
                back = back.origin;
            }
        }
        return pathDistance;
    }

    //chooses the next point to explore
    //if no valid candidate can be found returns null
    private static PathPoint findNext(PathPoint current){
        int upRow = current.row-1, upCol = current.col;
        int downRow = current.row+1, downCol = current.col;
        int leftRow = current.row, leftCol = current.col-1;
        int rightRow = current.row, rightCol = current.col+1;

        if(isWithin(rightRow, rightCol)){
            PathPoint pp = new PathPoint(rightRow, rightCol, current);
            processNeighbor(current, pp);
        }
        if(isWithin(leftRow, leftCol)){
            PathPoint pp = new PathPoint(leftRow, leftCol, current);
            processNeighbor(current, pp);
        }
        if(isWithin(upRow, upCol)){
            PathPoint pp = new PathPoint(upRow, upCol, current);
            processNeighbor(current, pp);
        }
        if(isWithin(downRow, downCol)){
            PathPoint pp = new PathPoint(downRow, downCol, current);
            processNeighbor(current, pp);
        }

        if(available.size() == 0){
            throw new IllegalStateException("No available points to proceed");
        }

        List<PathPoint> minimals = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        for (PathPoint candidate : available.values()){
            if (min > candidate.fCost){
                minimals.clear();
                min = candidate.fCost;
                minimals.add(candidate);
            }
            if (min == candidate.fCost){
                minimals.add(candidate);
            }
        }
        int size = minimals.size();
        if(size == 0){
            throw new IllegalStateException("No minimal points to proceed for " + current);
        }

        if(size ==  1){
            return minimals.get(0);
        }

        if(USE_MIN_H_COST && !USE_MAX_G_COST){
            int hMin = Integer.MAX_VALUE, index = -1;
            //scan minimals - choose min h cost
            for (int i = 0; i < minimals.size(); i++){
                PathPoint p = minimals.get(i);
                if (hMin > p.hCost){
                    hMin = p.hCost;
                    index = i;
                }
            }
            return minimals.get(index);
        }
        if(USE_MAX_G_COST && !USE_MIN_H_COST){
            int gMax = Integer.MIN_VALUE, index = -1;
            //scan minimals - choose max g cost
            for (int i = 0; i < minimals.size(); i++){
                PathPoint p = minimals.get(i);
                if (gMax < p.gCost){
                    gMax = p.gCost;
                    index = i;
                }
            }
            return minimals.get(index);
        }
        if(USE_MAX_H_COST && !USE_MIN_H_COST && !USE_MAX_G_COST){
            int hMax = Integer.MIN_VALUE, index = -1;
            //scan minimals - choose max h cost
            for (int i = 0; i < minimals.size(); i++){
                PathPoint p = minimals.get(i);
                if (hMax < p.hCost){
                    hMax = p.hCost;
                    index = i;
                }
            }
            return minimals.get(index);
        }
        return minimals.get(0);
    }


    //meant to process nodes which are in the immediate vicinity of current node
    private static void processNeighbor(PathPoint current, PathPoint neighbor){
        int hashNeighbor = neighbor.hashCode();
        // assuming some other method is responsible for removing 'visited' nodes from 'available'
        if(closed.containsKey(hashNeighbor)){
            return;
        }
        if(available.containsKey(hashNeighbor)){
            // contained - check if we can make the path shorter
            PathPoint anAvailable = available.get(hashNeighbor);
            if (anAvailable.origin.equals(current)){
                return;
            }
            // make sure it is not repointed toward a point it could have not stepped from
            if(!canStep(current.row, current.col, anAvailable.row, anAvailable.col)){
                return;
            }
            // by pointing the origin toward current (attaching available to current)
            int prvsDistance = anAvailable.pathDistance();
            int currentDistance = current.pathDistance() + 1;
            if(currentDistance < prvsDistance){
                anAvailable.origin = current;
                anAvailable.calculateCosts();
            }

        }else{
            //do not add it as a possibility if it cannot be stepped to
            if(canStep(current.row, current.col, neighbor.row, neighbor.col)){
                available.put(hashNeighbor, neighbor);
            }
        }
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
    public static void setupEnvironment(List<String> lines){
        ROWS = lines.size();
        COLS = lines.get(0).length();
        map = new char[ROWS][COLS];
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
        PathPoint.setEnd(end);
        GUI.initializeContext(ROWS, COLS);
        GUI.passClosedMap(closed);
    }
}
