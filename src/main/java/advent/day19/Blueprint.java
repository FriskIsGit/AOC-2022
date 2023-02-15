package advent.day19;

public class Blueprint{
    public int id;
    public int oreRobotCost, clayRobotCost;
    public ObsidianRobot obsidianRobotCost;
    public GeodeRobot geodeRobotCost;
    //ore robots
    //clay robots
    //obsidian robots
    //geode robots
    public static Blueprint parseBlueprint(String line){
        Blueprint blueprint = new Blueprint();
        int colon = line.indexOf(':', 10);
        String number = line.substring(10, colon);
        blueprint.id = Integer.parseInt(number);
        boolean singularId = blueprint.id < 10;

        if(singularId){
            blueprint.oreRobotCost = Integer.parseInt(line.substring(34, 35));
            blueprint.clayRobotCost = Integer.parseInt(line.substring(63, 64));
        }else{
            blueprint.oreRobotCost = Integer.parseInt(line.substring(35, 36));
            blueprint.clayRobotCost = Integer.parseInt(line.substring(64, 65));
        }

        int ore, clay, whitespace;
        if(singularId){
            ore = Integer.parseInt(line.substring(96, 97));
            whitespace = line.indexOf(' ', 106);
            clay = Integer.parseInt(line.substring(106, whitespace));
        }else{
            ore = Integer.parseInt(line.substring(97, 98));
            whitespace = line.indexOf(' ', 107);
            clay = Integer.parseInt(line.substring(107, whitespace));
        }

        blueprint.obsidianRobotCost = new ObsidianRobot(ore, clay);
        int valIndex1 = line.indexOf("costs", whitespace) + 6;
        int white1 = line.indexOf(' ', valIndex1);
        int valIndex2 = line.indexOf("and", white1) + 4;
        int white2 = line.indexOf(' ', valIndex2);
        ore = Integer.parseInt(line.substring(valIndex1, white1));
        int obsidian = Integer.parseInt(line.substring(valIndex2, white2));
        blueprint.geodeRobotCost = new GeodeRobot(ore, obsidian);
        return blueprint;
    }

    public int qualityLevel(int geodes){
        return id * geodes;
    }
    public int maxOreRobots(){
        int max = Math.max(oreRobotCost, clayRobotCost);
        max = Math.max(max, obsidianRobotCost.ore);
        return Math.max(max, geodeRobotCost.ore);
    }

    @Override
    public String toString(){
        return "Blueprint{" +
                "id=" + id +
                ", oreRobotCost=" + oreRobotCost +
                ", clayRobotCost=" + clayRobotCost +
                ", obsidianRobotCost=" + obsidianRobotCost +
                ", geodeRobotCost=" + geodeRobotCost +
                '}';
    }
    public static String toPrettyString(String bp){
        StringBuilder builder = new StringBuilder();

        int colonIndex = bp.indexOf(':') + 1;
        if(colonIndex == 0){
            return null;
        }
        builder.append(bp, 0, colonIndex).append('\n');
        int index, from = colonIndex;
        while(true){
            index = bp.indexOf('.', from);
            if(index == -1){
                break;
            }
            builder.append(bp, from, index+1);
            builder.append('\n');
            from = index+1;
        }
        return builder.toString();
    }
}




