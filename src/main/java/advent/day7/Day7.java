package advent.day7;

import advent.AdventOfCode;
import java.util.List;

public class Day7{
    public static final int SIZE_CAP = 100_000;
    public static final int FILE_SYSTEM_SIZE = 70_000_000;
    public static final int UNUSED_SPACE = 30_000_000;

    public static void actualLogicalImplementationDeemedIncorrectByAOC(List<String> lines){
        int totalSum = 0;
        int dirSum = 0;
        for (int i = 0; i < lines.size(); i++){
            String row = lines.get(i);
            int whitespace = row.indexOf(' ');
            String possiblyNum = row.substring(0, whitespace);
            int size;
            try{
                size = Integer.parseInt(possiblyNum);
                dirSum+=size;
            }catch (NumberFormatException nfExc){
                if (dirSum <= SIZE_CAP){
                    totalSum+=dirSum;
                }
                dirSum = 0;
                continue;
            }

            //if last
            if(i == lines.size()-1){
                if (dirSum <= SIZE_CAP){
                    totalSum+=dirSum;
                }
            }
        }
        System.out.println(totalSum);
    }
    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDay(7);
        doBothParts(lines);
    }

    private static void doBothParts(List<String> lines){
        //first line should be root
        Directory root = new Directory("/");
        Directory currentDir = root;
        int ALL_LINES = lines.size();
        //map directory tree
        for (int i = 1; i < ALL_LINES; i++){
            String row = lines.get(i);
            if(row.startsWith("$")){
                String command = row.substring(2,4);
                //lists dirs and files
                if(command.equals("ls")){
                    while(++i < ALL_LINES){
                        if(lines.get(i).startsWith("$")){
                            i--;
                            break;
                        }
                        String entry = lines.get(i);
                        if(entry.startsWith("dir")){
                            String dirName = entry.substring(4);
                            currentDir.createIfNotExists(dirName);
                        }else{
                            int fileSize = Integer.parseInt(entry.substring(0, entry.indexOf(' ')));
                            currentDir.appendFile(fileSize);
                        }
                    }
                }
                else{
                    String argument = row.substring(5);
                    //cd ..
                    if(argument.equals("..")){
                        currentDir = currentDir.getParentDir();
                    }
                    else{
                        currentDir = currentDir.stepInto(argument);
                    }
                }
            }
        }
        System.out.println("Mapped dirs and files");
        int rootSize = root.entireSize();
        System.out.println("Root size: " + rootSize);
        final int[] sum = new int[1];
        countSize(root, sum);
        System.out.println("Final answer to part1: " + sum[0]);

        final int[] min = {Integer.MAX_VALUE};
        int FREE_SPACE = FILE_SYSTEM_SIZE - rootSize;
        System.out.println("FREE SPACE: " + FREE_SPACE);
        findMinimalRemoval(root, min, UNUSED_SPACE - FREE_SPACE);
        System.out.println("Final answer to part2: " + min[0]);
    }

    private static void findMinimalRemoval(Directory dir, int[] min, final int spaceRequired){
        int entireSize = dir.entireSize();
        if(entireSize >= spaceRequired){
            min[0] = Math.min(min[0], entireSize);
        }
        //current dir
        for(Directory d : dir.getDirs()){
            findMinimalRemoval(d, min, spaceRequired);
        }
    }
    private static void countSize(Directory dir, int[] sum){
        int entireSize = dir.entireSize();
        if(entireSize <= SIZE_CAP){
            sum[0] += entireSize;
        }
        for(Directory d : dir.getDirs()){
            countSize(d, sum);
        }
    }
}
