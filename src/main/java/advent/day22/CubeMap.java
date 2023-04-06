package advent.day22;

import advent.day22.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubeMap{
    public List<EdgeLink> edgeLinks = new ArrayList<>(12);
    public CubeChunk[][] chunks;
    public CubeMap(CubeChunk[][] chunks, InputType inputType){
        this.chunks = chunks;
        labelChunks(inputType);
    }

    public void labelChunks(InputType inputType){
        addImmediateLinks();
        assert edgeLinks.size() == 5;
        addCloseLinks();
        assert edgeLinks.size() == 8;
        addDistantLinks();

        //hard coded, since maps differ
        if(inputType == InputType.DUMMY){
            chunks[0][2].side = CubeSide.U;
            chunks[1][2].side = CubeSide.F;
            chunks[2][2].side = CubeSide.D;
            chunks[2][3].side = CubeSide.R;
            chunks[1][1].side = CubeSide.L;
            chunks[1][0].side = CubeSide.B;
            //set missing edge links
            edgeLinks.add(EdgeLink.of(chunks[0][2], Edge.RIGHT, chunks[2][3], Edge.RIGHT));
            edgeLinks.add(EdgeLink.of(chunks[0][2], Edge.TOP, chunks[1][0], Edge.TOP));
            edgeLinks.add(EdgeLink.of(chunks[2][2], Edge.BOTTOM, chunks[1][0], Edge.BOTTOM));
            edgeLinks.add(EdgeLink.of(chunks[2][3], Edge.BOTTOM, chunks[1][0], Edge.LEFT));
        }
        else if(inputType == InputType.FULL){
            chunks[0][1].side = CubeSide.U;
            chunks[0][2].side = CubeSide.R;
            chunks[1][1].side = CubeSide.F;
            chunks[2][1].side = CubeSide.D;
            chunks[2][0].side = CubeSide.L;
            chunks[3][0].side = CubeSide.B;
            //set 4 missing edge links
            edgeLinks.add(EdgeLink.of(chunks[0][1], Edge.TOP, chunks[3][0], Edge.LEFT));
            edgeLinks.add(EdgeLink.of(chunks[0][1], Edge.LEFT, chunks[2][0], Edge.LEFT));

            edgeLinks.add(EdgeLink.of(chunks[0][2], Edge.TOP, chunks[3][0], Edge.BOTTOM));
            edgeLinks.add(EdgeLink.of(chunks[0][2], Edge.RIGHT, chunks[2][1], Edge.RIGHT));
        }
        assert edgeLinks.size() == 12;
        for(EdgeLink link : edgeLinks){
            System.out.println(link);
        }
    }

    private void addDistantLinks(){
        //find a universal way to connect distant edges
    }

    private void addCloseLinks(){
        int rows = chunks.length, cols = chunks[0].length;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                //should have exactly two non-empty neighbors
                CubeChunk currentChunk = chunks[row][col];
                if(!currentChunk.isEmpty){
                    continue;
                }
                //down
                if(row + 1 < chunks.length && !chunks[row+1][col].isEmpty){
                    CubeChunk downChunk = chunks[row+1][col];
                    // and right
                    if(col + 1 < chunks[0].length && !chunks[row][col+1].isEmpty){
                        CubeChunk rightChunk = chunks[row][col+1];
                        edgeLinks.add(EdgeLink.of(downChunk, Edge.TOP, rightChunk, Edge.LEFT));
                    }
                    // and left
                    else if(col - 1 >= 0 && !chunks[row][col-1].isEmpty){
                        CubeChunk leftChunk = chunks[row][col-1];
                        edgeLinks.add(EdgeLink.of(downChunk, Edge.TOP, leftChunk, Edge.RIGHT));
                    }
                }
                //top
                if(row - 1 >= 0 && !chunks[row-1][col].isEmpty){
                    CubeChunk topChunk = chunks[row-1][col];
                    // and right
                    if(col + 1 < chunks[0].length && !chunks[row][col+1].isEmpty){
                        CubeChunk rightChunk = chunks[row][col+1];
                        edgeLinks.add(EdgeLink.of(topChunk, Edge.BOTTOM, rightChunk, Edge.LEFT));
                    }
                    // and left
                    else if(col - 1 >= 0 && !chunks[row][col-1].isEmpty){
                        CubeChunk leftChunk = chunks[row][col-1];
                        edgeLinks.add(EdgeLink.of(topChunk, Edge.BOTTOM, leftChunk, Edge.RIGHT));
                    }
                }
            }
        }
    }

    private void addImmediateLinks(){
        int rows = chunks.length, cols = chunks[0].length;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                CubeChunk currentChunk = chunks[row][col];
                if(currentChunk.isEmpty){
                    continue;
                }
                //down
                if(row + 1 < chunks.length && !chunks[row+1][col].isEmpty){
                    CubeChunk bottomChunk = chunks[row+1][col];
                    edgeLinks.add(EdgeLink.of(currentChunk, Edge.BOTTOM, bottomChunk, Edge.TOP));
                }
                //right
                if(col + 1 < chunks[0].length && !chunks[row][col+1].isEmpty){
                    CubeChunk rightChunk = chunks[row][col+1];
                    edgeLinks.add(EdgeLink.of(currentChunk, Edge.RIGHT, rightChunk, Edge.LEFT));
                }
            }
        }
    }
    private int immediateChunks(int row, int col){
        int count = 0;
        //up
        if(row - 1 >= 0 && !chunks[row-1][col].isEmpty){
            count++;
        }
        //down
        if(row + 1 < chunks.length && !chunks[row+1][col].isEmpty){
            count++;
        }
        //left
        if(col - 1 >= 0 && !chunks[row][col-1].isEmpty){
            count++;
        }
        //right
        if(col + 1 < chunks[0].length && !chunks[row][col+1].isEmpty){
            count++;
        }
        return count;
    }

    //u cannot step out of the map in part2
    public void doMoves(CubeChunk current, final Move move){
        move.reset();
        int size = Chunk.chunkSize();

        Course course = new Course(new Point(0,0), current, move.dir);
        while(move.next()){
            course.direction = move.dir;
            stepsLoop:
            for (int i = 0; i < move.steps; i++){
                Point p = course.point;
                switch (course.direction){
                    case LEFT:
                        if(p.col == 0){
                            Course next = nextCourse(course);
                            if(next.chunk.map[next.point.row][next.point.col] == Chunk.WALL){
                                break stepsLoop;
                            }
                            course = next;
                            break;
                        }
                        if(course.chunk.map[p.row][p.col-1] == Chunk.WALL){
                            break stepsLoop;
                        }
                        p.col--;
                        break;
                    case RIGHT:
                        if(p.col == size - 1){
                            Course next = nextCourse(course);
                            if(next.chunk.map[next.point.row][next.point.col] == Chunk.WALL){
                                break stepsLoop;
                            }
                            course = next;
                            break;
                        }
                        if(course.chunk.map[p.row][p.col+1] == Chunk.WALL){
                            break stepsLoop;
                        }
                        p.col++;
                        break;
                    case UP:
                        if(p.row == 0){
                            Course next = nextCourse(course);
                            if(next.chunk.map[next.point.row][next.point.col] == Chunk.WALL){
                                break stepsLoop;
                            }
                            course = next;
                            break;
                        }
                        if(course.chunk.map[p.row -1][p.col] == Chunk.WALL){
                            break stepsLoop;
                        }
                        p.row--;
                        break;
                    case DOWN:
                        if(p.row == size - 1){
                            Course next = nextCourse(course);
                            if(next.chunk.map[next.point.row][next.point.col] == Chunk.WALL){
                                break stepsLoop;
                            }
                            course = next;
                            break;
                        }
                        if(course.chunk.map[p.row +1][p.col] == Chunk.WALL){
                            break stepsLoop;
                        }
                        p.row++;
                        break;
                }
                placeArrow(course);
            }
            move.dir = course.direction;
        }
        System.out.println("Finished main loop");
        int password = Day22.finalPassword(course.chunk, course.point.row, course.point.col, move);
        System.out.println("FINAL PASSWORD: " + password);
        //System.out.println("FINAL MAP:");
        //course.chunk.map[course.point.row][course.point.col] = 'L';
        //System.out.println(this.chunksToString());
    }

    private void placeArrow(Course course){
        switch (course.direction){
            case LEFT:
                course.chunk.map[course.point.row][course.point.col] = '<';
                break;
            case RIGHT:
                course.chunk.map[course.point.row][course.point.col] = '>';
                break;
            case UP:
                course.chunk.map[course.point.row][course.point.col] = '^';
                break;
            case DOWN:
                course.chunk.map[course.point.row][course.point.col] = 'v';
                break;
        }

    }

    public Course nextCourse(Course course){
        boolean mappingFound = false;
        CubeChunk nextChunk = null;
        Edge nextEdge = null;

        Edge exitingEdge = translateToEdge(course.direction);
        for(EdgeLink edgeLink: edgeLinks){
            if(!edgeLink.hasMappingFor(course.chunk, exitingEdge)){
                continue;
            }
            //[from mapping] on the left
            if(edgeLink.edge1 == exitingEdge && edgeLink.chunk1.equals(course.chunk)){
                mappingFound = true;
                nextChunk = edgeLink.chunk2;
                nextEdge = edgeLink.edge2;
                break;
            }
            //[from mapping] on the right
            if(edgeLink.edge2 == exitingEdge && edgeLink.chunk2.equals(course.chunk)){
                mappingFound = true;
                nextChunk = edgeLink.chunk1;
                nextEdge = edgeLink.edge1;
                break;
            }
            throw new IllegalStateException("Unreachable");
        }
        if(!mappingFound){
            System.out.println("Exiting edge: " + exitingEdge);
            System.out.println("CubeChunk: ");
            System.out.println(course.chunk);
            throw new IllegalStateException("No mapping found");
        }

        //CODE:
        Direction nextDir = decideNextDirection(nextEdge);
        Point point = nextPoint(course.point, exitingEdge, nextEdge);

        return new Course(point, nextChunk, nextDir);
    }

    private Point nextPoint(Point curr, Edge exitingEdge, Edge nextEdge){
        int chunkSize = Chunk.chunkSize();
        switch (exitingEdge){
            case TOP:
                switch (nextEdge){
                    case TOP:
                        return new Point(0, chunkSize - curr.col - 1);
                    case BOTTOM:
                        return new Point(chunkSize-1, curr.col);
                    case LEFT:
                        return new Point(curr.col, 0);
                    case RIGHT:
                        return new Point(chunkSize - curr.col - 1,chunkSize-1);
                }
                break;
            case LEFT:
                switch (nextEdge){
                    case TOP:
                        return new Point(0, curr.row);
                    case BOTTOM:
                        return new Point(chunkSize-1, chunkSize - curr.row - 1);
                    case LEFT:
                        return new Point(chunkSize - 1 - curr.row, 0);
                    case RIGHT:
                        return new Point(curr.row, chunkSize-1);
                }
                break;
            case RIGHT:
                switch (nextEdge){
                    case TOP:
                        return new Point(0, chunkSize - 1 - curr.row);
                    case BOTTOM:
                        return new Point(chunkSize - 1, curr.row);
                    case LEFT:
                        return new Point(curr.row, 0);
                    case RIGHT:
                        return new Point(chunkSize - 1 - curr.row, chunkSize - 1);
                }
                break;
            case BOTTOM:
                switch (nextEdge){
                    case TOP:
                        return new Point(0, curr.col);
                    case BOTTOM:
                        return new Point(chunkSize - 1, chunkSize - 1 - curr.col);
                    case LEFT:
                        return new Point(chunkSize - 1 - curr.col, 0);
                    case RIGHT:
                        return new Point(curr.col, chunkSize - 1);
                }
                break;
        }
        throw new IllegalStateException("Unreachable");
    }

    private Direction decideNextDirection(Edge nextEdge){
        switch (nextEdge){
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            case TOP:
                return Direction.DOWN;
            case BOTTOM:
                return Direction.UP;
        }
        throw new IllegalStateException("Unreachable");
    }

    public static Edge translateToEdge(Direction dir){
        switch (dir){
            case UP:
                return Edge.TOP;
            case DOWN:
                return Edge.BOTTOM;
            case LEFT:
                return Edge.LEFT;
            case RIGHT:
                return Edge.RIGHT;
        }
        throw new IllegalStateException("Unreachable");
    }
    public static Direction translateToDir(Edge edge){
        switch (edge){
            case TOP:
                return Direction.UP;
            case BOTTOM:
                return Direction.DOWN;
            case LEFT:
                return Direction.LEFT;
            case RIGHT:
                return Direction.RIGHT;
        }
        throw new IllegalStateException("Unreachable");
    }
    public char[][] rotateRight(char[][] original){
        int rows = original.length, cols = original[0].length;
        char[][] arr = new char[rows][cols];
        for (int row = 0; row < rows; row++){
            for (int col = 0, r = 0; col < cols; col++, r++){
                arr[r][cols-1-row] = original[row][col];
            }
        }
        return arr;
    }
    public void rotateLeft(char[][] original){
        int rows = original.length, cols = original[0].length;
        char[][] arr = new char[rows][cols];
    }
    public String chunksToString(){
        StringBuilder str = new StringBuilder();
        int size = Chunk.chunkSize();
        for (Chunk[] rowOfChunks : chunks){
            for (int i = 0; i < size; i++){
                //prints one line
                for (int col = 0; col < chunks[0].length; col++){
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
}
