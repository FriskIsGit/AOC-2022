package advent.day15;

public class ColumnRange{
    //inclusive range
    public int lower, upper;
    public ColumnRange(int lower, int upper){
        if(lower > upper){
            throw new IllegalArgumentException("Lower bound cannot be larger than upper");
        }
        this.lower = lower;
        this.upper = upper;
    }
    public ColumnRange merge(int lower, int upper){
        this.lower = Math.min(this.lower, lower);
        this.upper = Math.max(this.upper, upper);
        return this;
    }
    public boolean isSeparated(ColumnRange other){
        return isSeparated(other.lower, other.upper);
    }
    public boolean isSeparated(int lower, int upper){
        return this.upper < lower || upper < this.lower;
    }
    public ColumnRange merge(ColumnRange range){
        return merge(range.lower, range.upper);
    }
    public int elements(){
        return upper - lower + 1;
    }
    public String toString(){
        return "[" + lower + ", " + upper + "]";
    }
}
