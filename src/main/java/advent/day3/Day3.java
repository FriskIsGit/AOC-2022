package advent.day3;

import advent.AdventOfCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day3{
    public static void main(String[] args){
        List<String> list = AdventOfCode.readDay(3);
        part1(list);
        part2(list);

    }

    public static void part2(List<String> list){
        int prioritySum = 0;
        for(int i = 0; i<list.size()-2; i+=3){
            Set<Character> top = stringToSet(list.get(i));
            Set<Character> mid = stringToSet(list.get(i+1));
            Set<Character> bot = stringToSet(list.get(i+2));
            top.retainAll(mid);
            top.retainAll(bot);
            char commonPart = top.iterator().next();
            int priority = getCharPriority(commonPart);
            prioritySum += priority;
        }
        System.out.println("Priority sum: " + prioritySum);
    }
    public static void part1(List<String> list){
        int prioritySum = 0;
        for(String rucksack : list){
            int len = rucksack.length();
            Set<Character> leftCompartment = stringToSet(rucksack.substring(0, len/2));
            Set<Character> rightCompartment = stringToSet(rucksack.substring(len/2));
            leftCompartment.retainAll(rightCompartment);
            char commonPart = leftCompartment.iterator().next();
            int priority = getCharPriority(commonPart);
            prioritySum += priority;
        }
        System.out.println("Priority sum: " + prioritySum);
    }
    public static Set<Character> stringToSet(String substring){
        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < substring.length(); i++){
            char c = substring.charAt(i);
            chars.add(c);
        }
        return chars;
    }

    public static int getCharPriority(char c){
        if(Character.isUpperCase(c)){
            return c - 38;
        }else{
            return c - 96;
        }
    }

}
