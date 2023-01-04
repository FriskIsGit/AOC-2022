package advent.day2;

import advent.AdventOfCode;

import java.util.List;

public class Day2{
    public static void main(String[] args){
        List<String> list = AdventOfCode.readDay(2);
        part1(list);
        part2(list);
    }

    private static void part2(List<String> list){
        int sum = 0;
        for(String line : list){
            char elfChar = line.charAt(0);
            char youChar = line.charAt(2);
            Move elfMove = Move.toMove(elfChar);
            int futureResult = Move.toResult(youChar);

            if(futureResult == Result.TIE){
                sum += Move.getYourScore(elfMove, elfMove);
            }else if(futureResult == Result.LOSE){
                switch (elfMove){
                    case ROCK:
                        sum += Move.getYourScore(Move.SCISSORS, elfMove);
                        break;
                    case PAPER:
                        sum += Move.getYourScore(Move.ROCK, elfMove);
                        break;
                    case SCISSORS:
                        sum += Move.getYourScore(Move.PAPER, elfMove);
                        break;
                }
            }else if(futureResult == Result.WIN){
                switch (elfMove){
                    case ROCK:
                        sum += Move.getYourScore(Move.PAPER, elfMove);
                        break;
                    case PAPER:
                        sum += Move.getYourScore(Move.SCISSORS, elfMove);
                        break;
                    case SCISSORS:
                        sum += Move.getYourScore(Move.ROCK, elfMove);
                        break;
                }
            }
        }
        System.out.println("PART2 SCORE SUM: " + sum);
    }

    public static void part1(List<String> list){
        int sum = 0;
        for(String line : list){
            Move elfMove = Move.toMove(line.charAt(0));
            Move youMove = Move.toMove(line.charAt(2));
            sum += Move.getYourScore(youMove, elfMove);
        }
        System.out.println("PART1 SCORE SUM: " + sum);
    }
}
