package advent.day21;

public class Symbol{
    public String name;
    public boolean hasResult;
    //using int will cause an overflow
    public long result = 0;
    public Symbol s1, s2;
    public char sign;
    public Symbol(String name, int result){
        this.name = name;
        this.result = result;
        this.hasResult = true;
    }
    public Symbol(String name){
        this.name = name;
        this.hasResult = false;
    }
    public Symbol(String name, String name1, char sign, String name2){
        this.name = name;
        this.s1 = new Symbol(name1);
        this.sign = sign;
        this.s2 = new Symbol(name2);
        this.hasResult = false;
    }
    public boolean isRoot(){
        return name.equals("root");
    }
    public void setResult(long result){
        this.result = result;
        this.hasResult = true;
    }

    @Override
    public String toString(){
        return name + ": " + (hasResult ? result:
                (s1.hasResult ? s1.result : s1.name +
                        ' ' + sign + ' ' +
                        (s2.hasResult ? s2.result : s2.name)));
    }
}
