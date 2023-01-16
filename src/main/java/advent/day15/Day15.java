package advent.day15;

import advent.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day15{
    public static final int Y_CONSTANT = 2_000_000;
    public static final int Y_CONSTANT_DUMMY = 10;
    public static long startMs;
    public static final int DUMMY_CONSTANT = 10;
    public static void main(String[] args){
        part1();
        part2();
    }

    private static void part1(){
        List<String> lines = AdventOfCode.readDay(15);

        List<Sensor> sensors = parseSensors(lines);
        long st = System.currentTimeMillis();
        ExclusionZone.initializeBeacons(sensors);
        int notBeacons = ExclusionZone.numberOfNonBeacons(sensors, Y_CONSTANT);
        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Number of positions where beacon cannot be: " + notBeacons);
    }
    private static void part2(){
        List<String> lines = AdventOfCode.readDay(15);

        List<Sensor> sensors = parseSensors(lines);

        ExclusionZone.initializeBeacons(sensors);
        int tilesToCheck = 0;
        for(Sensor sensor : sensors){
            System.out.println(sensor);
            tilesToCheck += sensor.outsideTilesCount();
        }
        System.out.println("Tiles to check: " + tilesToCheck);
        startMs = System.currentTimeMillis();
        ExclusionZone.findDistressBeacon(sensors, Sensor.MAX_COORDINATE);
    }

    private static List<Sensor> parseSensors(List<String> lines){
        List<Sensor> sensors = new ArrayList<>();
        for(String line : lines){
            Sensor sensor = Sensor.parsePositions(line);
            sensors.add(sensor);
        }
        return sensors;
    }
}
