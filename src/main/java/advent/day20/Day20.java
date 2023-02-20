package advent.day20;

import advent.AdventOfCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day20{
    public static int DECRYPTION_KEY = 811589153;
    //Grove Positioning System
    public static void main(String[] args){
        //6732 too low
        List<String> input = AdventOfCode.readDay(20);
        int size = input.size();
        long[] arr = new long[size];
        long[] copyArr = new long[size];
        int i = 0;
        for (String num : input){
            arr[i] = Integer.parseInt(num);
            copyArr[i] = arr[i] * DECRYPTION_KEY;
            i++;
        }

        part1(arr);
        part2(copyArr);
    }

    private static void part1(long[] arr){
        BufferArray buffer = new BufferArray(arr);
        //HashMap<Integer, Integer> map1 = occurrences(arr);
        long st = System.currentTimeMillis();
        buffer.mix();
        long en = System.currentTimeMillis();
        System.out.println("Final: " + buffer);
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("PART1 Grove coordinates sum: " + buffer.getCoordinateSum());
    }
    private static void part2(long[] arr){
        BufferArray buffer = new BufferArray(arr);
        long st = System.currentTimeMillis();
        for (int i = 0; i < 10; i++){
            buffer.mix();
        }
        long en = System.currentTimeMillis();
        System.out.println("Final: " + buffer);
        System.out.println("Time taken ms: " + (en-st));
        System.out.println("PART2 Grove coordinates sum: " + buffer.getCoordinateSum());
    }

    private static int duplicateCount(int[] arr){
        HashSet<Long> duplicates = new HashSet<>();
        HashSet<Long> nums = new HashSet<>();
        for (long n : arr){
            if (!nums.add(n)){
                duplicates.add(n);
            }
        }
        return duplicates.size();
    }
    private static HashMap<Long, Integer> occurrences(long[] arr){
        HashMap<Long, Integer> occurrences = new HashMap<>();
        for (long n : arr){
            if(occurrences.containsKey(n)){
                occurrences.put(n, occurrences.get(n) + 1);
            }else{
                occurrences.put(n, 1);
            }
        }
        return occurrences;
    }
}
