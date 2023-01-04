package advent.day5;

import advent.AdventOfCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day5{
    public static final int NUM_OF_STACKS_PART = 9;
    public static void doBothParts(List<String> lines){
        List<Stack<Character>> stacks = new ArrayList<>();
        for (int i = 0; i< NUM_OF_STACKS_PART; i++){
            stacks.add(new Stack<>());
        }
        int allLines = lines.size();
        int stackBottom = -1;
        for (int i = 0; i < allLines; i++){
            if(lines.get(i).contains("1")){
                stackBottom = i;
                for (int j = i-1; j > -1; j--){
                    String level = lines.get(j);
                    for(int jump = 1, numeric = 0; jump < level.length(); jump+=4, numeric++){
                        char c = level.charAt(jump);
                        if(c != ' '){
                            stacks.get(numeric).add(c);
                        }
                    }
                }
                break;
            }
            //jump every 4 chars?
        }
        for(Stack st : stacks){
            System.out.println("Stacks1");
            System.out.println(st);
        }
        List<Operation> operations = new ArrayList<>();
        int FIRST_NUM_INDEX = 5;
        //move section begins
        for (int i = stackBottom+2; i < allLines; i++){
            String move = lines.get(i);

            int secondSpace = move.indexOf(' ', FIRST_NUM_INDEX+1);
            String firstNumString = move.substring(FIRST_NUM_INDEX, secondSpace);
            int amount = Integer.parseInt(firstNumString);

            int thirdSpace = move.indexOf(' ', secondSpace+1);
            int fourthSpace = move.indexOf(' ', thirdSpace+1);
            String fromNumberString = move.substring(thirdSpace+1, fourthSpace);
            int fromNumber = Integer.parseInt(fromNumberString);

            int fifthSpace = move.indexOf(' ', fourthSpace+1);
            String toNumberString = move.substring(fifthSpace+1);
            int toNumber = Integer.parseInt(toNumberString);
            Operation operation = Operation.move(amount).from(fromNumber).to(toNumber);
            operations.add(operation);
        }
        List<Stack<Character>> copiedStacks = copyStacks(stacks);
        part1(stacks, operations);
        part2(copiedStacks, operations);
    }

    private static List<Stack<Character>> copyStacks(List<Stack<Character>> stacks){
        List<Stack<Character>> copiedStacks = new ArrayList<>();
        for(Stack<Character> stack : stacks){
            Stack<Character> newStack = new Stack<>();
            newStack.addAll(stack);
            copiedStacks.add(newStack);
        }
        return copiedStacks;
    }

    private static void part1(List<Stack<Character>> stacks, List<Operation> operations){
        for(Operation operation : operations){
            operation.executeOnAs9000(stacks);
        }
        System.out.println("TOP CRATES: " + readTopsOfStacks(stacks));
    }
    private static void part2(List<Stack<Character>> stacks, List<Operation> operations){
        for(Operation op : operations){
            op.executeOnAs9001(stacks);
        }
        System.out.println("TOP CRATES: " + readTopsOfStacks(stacks));
    }

    public static String readTopsOfStacks(List<Stack<Character>> stacks){
        StringBuilder result = new StringBuilder();
        for(Stack<Character> stack : stacks){
            if(stack.size() < 1){
                continue;
            }
            result.append(stack.pop());
        }
        return result.toString();
    }
    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDay(5);
        for (String line : lines){
            System.out.println(line);
        }
        doBothParts(lines);
    }

}
