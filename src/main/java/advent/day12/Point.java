package advent.day12;

public class Point{
    public int row, col;
    public Point(int row, int col){
        this.row = row;
        this.col = col;
    }
    public boolean equals(Point point){
        return this.row == point.row && this.col == point.col;
    }
    public boolean equals(int row, int col){
        return this.row == row && this.col == col;
    }

    //perfect uniqueness if row <= Short.MAX_VALUE*2 && col <= Short.MAX_VALUE*2
    public int hashCode(){
        return row << 16 | col;
    }
    public int largeHashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override
    public String toString(){
        return "[" + this.row + ", " + this.col + "]";
    }
}
