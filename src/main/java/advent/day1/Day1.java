package advent.day1;

import java.util.Scanner;

public class Day1{
    //reads input straight from console
    public static void main(String[] args){
        int max = task1(new Scanner(System.in));
        System.out.println(max);
    }
    public static int task1(Scanner keyboard) {
        boolean nextEmpty = false;
        String line;
        int sum = 0;
        int max = -1;
        MaxQueue queue = new MaxQueue(3);
        long st = -1, end;
        while ((line = keyboard.nextLine()) != null) {
            if (line.isEmpty()) {
                if(nextEmpty){
                    break;
                }

                max = Math.max(sum, max);
                queue.place(sum);
                sum = 0;
                nextEmpty = true;
            }else{
                sum += Integer.parseInt(line);
                nextEmpty = false;
            }
        }
        end = System.currentTimeMillis();
        System.out.println("Diff ms: " + (end-st));
        System.out.println(queue);
        System.out.println(queue.arr[0] + queue.arr[1] + queue.arr[2]);
        return max;
    }
}
