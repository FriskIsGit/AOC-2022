package advent.day12;

public class PathPoint extends Point{
    public static Point END;
    //where fCost = gCost + hCost
    //gCost - distance from START point
    //hCost - distance from END point
    public int fCost = 0, gCost = 0, hCost = 0;
    //origin is where this node came from
    public PathPoint origin;

    public static void setEnd(Point END){
        PathPoint.END = END;
    }
    //this constructor should only be called to set the start point
    public PathPoint(int row, int col){
        super(row, col);
    }
    public PathPoint(int row, int col, PathPoint originPoint){
        super(row, col);
        this.origin = originPoint;
        calculateCosts();
    }

    public void calculateCosts(){
        gCost = pathDistance();
        hCost = distance(row, col, END.row, END.col);
        fCost = gCost + hCost;
    }

    public static int distPrvs(int row1, int col1, int row2, int col2){
        return (row1-row2)*(row1-row2) + (col1-col2)*(col1-col2);
    }
    public static int distance(int row1, int col1, int row2, int col2){
        return Math.abs(col1 - col2) + Math.abs(row1 - row2);
    }
    //distance so far from start
    public int pathDistance(){
        int nodes = 0;
        PathPoint point = this;
        while(point != null){
            point = point.origin;
            nodes++;
        }
        return nodes == 0 ? 0 : nodes-1;
    }
    @Override
    public int hashCode(){
        return super.hashCode();
    }
}
