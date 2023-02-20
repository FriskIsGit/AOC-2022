package advent.day20;

public class Element{
    public final long value, originalIndex;
    public Element(long value, int initialIndex){
        this.value = value;
        this.originalIndex = initialIndex;
    }

    @Override
    public String toString(){
        return "(" + value + ",i:" + originalIndex + ')';
    }
}
