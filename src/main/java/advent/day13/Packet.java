package advent.day13;

import java.util.ArrayList;
import java.util.List;

public class Packet{
    enum Comparison{
        LEFT_MORE, EQUAL, RIGHT_MORE, LEFT_OUT_OF_ELEMENTS, RIGHT_OUT_OF_ELEMENTS, OUT_OF_ELEMENTS;
    }
    public List<Object> contents = new ArrayList<>();
    private Packet(){
    }
    public int size(){
        return contents.size();
    }

    //left < right -> ordered
    public boolean isOrdered(Packet right){
        Comparison comparison = isOrderedImpl(this, right);
        switch (comparison){
            case RIGHT_MORE:
            case LEFT_OUT_OF_ELEMENTS:
                return true;
            case OUT_OF_ELEMENTS:
                int leftLen = this.size(), rightLen = right.size();
                //handle
                return leftLen < rightLen;
            case RIGHT_OUT_OF_ELEMENTS:
            case LEFT_MORE:
                return false;
            default:
                throw new IllegalStateException("Unexpected comparison: " + comparison);
        }
    }
    private static Comparison isOrderedImpl(Object left, Object right){
        boolean isLeftPacket = left instanceof Packet;
        boolean isRightPacket = right instanceof Packet;
        if(isLeftPacket && isRightPacket){
            Packet leftP = (Packet) left;
            Packet rightP = (Packet) right;

            int leftLen = leftP.size(), rightLen = rightP.size();
            int minSize = Math.min(leftLen, rightLen);
            if(minSize == 0){
                if(leftLen == rightLen){
                    return Comparison.OUT_OF_ELEMENTS;
                }
                if(leftLen < rightLen){
                    return Comparison.LEFT_OUT_OF_ELEMENTS;
                }else{
                    return Comparison.RIGHT_OUT_OF_ELEMENTS;
                }
            }

            for (int i = 0; i < minSize; i++){
                Comparison res = isOrderedImpl(leftP.contents.get(i), rightP.contents.get(i));
                switch (res){
                    case LEFT_MORE:
                        //quit already
                        return Comparison.LEFT_MORE;
                    case LEFT_OUT_OF_ELEMENTS:
                        //quit already
                        return Comparison.LEFT_OUT_OF_ELEMENTS;
                    case RIGHT_OUT_OF_ELEMENTS:
                        //quit already
                        return Comparison.RIGHT_OUT_OF_ELEMENTS;
                    case RIGHT_MORE:
                        //quit already
                        return Comparison.RIGHT_MORE;
                }
            }
            if(leftLen == rightLen){
                return Comparison.OUT_OF_ELEMENTS;
            }
            if(leftLen < rightLen){
                return Comparison.LEFT_OUT_OF_ELEMENTS;
            }else{
                return Comparison.RIGHT_OUT_OF_ELEMENTS;
            }
        }
        //one is an Integer, the other one isn't
        else if(isLeftPacket || isRightPacket){
            Packet wrapperPacket = new Packet();
            //leftPacket != Integer
            if(isLeftPacket){
                wrapperPacket.contents.add(right);
                return isOrderedImpl(left, wrapperPacket);
            }
            else{
                wrapperPacket.contents.add(left);
                return isOrderedImpl(wrapperPacket, right);
            }
        }
        //both Integers
        else{
            int leftNum = (int) left;
            int rightNum = (int) right;
            // if left < right -> 1
            int compare = Integer.compare(rightNum, leftNum);
            switch (compare){
                case 1:
                    return Comparison.RIGHT_MORE;
                case 0:
                    return Comparison.EQUAL;
                case -1:
                    return Comparison.LEFT_MORE;
            }
        }
        return Comparison.OUT_OF_ELEMENTS;
    }
    public static Packet parsePacket(String packet){
        int length = packet.length();
        if(packet.charAt(0) != '['){
            throw new IllegalArgumentException("Unexpected char at index 0");
        }
        if(packet.charAt(length-1) != ']'){
            throw new IllegalArgumentException("Unexpected char at index " + (length-1));
        }
        final int[] lastIndex = {0};
        return parseImpl(packet, lastIndex);
    }
    private static Packet parseImpl(String packet, int[] packetEnd){
        Packet p = new Packet();
        for (int i = 1; i < packet.length(); i++){
            char c = packet.charAt(i);
            if(Character.isDigit(c)){
                char next = packet.charAt(i+1);
                if(Character.isDigit(next)){
                    //add 10 :)
                    p.contents.add(10);
                    i++;
                }else{
                    //add 0-9
                    p.contents.add(c - 48);
                }
            }else if(c == '['){
                Packet internal = parseImpl(packet.substring(i), packetEnd);
                p.contents.add(internal);
                i = i + packetEnd[0];
            }
            else if(c == ']'){
                packetEnd[0] = i;
                return p;
            }
        }
        throw new IllegalArgumentException("Packet was never closed");
    }
    public boolean equals(Packet packet){
        return this.toString().equals(packet.toString());
    }
    public String toString(){
        StringBuilder str = new StringBuilder();
        int size = contents.size();
        str.append('[');
        for (int i = 0; i < size; i++){
            Object item = contents.get(i);
            if(item instanceof Integer){
                str.append(item);
            }
            else if(item instanceof Packet){
                Packet packet = (Packet) item;
                str.append(packet);
            }
            if(i != size-1){
                str.append(",");
            }
        }
        str.append(']');
        return str.toString();
    }
}
