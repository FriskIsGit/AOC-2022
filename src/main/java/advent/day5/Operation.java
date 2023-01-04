package advent.day5;

import java.util.List;
import java.util.Stack;

public class Operation{
    public int amount, from, to;
    private Operation(){

    }
    public static Operation move(int amount){
        Operation op = new Operation();
        op.amount = amount;
        return op;
    }
    public Operation from(int from){
        this.from = from;
        return this;
    }
    public Operation to(int to){
        this.to = to;
        return this;
    }
    public void executeOnAs9000(List<Stack<Character>> stacks){
        Stack<Character> fromStack = stacks.get(from-1);
        Stack<Character> toStack = stacks.get(to-1);
        //amount = Math.min(amount, fromStack.size());
        for (int i = 0; i < amount; i++){
            toStack.add(fromStack.pop());
        }
    }
    public void executeOnAs9001(List<Stack<Character>> stacks){
        Stack<Character> dummyStack = new Stack<>();
        Stack<Character> fromStack = stacks.get(from-1);
        Stack<Character> toStack = stacks.get(to-1);
        //amount = Math.min(amount, fromStack.size());
        for (int i = 0; i < amount; i++){
            dummyStack.add(fromStack.pop());
        }
        for (int i = 0; i < amount; i++){
            toStack.add(dummyStack.pop());
        }
    }

    @Override
    public String toString(){
        return "move " + amount + " from " + from + " to " + to;
    }
}
