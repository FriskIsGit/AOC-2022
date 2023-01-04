package advent.day1;

import java.util.Arrays;

public class MaxQueue{
    //positive values, possibly use Arrays.fill(Integer.MIN_VALUE)
    public int[] arr;
    public int lastIndex;
    public MaxQueue(int size){
        lastIndex = size - 1;
        arr = new int[size];
    }
    public void place(int el){
        int index = -1;
        for(int i = lastIndex; i>-1; i--){
            int current = arr[i];
            if(current < el){
                index = i;
            }
            else{
                break;
            }
        }
        if(index != -1){
            System.arraycopy(arr, index, arr, index+1, lastIndex - index);
            arr[index] = el;
        }
    }
    @Override
    public String toString(){
        return Arrays.toString(arr);
    }
}