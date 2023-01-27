package advent.day17.shapes;

public class ShapeL extends Shape{
    /*
       #    #
       #    #
     ###  ##C
     */
    public int botRow, botRightCol = 4;
    public ShapeL(int baseY){
        super(baseY);
        botRow = baseY;
        place();
    }

    @Override
    public boolean pushRight(){
        if(cols() <= botRightCol + 1){
            return false;
        }
        //iterates from top
        for (int r = botRow - 2; r <= botRow; r++){
            if(map[r][botRightCol+1] != EMPTY){
                return false;
            }
        }
        remove();
        botRightCol++;
        place();
        return true;
    }

    @Override
    public boolean pushLeft(){
        if(botRightCol - 3 < 0 || map[botRow][botRightCol-3] != EMPTY){
            return false;
        }
        if(map[botRow-1][botRightCol-1] != EMPTY || map[botRow-2][botRightCol-1] != EMPTY){
            return false;
        }
        remove();
        botRightCol--;
        place();
        return true;
    }

    @Override
    public boolean fall(){
        if(botRow + 1 >= rows()){
            solidify();
            return false;
        }
        for (int col = botRightCol - 2; col <= botRightCol; col++){
            if(map[botRow+1][col] != EMPTY){
                solidify();
                return false;
            }
        }
        remove();
        botRow++;
        place();
        return true;
    }


    @Override
    void fillWith(char c){
        map[botRow][botRightCol] = c;
        map[botRow][botRightCol-1] = c;
        map[botRow][botRightCol-2] = c;

        map[botRow-1][botRightCol] = c;
        map[botRow-2][botRightCol] = c;
    }
}
