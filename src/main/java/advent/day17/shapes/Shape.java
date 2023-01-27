package advent.day17.shapes;

public abstract class Shape{
    public Shape(int baseY){
        this.baseY = baseY;
    }
    public static final char ROCK = '#', FALLING_ROCK = '@', EMPTY = '.';
    public static char[][] map;
    public final int baseY;
    public boolean isSolid = false;

    //returns true if call caused a change in the position
    public abstract boolean pushRight();
    public abstract boolean pushLeft();
    public abstract boolean fall();

    //methods responsible for updates inside the map
    abstract void fillWith(char c);
    public void solidify(){
        isSolid = true;
        fillWith(ROCK);
    }
    public void remove(){
        fillWith(EMPTY);
    }
    public void place(){
        fillWith(FALLING_ROCK);
    }
    public static void setMap(char[][] map){
        Shape.map = map;
    }
    public static int rows(){
        return map.length;
    }
    public static int cols(){
        return map[0].length;
    }
}
