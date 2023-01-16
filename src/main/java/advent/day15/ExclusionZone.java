package advent.day15;

import java.util.*;

public class ExclusionZone{

    enum Relative{
        TOP_LEFT, TOP, TOP_RIGHT,
        LEFT, MID, RIGHT,
        BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT,
    }

    public static HashSet<Integer> beaconHashes = new HashSet<>();

    public static boolean isBeacon(int row, int col){
        int hash = row*col;
        return beaconHashes.contains(hash);
    }

    private ExclusionZone(){
    }

    public static void initializeBeacons(List<Sensor> sensorList){
        for(Sensor sensor : sensorList){
            beaconHashes.add(sensor.beaconRow * sensor.beaconCol);
        }
    }
    public int numberOfNonBeaconsBruteForce(List<Sensor> sensors, int row){
        Set<Integer> columns = new HashSet<>();
        for(Sensor sensor : sensors){
            int radius = sensor.distanceToBeacon();
            int rowOffset = Math.abs(row - sensor.row);
            int colStart = sensor.col - radius;
            int colEnd = sensor.col + radius;
            colStart += rowOffset;
            colEnd   -= rowOffset;
            for (int c = colStart; c<=colEnd; c++){
                if(!isBeacon(row, c)){
                    columns.add(c);
                }
            }
        }
        return columns.size();
    }
    //y
    public static int numberOfNonBeacons(List<Sensor> sensors, int row){
        HashSet<Integer> alreadyExcluded = new HashSet<>();
        List<ColumnRange> ranges = new ArrayList<>();
        for(Sensor sensor : sensors){
            int radius = sensor.distanceToBeacon();
            int rowOffset = Math.abs(row - sensor.row);
            int colStart = sensor.col - radius;
            int colEnd = sensor.col + radius;
            colStart += rowOffset;
            colEnd   -= rowOffset;
            if(colStart > colEnd){
                continue;
            }
            ranges.add(new ColumnRange(colStart, colEnd));
            for(Sensor s : sensors){
                if(row == s.beaconRow){
                    if(colStart <= s.beaconCol && s.beaconCol <= colEnd){
                        //guarantees the same beacon is not counted twice
                        alreadyExcluded.add(s.beaconRow * s.beaconCol);
                    }
                }
            }
        }

        //insanity, convert to array to sort
        ColumnRange[] rangeArr = ranges.toArray(new ColumnRange[0]);
        Arrays.sort(rangeArr, new Comparator<ColumnRange>(){
            @Override
            public int compare(ColumnRange less, ColumnRange more){
                if(less.lower < more.lower){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        //create modifiable list
        ranges = new ArrayList<>(Arrays.asList(rangeArr));
        //merge ranges and remove
        for (int i = 0; i < ranges.size()-1; i++){
            ColumnRange first = ranges.get(i);
            ColumnRange second = ranges.get(i+1);
            if (!first.isSeparated(second)){
                first.merge(second);
                ranges.remove(i+1);
                i--;
            }
        }
        System.out.println(ranges);

        int fullRange = 0;
        for(ColumnRange range : ranges){
            fullRange += range.elements();
        }
        return fullRange - alreadyExcluded.size();
    }
    public static void findDistressBeacon(List<Sensor> sensorList, final int MAX_COORDINATE){
        Sensor[] sensors = toArrayOfSensors(sensorList);
        int size = sensors.length;
        for (int i = 0; i < size-1; i++){
            Sensor currentSensor = sensors[i];

            //sensors which are close enough to be considered in the calculations
            List<Sensor> candidates = new ArrayList<>();
            for (int j = 0; j < size; j++){
                if(i == j){
                    continue;
                }
                Sensor other = sensors[j];
                Sensor.Signal signal = Sensor.signal(currentSensor, other);
                if(signal == Sensor.Signal.SEPARATED){
                    continue;
                }
                candidates.add(other);
            }
            //System.out.println("sensor1:" + sensor1);
            //System.out.println("candidates: " + candidates);
            final Sensor[] sensorsToConsider = toArrayOfSensors(candidates);
            int radius1 = currentSensor.distanceToBeacon();

            //bot -> right -> going up
            Point start = new Point(currentSensor.row + radius1 + 1, currentSensor.col);
            int endColumn = currentSensor.col + radius1 + 1;

            //reduce rows -> then columns (based on diff)
            if(MAX_COORDINATE < start.row){
                int diff = start.row - MAX_COORDINATE;
                start.row = MAX_COORDINATE;
                start.col += diff;
            }
            if(MAX_COORDINATE < endColumn){
                endColumn = MAX_COORDINATE;
            }
            //int outsideTiles = (radius1 + 1) * 4;
            checkCoverageBottomRight(sensorsToConsider, start, endColumn, currentSensor);

            //bot -> left -> going up
            start = new Point(currentSensor.row + radius1 + 1, currentSensor.col);
            endColumn = currentSensor.col - radius1 - 1;

            //reduce rows -> then columns (based on diff)
            if(MAX_COORDINATE < start.row){
                int diff = start.row - MAX_COORDINATE;
                start.row = MAX_COORDINATE;
                start.col -= diff;
            }
            if(endColumn < Sensor.MIN_COORDINATE){
                endColumn = Sensor.MIN_COORDINATE;
            }

            checkCoverageBottomLeft(sensorsToConsider, start, endColumn, currentSensor);

            //top -> right -> going down
            start = new Point(currentSensor.row - radius1 - 1, currentSensor.col);
            endColumn = currentSensor.col + radius1 + 1;
            if(start.row < Sensor.MIN_COORDINATE){
                int diff = Sensor.MIN_COORDINATE - start.row;
                start.row = Sensor.MIN_COORDINATE;
                start.col += diff;

            }
            if(MAX_COORDINATE < endColumn){
                endColumn = MAX_COORDINATE;
            }

            checkCoverageTopRight(sensorsToConsider, start, endColumn, currentSensor);

            //top -> left -> going down
            start = new Point(currentSensor.row - radius1 - 1, currentSensor.col);
            endColumn = currentSensor.col - radius1 - 1;
            if(start.row < Sensor.MIN_COORDINATE){
                int diff = Sensor.MIN_COORDINATE - start.row;
                start.row = Sensor.MIN_COORDINATE;
                start.col -= diff;

            }
            if(endColumn < Sensor.MIN_COORDINATE){
                endColumn = Sensor.MIN_COORDINATE;
            }

            checkCoverageTopLeft(sensorsToConsider, start, endColumn, currentSensor);
        }

    }

    private static void checkCoverageTopLeft(Sensor[] sensorsToConsider, Point start, int endColumn, Sensor sensorFrom){
        //start from top
        for(int row = start.row, col = start.col; endColumn <= col; col--, row++){
            boolean anyCoverage = false;
            for (int s = 0; s < sensorsToConsider.length; s++){
                Sensor aSensor = sensorsToConsider[s];
                boolean isWithin = Sensor.distance(aSensor.row, aSensor.col, row, col) <= aSensor.beaconDist;
                if(isWithin){
                    anyCoverage = true;
                    break;
                }
            }
            if(!anyCoverage){
                System.out.println("Time taken ms: " +(System.currentTimeMillis()-Day15.startMs));
                System.out.println("tuningFrequency: " + Sensor.tuningFrequency(row, col));
                throw new FoundException("We found it?: " + (row + " x " + col) + " for sensor: " + sensorFrom);
            }
        }
    }

    private static void checkCoverageTopRight(Sensor[] sensorsToConsider, Point start, int endColumn, Sensor sensorFrom){
        //start from top
        for(int row = start.row, col = start.col; col<= endColumn; col++, row++){
            boolean anyCoverage = false;
            for (int s = 0; s < sensorsToConsider.length; s++){
                Sensor aSensor = sensorsToConsider[s];
                boolean isWithin = Sensor.distance(aSensor.row, aSensor.col, row, col) <= aSensor.beaconDist;
                if(isWithin){
                    anyCoverage = true;
                    break;
                }
            }
            if(!anyCoverage){
                System.out.println("Time taken ms: " +(System.currentTimeMillis()-Day15.startMs));
                System.out.println("tuningFrequency: " + Sensor.tuningFrequency(row, col));
                throw new FoundException("We found it?: " + (row + " x " + col) + " for sensor: " + sensorFrom);
            }
        }
    }

    private static void checkCoverageBottomLeft(Sensor[] sensorsToConsider, Point start, int endColumn, Sensor sensorFrom){
        //start from bottom
        for(int row = start.row, col = start.col; endColumn <= col; col--, row--){
            boolean anyCoverage = false;
            for (int s = 0; s < sensorsToConsider.length; s++){
                Sensor aSensor = sensorsToConsider[s];
                boolean isWithin = Sensor.distance(aSensor.row, aSensor.col, row, col) <= aSensor.beaconDist;
                if(isWithin){
                    anyCoverage = true;
                    break;
                }
            }
            if(!anyCoverage){
                System.out.println("Time taken ms: " +(System.currentTimeMillis()-Day15.startMs));
                System.out.println("tuningFrequency: " + Sensor.tuningFrequency(row, col));
                throw new FoundException("We found it?: " + (row + " x " + col) + " for sensor: " + sensorFrom);
            }
        }
    }

    //
    private static void checkCoverageBottomRight(Sensor[] sensorsToConsider, final Point startPoint, final int endColumn, Sensor sensorFrom){
        //start from bottom
        for(int row = startPoint.row, col = startPoint.col; col <= endColumn; col++, row--){
            boolean anyCoverage = false;
            for (int s = 0; s < sensorsToConsider.length; s++){
                Sensor aSensor = sensorsToConsider[s];
                boolean isWithin = Sensor.distance(aSensor.row, aSensor.col, row, col) <= aSensor.beaconDist;
                if(isWithin){
                    anyCoverage = true;
                    break;
                }
            }
            if(!anyCoverage){
                System.out.println("Time taken ms: " +(System.currentTimeMillis()-Day15.startMs));
                System.out.println("tuningFrequency: " + Sensor.tuningFrequency(row, col));
                throw new FoundException("We found it?: " + (row + " x " + col) + " for sensor: " + sensorFrom);
            }
        }
    }

    private static Sensor[] toArrayOfSensors(final List<Sensor> sensorList){
        Sensor[] sensors = new Sensor[sensorList.size()];
        for (int i = 0; i < sensorList.size(); i++){
            sensors[i] = sensorList.get(i);
        }
        return sensors;
    }

    //where the first sensor is relative to the second one
    public static Relative relativeTo(Sensor first, Sensor to){
        if(first.row == to.row){
            if(first.col == to.col){
                return Relative.MID;
            }else if(first.col < to.col){
                return Relative.LEFT;
            }else{
                return Relative.RIGHT;
            }
        }
        if(first.col == to.col){
            if(first.row < to.row){
                return Relative.TOP;
            }
            //always true
            return Relative.BOTTOM;
        }
        //at this point only corners are left
        if(to.row < first.row){
            if(to.col < first.col){
                return Relative.BOTTOM_RIGHT;
            }
            else {
                return Relative.BOTTOM_LEFT;
            }
        }else{
            if(to.col < first.col){
                return Relative.TOP_RIGHT;
            }
            else {
                return Relative.TOP_LEFT;
            }
        }
    }
}
