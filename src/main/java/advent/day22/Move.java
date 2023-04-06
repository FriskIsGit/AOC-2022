package advent.day22;

import advent.day22.enums.Direction;

public class Move{
    //this flag exists for more convenient loops with .next() without handling the first element separately
    private boolean first = true;
    private int i = 0;
    public int steps;
    public Direction dir = Direction.RIGHT;
    private final char[] arr;
    private final int size;

    public Move(String path){
        this.arr = path.toCharArray();
        this.size = arr.length;
        nextNumber();
    }
    public boolean next(){
        if(first){
            first = false;
            return true;
        }
        if(i == size){
            return false;
        }
        nextDirection(arr[i++]);
        //should end with a number so if it returns false, it means it's the last one
        nextNumber();
        return true;
    }

    public void reset(){
        first = true;
        i = 0;
        dir = Direction.RIGHT;
        nextNumber();
    }
    //returns true if and only if the second digit check was out of bounds
    private void nextNumber(){
        int dig1 = arr[i++] - 48;
        if(i == size){
            return;
        }
        if(Character.isDigit(arr[i])){
            steps = dig1 * 10 + (arr[i] - 48);
            i++;
        }else{
            steps = dig1;
        }
    }

    private void nextDirection(char rotation){
        if(rotation == 'L'){
            switch (dir){
                case UP:
                    dir = Direction.LEFT;
                    break;
                case LEFT:
                    dir = Direction.DOWN;
                    break;
                case DOWN:
                    dir = Direction.RIGHT;
                    break;
                case RIGHT:
                    dir = Direction.UP;
                    break;
            }
        }else if(rotation == 'R'){
            switch (dir){
                case UP:
                    dir = Direction.RIGHT;
                    break;
                case RIGHT:
                    dir = Direction.DOWN;
                    break;
                case DOWN:
                    dir = Direction.LEFT;
                    break;
                case LEFT:
                    dir = Direction.UP;
                    break;
            }
        }else{
            throw new IllegalArgumentException("Given char is neither L nor R");
        }
    }
    @Override
    public String toString(){
        return "Move{" +
                "steps=" + steps +
                ", dir=" + dir +
                '}';
    }
}
