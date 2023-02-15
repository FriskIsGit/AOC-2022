package advent.day19;

import netscape.security.UserTarget;

public class State{
    //minutes are 1-indexed with minute 24 included
    public static int MAX_MINUTES = 24, OBSIDIAN_CAP, CLAY_CAP, ORE_CAP;
    static{
        refreshCap();
    }
    public Blueprint blueprint;
    public int minute = 1;
    public int ore = 0, clay = 0, obsidian = 0, geodes = 0;
    public int oreRobots = 1, clayRobots = 0, obsidianRobots = 0, geodeRobots = 0;
    public boolean orderedOreBot = false, orderedClayBot = false, orderedObsidianBot = false, orderedGeodeBot = false;

    public State(Blueprint blueprint){
        this.blueprint = blueprint;
    }

    //returns false if time is up
    //current minute is the very beginning of it
    public boolean nextMinute(){
        if(minute > MAX_MINUTES){
            return false;
        }
        //buy
        collect();
        finishBuilds();
        minute++;
        return true;
    }

    private void finishBuilds(){
        if(orderedOreBot){
            oreRobots++;
            orderedOreBot = false;
        }
        else if(orderedClayBot){
            clayRobots++;
            orderedClayBot = false;
        }
        else if(orderedObsidianBot){
            obsidianRobots++;
            orderedObsidianBot = false;
        }
        else if(orderedGeodeBot){
            geodeRobots++;
            orderedGeodeBot = false;
        }
    }

    public void collect(){
        ore += oreRobots;
        clay += clayRobots;
        obsidian += obsidianRobots;
        geodes += geodeRobots;
    }

    public State copy(){
        State state = new State(this.blueprint);
        state.minute = this.minute;
        state.ore = this.ore;
        state.clay = this.clay;
        state.obsidian = this.obsidian;
        state.geodes = this.geodes;

        state.oreRobots = this.oreRobots;
        state.clayRobots = this.clayRobots;
        state.obsidianRobots = this.obsidianRobots;
        state.geodeRobots = this.geodeRobots;

        state.orderedOreBot = this.orderedOreBot;
        state.orderedClayBot = this.orderedClayBot;
        state.orderedObsidianBot = this.orderedObsidianBot;
        state.orderedGeodeBot = this.orderedGeodeBot;
        return state;
    }

    public boolean canMakeOreRobot(){
        return ore >= blueprint.oreRobotCost;
    }
    public boolean canMakeClayRobot(){
        return ore >= blueprint.clayRobotCost;
    }
    public boolean canMakeObsidianRobot(){
        ObsidianRobot bot = blueprint.obsidianRobotCost;
        return ore >= bot.ore && clay >= bot.clay;
    }
    public boolean canMakeGeodeRobot(){
        GeodeRobot bot = blueprint.geodeRobotCost;
        return ore >= bot.ore && obsidian >= bot.obsidian;
    }
    public void makeOreRobot(){
        int cost = blueprint.oreRobotCost;
        if(ore < cost){
            return;
        }
        ore -= cost;
        orderedOreBot = true;
    }
    public void makeClayRobot(){
        int cost = blueprint.clayRobotCost;
        if(ore < cost){
            return;
        }
        ore -= cost;
        orderedClayBot = true;
    }
    public void makeObsidianRobot(){
        ObsidianRobot bot = blueprint.obsidianRobotCost;
        if(ore < bot.ore || clay < bot.clay){
            return;
        }
        ore  -= bot.ore;
        clay -= bot.clay;
        orderedObsidianBot = true;
    }
    public void makeGeodeRobot(){
        GeodeRobot bot = blueprint.geodeRobotCost;
        if(ore < bot.ore || obsidian < bot.obsidian){
            return;
        }
        ore -= bot.ore;
        obsidian -= bot.obsidian;
        orderedGeodeBot = true;
    }
    //returns the time the next robot of that type will be available to buy (doesn't go back in time)
    //given price, stock and current prod
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
    public void skipToMinute(int minute){
        if(minute <= this.minute || this.minute > MAX_MINUTES + 1){
            return;
        }
        if(minute > MAX_MINUTES + 1){
            minute = MAX_MINUTES + 1;
        }
        collect();
        finishBuilds();
        this.minute++;
        //next minute
        int diff = minute - this.minute;
        if(diff > 0){
            ore += oreRobots * diff;
            clay += clayRobots * diff;
            obsidian += obsidianRobots * diff;
            geodes += geodeRobots * diff;
        }
        this.minute = minute;
    }
    public static void refreshCap(){
        OBSIDIAN_CAP = MAX_MINUTES-2;
        CLAY_CAP = MAX_MINUTES-3;
        ORE_CAP = MAX_MINUTES-4;
    }
}
