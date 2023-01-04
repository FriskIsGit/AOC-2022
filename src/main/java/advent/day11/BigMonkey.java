package advent.day11;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigMonkey{
    public static List<BigMonkey> monkeys = new ArrayList<>();
    public static long biggestCommonMultiple = 1;
    public List<Long> items = new ArrayList<>();
    public Operation operation;
    public int divisible, inspections = 0;
    public int trueMonkey, falseMonkey;

    //expecting a digit of an element at index 0
    //items separated with a comma and a space after it
    public void parseItems(String itemStr){
        int st = 0, end;
        while((end = itemStr.indexOf(',', st)) != -1 && st<end){
            String item = itemStr.substring(st, end);
            st = end + 2;
            items.add(Long.parseLong(item));
        }
        String lastItem = itemStr.substring(st);
        items.add(Long.parseLong(lastItem));
    }
    //the only function which moves items
    public void doThrows(){
        int size = items.size();
        for (int i = 0; i < size; i++){
            long item = items.get(i);
            long newVal = operation.getBigWorryLevel(item);
            //no relief this time
            BigMonkey monkey;
            if(newVal % divisible == 0){
                monkey = monkeys.get(trueMonkey);
            }
            else{
                monkey = monkeys.get(falseMonkey);
            }
            monkey.items.add(newVal);
        }
        this.inspections += size;
        this.items.clear();
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("{");
        str.append(items).append(", ");
        str.append(operation).append(", ");
        str.append(divisible).append(", ");
        str.append(trueMonkey).append(", ");
        str.append(falseMonkey);
        str.append("}");
        return str.toString();
    }

}
