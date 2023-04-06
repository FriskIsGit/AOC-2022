package advent.day22;

public class Point{
    public int row, col;

    public Point(int row, int col){
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString(){
        return "[" + row + ", " + col + "]";
    }
}
