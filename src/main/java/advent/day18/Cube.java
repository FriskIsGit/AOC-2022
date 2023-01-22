package advent.day18;

public class Cube{
    public int x,y,z;
    public int connections = 0;

    public Cube(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString(){
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public boolean equals(Cube cube){
        return this.x == cube.x && this.y == cube.y && this.z == cube.z;
    }
    @Override
    public int hashCode(){
        return x << 16 | y << 8 | z;
    }
    public boolean isConnected(Cube cube){
        //at the same level
        if(this.z == cube.z){
            if(cube.y == this.y){
                return cube.x == this.x - 1 || cube.x == this.x + 1;
            }else if(cube.x == this.x){
                return cube.y == this.y - 1 || cube.y == this.y + 1;
            }
            return false;
        }
        //above or below
        else if(this.z == cube.z + 1 || this.z == cube.z - 1){
            return this.x == cube.x && this.y == cube.y;
        }
        return false;
    }
    public boolean isWithinBounds(int lowerBound, int upperBound){
        if(this.x < lowerBound || this.x > upperBound){
            return false;
        }
        if(this.y < lowerBound || this.y > upperBound){
            return false;
        }
        return this.z >= lowerBound && this.z <= upperBound;
    }
}
