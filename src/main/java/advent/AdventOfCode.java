package advent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AdventOfCode{
    //example input file names: input3.txt, input7.txt, input12.txt, etc.
    private static final String inputName = "input.txt";
    private static final String dummyName = "dummy.txt";

    public static final String adventOfCodeDirectory = System.getProperty("user.home") + "/desktop/aoc/";

    public static List<String> readCustom(String filename){
        return read(adventOfCodeDirectory + filename);
    }
    public static List<String> readDay(int day){
        if(day < 1 || day > 25){
            throw new IllegalStateException("Advent of code consists of 25 days!");
        }
        int dot = inputName.lastIndexOf('.');
        String baseName = inputName.substring(0, dot);
        String extension = inputName.substring(dot);
        return read(adventOfCodeDirectory + baseName + day + extension);
    }
    public static List<String> readDummy(int day){
        if(day < 1 || day > 25){
            throw new IllegalStateException("Advent of code consists of 25 days!");
        }
        int dot = dummyName.lastIndexOf('.');
        String baseName = dummyName.substring(0, dot);
        String extension = dummyName.substring(dot);
        return read(adventOfCodeDirectory + baseName + day + extension);
    }
    private static List<String> read(String strPath){
        Path path = Paths.get(strPath);
        if(!Files.exists(path)){
            throw new IllegalStateException("AOC input file doesn't exist, path: " + path);
        }
        List<String> list;
        try{
            list = Files.readAllLines(path);
            System.out.println("Lines read:" + list.size());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return list;
    }
}
