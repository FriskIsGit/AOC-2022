package advent.day20;

public class Element{
    public final int value, index;
    public Element(int value, int index){
        this.value = value;
        this.index = index;
    }

    @Override
    public String toString(){
        return "{" +
                "v=" + value +
                ":i=" + index +
                '}';
    }
}
