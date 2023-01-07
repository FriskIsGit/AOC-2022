package advent.day12;

public class PathPoint extends Point{
    public static Point START, END;
    //where fCost = gCost + hCost
    //gCost - distance from START point
    //hCost - distance from END point
    public int fCost, gCost, hCost;
    //origin is where this node came from
    public PathPoint origin;

    public static void initializeContext(Point START, Point END){
        PathPoint.START = START;
        PathPoint.END = END;
    }

    public PathPoint(int row, int col){
        super(row, col);
    }
    public PathPoint(int row, int col, PathPoint originPoint){
        super(row, col);
        this.origin = originPoint;
        initializeCosts();
    }

    private void initializeCosts(){
        //from starting point
        gCost = distance(START.row, START.col, row, col);
        //from end point
        hCost = distance(END.row, END.col, row, col);
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
