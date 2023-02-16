package advent.day20;

import advent.AdventOfCode;

import java.util.HashSet;
import java.util.List;

public class Day20{
    //Grove Positioning System
    public static void main(String[] args){
        //6732 too low
        List<String> input = AdventOfCode.readDay(20);
        int[] arr = new int[input.size()];
        int i = 0;
        for (String num : input){
            arr[i++] = Integer.parseInt(num);
        }
        System.out.println("Duplicates: " + duplicateCount(arr));
        BufferArray buffer = new BufferArray(arr);
        long st = System.currentTimeMillis();
        buffer.mix();
        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("Final: " + buffer);
        System.out.println("Grove coordinates sum: " + buffer.getCoordinateSum());
    }
    private static int duplicateCount(int[] arr){
        HashSet<Integer> duplicates = new HashSet<>();
        HashSet<Integer> nums = new HashSet<>();
        for (int n : arr){
            if (!nums.add(n)){
                duplicates.add(n);
            }
        }
        return duplicates.size();
    }
}
