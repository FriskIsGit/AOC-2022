package advent.day11;


public class Operation{
    public boolean isAddition, isByOld;
    public int value = - 1;

    private Operation(){

    }

    //expects operator at index 0 since whatever comes before it is always the same
    public static Operation parse(String opStr){
        Operation op = new Operation();
        char operator = opStr.charAt(0);
        if(opStr.charAt(2) == 'o'){
            op.isByOld = true;
        }
        else{
            op.value = Integer.parseInt(opStr.substring(2));
        }
        if(operator == '+'){
            op.isAddition = true;
        }
        return op;
    }
    public int getWorryLevel(int old){
        if(isByOld){
            if(isAddition){
                return old + old;
            }else{
                return old * old;
            }
        }else{
            if(isAddition){
                return old + value;
            }else{
                return old * value;
            }
        }
    }
    public long getBigWorryLevel(long old){
        old = old % BigMonkey.biggestCommonMultiple;
        long result;
        if(isByOld){
            if(isAddition){
                result = old + old;
            }else{
                result = old * old;
            }
        }else{
            if(isAddition){
                result = old + value;
            }else{
                result = old * value;
            }
        }
        return result;
    }

    public String toString(){
        return "old" +
                (isAddition ? "+" : "*") +
                (isByOld ? "old" : value);
    }
}
