package advent.day15;

public class Sensor{
    enum Signal{
        TOUCHING, OVERLAPPING, SEPARATED
    }
    public static final int MAX_COORDINATE = 4000000;
    public static final int MAX_COORDINATE_DUMMY = 20;
    public static final int MIN_COORDINATE = 0;
    private static final String X_PREFIX = "x=", Y_PREFIX = "y=";
    //y is row, x is col
    //0-indexed
    public int row, col, beaconRow, beaconCol;
    public int beaconDist;
    private Sensor(){

    }
    public Sensor(int x, int y, int beaconX, int beaconY){
        this.row = y;
        this.col = x;
        this.beaconRow = beaconY;
        this.beaconCol = beaconX;
    }
    public static Sensor parsePositions(String coordinates){
        Sensor sensor = new Sensor();
        int xIndex = coordinates.indexOf(X_PREFIX) + X_PREFIX.length();
        int yIndex = coordinates.indexOf(Y_PREFIX) + Y_PREFIX.length();
        int comma = coordinates.indexOf(',', xIndex);
        int onlyColon = coordinates.indexOf(':', yIndex);

        sensor.col = Integer.parseInt(coordinates.substring(xIndex, comma));
        sensor.row = Integer.parseInt(coordinates.substring(yIndex, onlyColon));

        xIndex = coordinates.indexOf(X_PREFIX, xIndex) + X_PREFIX.length();
        yIndex = coordinates.indexOf(Y_PREFIX, yIndex) + Y_PREFIX.length();
        comma = coordinates.indexOf(',', xIndex);

        sensor.beaconCol = Integer.parseInt(coordinates.substring(xIndex, comma));
        sensor.beaconRow = Integer.parseInt(coordinates.substring(yIndex));
        sensor.beaconDist = distance(sensor.beaconRow, sensor.beaconCol, sensor.row, sensor.col);
        return sensor;
    }
    public String toString(){
        return "Sensor at [" + row + ", " + col + ']' +
                ": closest beacon at [" + beaconRow + ", " + beaconCol + ']';
    }
    //(There is never a tie where two beacons are the same distance to a sensor.)
    public int distanceToBeacon(){
        return beaconDist;
    }
    public static long tuningFrequency(int beaconRow, int beaconCol){
        return (long) beaconCol * MAX_COORDINATE + beaconRow;
    }
    public int distance(Sensor otherSensor){
        return Math.abs(row - otherSensor.row) + Math.abs(col - otherSensor.col);
    }
    public static int distance(int row, int col, int row2, int col2){
        return Math.abs(row - row2) + Math.abs(col - col2);
    }
    public boolean equals(int r, int col){
        return this.row == r && this.col == col;
    }

    public boolean isWithinSensor(int r, int c){
        return distance(this.row, this.col, r, c) == distanceToBeacon();
    }
    public int outsideTilesCount(){
        return (distanceToBeacon() + 1) * 4;
    }
    public static Signal signal(Sensor s1, Sensor s2){
        int distance = s1.distance(s2) -1;
        int r1 = s1.distanceToBeacon();
        int r2 = s2.distanceToBeacon();
        int rSum = r1+r2;
        if(distance == rSum){
            return Signal.TOUCHING;
        }
        else if(distance > rSum){
            return Signal.SEPARATED;
        }else{
            return Signal.OVERLAPPING;
        }
    }
}
