package advent.day22;

import advent.AdventOfCode;
import advent.day22.enums.Direction;
import advent.day22.enums.InputType;

import java.util.Arrays;
import java.util.List;

//Day 22: Monkey Map
public class Day22{
    public static Chunk[][] chunks1;
    public static InputType inputType;
    public static void main(String[] args){
        //part1 full: 190066
        //part2 full: 134170
        inputType = InputType.FULL;
        List<String> lines = AdventOfCode.readDay(22);

        Chunk.setInputType(inputType);
        String path = lines.get(lines.size() - 1);
        int mapRows = lines.size()-2;

        int maxLength = 1;
        for (int i = 0; i < mapRows; i++){
            maxLength = Math.max(maxLength, lines.get(i).length());
        }

        int chunkSize = Chunk.chunkSize();
        chunks1 = new Chunk[mapRows / chunkSize][maxLength / chunkSize];
        System.out.println("Chunk map rows x cols: " + chunks1.length + " x " + chunks1[0].length);

        Chunk firstChunk = null;
        boolean startingChunkSet = false;
        for (int row = 0; row < chunks1.length; row++){
            for (int col = 0; col < chunks1[0].length; col++){
                String line = lines.get(row * chunkSize);
                boolean isAnEmptyChunk = line.length() <= col*chunkSize || line.charAt(col * chunkSize) == ' ';
                if(isAnEmptyChunk){
                    chunks1[row][col] = new Chunk(row, col);
                    continue;
                }
                Chunk chunk = new Chunk(new char[chunkSize][0], row, col);
                chunks1[row][col] = chunk;
                //iterates down and takes substrings of chunkSize
                for (int i = 0; i < chunkSize; i++){
                    char[] singleRow = lines.get(row*chunkSize + i).substring(col*chunkSize, col*chunkSize + chunkSize).toCharArray();
                    chunk.map[i] = singleRow;
                }
                if(!startingChunkSet){
                    startingChunkSet = true;
                    firstChunk = chunk;
                }
            }
        }
        //copy for part2
        CubeChunk[][] cubeChunks = copyToCubeChunks(chunks1);
        assert firstChunk != null;
        CubeChunk firstCubeChunk = cubeChunks[firstChunk.chunkTableRow][firstChunk.chunkTableCol];

        Move move = new Move(path);
        while(move.next()){
            System.out.println(move);
        }

        System.out.println(mapToString());
        //part1
        part1(firstChunk, move);

        //part2
        CubeMap map = new CubeMap(cubeChunks, inputType);
        map.doMoves(new CubeChunk(firstCubeChunk), move);
    }

    private static CubeChunk[][] copyToCubeChunks(Chunk[][] table){
        CubeChunk[][] newChunks = new CubeChunk[table.length][table[0].length];
        for (int row = 0; row < table.length; row++){
            CubeChunk[] cubeRow = newChunks[row];
            for (int i = 0; i < cubeRow.length; i++){
                Chunk chunk = table[row][i];
                if(chunk.isEmpty){
                    newChunks[row][i] = new CubeChunk(chunk.chunkTableRow, chunk.chunkTableCol);
                    continue;
                }
                newChunks[row][i] = new CubeChunk(chunk.copy());
            }
        }
        return newChunks;
    }

