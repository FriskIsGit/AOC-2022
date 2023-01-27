package advent.day17.shapes;

public class ShapeHorizontal extends Shape {
    // ####
    public static final int HORIZONTAL_LENGTH = 4;
    public int row, colLeft = 2;
    public ShapeHorizontal(int baseY){
        super(baseY);
        row = baseY;
        place();
    }

    @Override
    public boolean pushRight(){
        if(colLeft + HORIZONTAL_LENGTH >= cols() || map[row][colLeft + HORIZONTAL_LENGTH] != EMPTY){
            return false;
        }
        remove();
        colLeft++;
        place();
        return true;
    }

    @Override
    public boolean pushLeft(){
        if(colLeft - 1 < 0 || map[row][colLeft - 1] != EMPTY){
            return false;
        }
        remove();
        colLeft--;
        place();
        return true;
    }

    @Override
    public boolean fall(){
        if(row + 1 >= rows()){
            solidify();
            return false;
        }
        for (int col = colLeft; col < colLeft + HORIZONTAL_LENGTH; col++){
            if(map[row+1][col] != EMPTY){
                solidify();
                return false;
            }
        }
        remove();
        row++;
        place();
        return true;
    }

    @Override
    void fillWith(char c){
        for (int col = colLeft; col < colLeft + HORIZONTAL_LENGTH; col++){
            map[row][col] = c;
        }
    }

}
