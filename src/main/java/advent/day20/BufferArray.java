package advent.day20;

import java.util.Arrays;

public class BufferArray{
    public static final boolean PRINT = false;
    public final Element[] arr;
    public final long[] pattern;
    public final int size;
    public BufferArray(long[] pattern){
        this.size = pattern.length;
        this.pattern = pattern;
        this.arr = new Element[size];
        for (int i = 0; i < size; i++){
            arr[i] = new Element(pattern[i], i);
        }
    }

    public long getCoordinateSum(){
        long sum = 0;
        int zeroIndex = locateZero();
        sum += arr[(zeroIndex + 1000) % size].value;
        sum += arr[(zeroIndex + 2000) % size].value;
        sum += arr[(zeroIndex + 3000) % size].value;
        return sum;
    }

    public void mix(){
        for (int p = 0; p < size; p++){
            if(PRINT){
                System.out.println("About to do move on value: " + pattern[p]);
                System.out.println(this);
            }
            int i = locate(p);
            //the element is first removed and the target location is found based on size without that element
            int move = (int) (pattern[p] % (size-1));
            Element temp = arr[i];
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
                arr[next] = temp;
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
                arr[next] = temp;
            }
            //zero does not move
        }
    }

    private int locateZero(){
        for (int i = 0; i < size; i++){
            if(arr[i].value == 0){
                return i;
            }
        }
        throw new IllegalStateException("Value " + 0 + " wasn't found.");
    }
    private int locate(int index){
        for (int i = 0; i < size; i++){
            if(arr[i].originalIndex == index){
                return i;
            }
        }
        throw new IllegalStateException("Index of: " + index + " wasn't found.");
    }

    @Override
    public String toString(){
        return Arrays.toString(arr);
    }
}
