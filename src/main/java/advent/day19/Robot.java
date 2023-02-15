package advent.day19;

public enum Robot{
    ORE(1), CLAY(2), OBSIDIAN(3), GEODE(4);

    public final int id;
    Robot(int id){
        this.id = id;
    }

    public boolean isLowerTierThan(Robot bot){
        return this.id < bot.id;
    }

}
