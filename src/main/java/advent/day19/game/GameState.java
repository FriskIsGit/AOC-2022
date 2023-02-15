package advent.day19.game;


import advent.day19.Blueprint;
import advent.day19.GeodeRobot;
import advent.day19.ObsidianRobot;
import advent.day19.Robot;

public class GameState{
    //minutes are 1-indexed with minute 24 included
    public static int MAX_MINUTES = 24;
    public Blueprint blueprint;
    public int minute = 1;
    public int ore = 0, clay = 0, obsidian = 0, geodes = 0;
    public int oreRobots = 1, clayRobots = 0, obsidianRobots = 0, geodeRobots = 0;
    public boolean orderedOreBot = false, orderedClayBot = false, orderedObsidianBot = false, orderedGeodeBot = false;
    public boolean canBuy = true;

    public GameState(Blueprint blueprint){
        this.blueprint = blueprint;
    }

    //returns false if time is up
    public boolean nextMinute(){
        if(minute > MAX_MINUTES){
            return false;
        }
        collect();
        finishBuilds();
        minute++;
        canBuy = true;
        return true;
    }

    private void finishBuilds(){
        if(orderedOreBot){
            oreRobots++;
            orderedOreBot = false;
        }

        if(orderedClayBot){
            clayRobots++;
            orderedClayBot = false;
        }
        if(orderedObsidianBot){
            obsidianRobots++;
            orderedObsidianBot = false;
        }
        if(orderedGeodeBot){
            geodeRobots++;
            orderedGeodeBot = false;
        }
    }

    public boolean collect(){
        canBuy = false;
        ore += oreRobots;
        clay += clayRobots;
        obsidian += obsidianRobots;
        geodes += geodeRobots;
        return true;
    }
    public boolean makeOreRobot(){
        if(!canBuy)
            return false;

        int cost = blueprint.oreRobotCost;
        if(ore < cost){
            return false;
        }
        ore -= cost;
        orderedOreBot = true;
        canBuy = false;
        return true;
    }
    public boolean makeClayRobot(){
        if(!canBuy)
            return false;

        int cost = blueprint.clayRobotCost;
        if(ore < cost){
            return false;
        }
        ore -= cost;
        orderedClayBot = true;
        canBuy = false;
        return true;
    }
    public boolean makeObsidianRobot(){
        if(!canBuy)
            return false;

        ObsidianRobot bot = blueprint.obsidianRobotCost;
        if(ore < bot.ore || clay < bot.clay){
            return false;
        }
        ore  -= bot.ore;
        clay -= bot.clay;
        orderedObsidianBot = true;
        canBuy = false;
        return true;
    }
    public boolean makeGeodeRobot(){
        if(!canBuy)
            return false;

        GeodeRobot bot = blueprint.geodeRobotCost;
        if(ore < bot.ore || obsidian < bot.obsidian){
            return false;
        }
        ore -= bot.ore;
        obsidian -= bot.obsidian;
        orderedGeodeBot = true;
        canBuy = false;
        return true;
    }
    public int nextRobot(int required, int stock, int prod){
        double fraction = (double) (required - stock) / prod;
        int next = (int) (this.minute + Math.ceil(fraction));
        return Math.max(this.minute, next);
    }
    public int nextRobot(Robot robot){
        switch (robot){
            case ORE:
                return nextRobot(blueprint.oreRobotCost, this.ore, this.oreRobots);
            case CLAY:
                return nextRobot(blueprint.clayRobotCost, this.ore, this.oreRobots);
            case OBSIDIAN:
                int oreMinute = nextRobot(blueprint.obsidianRobotCost.ore, this.ore, this.oreRobots);
                int otherMinute = nextRobot(blueprint.obsidianRobotCost.clay, this.clay, this.clayRobots);
                return Math.max(oreMinute, otherMinute);
            case GEODE:
                oreMinute = nextRobot(blueprint.geodeRobotCost.ore, this.ore, this.oreRobots);
                otherMinute = nextRobot(blueprint.geodeRobotCost.obsidian, this.obsidian, this.obsidianRobots);
                return Math.max(oreMinute, otherMinute);
            default:
                throw new Error("Unsupported robot");
        }
    }
}
