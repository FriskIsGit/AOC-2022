package advent.day4;

import advent.AdventOfCode;

import java.util.List;

public class Day4{
    public static void main(String[] args){
        List<String> list = AdventOfCode.readDay(4);

        int overlapping = 0;
        int fullyContained = 0;
        for(String line : list){
            int dash1 = line.indexOf('-');
            int separator = line.indexOf(',', dash1+1);
            int dash2 = line.indexOf('-', separator+1);

            int rangeStart = Integer.parseInt(line.substring(0, dash1));
            int rangeEnd = Integer.parseInt(line.substring(dash1+1, separator));
            Range left = new Range(rangeStart, rangeEnd);
            int rangeStart2 = Integer.parseInt(line.substring(separator+1, dash2));
            int rangeEnd2 = Integer.parseInt(line.substring(dash2+1));
            Range right = new Range(rangeStart2, rangeEnd2);
            if(left.overlaps(right)){
                overlapping++;
            }
            if(left.fullyContains(right)){
                fullyContained++;
            }
        }
        System.out.println("Overlaps: " + overlapping);
        System.out.println("Fully contained: " + fullyContained);
    }
}
