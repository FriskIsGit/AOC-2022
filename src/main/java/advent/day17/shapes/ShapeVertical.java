package advent.day17.shapes;

public class ShapeVertical extends Shape{
    public static final int VERTICAL_HEIGHT = 4;
    /*  #
        #
        #
        #  */
    public int row, col;
    public ShapeVertical(int baseY){
        super(baseY);
        row = baseY;
        col = 2;
        place();
    }

    @Override
    public boolean pushRight(){
        if(cols() <= col + 1){
            return false;
        }
        //iterates from top
        for (int r = row - VERTICAL_HEIGHT + 1; r <= row; r++){
            if(map[r][col+1] != EMPTY){
                return false;
            }
        }
        remove();
        col++;
        place();
        return true;
    }

    @Override
    public boolean pushLeft(){
        if(col - 1 < 0){
            return false;
        }
        //iterates from top
        for (int r = row - VERTICAL_HEIGHT + 1; r <= row; r++){
            if(map[r][col-1] != EMPTY){
                return false;
            }
        }
        remove();
        col--;
        place();
        return true;
    }

    @Override
    public boolean fall(){
        if(row + 1 >= rows() || map[row + 1][col] != EMPTY){
            solidify();
            return false;
        }
        remove();
        row++;
        place();
        return true;
    }

    @Override
    void fillWith(char c){
        for (int r = row - VERTICAL_HEIGHT + 1; r <= row; r++){
            map[r][col] = c;
        }
    }
}
