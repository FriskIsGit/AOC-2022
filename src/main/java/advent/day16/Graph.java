package advent.day16;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Graph{
    public long calls = 0;
    public static final int FLOW_RATE_THRESHOLD = 4;
    //this variable is required to get the right answer for dummy input (1651 instead of 1650)
    //but ruins overall performance
    public static final boolean SKIP_UNOPENED = false;
    public List<Connection> startingConnections;
    public List<Valve> allValves, majorValves;
    public Valve startingValve;

    public Graph(String start, List<Valve> allValves){
        this.allValves = allValves;
        startingValve = allValves.stream().filter(v -> v.equals(start)).findFirst().orElse(null);
        if(startingValve == null){
            throw new NullPointerException(start + " valve does not exist");
        }
        startingConnections = findInitialMajorValves();
        majorValves = simplifyGraph();
    }

    //algorithm will be run starting from these valves
    public List<Connection> findInitialMajorValves(){
        if(startingValve.isMajor){
            return Collections.singletonList(new Connection(startingValve, 0));
        }
        List<Connection> majorValves = new ArrayList<>();
        for(Connection con : startingValve.connections){
            findMajorConnections(con.valve, 1, startingValve, majorValves);
        }
        return majorValves;
    }

    private void findMajorConnections(Valve current, int distance, Valve previous, List<Connection> majorValves){
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
    public List<Valve> simplifyGraph(){
        List<Valve> majorValves = new ArrayList<>();
        for(Valve valve : allValves){
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

    public static boolean[] copyOf(boolean[] srcArr){
        boolean[] newArr = new boolean[srcArr.length];
        System.arraycopy(srcArr, 0, newArr, 0, srcArr.length);
        return newArr;
    }

    //develop some form of repetition detection, overall performance can be improved by approx  x140
    private int explore(Valve current, int minute, boolean[] opened){
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

    private int explore2(Valve current, int minute, boolean[] opened, boolean isElephant){
        calls++;
        if(minute >= Valve.MAX_MINUTES_ELEPHANT){
            if(!isElephant){
                int max = 0;
                //done - dispatch elephant
                for(Connection connection : startingConnections){
                    //minutes are 1-indexed
                    int pressure = explore2(connection.valve, connection.travelTime + 1, copyOf(opened), true);
                    max = Math.max(pressure, max);
                }
                return max;
            }else{
                return 0;
            }
            //nothing can be done anymore
        }
        int valvePressure = current.getPressure(minute);

        int maxNodePressure = 0;
        for(Connection connection : current.connections){
            Valve con = connection.valve;
            //if is opened - always go to connection (previous mistake, not acknowledging the state after the call)
            boolean[] openedCopy1 = copyOf(opened);
            if(openedCopy1[current.index]){
                int tempPressure = explore2(con, minute + connection.travelTime, openedCopy1, isElephant);
                maxNodePressure = Math.max(tempPressure, maxNodePressure);
            }
            else{
                //closed but skip (only relevant to dummy input)
                if(SKIP_UNOPENED && current.flowRate < FLOW_RATE_THRESHOLD){
                    int tempPressure = explore2(con, minute + connection.travelTime, copyOf(opened), isElephant);
                    maxNodePressure = Math.max(tempPressure, maxNodePressure);
                }
                //closed - open valve
                boolean[] openedCopy2 = copyOf(opened);
                openedCopy2[current.index] = true;
                int tempPressure = explore2(con, minute + connection.travelTime + 1, openedCopy2, isElephant) + valvePressure;
                maxNodePressure = Math.max(tempPressure, maxNodePressure);
            }
        }
        return maxNodePressure;
    }
    //part1
    public int findMaxPressureAlone(){
        calls = 0;
        if(SKIP_UNOPENED){
            System.out.println("Launched with skips enabled, will take significantly longer to execute");
        }
        int majors = majorValves.size();
        long st = System.currentTimeMillis();

        int max = 0;
        for(Connection connection : startingConnections){
            //minutes are 1-indexed
            int pressure = explore(connection.valve, connection.travelTime + 1, new boolean[majors]);
            max = Math.max(pressure, max);
        }

        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Calls to explore: " + calls);
        return max;
    }

    public int findMaxPressureWithElephant(){
        calls = 0;
        if(SKIP_UNOPENED){
            System.out.println("Launched with skips enabled, will take significantly longer to execute");
        }
        int majors = majorValves.size();
        long st = System.currentTimeMillis();

        int max = 0;
        for(Connection connection : startingConnections){
            //minutes are 1-indexed
            int pressure = explore2(connection.valve, connection.travelTime + 1, new boolean[majors], false);
            max = Math.max(pressure, max);
        }
        System.out.println("Maximum pressure: " + max);

        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Calls to explore: " + calls);
        return max;
    }
}
