package advent.day20;

import java.util.Arrays;

public class BufferArray{
    public static final boolean PRINT = false;
    public final int[] arr;
    public final int[] pattern;
    public final int size;
    public BufferArray(int[] arr){
        this.arr = arr;
        this.size = arr.length;
        this.pattern = new int[size];
        System.arraycopy(arr, 0, pattern, 0, size);
    }

    public int getCoordinateSum(){
        int sum = 0;
        int zeroIndex = locate(0);
        sum += arr[(zeroIndex + 1000) % size];
        sum += arr[(zeroIndex + 2000) % size];
        sum += arr[(zeroIndex + 3000) % size];
        return sum;
    }

    public void mix(){
        for (int p = 0; p < size; p++){
            if(PRINT){
                System.out.println("About to do move on value: " + pattern[p]);
                System.out.println(this);
            }
            int i = locate(pattern[p]);
            int move = pattern[p] % size;
            if(move > 0){
                int next = i + move;
                //overflow
                if(next >= size){
                    next -= size;
                    System.arraycopy(arr, i+1, arr, i, size - i - 1);
                    arr[size-1] = arr[0];
                    if (next > 0){
                        System.arraycopy(arr, 1, arr, 0, next);
                    }
                }else{
                    //shift to the left (full)
                    System.arraycopy(arr, i+1, arr, i, move);
                }
                arr[next] = pattern[p];
            }else if(move < 0){
                int next = i + move;
                if(next < 0){
                    next = size - (-next);
                    System.arraycopy(arr, 0, arr, 1, i);
                    arr[0] = arr[size-1];
                    int remaining = (-move) - 1 - i;
                    if (remaining > 0){
                        System.arraycopy(arr, size-1 - remaining, arr, size - remaining, remaining);
                    }
                }else{
                    //shift to the right (full)
                    System.arraycopy(arr, next, arr, next+1, -move);
                }
                arr[next] = pattern[p];
            }
            //zero does not move
        }
    }

    private int locate(int value){
        for (int i = 0; i < size; i++){
            if(arr[i] == value){
                return i;
            }
        }
        throw new IllegalStateException("Value " + value + " wasn't found.");
    }

    @Override
    public String toString(){
        return Arrays.toString(arr);
    }
}
