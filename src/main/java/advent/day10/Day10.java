package advent.day10;

import advent.AdventOfCode;

import java.util.List;

public class Day10{
    //addx, noop
    public static final int CRT_WIDTH = 40;
    public static final int CRT_HEIGHT = 6;
    public static final char[][] CRT = new char[CRT_HEIGHT][CRT_WIDTH];
    public static final int OFFSET = 5;
    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDay(10);
        System.out.println(lines);
        //part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines){
        int sum = 0;
        int register = 1, cycles = 0;
        for(String line : lines){
            switch (line.charAt(0)){
                case 'a':
                    int value = Integer.parseInt(line.substring(OFFSET));
                    sum += doCycleCheck(++cycles, register);
                    sum += doCycleCheck(++cycles, register);
                    register += value;
                    break;
                case 'n':
                    sum += doCycleCheck(++cycles, register);
                    break;
            }
        }
        System.out.println("Sum: " + sum);
    }

    private static void part2(List<String> lines){
        StringBuilder str = new StringBuilder();
        //int row = 0, col = 0;
        int register = 1, cycles = 0;
        for(String line : lines){
            switch (line.charAt(0)){
                case 'a':
                    int value = Integer.parseInt(line.substring(OFFSET));
                    cycles++;
                    draw(str, register, cycles);
                    cycles++;
                    draw(str, register, cycles);
                    register += value;
                    break;
                case 'n':
                    cycles++;
                    draw(str, register, cycles);
                    break;
            }
        }
        System.out.println("All cycles: " + cycles);
        System.out.println(str);
    }

    private static void draw(StringBuilder str, int register, int cycles){
        System.out.println(cycles + ":" + register);
        if(cycles % CRT_WIDTH-1 == 0){
            str.append("\n");
        }
        if(cycles % CRT_WIDTH-1 == register-1 || cycles % CRT_WIDTH-1 == register || cycles % CRT_WIDTH-1 == register+1){
            str.append("#");
        }else{
            str.append(".");
        }
    }

    private static int doCycleCheck(int cycle, int register){
        switch (cycle){
            case 20:
                return register*20;
            case 60:
                return register*60;
            case 100:
                return register*100;
            case 140:
                return register*140;
            case 180:
                return register*180;
            case 220:
                return register*220;
        }
        return 0;
    }
}
