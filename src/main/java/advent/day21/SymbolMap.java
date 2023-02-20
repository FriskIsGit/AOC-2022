package advent.day21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//stores/creates bindings and symbols
public class SymbolMap{
    public HashMap<String, Symbol> bindings = new HashMap<>();
    public List<Symbol> symbols = new ArrayList<>();
    public SymbolMap(List<String> lines){
        parseSymbols(lines);
    }
    private List<Symbol> parseSymbols(List<String> lines){
        for(String line : lines){
            String name = line.substring(0, 4);
            char sixth = line.charAt(6);
            if(Character.isDigit(sixth)){
                int num = Integer.parseInt(line.substring(6));
                Symbol symbol = new Symbol(name, num);
                symbols.add(symbol);
                bindings.put(name, symbol);
                continue;
            }
            String name1 = line.substring(6, 10);
            char sign = line.charAt(11);
            String name2 = line.substring(13);
            Symbol symbol = new Symbol(name, name1, sign, name2);
            symbols.add(symbol);
            bindings.put(name, symbol);
        }
        bindReferences();
        return symbols;
    }
    private void bindReferences(){
        for (Map.Entry<String, Symbol> entry : bindings.entrySet()) {
            Symbol symbol = entry.getValue();
            if(symbol.hasResult){
                continue;
            }
            Symbol s1 = bindings.get(symbol.s1.name);
            if(s1 == null){
                throw new IllegalArgumentException("Unknown mapping for string: " + symbol.s1.name);
            }
            Symbol s2 = bindings.get(symbol.s2.name);
            if(s2 == null){
                throw new IllegalArgumentException("Unknown mapping for string: " + symbol.s2.name);
            }
            symbol.s1 = s1;
            symbol.s2 = s2;
        }
    }
    //returns all symbol names that the human variable has an effect on, starting from the bottom most expression
    public List<String> humanEffect(){
        List<String> backtracking = new ArrayList<>();
        String backtrack = "humn";
        backtracking.add(backtrack);
        while(true){
            for (Symbol s : symbols){
                boolean isRoot = s.isRoot();
                if (s.s1 != null && s.s1.name.equals(backtrack)){
                    if(isRoot){
                        return backtracking;
                    }
                    backtrack = s.name;
                    backtracking.add(backtrack);
                    break;
                }
                if (s.s2 != null && s.s2.name.equals(backtrack)){
                    if(isRoot){
                        return backtracking;
                    }
                    backtrack = s.name;
                    backtracking.add(backtrack);
                    break;
                }
            }
        }
    }
}


