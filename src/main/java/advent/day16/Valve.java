package advent.day16;

import java.util.ArrayList;
import java.util.List;

public class Valve{
    public static final int MAX_MINUTES = 30;
    public static final int MAX_MINUTES_ELEPHANT = 26;
    public String name;
    public int flowRate;
    public int index = -1;
    public boolean isMajor;
    public List<Valve> valves = new ArrayList<>();

    public Valve(String name, int flowRate){
        this.name = name;
        this.flowRate = flowRate;
        this.isMajor = flowRate > 0;
    }
    public Valve(String name, int flowRate, int index){
        this(name, flowRate);
        this.index = index;
    }
    public static int getPressure(Valve valve, int minute){
        return valve.flowRate * minute;
    }
    public int getPressure(int minute){
        return this.flowRate * (MAX_MINUTES - minute);
    }
    public int getPressureElephant(int minute){
        return this.flowRate * (MAX_MINUTES_ELEPHANT - minute);
    }
    public boolean equals(Valve other){
        return this.name.equals(other.name);
    }
    public boolean equals(String other){
        return this.name.equals(other);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append('[').append(name).append(':').append(flowRate).append(" -> ");
        for (int i = 0; i < valves.size(); i++){
            str.append(valves.get(i).name);
            if(i != valves.size()-1){
                str.append(", ");
            }
        }
        str.append(']');
        return str.toString();
    }
}
