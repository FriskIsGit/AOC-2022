package advent.day16;

import advent.AdventOfCode;

import java.util.*;

public class Day16{
    public static final int NAME_OFFSET = 6;
    public static final int FLOW_OFFSET = 23;
    public static final int VALVE_NAMES_OFFSET = 47;


    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDay(16);
        //List<String> lines = AdventOfCode.readDummy(16);
        List<Valve> allValves = parseValves(lines);

        String start = "AA";
        Graph graph = new Graph(start, allValves);

        int maxPressure = graph.findMaxPressureAlone();

        System.out.println("Major valves: " + graph.majorValves.size());
        System.out.println("Max pressure part1: " + maxPressure);

        int maxPressure2 = graph.findMaxPressureWithElephant();
        System.out.println("Max pressure part2: " + maxPressure2);


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
