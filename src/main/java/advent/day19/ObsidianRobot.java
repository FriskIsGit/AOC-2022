package advent.day19;

public class ObsidianRobot{
    public int ore, clay;

    public ObsidianRobot(int ore, int clay){
        this.ore = ore;
        this.clay = clay;
    }

    @Override
    public String toString(){
        return "ObsidianRobot{" +
                "oreCost=" + ore +
                ", clayCost=" + clay +
                '}';
    }
}
