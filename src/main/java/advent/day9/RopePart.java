package advent.day9;

public class RopePart extends Point{
    public Point prvs;
    //back?
    public RopePart(int x, int y){
        super(x, y);
        prvs = new Point();
    }
    public void setPrevious(int x, int y){
        this.prvs.x = x;
        this.prvs.y = y;
    }
    public void setPrvs(Point previous){
        this.prvs.x = previous.x;
        this.prvs.y = previous.y;
    }
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setXY(Point previous){
        this.x = previous.x;
        this.y = previous.y;
    }
}
