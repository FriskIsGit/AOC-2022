package advent.day22;

import advent.day22.enums.InputType;

import java.util.Arrays;

public class Chunk{
    public static final char WALL = '#', WALKABLE = '.';
    public final int chunkTableRow, chunkTableCol;
    public char[][] map;
    public boolean isEmpty = false;
    public Chunk(char[][] map, int chunkTableRow, int chunkTableCol){
        this.map = map;
        this.chunkTableRow = chunkTableRow;
        this.chunkTableCol = chunkTableCol;

    }
    public Chunk(int chunkTableRow, int chunkTableCol){
        this.isEmpty = true;
        this.chunkTableRow = chunkTableRow;
        this.chunkTableCol = chunkTableCol;
    }
    public static InputType mode = InputType.UNSET;

    public Chunk copy(){
        if(isEmpty){
            return new Chunk(chunkTableRow, chunkTableCol);
        }
        char[][] arr = new char[map.length][map[0].length];
        for (int row = 0; row < map.length; row++){
            System.arraycopy(map[row], 0, arr[row], 0, map[0].length);
        }
        return new Chunk(arr, chunkTableRow, chunkTableCol);
    }

    public static void setInputType(InputType mode){
        Chunk.mode = mode;
    }
    public static int chunkSize(){
        switch (mode){
            case FULL:
                return 50;
            case DUMMY:
                return 4;
            case UNSET:
            default:
                throw new IllegalStateException("Unset mode");
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(char[] row : map){
            sb.append(Arrays.toString(row)).append('\n');
        }
        return sb.toString();
    }
}


