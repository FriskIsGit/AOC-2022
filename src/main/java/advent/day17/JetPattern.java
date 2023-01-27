package advent.day17;

import java.util.Arrays;

public class JetPattern{
    public final char[] arr;
    private int i = 0;
    private final int length;

    public JetPattern(String pattern){
        if(pattern == null){
            throw new IllegalArgumentException("Pattern cannot be null");
        }
        if(pattern.isEmpty()){
            throw new IllegalArgumentException("Pattern cannot be empty");
        }
        arr = pattern.toCharArray();
        length = arr.length;
    }

    //always moves the pointer
    public char next(){
        if(i == length){
            i = 0;
        }
        return arr[i++];
    }

    public char current(){
        return arr[i-1];
    }
    public void reset(){
        i = 0;
    }
    public int getPointer(){
        return i;
    }
    public int length(){
        return length;
    }
    @Override
    public String toString(){
        return Arrays.toString(arr);
    }
}
