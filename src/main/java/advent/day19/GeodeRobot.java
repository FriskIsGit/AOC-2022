package advent.day19;

public class GeodeRobot{
    public int ore, obsidian;

    public GeodeRobot(int ore, int obsidian){
        this.ore = ore;
        this.obsidian = obsidian;
    }

    @Override
    public String toString(){
        return "GeodeRobot{" +
                "oreCost=" + ore +
                ", obsidianCost=" + obsidian +
                '}';
    }
}