package advent.day17;

import advent.day17.shapes.*;

public class ShapePattern{

    private static final int SIZE = 5;
    private int i = 0;

    public Shape next(int baseY){
        Shape currentShape;
        if(i == SIZE){
            i = 0;
        }
        switch (i){
            case 0:
                currentShape = new ShapeHorizontal(baseY);
                break;
            case 1:
                currentShape = new ShapePlus(baseY);
                break;
            case 2:
                currentShape = new ShapeL(baseY);
                break;
            case 3:
                currentShape = new ShapeVertical(baseY);
                break;
            case 4:
                currentShape = new ShapeSquare(baseY);
                break;
            default:
                throw new IllegalStateException("SIZE is incorrect");
        }
        i++;
        return currentShape;
    }

    public void reset(){
        i = 0;
    }

    public int size(){
        return SIZE;
    }
}
