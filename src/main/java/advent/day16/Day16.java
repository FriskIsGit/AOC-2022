package advent.day16;

import advent.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day16{
    public static int calls = 0;
    public static final int FLOW_RATE_THRESHOLD = 4;
    //this variable is required to get the right answer for dummy input (1651 instead of 1650)
    //but ruins overall performance
    public static final boolean SKIP_UNOPENED = false;
    public static int NAME_OFFSET = 6;
    public static int FLOW_OFFSET = 23;
    public static int VALVE_NAMES_OFFSET = 47;


    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDay(16);
        List<Valve> allValves = parseValves(lines);
        part1(allValves);
    }


    private static void part2(){
        List<String> lines = AdventOfCode.readDummy(16);
        List<Valve> allValves = parseValves(lines);
        final String startingValveName = "AA";
        Valve startingValve = null;
        int majorValves = 0;
        for(Valve valve : allValves){
            if(valve.isMajor){
                majorValves++;
            }
            if(valve.equals(startingValveName)){
                startingValve = valve;
            }
        }
        System.out.println("majorValves: " + majorValves);
        assert(startingValve != null);
        long st = System.currentTimeMillis();

        int maxPressure = findMaximumPressure(startingValve, majorValves);
        System.out.println("Maximum pressure: " + maxPressure);

        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Calls to explore: " + calls);
    }

    //2080-2205 range for input16
    private static void part1(List<Valve> allValves){

        final String startingValveName = "AA";
        Valve startingValve = null;
        int majorValves = 0;
        for(Valve valve : allValves){
            if(valve.isMajor){
                majorValves++;
            }
            if(valve.equals(startingValveName)){
                startingValve = valve;
            }
        }
        System.out.println("majorValves: " + majorValves);
        assert(startingValve != null);
        if(SKIP_UNOPENED){
            System.out.println("Launched with skips enabled, will take significantly longer to execute");
        }
        long st = System.currentTimeMillis();

        int maxPressure = findMaximumPressure(startingValve, majorValves);
        System.out.println("Maximum pressure: " + maxPressure);

        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Calls to explore: " + calls);
    }
    public static boolean[] copyOf(boolean[] srcArr){
        boolean[] newArr = new boolean[srcArr.length];
        System.arraycopy(srcArr, 0, newArr, 0, srcArr.length);
        return newArr;
    }

    public static int findMaximumPressure(Valve startingValve, int majorValves){
        //starting node is not a major one and has no origin
        if(startingValve.flowRate > 0){
            throw new IllegalStateException("Unexpected flowRate of the starting node");
        }
        int max = 0;

        for(Valve connection : startingValve.valves){
            int pressure = explore(connection, 2, startingValve, new boolean[majorValves]);
            max = Math.max(pressure, max);
        }
        return max;
    }

    private static int explore(Valve valve, int minute, Valve origin, boolean[] opened){
        calls++;
        if(minute >= Valve.MAX_MINUTES){
            return 0;
            //nothing can be done anymore
        }
        int valvePressure = valve.getPressure(minute);

        int maxNodePressure = 0;
        for(Valve con : valve.valves){
            if(valve.isMajor){
                //if is opened - always go to connection (previous mistake, not acknowledging the state after the call)
                boolean[] openedCopy1 = copyOf(opened);
                if(openedCopy1[valve.index]){
                    int tempPressure = explore(con, minute+1, valve, openedCopy1);
                    maxNodePressure = Math.max(tempPressure, maxNodePressure);
                }
                else{
                    //closed but skip (only relevant to dummy input)
                    if(SKIP_UNOPENED && valve.flowRate < FLOW_RATE_THRESHOLD){
                        int tempPressure = explore(con, minute+1, valve, copyOf(opened));
                        maxNodePressure = Math.max(tempPressure, maxNodePressure);
                    }
                    //closed - open valve
                    boolean[] openedCopy2 = copyOf(opened);
                    openedCopy2[valve.index] = true;
                    int tempPressure = explore(con, minute+2, valve, openedCopy2) + valvePressure;
                    maxNodePressure = Math.max(tempPressure, maxNodePressure);
                }
            }else if(!con.equals(origin)){
                // go forward
                int tempPressure = explore(con, minute+1, valve, copyOf(opened));
                maxNodePressure = Math.max(tempPressure, maxNodePressure);
            }
        }
        return maxNodePressure;
    }

    public static List<Valve> parseValves(List<String> lines){
        List<Valve> allValves = new ArrayList<>();
        int index = 0;
        for(String line : lines){
            String name = line.substring(NAME_OFFSET, NAME_OFFSET + 2);
            String flowStr = line.substring(FLOW_OFFSET, line.indexOf( ';', FLOW_OFFSET));
            Valve v = new Valve(name, Integer.parseInt(flowStr));
            if(v.isMajor){
                v.index = index++;
            }
            allValves.add(v);
        }
        int valves = allValves.size();
        if(valves != lines.size()){
            throw new IllegalStateException("Sizes differ");
        }
        for (int v = 0; v < valves; v++){
            String line = lines.get(v);
            Valve valve = allValves.get(v);
            int whitespace = line.indexOf(' ', VALVE_NAMES_OFFSET);
            for (int i = whitespace+1; i < line.length(); i+=4){
                String aName = line.substring(i, i+2);
                for (int j = 0; j < valves; j++){
                    if(j != v){
                        Valve connectedValve = allValves.get(j);
                        if(connectedValve.name.equals(aName)){
                            valve.valves.add(connectedValve);
                            break;
                        }
                    }
                }
            }
        }
        return allValves;
    }
}
