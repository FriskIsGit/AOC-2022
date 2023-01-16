package advent.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Structure{
    public List<Rock> rockPath = new ArrayList<>();
    public static final String ROCK_SEPARATOR = " -> ";

    private Structure(){
    }
    public Structure(Rock ... rocks){
        rockPath.addAll(Arrays.asList(rocks));
    }


    public static Structure parseStructure(String str){
        Structure struct = new Structure();
        boolean parsing = true;
        String rockStr;
        int index, from = 0;
        while(parsing){
            index = str.indexOf(ROCK_SEPARATOR, from);
            if(index == -1){
                rockStr = str.substring(from);
                parsing = false;
            }else{
                rockStr = str.substring(from, index);
            }
            from = index + ROCK_SEPARATOR.length();
            int comma = rockStr.indexOf(',');
            int col = Integer.parseInt(rockStr.substring(0, comma));
            int row = Integer.parseInt(rockStr.substring(comma+1));
            struct.rockPath.add(new Rock(row, col));
        }
        return struct;
    }
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        if(rockPath.size() < 1){
            return "";
        }
        for (int i = 0; true; i++){
            Rock rock = rockPath.get(i);
            str.append(rock);
            if(rockPath.size() - 1 == i){
                break;
            }
            else{
                str.append(ROCK_SEPARATOR);
            }
        }
        return str.toString();
    }
}
class Rock{
    public int row, col;

    public Rock(int row, int col){
        if(row < 0 || col < 0){
            throw new IllegalArgumentException("Row or column cannot be negative");
        }
        this.row = row;
        this.col = col;
    }
    @Override
    public String toString(){
        return "" + col + ',' + row;
    }
}
