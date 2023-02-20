package advent.day21;

import advent.AdventOfCode;

import java.util.*;

public class Day21{
    public static void main(String[] args){
        List<String> lines = AdventOfCode.readDay(21);
        SymbolMap map = new SymbolMap(lines);
        doBothParts(map);
    }

    private static void doBothParts(SymbolMap map){
        Symbol root = map.bindings.get("root");
        assert root != null;
        decode(root);
        if(!root.hasResult){
            throw new IllegalStateException("Root hasn't been decoded");
        }
        System.out.println("Root result: " + root.result);

        List<String> backtrack = map.humanEffect();
        assert backtrack.size() > 0;
        Collections.reverse(backtrack);

        //set both root symbols equal
        root.sign = '=';
        String topMost = backtrack.get(0);
        if(root.s1.name.equals(topMost)){
            //left side of root is human
            root.s1.result = root.s2.result;
        }else{
            //right side of root is human
            root.s2.result = root.s1.result;
        }

        long expected = Long.MAX_VALUE;
        for (int i = 0; i < backtrack.size()-1; i++){
            Symbol main = map.bindings.get(backtrack.get(i));
            Symbol human = map.bindings.get(backtrack.get(i+1)); //human has const value
            Side knownSide = human.name.equals(main.s1.name) ? Side.RIGHT : Side.LEFT;
            expected = calculateUnknown(main, knownSide);
            if(knownSide == Side.LEFT){
                main.s2.result = expected;
            }else{
                main.s1.result = expected;
            }
        }
        System.out.println("Final expected: " + expected);
    }

    private static long calculateUnknown(Symbol main, Side known){
        // main = expected =
        long expected = main.result;
        switch (known){
            case LEFT:
                //expected = main.s1 sign x
                switch (main.sign){
                    case '+':
                        return expected - main.s1.result;
                    case '-':
                        return main.s1.result - expected;
                    case '*':
                        return expected / main.s1.result;
                    case '/':
                        return main.s1.result / expected;
                }
                break;
            case RIGHT:
                //expected = x sign main.s2
                switch (main.sign){
                    case '+':
                        return expected - main.s2.result;
                    case '-':
                        return expected + main.s2.result;
                    case '*':
                        return expected / main.s2.result;
                    case '/':
                        return expected * main.s2.result;
                }
                break;
        }
        throw new Error();
    }

    private static void decode(Symbol symbol){
        if(symbol.hasResult){
            return;
        }
        if(!symbol.s1.hasResult){
            decode(symbol.s1);
        }
        if(!symbol.s2.hasResult){
            decode(symbol.s2);
        }
        assert symbol.s1.hasResult;
        assert symbol.s2.hasResult;

        long num1 = symbol.s1.result;
        char sign = symbol.sign;
        long num2 = symbol.s2.result;
        switch (sign){
            case '+':
                symbol.setResult(num1 + num2);
                break;
            case '-':
                symbol.setResult(num1 - num2);
                break;
            case '*':
                symbol.setResult(num1 * num2);
                break;
            case '/':
                //no remainder
                symbol.setResult(num1 / num2);
                break;
        }
    }
}
