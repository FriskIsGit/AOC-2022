package advent.day9;

import advent.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day9{
    public static final int GRID_ROWS = 30;
    public static final int GRID_COLUMNS = 30;
    public static final int ROPE_PARTS = 10;

    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDummy(9);
        part1(lines);
        //part2 is wrong for some reason
        part2(lines);
    }

    private static void part2(List<String> lines){
        List<RopePart> rope = new ArrayList<>();
        boolean[][] visited = new boolean[GRID_ROWS][GRID_COLUMNS];
        //fill
        for(int i = 0; i<ROPE_PARTS; i++){
            RopePart part = new RopePart(GRID_ROWS/2, GRID_COLUMNS/2);
            rope.add(part);
            part.setPrvs(part);
        }
        for(String line : lines){
            char dir = line.charAt(0);
            int move = Integer.parseInt(line.substring(2));
            System.out.println(dir + " " + move);
            RopePart head;
            for (int m = 0; m < move; m++){
                System.out.println("--------");
                head = rope.get(0);
                head.setPrvs(head);
                switch (dir){
                    case 'L':
                        head.x = head.x - 1;
                        break;
                    case 'R':
                        head.x = head.x + 1;
                        break;
                    case 'U':
                        head.y = head.y - 1;
                        break;
                    case 'D':
                        head.y = head.y + 1;
                        break;
                }
                for(int r = 0; r < ROPE_PARTS-1; r++){
                    RopePart front = rope.get(r);
                    RopePart back = rope.get(r+1);

                    boolean touching = isTouching(front.x, front.y, back.x, back.y);
                    if(!touching){
                        //same axis situation
                        if(front.x == back.x || front.y == back.y){
                            back.setPrvs(back);
                            back.setXY(front.prvs);
                        }
                        //move diagonally
                        else{
                            back.setPrvs(back);
                            int min = 100;
                            int dUL = distance(front.x, front.y, back.x-1, back.y-1);
                            int dUR = distance(front.x, front.y, back.x+1, back.y-1);
                            int dBL = distance(front.x, front.y, back.x-1, back.y+1);
                            int dBR = distance(front.x, front.y, back.x+1, back.y+1);
                            min = Math.min(dUL, min);
                            min = Math.min(dUR, min);
                            min = Math.min(dBL, min);
                            min = Math.min(dBR, min);
                            if (min == dUL){
                                back.setXY(back.x-1, back.y-1);
                            }
                            else if (min == dUR){
                                back.setXY(back.x+1, back.y-1);
                            }
                            else if (min == dBL){
                                back.setXY(back.x-1, back.y+1);
                            }
                            else if (min == dBR){
                                back.setXY(back.x+1, back.y+1);
                            }

                        };
                    }
                }
                Point tail = rope.get(ROPE_PARTS-1);
                visited[tail.x][tail.y] = true;
                printGrid(rope);
                outer:{

                }

            }
        }
        int visits = getVisitedCount(visited);
        System.out.println("Visited2: " + visits);
    }

    private static int distance(int x, int y, int x2, int y2){
        return (int)Math.sqrt((x-x2) * (x-x2) + (y-y2) * (y-y2));
    }

    private static void part1(List<String> lines){
        boolean[][] visited = new boolean[GRID_ROWS][GRID_COLUMNS];
        int headRow = GRID_ROWS/2, headCol = GRID_COLUMNS/2;
        int tailRow = GRID_ROWS/2, tailCol = GRID_COLUMNS/2;
        for(String line : lines){
            char dir = line.charAt(0);
            int move = Integer.parseInt(line.substring(2));
            //System.out.println(dir + " " + move);
            //previous head position
            int prvsRow, prvsCol;
            for (int i = 0; i < move; i++){
                prvsRow = headRow;
                prvsCol = headCol;
                //printPositionOnGrid(headRow, headCol, tailRow, tailCol);
                switch (dir){
                    case 'L':
                        headCol = headCol-1;
                        break;
                    case 'R':
                        headCol = headCol+1;
                        break;
                    case 'U':
                        headRow = headRow-1;
                        break;
                    case 'D':
                        headRow = headRow+1;
                        break;
                }
                boolean isTouching = isTouching(headRow, headCol, tailRow, tailCol);
                if(!isTouching){
                    tailRow = prvsRow;
                    tailCol = prvsCol;
                }
                visited[tailRow][tailCol] = true;
            }

        }
        int visits = getVisitedCount(visited);
        System.out.println("Visited1: " + visits);
    }

    //this can be simplified
    public static boolean isTouching(int x, int y, int x2, int y2){
        return Math.sqrt((x-x2) * (x-x2) + (y-y2) * (y-y2)) < 2;
    }
    private static int getVisitedCount(boolean[][] visited){
        int sum = 0;
        for (boolean[] booleans : visited){
            for (int col = 0; col < visited[0].length; col++){
                if (booleans[col])
                    sum++;
            }
        }
        return sum;
    }
    private static void printGrid(int headRow, int headCol, int tailRow, int tailCol){
        for(int row = 0; row < GRID_ROWS; row++){
            for (int col = 0; col < GRID_COLUMNS; col++){
                if(row == headRow && col == headCol){
                    System.out.print('H');
                    continue;
                }else if (row == tailRow && col == tailCol){
                    System.out.print('T');
                    continue;
                }
                System.out.print('.');
            }
            System.out.println();
        }
    }
    private static void printGrid(List<RopePart> rope){
        for(int row = 0; row < GRID_ROWS; row++){
            for (int col = 0; col < GRID_COLUMNS; col++){
                /*RopePart head = rope.get(0);
                RopePart tail = rope.get(ROPE_PARTS-1);
                if(row == head.x && col == head.y){
                    System.out.print('H');
                    continue;
                }else if (row == tail.x && col == tail.y){
                    System.out.print('T');
                    continue;
                }*/
                boolean roped = false;
                for (int i = 0; i < rope.size(); i++){
                    RopePart part = rope.get(i);
                    if (row == part.y && col == part.x){
                        roped = true;
                        System.out.print(i);
                        break;
                    }
                }
                if(!roped){
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }
}
