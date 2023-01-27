package advent.day17;

import advent.AdventOfCode;
import advent.day17.shapes.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//1517309594460,9297725024727992087
//1514285714288,
public class Day17{
    public static final int WIDTH = 7, HEIGHT = 17000;
    //public static final int ROCKS = 2022;
    public static final int ROCKS = 2022, MORE_ROCKS = 10000;
    public static final long TRILLION_ROCKS = 1_000_000_000_000L;

    //the tallest shape height
    public static final int Y_BASE_OFFSET = 4;
    public static char[][] map;
    //pushes: < indicates left, > indicates right
    //Pyroclastic Flow
    public static void main(String[] args){
        doBothParts();
    }

    private static void doBothParts(){

        List<String> line = AdventOfCode.readDay(17);
        //List<String> line = AdventOfCode.readDummy(17);
        assert(line.size() == 1);
        JetPattern pattern = new JetPattern(line.get(0));
        System.out.println("Jet pattern length: " + pattern.length());

        int baseY = HEIGHT;
        map = new char[HEIGHT][WIDTH];
        fillMapWithEmptyValues(map);
        Shape.setMap(map);

        ShapePattern shapePattern = new ShapePattern();
        long st = System.currentTimeMillis();
        for(int rock = 0; rock < ROCKS; rock++){
            baseY = determineNextBaseY(baseY);
            Shape next = shapePattern.next(baseY);
            fallShape(next, pattern);
        }
        long en = System.currentTimeMillis();
        System.out.println("Main loop time taken ms: " + (en-st));
        System.out.println("Part1 - Rock tower height: " + towerHeight());
        System.out.println();
        part2(pattern, shapePattern);
    }

    //matching rows
    private static void part2(JetPattern pattern, ShapePattern shapePattern){
        int baseY = HEIGHT;
        map = new char[HEIGHT][WIDTH];
        fillMapWithEmptyValues(map);
        Shape.setMap(map);
        pattern.reset();
        shapePattern.reset();
        List<Integer> rocksPerPattern = new ArrayList<>();
        List<Integer> heightPerPattern = new ArrayList<>();
        //simulate more rocks to find repetitions
        //this time tracking how many rocks fall between matches
        for(int rock = 0; rock < MORE_ROCKS; rock++){
            baseY = determineNextBaseY(baseY);
            Shape shape = shapePattern.next(baseY);
            while(!shape.isSolid){
                // it appears that every time jet pattern repeats the rock structure loops
                // therefore instead of simulating rocks between matching rows -
                // we count how many rocks must fall before jetPattern loops
                if(pattern.getPointer() == pattern.length()){
                    rocksPerPattern.add(rock);
                    //height relative to 0
                    heightPerPattern.add(map.length - (baseY+4));
                }
                switch (pattern.next()){
                    case '>':
                        shape.pushRight();
                        break;
                    case '<':
                        shape.pushLeft();
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected char in pattern: " + pattern.current());
                }
                shape.fall();
            }
        }
        System.out.println(mapToString(map, 0));
        assert (heightPerPattern.size() == rocksPerPattern.size());

        List<Integer> matchingRows = findRepeatingRows(8);

        //in units of rows
        int mostCommonRowDiff = findMostCommonMatch(matchingRows);
        //find out how many rocks can be fit into a range of difference
        System.out.println("Structure loops every: " + mostCommonRowDiff + " rows");
        System.out.println("Matches at rows: " + matchingRows);

        //in units of rows
        int mostCommonHeightDiff = findMostCommonMatch(heightPerPattern);
        System.out.println("Height loops every: " + mostCommonHeightDiff + " rows");
        System.out.println("Jet heights: " + heightPerPattern);

        //in units of rocks - every patternLoopEvery is of height rowLoopEvery
        int patternLoopEvery = findMostCommonMatch(rocksPerPattern);
        System.out.println("Jet pattern loops every: " + patternLoopEvery + " rocks");
        System.out.println("Rocks per pattern: " + rocksPerPattern);

        int bottomRocks = rocksPerPattern.get(0);
        long rocksToCover = TRILLION_ROCKS - bottomRocks;
        long parts = rocksToCover / patternLoopEvery;
        long remainingRocks = rocksToCover - (parts * patternLoopEvery);
        System.out.println("Remaining rocks: " + remainingRocks);

        //simulate the remaining rocks
        baseY = mostCommonHeightDiff*2;
        map = new char[baseY][WIDTH];
        fillMapWithEmptyValues(map);
        Shape.setMap(map);
        pattern.reset();
        shapePattern.reset();

        int bottomHeight = heightPerPattern.get(0);
        long middleHeights = parts * mostCommonHeightDiff;
        //simulate bottom rocks then up to remainingRocks
        int firstStageRocks = rocksPerPattern.get(0);
        for(int rock = 0; rock < firstStageRocks; rock++){
            baseY = determineNextBaseY(baseY);
            Shape shape = shapePattern.next(baseY);
            fallShape(shape, pattern);
        }
        for(int rock = 0; rock < remainingRocks; rock++){
            baseY = determineNextBaseY(baseY);
            Shape shape = shapePattern.next(baseY);
            fallShape(shape, pattern);
        }
        baseY = determineNextBaseY(baseY);
        baseY += 4;
        int topHeight = map.length - baseY;
        topHeight -= bottomHeight;
        System.out.println("Height of: " + remainingRocks + " is roughly: " + topHeight);
        System.out.println("Total height of a trillion rocks: " +
                (topHeight + middleHeights + bottomHeight));

    }
    //validate if difference between matches is always the same
    private static int findMostCommonMatch(List<Integer> matches){
        //rowNumber to occurrences
        HashMap<Integer, Integer> numberToFreq = new HashMap<>();
        //last shouldn't be compared
        for (int i = 0; i < matches.size() - 1; i++){
            int v1 = matches.get(i);
            int v2 = matches.get(i+1);
            int diff = Math.abs(v1 - v2);
            if(numberToFreq.containsKey(diff)){
                numberToFreq.put(diff, numberToFreq.get(diff) + 1);
            }else{
                numberToFreq.put(diff, 1);
            }
        }
        int mostCommonKey = -1;
        int mostFreq = -1;
        for (HashMap.Entry<Integer, Integer> entry : numberToFreq.entrySet()) {
            int key = entry.getKey();
            int freq = entry.getValue();
            if(freq > mostFreq){
                mostFreq = freq;
                mostCommonKey = key;
            }
        }
        return mostCommonKey;
    }

