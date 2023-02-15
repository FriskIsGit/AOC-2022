package advent.day19;

import advent.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day19{
    public static int maxGeodes = 0;
    public static int MAX_ORE_ROBOTS = 4, MAX_CLAY_ROBOTS = 20;
    public static boolean recurseOnObs = true;
    public static List<Robot> tracked = null;
    public static void main(String[] args){
        //List<String> lines = AdventOfCode.readDummy(19);
        List<String> lines = AdventOfCode.readDay(19);

        List<Blueprint> blueprints = new ArrayList<>();
        for(String line : lines){
            Blueprint bp = Blueprint.parseBlueprint(line);
            blueprints.add(bp);
        }
        //1346
        part1(blueprints);
        //7644
        part2(blueprints);
    }


    private static void trackRobots(List<Blueprint> blueprints){
        long st = System.currentTimeMillis();
        for(Blueprint bp : blueprints){
            List<Robot> robots = Day19.trackMaxNumberOfGeodes(bp);
            System.out.println(robots);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (end-st));
    }
    private static void part2(List<Blueprint> blueprints){
        int bps = Math.min(blueprints.size(), 3);
        int max = 0;
        int product = 1;
        State.MAX_MINUTES = 32;
        State.refreshCap();
        recurseOnObs = false;
        long st = System.currentTimeMillis();
        for (int i = 0; i < bps; i++){
            Blueprint bp = blueprints.get(i);
            int res = findMaxGeodes(bp);
            product *= res;
            max = Math.max(res, max);
            System.out.println(bp + " completed with a res of: " + res + " time so far: " + (System.currentTimeMillis() - st));
        }
        long en = System.currentTimeMillis();
        System.out.println("Max found to be: " + max);
        System.out.println("Max geodes multiplied: " + product);
        System.out.println("Time taken ms: " + (en-st));
    }

    private static void part1(List<Blueprint> blueprints){
        int max = 0;
        int qualitySum = 0;
        recurseOnObs = true;
        long st = System.currentTimeMillis();
        for(Blueprint blueprint : blueprints){
            int res = findMaxGeodes(blueprint);
            int quality = blueprint.qualityLevel(res);
            qualitySum += quality;
            max = Math.max(res, max);
            System.out.println(blueprint + " completed with a res of: " + res + " time taken: " + (System.currentTimeMillis() - st));
        }
        long en = System.currentTimeMillis();
        System.out.println("Max found to be: " + max);
        System.out.println("The sum of quality levels is: " + qualitySum);
        System.out.println("Time taken ms: " + (en-st));
    }

    public static int findMaxGeodes(Blueprint bp){
        State state = new State(bp);
        maxGeodes = 0;
        decide(state);
        return maxGeodes;
    }

    private static void decide(State state){
        while(state.nextMinute()){
            //buying a geode is the right choice all the time, not buying it when it's available is therefore not considered
            if(state.canMakeGeodeRobot()){
                state.makeGeodeRobot();
                continue;
            }
            if(state.canMakeObsidianRobot()){
                if(state.minute > State.OBSIDIAN_CAP){
                    continue;
                }
                if(recurseOnObs){
                    State newState = state.copy();
                    newState.makeObsidianRobot();
                    decide(newState);
                }else{
                    state.makeObsidianRobot();
                    continue;
                }

            }
            if(state.clayRobots < MAX_CLAY_ROBOTS && state.canMakeClayRobot()){
                if(state.minute > State.CLAY_CAP){
                    continue;
                }
                State newState = state.copy();
                newState.makeClayRobot();
                decide(newState);
            }
            if(state.oreRobots < MAX_ORE_ROBOTS && state.canMakeOreRobot()){
                if(state.minute > State.ORE_CAP){
                    continue;
                }
                State newState = state.copy();
                newState.makeOreRobot();
                decide(newState);
            }
        }
        maxGeodes = Math.max(maxGeodes, state.geodes);
    }

    //never yields correct answers
    private static int findMaxGeodesTierBased(Blueprint bp){
        State state = new State(bp);
        int max = 0;
        //List<State> states = new ArrayList<>();
        State[] oreStates = new State[5];
        oreStates[0] = state;
        //no ore
        State current = oreStates[0];
        //ore to buy
        for (int ore = 1; ore <= MAX_ORE_ROBOTS; ore++){
            State copy = current.copy();
            int minute = copy.nextRobot(Robot.ORE);
            copy.skipToMinute(minute);
            copy.makeOreRobot();
            copy.nextMinute();

            oreStates[ore] = copy;
            current = copy;
        }

        final int clays = 15;
        List<State> clayStates = new ArrayList<>();
        //clay to buy
        for (State oreState : oreStates){
            current = oreState;
            for (int clay = 1; clay <= clays; clay++){
                State copy = current.copy();
                int minute = copy.nextRobot(Robot.CLAY);
                if(minute >= State.MAX_MINUTES){
                    continue;
                }
                copy.skipToMinute(minute);
                copy.makeClayRobot();
                copy.nextMinute();
                clayStates.add(copy);
                current = copy;
            }
        }
        oreStates = null;

        final int obsidians = 15;
        List<State> obsidianStates = new ArrayList<>();
        //obsidian to buy
        for (State clayState : clayStates){
            current = clayState;
            for (int obs = 1; obs <= obsidians; obs++){
                State copy = current.copy();
                int minute = copy.nextRobot(Robot.OBSIDIAN);
                if(minute >= State.MAX_MINUTES){
                    continue;
                }
                copy.skipToMinute(minute);
                copy.makeObsidianRobot();
                copy.nextMinute();
                obsidianStates.add(copy);
                current = copy;
            }
        }
        clayStates = null;
        //buy as many geodes as possible
        for (State finalState : obsidianStates){
            while(true){
                int minute = finalState.nextRobot(Robot.GEODE);
                if(minute >= State.MAX_MINUTES){
                    finalState.skipToMinute(State.MAX_MINUTES+1);
                    max = Math.max(finalState.geodes, max);
                    break;
                }
                finalState.skipToMinute(minute);
                finalState.makeGeodeRobot();
                finalState.nextMinute();
            }
        }
        return max;
    }

    //track purchase choices
    private static void decideTrack(State state, List<Robot> bots){
        while(state.nextMinute()){
            //buying a geode is the right choice all the time, not buying it when it's available is therefore not considered
            if(state.canMakeGeodeRobot()){
                state.makeGeodeRobot();
                bots.add(Robot.GEODE);
                continue;
            }
            if(state.canMakeObsidianRobot()){
                State newState = state.copy();
                newState.makeObsidianRobot();
                List<Robot> copy = new ArrayList<>(bots);
                copy.add(Robot.OBSIDIAN);
                decideTrack(newState, copy);
            }
            if(state.canMakeClayRobot()){
                State newState = state.copy();
                newState.makeClayRobot();
                List<Robot> copy = new ArrayList<>(bots);
                copy.add(Robot.CLAY);
                decideTrack(newState, copy);
            }
            if(state.canMakeOreRobot() && state.oreRobots < 4){
                State newState = state.copy();
                newState.makeOreRobot();
                List<Robot> copy = new ArrayList<>(bots);
                copy.add(Robot.ORE);
                decideTrack(newState, copy);
            }
        }
        if(state.geodes > maxGeodes){
            maxGeodes = state.geodes;
            tracked = bots;
        }
    }
    public static List<Robot> trackMaxNumberOfGeodes(Blueprint bp){
        State state = new State(bp);
        maxGeodes = 0;
        tracked = null;
        decideTrack(state, new ArrayList<>());
        return tracked;
    }
}
