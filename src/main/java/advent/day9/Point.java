package advent.day9;

public class Point{
    public int x,y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(){
    }

    @Override
    public String toString(){
        return "x:" + x + ";y:" + y;
    }
}
