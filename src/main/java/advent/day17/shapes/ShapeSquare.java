package advent.day17.shapes;

public class ShapeSquare extends Shape{
    public int botRow, botLeftCol = 2;
    public ShapeSquare(int minY){
        super(minY);
        botRow = minY;
        place();
    }

    @Override
    public boolean pushRight(){
        if(botLeftCol + 2 >= cols() || map[botRow][botLeftCol+2] != EMPTY || map[botRow-1][botLeftCol+2] != EMPTY){
            return false;
        }
        remove();
        botLeftCol++;
        place();
        return true;
    }

    @Override
    public boolean pushLeft(){
        if(botLeftCol - 1 < 0 || map[botRow][botLeftCol-1] != EMPTY || map[botRow-1][botLeftCol-1] != EMPTY){
            return false;
        }
        remove();
        botLeftCol--;
        place();
        return true;
    }

    @Override
    public boolean fall(){
        if(botRow + 1 >= rows() || map[botRow+1][botLeftCol] != EMPTY || map[botRow+1][botLeftCol+1] != EMPTY){
            solidify();
            return false;
        }
        remove();
        botRow++;
        place();
        return true;
    }

    @Override
    void fillWith(char c){
        map[botRow][botLeftCol] = c;
        map[botRow][botLeftCol+1] = c;

        map[botRow-1][botLeftCol] = c;
        map[botRow-1][botLeftCol+1] = c;
    }
}
