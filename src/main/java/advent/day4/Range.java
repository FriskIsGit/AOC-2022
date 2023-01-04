package advent.day4;

public class Range{
    public int lower, upper;

    public Range(int lower, int upper){
        this.lower = lower;
        this.upper = upper;
    }
    public boolean overlaps(Range other){
        return this.upper >= other.lower && other.upper >= this.lower;
    }
    public boolean fullyContains(Range other){
        if(this.upper < other.lower || other.upper < this.lower){
            return false;
        }
        if(other.lower <= this.lower && this.upper <= other.upper){
            return true;
        }
        return this.lower <= other.lower && other.upper <= this.upper;
    }
}
