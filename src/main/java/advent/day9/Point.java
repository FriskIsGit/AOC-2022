package advent.day9;

public class Point{
    public int row, col;

    public Point(int row, int col){
        this.row = row;
        this.col = col;
    }

    public boolean equals(Point p){
        return this.row == p.row && this.col == p.col;
    }
    public Point(){
    }

    @Override
    public String toString(){
        return "[" + row + ", " + col + "]";
    }
}