    public static final int MATCHES_THRESHOLD = 3;
    private static List<Integer> findRepeatingRows(final int sampleRows){
        return findRepeatingRows(sampleRows, sampleRows);
    }
    private static List<Integer> findRepeatingRows(final int sampleRows, int multiplier){
        //optimal sampleRows to start with is 8
        List<Integer> matches = new ArrayList<>();
        //2620 -> input17
        // after playing with sampleRows and sampleFromY for some time
        // you can notice that by reducing sampleFrom by a double of previous value
        // reliable matches will begin to show up

        final int sampleFromY = map.length - multiplier;
        if(sampleFromY < 0){
            throw new IllegalStateException("No rock pattern match was found for given sampleRows: " + sampleRows);
        }
        char[][] sampleSnippet = new char[sampleRows][WIDTH];
        //take a sample of rows
        for (int row = sampleFromY, r = 0; row < sampleFromY+sampleRows; row++, r++){
            System.arraycopy(map[row], 0, sampleSnippet[r], 0, WIDTH);
        }
        System.out.println("SAMPLE-MAP");
        System.out.println(mapToString(sampleSnippet, 0));
        //look for a match (if there is one)
        for (int topRow = sampleFromY - sampleRows; topRow >= 0; topRow--){
            boolean match = true;

            comparisonLoop:
            for (int mapRow = topRow, r = 0; mapRow<topRow+sampleRows; mapRow++, r++){
                for (int col = 0; col < WIDTH; col++){
                    if(map[mapRow][col] != sampleSnippet[r][col]){
                        match = false;
                        break comparisonLoop;
                    }
                }
            }
            if(match){
                matches.add(topRow);
            }
        }
        if(matches.size() < MATCHES_THRESHOLD){
            return findRepeatingRows(sampleRows, multiplier*2);
        }
        return matches;
    }

    private static void fallShape(Shape shape, JetPattern pattern){
        while(!shape.isSolid){
            switch (pattern.next()){
                case '>':
                    shape.pushRight();
                    break;
                case '<':
                    shape.pushLeft();
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected char in pattern: " + pattern.current());
            }
            shape.fall();
        }
    }

    private static void fillMapWithEmptyValues(char[][] map){
        for(char[] row : map){
            Arrays.fill(row, Shape.EMPTY);
        }
    }

    private static int determineNextBaseY(int previousY){
        //find first not empty
        //then add 3 to it
        for (int row = Math.max(0, previousY - Y_BASE_OFFSET); row < map.length; row++){
            for (int col = 0; col < WIDTH; col++){
                if(map[row][col] != Shape.EMPTY){
                    //3 units between
                    return row - 4;
                }
            }
        }
        return map.length - 4;
    }
    private static int towerHeight(){
        //first not empty
        for (int row = 0; row < map.length; row++){
            for (int col = 0; col < WIDTH; col++){
                if(map[row][col] != Shape.EMPTY){
                    return map.length - row;
                }
            }
        }
        return -1;
    }
    //height from bottom
    private static String mapToString(char[][] map, int fromRow, int toRowExclusive){
        StringBuilder str = new StringBuilder();
        for (int r = fromRow; r < toRowExclusive; r++){
            char[] row = map[r];
            str.append(Arrays.toString(row)).append('\n');
        }
        return str.toString();
    }
    private static String mapToString(char[][] map, int fromRow){
        return mapToString(map, fromRow, map.length);
    }
}
