package advent.day12;

import java.util.HashSet;

public class HashTest{
    public static void main(String[] args){
        HashSet<Point> points = new HashSet<>();
        Point p1 = new Point(20,6);
        Point p2 = new Point(20,6);
        Point p3 = new Point(20,6);
        points.add(p1);
        points.add(p2);
        System.out.println(points.contains(p3));
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());
        System.out.println(p3.hashCode());
    }
}