    private static void part1(Chunk current, final Move move){
        move.reset();
        int row = 0, col = 0;
        int size = Chunk.chunkSize();
        while(move.next()){
            switch (move.dir){
                case RIGHT:
                    for (int i = 0; i < move.steps; i++){
                        int nextIndex = col+1;
                        if(nextIndex == size){
                            //looping out of bounds
                            Chunk nextChunk = nextChunk(current, Direction.RIGHT);
                            if(nextChunk.map[row][0] == Chunk.WALL){
                                //stop
                                current.map[row][col] = '>';
                                break;
                            }
                            current.map[row][col] = '>';
                            col = 0;
                            current = nextChunk;
                            continue;
                        }
                        if(current.map[row][nextIndex] == Chunk.WALL){
                            //stop
                            break;
                        }
                        current.map[row][col] = '>';
                        col++;
                    }
                    break;
                case LEFT:
                    for (int i = 0; i < move.steps; i++){
                        int nextIndex = col-1;
                        if(nextIndex == -1){
                            //looping out of bounds
                            Chunk nextChunk = nextChunk(current, Direction.LEFT);
                            if(nextChunk.map[row][size-1] == Chunk.WALL){
                                //stop
                                break;
                            }
                            current.map[row][col] = '<';
                            col = size-1;
                            current = nextChunk;
                            continue;
                        }
                        if(current.map[row][nextIndex] == Chunk.WALL){
                            //stop
                            break;
                        }
                        current.map[row][col] = '<';
                        col--;
                    }
                    break;
                case UP:
                    for (int i = 0; i < move.steps; i++){
                        int nextIndex = row-1;
                        if(nextIndex == -1){
                            //looping out of bounds
                            Chunk nextChunk = nextChunk(current, Direction.UP);
                            if(nextChunk.map[size-1][col] == Chunk.WALL){
                                //stop
                                break;
                            }
                            current.map[row][col] = '^';
                            row = size-1;
                            current = nextChunk;
                            continue;
                        }
                        if(current.map[nextIndex][col] == Chunk.WALL){
                            //stop
                            break;
                        }
                        current.map[row][col] = '^';
                        row--;
                    }
                    break;
                case DOWN:
                    for (int i = 0; i < move.steps; i++){
                        int nextIndex = row+1;
                        if(nextIndex == size){
                            //looping out of bounds
                            Chunk nextChunk = nextChunk(current, Direction.DOWN);
                            if(nextChunk.map[0][col] == Chunk.WALL){
                                //stop
                                //put facing in place
                                break;
                            }
                            current.map[row][col] = 'v';
                            row = 0;
                            current = nextChunk;
                            continue;
                        }
                        if(current.map[nextIndex][col] == Chunk.WALL){
                            //stop
                            break;
                        }
                        //for walls puts facing in place
                        current.map[row][col] = 'v';
                        row++;
                    }
                    break;
            }
        }
        //insert last facing
        int password = finalPassword(current, row, col, move);
        System.out.println("Part1 password: " + password);
    }


    private static Chunk nextChunk(Chunk current, Direction dir){
        int chunksRows = chunks1.length, chunksCols = chunks1[0].length;

        int nextRow, nextCol;
        switch (dir){
            case LEFT:
                nextRow = current.chunkTableRow;
                nextCol = current.chunkTableCol - 1;
                if(nextCol < 0){
                    nextCol = chunksCols - 1;
                }
                break;
            case RIGHT:
                nextRow = current.chunkTableRow;
                nextCol = current.chunkTableCol + 1;
                if(nextCol == chunksCols){
                    nextCol = 0;
                }
                break;
            case UP:
                nextRow = current.chunkTableRow - 1;
                nextCol = current.chunkTableCol;
                if(nextRow < 0){
                    nextRow = chunksRows - 1;
                }
                break;
            case DOWN:
                nextRow = current.chunkTableRow + 1;
                nextCol = current.chunkTableCol;
                if(nextRow == chunksRows){
                    nextRow = 0;
                }
                break;
            default:
                throw new Error();
        }
        current = chunks1[nextRow][nextCol];
        if(current.isEmpty){
            return nextChunk(current, dir);
        }
        return current;
    }

    public static int finalPassword(Chunk lastChunk, int row, int col){
        return finalPassword(lastChunk, row, col, null);
    }
    public static int finalPassword(Chunk lastChunk, int row, int col, Move move){
        System.out.println("Last in-chunk coordinates (row x col): " + row + " x " + col);
        int sum;
        char facing = lastChunk.map[row][col];
        switch (facing){
            case '>':
                sum = 0;
                break;
            case 'v':
                sum = 1;
                break;
            case '<':
                sum = 2;
                break;
            case '^':
                sum = 3;
                break;
            default:
                if(move != null){
                    sum = dirToPoints(move.dir);
                }else{
                    throw new IllegalStateException("Last character cannot be determined");
                }
        }
        int size = Chunk.chunkSize();
        //1, 1, starts
        int mapRow = lastChunk.chunkTableRow * size + row + 1;
        int mapCol = lastChunk.chunkTableCol * size + col + 1;
        sum += 1000 * mapRow;
        sum += 4 * mapCol;
        return sum;
    }
    public static String mapToString(){
        StringBuilder str = new StringBuilder();

        int size = Chunk.chunkSize();
        for (Chunk[] rowOfChunks : chunks1){
            for (int i = 0; i < size; i++){
                //prints one line
                for (int col = 0; col < chunks1[0].length; col++){
                    Chunk chunk = rowOfChunks[col];
                    if (chunk.isEmpty){
                        str.append('[');
                        for (int j = 0; true; j++){
                            str.append("  ");
                            if(j == size){
                                str.append(']');
                                break;
                            }
                        }
                        continue;
                    }
                    char[] line = chunk.map[i];
                    str.append(Arrays.toString(line));
                }
                str.append('\n');
            }
        }
        return str.toString();
    }
    public static int dirToPoints(Direction dir){
        switch (dir){
            case RIGHT:
                return 0;
            case DOWN:
                return 1;
            case LEFT:
                return 2;
            case UP:
                return 3;
            default:
                throw new Error();
        }
    }
}
