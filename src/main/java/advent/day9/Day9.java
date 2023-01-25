package advent.day9;

import advent.AdventOfCode;
import advent.day12.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9{
    //part1: grid size 700 x 700 for full input
    //part1: grid size 12 x 12 for dummy input

    //part2: grid size 30 x 30 for dummy input
    public static final int GRID_ROWS = 900;
    public static final int GRID_COLUMNS = 900;
    public static final int ROPE_PARTS = 10;
    static Point head;
    static Point tail;

    public static void main(String[] args){
        //List<String> lines = AdventOfCode.readDummy(9);
        List<String> lines = AdventOfCode.readDay(9);
        //part1(lines);
        part2(lines);
    }

    private static void part2(List<String> lines){
        System.out.println("Map size[rows x cols]: " + GRID_ROWS + "x" + GRID_COLUMNS);

        char[][] map = new char[GRID_ROWS][GRID_COLUMNS];
        fillMapWithEmptyValues(map);
        boolean[][] visited = new boolean[GRID_ROWS][GRID_COLUMNS];
        //LINE - BEGIN
        head = new Point(GRID_ROWS/2, GRID_COLUMNS/2);
        tail = new Point(GRID_ROWS/2, GRID_COLUMNS/2);
        List<Point> parts = new ArrayList<>();
        parts.add(head);
        for (int i = 0; i < 8; i++){
            parts.add(new Point(head.row, head.col));
        }
        parts.add(tail);
        //LINE - END
        for(String line : lines){
            char dir = line.charAt(0);
            int moves = Integer.parseInt(line.substring(2));

            System.out.println(dir + " " + moves);
            for (int i = 0; i < moves; i++){
                //update previous for each part
                switch (dir){
                    case 'L':
                        head.col = head.col-1;
                        break;
                    case 'R':
                        head.col = head.col+1;
                        break;
                    case 'U':
                        head.row = head.row-1;
                        break;
                    case 'D':
                        head.row = head.row+1;
                        break;
                }
                repositionParts(parts);
                visited[tail.row][tail.col] = true;
                //System.out.println(ropePartsAsString(map, parts));
            }

        }
        int visits = getVisitedCount(visited);
        System.out.println("PART2: " + visits);
    }

    public static void repositionParts(List<Point> parts){
        for (int j = 0; j < parts.size()-1; j++){
            Point p1 = parts.get(j);
            Point p2 = parts.get(j+1);
            int distance = distance(p1.row, p1.col, p2.row, p2.col);
            if(distance <= 1){
                //next to or overlapping
                continue;
            }
            if(distance == 2){
                //diagonal
                boolean diagonal = Math.abs(p1.row - p2.row) == 1 && Math.abs(p1.col - p2.col) == 1;
                if(diagonal){
                    continue;
                }
                //either same x or same y
                Day9.repositionOnSameAxis(p1, p2);
                continue;
            }
            //distance 3 or 4 - definitely diagonal
            Day9.repositionDiagonally(p1, p2);
        }
    }

    //reposition p2 to p1
    protected static void repositionOnSameAxis(Point p1, Point p2){
        Point left = new Point(p2.row, p2.col - 1);
        Point right = new Point(p2.row, p2.col + 1);
        Point up = new Point(p2.row-1, p2.col);
        Point down = new Point( p2.row+1, p2.col);
        Point[] candidates = {left, right, up, down};

        for(Point candidate : candidates){
            if(distance(p1, candidate) == 1){
                p2.row = candidate.row;
                p2.col = candidate.col;
                return;
            }
        }
    }

    //distance 3-4 should reposition to distance 1 or 2
    protected static void repositionDiagonally(Point p1, Point p2){
        Point topLeft = new Point(p2.row-1, p2.col - 1);
        Point topRight = new Point(p2.row - 1, p2.col + 1);
        Point bottomLeft = new Point(p2.row+1, p2.col-1);
        Point bottomRight = new Point(p2.row+1, p2.col+1);
        Point[] candidates = {topLeft, topRight, bottomLeft, bottomRight};

        for(Point candidate : candidates){
            if(distance(p1, candidate) == 1){
                p2.row = candidate.row;
                p2.col = candidate.col;
                return;
            }
        }
        for(Point candidate : candidates){
            if(distance(p1, candidate) == 2){
                p2.row = candidate.row;
                p2.col = candidate.col;
                return;
            }
        }
    }

    private static void part1(List<String> lines){
        System.out.println("Map size[rows x cols]: " + GRID_ROWS + "x" + GRID_COLUMNS);

        char[][] map = new char[GRID_ROWS][GRID_COLUMNS];
        fillMapWithEmptyValues(map);

        boolean[][] visited = new boolean[GRID_ROWS][GRID_COLUMNS];

        Point head = new Point(GRID_ROWS/2, GRID_COLUMNS/2);
        Point tail = new Point(GRID_ROWS/2, GRID_COLUMNS/2);
        for(String line : lines){
            char dir = line.charAt(0);
            int moves = Integer.parseInt(line.substring(2));

            //previous head position
            int prvsRow, prvsCol;
            //System.out.println(dir + " " + moves);
            for (int i = 0; i < moves; i++){
                prvsRow = head.row;
                prvsCol = head.col;
                switch (dir){
                    case 'L':
                        head.col = head.col-1;
                        break;
                    case 'R':
                        head.col = head.col+1;
                        break;
                    case 'U':
                        head.row = head.row-1;
                        break;
                    case 'D':
                        head.row = head.row+1;
                        break;
                }
                boolean isTouching = isTouching(head.row, head.col, tail.row, tail.col);
                if(!isTouching){
                    tail.row = prvsRow;
                    tail.col = prvsCol;
                }
                visited[tail.row][tail.col] = true;
                //printMapHeadTail(map, head, tail);
                //System.out.println();
            }

        }
        int visits = getVisitedCount(visited);
        System.out.println("Visited1: " + visits);
    }

    //this can be simplified
    public static boolean isTouching(int x, int y, int x2, int y2){
        int distance = distance(x, y, x2, y2);
        if(distance <= 1){
            //next to or overlapping
            return true;
        }
        if(distance == 2){
            //diagonal
            return Math.abs(x - x2) == 1 && Math.abs(y - y2) == 1;
        }
        return false;
    }

    static int getVisitedCount(boolean[][] visited){
        int sum = 0;
        for (boolean[] booleans : visited){
            for (boolean aBoolean : booleans){
                if (aBoolean)
                    sum++;
            }
        }
        return sum;
    }
    static void fillMapWithEmptyValues(char[][] map){
        for(char[] row : map){
            Arrays.fill(row, '.');
        }
    }
    public static void printMapHeadTail(char[][] map, Point head, Point tail){
        boolean same = head.equals(tail);
        map[head.row][head.col] = 'H';
        if(!same){
            map[tail.row][tail.col] = 'T';
        }

        for(char[] row : map){
            System.out.println(Arrays.toString(row));
        }
        map[head.row][head.col] = '.';
        map[tail.row][tail.col] = '.';
    }
    protected static String ropePartsAsString(char[][] map, List<Point> parts){
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < map.length; row++){
            for (int col = 0; col < map[0].length; col++){
                for(int reverse = parts.size()-1; reverse>-1; reverse--){
                    Point part = parts.get(reverse);
                    if(row == part.row && col == part.col){
                        map[row][col] = (char) (reverse + 48);
                    }
                }
            }
        }

        for(char[] row : map){
            for (char c : row){
                sb.append(c).append(' ');
            }
            sb.append('\n');
        }
        for (int row = 0; row < map.length; row++){
            for (int col = 0; col < map[0].length; col++){
                //draw visited
                map[row][col] = '.';
            }
        }
        return sb.toString();
    }
    public static int distance(int row1, int col1, int row2, int col2){
        return Math.abs(col1 - col2) + Math.abs(row1 - row2);
    }
    public static int distance(Point p1, Point p2){
        return Math.abs(p1.col - p2.col) + Math.abs(p1.row - p2.row);
    }
}
