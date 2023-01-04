package advent.day12;

import java.awt.Point;

public class Scratch{
    public static void main(String[] args){
        Point p = new Point(1,2);
        System.out.println(p.hashCode());
        System.out.println(new Point(1,2).hashCode());
        long bits = java.lang.Double.doubleToLongBits(7D);
        System.out.println(bits);
        int row = 351;
        int col = 43612;
        int out = (int) ((long)row << 32 | (long)col);
        //System.out.println(x);
        System.out.println(row);
    }

}
