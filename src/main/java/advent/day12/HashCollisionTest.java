package advent.day12;

import java.util.HashMap;
import java.util.Random;

public class HashCollisionTest{
    public static int collisions = 0;
    public static int i;
    public static int j;
    public static int ITER = 10000;
    public static void main(String[] args){
        long heapSize = Runtime.getRuntime().maxMemory();
        System.out.println("Heap size MBs: " + heapSize/1048576D);
        Thread monitor = new Thread(new Runnable(){
            @Override
            public void run(){
                final int intervalMs = 2000;
                System.out.println("This thread will print info every " + intervalMs + " ms");
                while(true){
                    try{
                        Thread.sleep(intervalMs);
                    }catch (InterruptedException e){
                        throw new RuntimeException(e);
                    }
                    System.out.println("HashCol: " + collisions);
                    System.out.println("Col%: " + ((double)collisions/(i*ITER)) * 100 + " %");
                    System.out.println("i: " + i);
                }
            }
        });
        monitor.setDaemon(true);
        monitor.start();
        Point oneTwo = new Point(1,2);
        Point oneTwelve = new Point(1,12);
        HashMap<Integer, Point> points = new HashMap<>();
        //points.add(oneTwo);
        System.out.println(oneTwo.hashCode());
        System.out.println(new Point(1,2).hashCode());
        System.out.println(oneTwelve.hashCode());
        Random rand = new Random();
        int doubleMaxShort = (Short.MAX_VALUE*2 + 3);
        long st = System.currentTimeMillis();
        for (i = 0; i < ITER; i++){
            for (j = 0; j < ITER; j++){
                Point newPoint = new Point(rand.nextInt(doubleMaxShort), rand.nextInt(doubleMaxShort));
                //Point newPoint = new Point(i, j);
                int hash = newPoint.hashCode();
                if(points.containsKey(hash)){
                    Point previous = points.get(hash);
                    //should not equal if the loop guarantees they don't repeat
                    if(!newPoint.equals(previous)){
                        collisions++;
                    }
                }
                else{
                    points.put(newPoint.hashCode(), newPoint);
                }
            }
        }
        long en = System.currentTimeMillis();
        System.out.println("Millis: " + (en-st));
        System.out.println("Collisions: " + collisions);
        System.out.println("All: " + ITER*ITER);

    }
}
