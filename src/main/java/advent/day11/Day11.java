package advent.day11;

import advent.AdventOfCode;

import java.util.List;

public class Day11{
    public final static int STARTING_ITEMS_OFFSET = 18;
    public final static int OPERATION_OFFSET = 23;
    public final static int DIVISIBLE_OFFSET = 21;
    public final static int TRUE_OFFSET = 29;
    public final static int FALSE_OFFSET = 30;

    public static void parseMonkeys(){
        List<String> lines = AdventOfCode.readDay(11);
        for (int i = 0; i < lines.size()-5; i++){
            String line = lines.get(i);
            if (line.startsWith("M")){
                Monkey monkey = new Monkey();
                Monkey.monkeys.add(monkey);
                String itemString = lines.get(i+1).substring(STARTING_ITEMS_OFFSET);
                monkey.parseItems(itemString);
                String operation = lines.get(i+2).substring(OPERATION_OFFSET);
                monkey.operation = Operation.parse(operation);
                monkey.divisible = Integer.parseInt(lines.get(i+3).substring(DIVISIBLE_OFFSET));
                String trueMonkey = lines.get(i+4).substring(TRUE_OFFSET);
                String falseMonkey = lines.get(i+5).substring(FALSE_OFFSET);
                monkey.trueMonkey = Integer.parseInt(trueMonkey);
                monkey.falseMonkey = Integer.parseInt(falseMonkey);
            }
        }
    }
    public static void part1(){
        parseMonkeys();
        int rounds = 20;
        for (int r = 0; r < rounds; r++){
            System.out.println("ROUND: " + (r+1));
            int numberOfMonkeys = Monkey.monkeys.size();
            for (int m = 0; m < numberOfMonkeys; m++){
                Monkey monkey = Monkey.monkeys.get(m);
                int itemsLen = monkey.items.size();
                for (int i = 0; i < itemsLen; i++){
                    printMonkey(monkey);
                    monkey.doThrow();
                }
            }
        }

        int max1 = 0, index = -1, max2 = 0;
        List<Monkey> monkeys = Monkey.monkeys;
        for (int i = 0; i < monkeys.size(); i++){
            Monkey monkey = monkeys.get(i);
            if(max1 < monkey.inspections){
                max1 = monkey.inspections;
                index = i;
            }
        }
        monkeys.remove(index);
        for(Monkey monkey : Monkey.monkeys){
            max2 = Math.max(max2, monkey.inspections);
        }
        System.out.println("Inspections");
        int allInspections = 0;
        for(Monkey monkey : Monkey.monkeys){
            System.out.println(monkey.inspections);
            allInspections += monkey.inspections;
        }
        System.out.println("MAX1: " + max1);
        System.out.println("MAX2: " + max2);
        System.out.println("Total inspections: " + allInspections);
        System.out.println("BUSINESS score: " + max1*max2);
    }
    //implementing this on BigIntegers causes extremely lengthy operations
    //eventually stops at round 138 either due to heap size or overflow
    private static void part2(){
        parseBigMonkeys();
        //all divisors in my case are prime numbers
        BigMonkey.biggestCommonMultiple = 1;
        for(BigMonkey m : BigMonkey.monkeys){
            BigMonkey.biggestCommonMultiple *= m.divisible;
        }
        //19, 23, 17, 13
        System.out.println("Multiple: " + BigMonkey.biggestCommonMultiple);
        int rounds = 10000;
        int numberOfMonkeys = BigMonkey.monkeys.size();
        long st = System.currentTimeMillis();
        for (int r = 0; r < rounds; r++){
            //System.out.println("ROUND: " + (r+1));
            for (int m = 0; m < numberOfMonkeys; m++){
                BigMonkey monkey = BigMonkey.monkeys.get(m);
                //printMonkey(monkey);
                monkey.doThrows();
            }
        }
        int index = -1;
        long max1 = 0, max2 = 0;
        List<BigMonkey> monkeys = BigMonkey.monkeys;
        for (int i = 0; i < monkeys.size(); i++){
            BigMonkey monkey = monkeys.get(i);
            if(max1 < monkey.inspections){
                max1 = monkey.inspections;
                index = i;
            }
        }
        monkeys.remove(index);
        for(BigMonkey monkey : BigMonkey.monkeys){
            max2 = Math.max(max2, monkey.inspections);
        }
        long en = System.currentTimeMillis();
        System.out.println("ROUNDs TIME: " + (en-st));
        System.out.println("MAX1: " + max1);
        System.out.println("MAX2: " + max2);
        System.out.println("Monkey business:" + max1*max2);
    }

    public static void printMonkey(BigMonkey monkey) {
        System.out.print("Monkey equipment: ");
        for (int i = 0; i < monkey.items.size(); i++) {
            System.out.print(monkey.items.get(i));
            System.out.print(", ");
        }
        System.out.println();
        //System.out.println(monkey.operation);
        //System.out.println("Test: divisible by " + monkey.divisible);
        //System.out.println("    If true: throw to monkey  " + monkey.trueMonkey);
        //System.out.println("    If false: throw to monkey  " + monkey.falseMonkey);
        System.out.println("Number of inspections " + monkey.inspections);
        System.out.println();
    }

    private static void parseBigMonkeys(){
        List<String> lines = AdventOfCode.readDay(11);
        for (int i = 0; i < lines.size()-5; i++){
            String line = lines.get(i);
            if (line.startsWith("M")){
                BigMonkey monkey = new BigMonkey();
                BigMonkey.monkeys.add(monkey);
                String itemString = lines.get(i+1).substring(STARTING_ITEMS_OFFSET);
                monkey.parseItems(itemString);
                String operation = lines.get(i+2).substring(OPERATION_OFFSET);
                monkey.operation = Operation.parse(operation);
                monkey.divisible = Integer.parseInt(lines.get(i+3).substring(DIVISIBLE_OFFSET));
                String trueMonkey = lines.get(i+4).substring(TRUE_OFFSET);
                String falseMonkey = lines.get(i+5).substring(FALSE_OFFSET);
                monkey.trueMonkey = Integer.parseInt(trueMonkey);
                monkey.falseMonkey = Integer.parseInt(falseMonkey);
            }
        }
        for(BigMonkey m : BigMonkey.monkeys){
            System.out.println(m);
        }
    }

    public static void main(String[] args){
        //part1();
        part2();
    }



    static void printMonkey(Monkey monkey) {
        System.out.print("Monkey equipment: ");
        for (int i = 0; i < monkey.items.size(); i++) {
            System.out.print(monkey.items.get(i));
            System.out.print(", ");
        }
        System.out.println();
        //System.out.println(monkey.operation);
        //System.out.println("Test: divisible by " + monkey.divisible);
        //System.out.println("    If true: throw to monkey  " + monkey.trueMonkey);
        //System.out.println("    If false: throw to monkey  " + monkey.falseMonkey);
        System.out.println("Number of inspections " + monkey.inspections);
        System.out.println();
    }
}
