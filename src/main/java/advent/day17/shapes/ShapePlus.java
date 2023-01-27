package advent.day17.shapes;

public class ShapePlus extends Shape{
    /*  #
       ###
        #  */
    public int[] top = new int[2], left = new int[2], mid = new int[2], right = new int[2], bot = new int[2];
    public ShapePlus(int baseY){
        super(baseY);
        //row                col
        bot[0] = baseY;       bot[1] = 3;
        left[0] = baseY - 1;  left[1] = 2;
        mid[0] = baseY - 1;   mid[1] = 3;
        right[0] = baseY - 1; right[1] = 4;
        top[0] = baseY - 2;   top[1] = 3;
        place();
    }

    @Override
    public boolean pushRight(){
        if(right[1] + 1 >= cols() || map[right[0]][right[1] + 1] != EMPTY){
            return false;
        }
        if(map[top[0]][top[1] + 1] != EMPTY){
            return false;
        }
        if(map[bot[0]][bot[1] + 1] != EMPTY){
            return false;
        }
        remove();
        right[1]++;
        top[1]++;
        bot[1]++;
        left[1]++;
        mid[1]++;
        place();
        return true;
    }

    @Override
    public boolean pushLeft(){
        if(left[1] - 1 < 0 || map[left[0]][left[1] - 1] != EMPTY){
            return false;
        }
        if(map[top[0]][top[1] - 1] != EMPTY){
            return false;
        }
        if(map[bot[0]][bot[1] - 1] != EMPTY){
            return false;
        }
        remove();
        right[1]--;
        top[1]--;
        bot[1]--;
        left[1]--;
        mid[1]--;
        place();
        return true;
    }

    @Override
    public boolean fall(){
        if(bot[0] + 1 >= rows() || map[bot[0] + 1][bot[1]] != EMPTY){
            solidify();
            return false;
        }
        if(map[left[0] + 1][left[1]] != EMPTY){
            solidify();
            return false;
        }
        if(map[right[0] + 1][right[1]] != EMPTY){
            solidify();
            return false;
        }
        remove();
        right[0]++;
        top[0]++;
        bot[0]++;
        left[0]++;
        mid[0]++;
        place();
        return true;
    }

    @Override
    void fillWith(char c){
        map[right[0]][right[1]] = c;
        map[top[0]][top[1]] = c;
        map[bot[0]][bot[1]] = c;
        map[left[0]][left[1]] = c;
        map[mid[0]][mid[1]] = c;
    }
}
