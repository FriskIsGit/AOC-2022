package advent.day16;

public class Connection{
    public Valve valve;
    public int travelTime;

    public Connection(Valve valve, int travelTime){
        this.valve = valve;
        this.travelTime = travelTime;
    }
    public Connection(Valve valve){
        this.valve = valve;
        this.travelTime = Integer.MAX_VALUE;
    }

    @Override
    public String toString(){
        return "Connection{" +
                 valve.name +
                ", travelTime=" + travelTime +
                '}';
    }
}
