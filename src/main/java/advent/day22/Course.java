package advent.day22;

import advent.day22.enums.Direction;

//used as a wrapper for the return values to define the change of movement after entering a new chunk
public class Course{
    public Point point;
    public CubeChunk chunk;
    //chunk-wise direction
    public Direction direction;

    public Course(){
    }

    public Course(Point point, CubeChunk chunk, Direction direction){
        this.point = point;
        this.chunk = chunk;
        this.direction = direction;
    }
}
