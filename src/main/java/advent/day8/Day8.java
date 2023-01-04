package advent.day8;

import advent.AdventOfCode;

import java.util.List;

public class Day8{
    private static int[][] arr;
    private static int rows, columns;
    public static void main(String[] args){
        //visible at all
        List<String> lines = AdventOfCode.readDay(8);
        mapTo2D(lines);
        part1();
        part2();
    }

    private static void part2(){
        int max = 0;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < columns; col++){
                if(visibleAtAll(row, col)){
                    max = Math.max(max, getScenicScore(row, col));
                }
            }
        }
        System.out.println("Highest scenic score: " + max);
    }

    private static void mapTo2D(List<String> lines){
        rows = lines.size();
        columns = lines.get(0).length();
        arr = new int[rows][columns];
        for (int row = 0; row<rows; row++){
            String line = lines.get(row);
            for (int col = 0; col < columns; col++){
                char charNum = line.charAt(col);
                int num = charNum - 48;
                arr[row][col] = num;
            }
        }
    }

    private static void part1(){
        //counting in the outer trees first and avoiding iterating over them
        int visibles = 2*rows + 2*(columns-2);
        for (int row = 1; row < rows-1; row++){
            for (int col = 1; col < columns-1; col++){
                if(visibleAtAll(row, col)){
                    visibles++;
                }
            }
        }
        System.out.println("Visibles: " + visibles);
    }

    private static boolean visibleAtAll(int row, int col){
        int height = arr[row][col];
        boolean isTallest = true;
        //left
        for(int i = col-1; i>-1;i--){
            if (arr[row][i] >= height){
                isTallest = false;
                break;
            }
        }
        if(isTallest) return true;
        isTallest = true;
        //right
        for(int i = col+1; i < columns; i++){
            if(arr[row][i] >= height){
                isTallest = false;
                break;
            }
        }
        if(isTallest) return true;
        isTallest = true;
        //top
        for(int i = row-1; i > -1; i--){
            if(arr[i][col] >= height){
                isTallest = false;
                break;
            }
        }
        if(isTallest) return true;
        isTallest = true;
        //bot
        for(int i = row+1; i < rows; i++){
            if(arr[i][col] >= height){
                isTallest = false;
                break;
            }
        }
        return isTallest;
    }
    private static int getScenicScore(int row, int col){
        int left = 0;
        int height = arr[row][col];
        for(int i = col-1; i>-1;i--){
            if(arr[row][i] < height){
                left++;
            }else if(arr[row][i] >= height){
                left++;
                break;
            }
        }
        int right = 0;
        //right
        for(int i = col+1; i < columns; i++){
            if(arr[row][i] < height){
                right++;
            }else if(arr[row][i] >= height){
                right++;
                break;
            }
        }
        int top = 0;
        //top
        for(int i = row-1; i > -1; i--){
            if(arr[i][col] < height){
                top++;
            }else if(arr[i][col] >= height){
                top++;
                break;
            }
        }
        int bot = 0;
        //bot
        for(int i = row+1; i < rows; i++){
            if(arr[i][col] < height){
                bot++;
            }else if(arr[i][col] >= height){
                bot++;
                break;
            }
        }
        return left * right * top * bot;
    }
}
