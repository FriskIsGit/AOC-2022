package advent.day14;

public class ReservoirMap{
    private char[][] map;
    private int rowOffset, colOffset;
    public static final int SAND_SOURCE_COL = 500, SAND_SOURCE_ROW = 0;
    public static final char ROCK = '#', AIR = '.', SAND_SOURCE = '+', SAND = 'O';
    public int rows(){
        return map.length;
    }
    public int columns(){
        return map[0].length;
    }

    //translates arguments
    public char get(int row, int col){
        return map[row - rowOffset][col - colOffset];
    }
    public void set(int row, int col, char c){
        map[row - rowOffset][col - colOffset] = c;
    }
    public boolean isWithin(int row, int col){
        int translatedRow = row - rowOffset;
        if(translatedRow < 0 || translatedRow >= map.length){
            return false;
        }
        int translatedCol = col - colOffset;
        return translatedCol >= 0 && translatedCol < map[0].length;
    }

    private ReservoirMap(){

    }
    //inclusive args
    public static ReservoirMap create(int rowFrom, int rowTo, int colFrom, int colTo){
        ReservoirMap reservoir = new ReservoirMap();
        rowFrom = Math.min(SAND_SOURCE_ROW, rowFrom);
        int rowDiff = rowTo - rowFrom;
        int colDiff = colTo - colFrom;
        if(rowDiff < 0 || colDiff < 0){
            throw new IllegalArgumentException("rowFrom <= rowTo && colFrom <= colTo");
        }
        reservoir.map = new char[rowDiff + 1][colDiff+ 1];
        reservoir.rowOffset = rowFrom;
        reservoir.colOffset = colFrom;
        fillWithAir(reservoir.map);
        reservoir.set(SAND_SOURCE_ROW, SAND_SOURCE_COL, SAND_SOURCE);
        return reservoir;
    }

    private static void fillWithAir(char[][] map){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                map[i][j] = AIR;
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(char[] row : map){
            for(char c : row){
                str.append(c).append(' ');
            }
            str.append('\n');
        }
        return str.toString();
    }
}
