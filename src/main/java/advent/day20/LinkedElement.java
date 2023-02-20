package advent.day20;

class LinkedElement extends Element{
    public LinkedElement prvs, next;
    public LinkedElement(long value, int index){
        super(value, index);
    }
}
