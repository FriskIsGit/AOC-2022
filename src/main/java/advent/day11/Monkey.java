package advent.day11;

import java.util.ArrayList;
import java.util.List;

public class Monkey{
    public static List<Monkey> monkeys = new ArrayList<>();
    public List<Integer> items = new ArrayList<>();
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
            items.add(Integer.parseInt(item));
        }
        int lastItem = Integer.parseInt(itemStr.substring(st));
        items.add(lastItem);
    }
    //the only function which moves items
    public void doThrow(){
        if(items.size() < 1){
            return;
        }
        int first = items.remove(0);
        int newVal = operation.getWorryLevel(first);
        //relief after it has played with item
        newVal = newVal/3;
        Monkey monkey;
        if(newVal % divisible == 0){
            monkey = monkeys.get(trueMonkey);
        }
        else{
            monkey = monkeys.get(falseMonkey);
        }
        monkey.items.add(newVal);
        this.inspections++;
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
