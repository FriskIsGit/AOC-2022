package advent.day20;

public class LinkedBuffer{
    public final LinkedElement head;
    public final long[] pattern;
    public final int size;

    public LinkedBuffer(long[] pattern){
        if(pattern == null || pattern.length < 1){
            throw new IllegalArgumentException("Pattern array cannot be null or be size 0");
        }
        this.pattern = pattern;
        this.size = pattern.length;
        this.head = new LinkedElement(pattern[0], 0);
        LinkedElement explorer = head;
        for (int i = 1; i < size; i++){
            explorer.next = new LinkedElement(pattern[i], i);
            explorer.next.prvs = explorer;
            explorer = explorer.next;
        }
        explorer.next = head;
        head.prvs = explorer;
    }

    public long getCoordinateSum(){
        long sum = 0;
        LinkedElement zero = locateZero();
        sum += valueAtFrom(1000 % size, zero);
        sum += valueAtFrom(2000 % size, zero);
        sum += valueAtFrom(3000 % size, zero);
        return sum;
    }

    private long valueAtFrom(int index, LinkedElement from){
        for (int i = 0; i < index; i++){
            from = from.next;
        }
        return from.value;
    }

    private LinkedElement locateZero(){
        LinkedElement explorer = head;
        for (int i = 0; i < size; i++){
            if(explorer.value == 0){
                return explorer;
            }
            explorer = explorer.next;
        }
        throw new IllegalStateException("Failed to locate zero");
    }

    public void mix(){
        for (int p = 0; p < size; p++){
            LinkedElement move = locate(pattern[p], p);
            LinkedElement left = move.prvs, right = move.next;
            //the element is first removed and the target location is found based on size without that element
            long mod = move.value % (size-1);
            if(mod == 0){
                continue;
            }
            LinkedElement target = move;
            //move to target location
            boolean negative = false;
            if(mod < 0){
                negative = true;
                mod = Math.abs(mod);
                for (int i = 0; i < mod; i++){
                    target = target.prvs;
                }
            }else{
                for (int i = 0; i < mod; i++){
                    target = target.next;
                }
            }
            //remove middle element
            left.next = right;
            right.prvs = left;
            //nullify for good practice, should be overwritten anyway though
            move.prvs = null;
            move.next = null;
            if(negative){
                placeBetween(target.prvs, move, target);
                continue;
            }
            placeBetween(target, move, target.next);
        }
    }

    private void placeBetween(LinkedElement left, LinkedElement mid, LinkedElement right){
        if(left == null || mid == null || right == null){
            throw new IllegalArgumentException("Null arguments were given");
        }
        left.next = mid;
        mid.prvs = left;
        mid.next = right;
        right.prvs = mid;
    }

    public LinkedElement locate(long val, int index){
        LinkedElement explorer = head;
        for (int i = 0; i < size; i++){
            if(explorer.originalIndex == index){
                return explorer;
            }
            explorer = explorer.next;
        }
        throw new IllegalStateException("Value: " + val + " at index: " + index + " wasn't found");
    }
    public long[] toArray(){
        long[] arr = new long[size];
        LinkedElement explorer = head;
        for (int i = 0; i < size; i++){
            arr[i] = explorer.value;
            explorer = explorer.next;
        }
        return arr;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append('[');
        LinkedElement explorer = head;
        for (int i = 0; i < size; i++){
            str.append(explorer);
            if(i != size - 1){
                str.append(", ");
            }
            explorer = explorer.next;
        }
        str.append(']');
        return str.toString();
    }
}

