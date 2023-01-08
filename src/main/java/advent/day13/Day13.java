package advent.day13;

import advent.AdventOfCode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day13{
    public static void main(String[] args){
        part1();
        part2();
    }

    private static void part1(){
        List<String> lines = AdventOfCode.readDay(13);
        long st = System.currentTimeMillis();
        int SIZE = lines.size();
        int validPairs = 0, index = 1, indices = 0;
        for (int i = 0; i < SIZE-1; i+=3, index++){
            String line1 = lines.get(i);
            String line2 = lines.get(i+1);
            Packet p1 = Packet.parsePacket(line1);
            Packet p2 = Packet.parsePacket(line2);
            boolean res = p1.isOrdered(p2);
            if(res){
                validPairs++;
                indices+=index;
            }
            System.out.println(index + " " + line1);
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (end-st));
        System.out.println("Valid pairs: " + validPairs);
        System.out.println("indices: " + indices);
    }
    private static void part2(){
        List<String> lines = AdventOfCode.readDay(13);
        Packet PACKET_2 = Packet.parsePacket("[[2]]");
        Packet PACKET_6 = Packet.parsePacket("[[6]]");
        long st = System.currentTimeMillis();
        int SIZE = lines.size();
        int count = 0;
        //count packets
        for (int i = 0; i < SIZE-1; i+=3){
            count+=2;
        }
        //add two for additional divider packets
        count+=2;
        System.out.println("ALL packets: " + count);
        Packet[] packets = new Packet[count];
        int index = 1;
        for (int i = 0, p = 0; i < SIZE-1; i+=3, index++, p+=2){
            String line1 = lines.get(i);
            String line2 = lines.get(i+1);
            Packet p1 = Packet.parsePacket(line1);
            Packet p2 = Packet.parsePacket(line2);
            packets[p] = p1;
            packets[p+1] = p2;
        }

        packets[count-1] = PACKET_2;
        packets[count-2] = PACKET_6;
        Arrays.sort(packets, new Comparator<Packet>(){
            @Override
            public int compare(Packet o1, Packet o2){
                boolean order = o1.isOrdered(o2);
                if(order){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        });
        int first =-1, second = -1;
        for (int i = 0; i < count; i++){
            Packet p = packets[i];
            if(first == -1 && p.equals(PACKET_2)){
                first = i+1;
            }
            else if(second == -1 && p.equals(PACKET_6)){
                second = i+1;
            }
        }
        System.out.println("Product: " + (first*second));
        long en = System.currentTimeMillis();
        System.out.println("Time taken ms: " + (en-st));
    }
}
