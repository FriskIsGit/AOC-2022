package advent.day16;

import advent.AdventOfCode;

import java.util.*;

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
        //List<String> lines = AdventOfCode.readDummy(16);
        List<Valve> allValves = parseValves(lines);

        String start = "AA";
        Valve startingValve = allValves.stream().filter(v -> v.equals(start)).findFirst().orElse(null);
        if(startingValve == null){
            throw new NullPointerException(start + " valve does not exist");
        }
        List<Connection> startingConnections = findInitialMajorValves(startingValve);

        long st = System.currentTimeMillis();
        List<Valve> majorValves = simplifyGraph(allValves);
        long en = System.currentTimeMillis();

        System.out.println("Time taken to simplify graph: " + (en-st));
        int majors = majorValves.size();
        System.out.println("Major valves: " + majors);
        part1(startingConnections, majors);
    }

    private static void part1(List<Connection> startingConnections, int majorValves){
        if(SKIP_UNOPENED){
            System.out.println("Launched with skips enabled, will take significantly longer to execute");
        }
        long st = System.currentTimeMillis();

        int max = 0;
        for(Connection connection : startingConnections){
            //minutes are 1-indexed
            int pressure = explore(connection.valve, connection.travelTime + 1, new boolean[majorValves]);
            max = Math.max(pressure, max);
        }
        System.out.println("Maximum pressure: " + max);

        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Calls to explore: " + calls);
    }

    //algorithm will be run starting from these valves
    public static List<Connection> findInitialMajorValves(Valve startingValve){
        if(startingValve.isMajor){
            return Collections.singletonList(new Connection(startingValve, 0));
        }
        List<Connection> majorValves = new ArrayList<>();
        for(Connection con : startingValve.connections){
            findMajorConnections(con.valve, 1, startingValve, majorValves);
        }
        return majorValves;
    }

    private static void findMajorConnections(Valve current, int distance, Valve previous, List<Connection> majorValves){
        if(current.isMajor){
            majorValves.add(new Connection(current, distance));
            return;
        }
        for(Connection c : current.connections){
            //don't go back where you came from
            if(c.valve == previous){
                continue;
            }
            findMajorConnections(c.valve, distance+1, current, majorValves);
        }
    }

    public static List<Valve> simplifyGraph(List<Valve> valves){
        List<Valve> majorValves = new ArrayList<>();
        for(Valve valve : valves){
            if(valve.isMajor){
                List<Connection> cons = valve.connections;
                int initialSize = cons.size();
                //ConcurrentModificationException will be thrown if for each loop is used
                for(int i = 0; i<initialSize; i++){
                    Connection con = cons.get(i);
                    setMajorConnections(con.valve, valve, 1, valve);
                }
                //remove no longer valid connections
                valve.connections.removeIf(con -> !con.valve.isMajor);
                majorValves.add(valve);
            }
        }
        return majorValves;
    }

    private static void setMajorConnections(Valve current, Valve previous, int distance, Valve dispatchingValve){
        if(current.isMajor){
            //don't duplicate connections as immediate ones should have been added during parsing
            if(distance == 1){
                return;
            }
            //check if there's a better connection for this exact valve or any at all
            List<Connection> connections = dispatchingValve.connections;
            Iterator<Connection> it = connections.iterator();
            boolean deleted = false, matchExists = false;
            while(it.hasNext()){
                Connection c = it.next();
                if(c.valve == current){
                    matchExists = true;
                    if(c.travelTime > distance){
                        it.remove();
                        deleted = true;
                        break;
                    }
                }
            }
            if(!matchExists || deleted){
                connections.add(new Connection(current, distance));
            }

            return;
        }
        for(Connection c : current.connections){
            //don't go back where you came from
            if(c.valve == previous){
                continue;
            }
            setMajorConnections(c.valve, current, distance+1, dispatchingValve);
        }
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

        //int maxPressure = findMaximumPressure(startingValve, majorValves);
        //System.out.println("Maximum pressure: " + maxPressure);

        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Calls to explore: " + calls);
    }

    public static boolean[] copyOf(boolean[] srcArr){
        boolean[] newArr = new boolean[srcArr.length];
        System.arraycopy(srcArr, 0, newArr, 0, srcArr.length);
        return newArr;
    }


    private static int explore(Valve current, int minute, boolean[] opened){
        calls++;
        if(minute >= Valve.MAX_MINUTES){
            return 0;
            //nothing can be done anymore
        }
        int valvePressure = current.getPressure(minute);

        int maxNodePressure = 0;
        for(Connection connection : current.connections){
            Valve con = connection.valve;
            //if is opened - always go to connection (previous mistake, not acknowledging the state after the call)
            boolean[] openedCopy1 = copyOf(opened);
            if(openedCopy1[current.index]){
                int tempPressure = explore(con, minute + connection.travelTime, openedCopy1);
                maxNodePressure = Math.max(tempPressure, maxNodePressure);
            }
            else{
                //closed but skip (only relevant to dummy input)
                if(SKIP_UNOPENED && current.flowRate < FLOW_RATE_THRESHOLD){
                    int tempPressure = explore(con, minute + connection.travelTime, copyOf(opened));
                    maxNodePressure = Math.max(tempPressure, maxNodePressure);
                }
                //closed - open valve
                boolean[] openedCopy2 = copyOf(opened);
                openedCopy2[current.index] = true;
                int tempPressure = explore(con, minute + connection.travelTime + 1, openedCopy2) + valvePressure;
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
                String conName = line.substring(i, i+2);
                for (int j = 0; j < valves; j++){
                    if(j != v){
                        Valve connectedValve = allValves.get(j);
                        if(connectedValve.name.equals(conName)){
                            valve.connections.add(new Connection(connectedValve, 1));
                            break;
                        }
                    }
                }
            }
        }
        return allValves;
    }
}
