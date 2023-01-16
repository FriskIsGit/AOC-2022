package advent.day13;

import java.util.List;

public class Test{
    public static int success = 0;
    public static int fail = 0;
    public static void main(String[] args){
    /*    comparePackets("[5,0]",
                "[5,[[0],6,[7,8,7,5],[4,8,7,7],10],[0,9,[4,9,9,6,3],[6],4],[[8,9],3,[]]],[[]],[0,[[10,10,5,8,5],2,7,0,[3]],[2,[4,6,5,1,6],[7,10,10,4],7]]", false);*/
        comparePackets("[[5,[[0],6,[7,8,7,5],[4,8,7,7],10],[0,9,[4,9,9,6,3],[6],4],[[8,9],3,[]]],[[]],[0,[[10,10,5,8,5],2,7,0,[3]],[2,[4,6,5,1,6],[7,10,10,4],7]]]",
                "[[5,0],[[[6,3,5],[3],[8,1,5],5,9],[6],[2,0],2,[10]],[]]", false);
        comparePackets("[[6,[6,[1,8],[8,10,0,8,5]],[[7],7]],[[],0,[[8,9,8],1,3,[]]]]",
                "[[[[6,2,6,4,5],3,[0],[9]],4],[],[5,1,[[]],[3,[10,1,10,5,10]]]]", true);
        comparePackets("[[],[5,3]]",
                "[[2,[]]]", true);
        comparePackets("[[],[[[0,3,2,6],7],8,5,5],[1],[[6,6,10,10],9,[[9,1],[6,0,2,10],[0,7,1,1,2]],[0,[],[8,6,5,6],[9,8,6],8]],[0,[],[[],[7,10,10]]]]",
                "[[2,[[9,2,7],2,[4,8,5],4],0,[[7,4,0,9],3]]]", true);
        comparePackets("[[]]", "[[[]]]", true);
        comparePackets("[3,6]", "[[2],[7]]", false);
        comparePackets("[[[]]]", "[[]]", false);
        comparePackets("[[7],8]", "[[],9]", false);
        comparePackets("[7,7,7,7]", "[7,7,7]", false);
        comparePackets("[]", "[3]", true);
        comparePackets("[1,1,3,1,1]", "[1,1,5,1,1]", true);
        comparePackets("[[1],[2,3,4]]", "[[1],4]", true);
        comparePackets("[9]", "[[8,7,6]]", false);
        comparePackets("[1,[2,[3,[4,[5,6,7]]]],8,9]", "[1,[2,[3,[4,[5,6,0]]]],8,9]", false);
        comparePackets("[1,2,3]", "[1,2,4]", true);
        comparePackets("[1,2,3]", "[1,2,2]", false);
        comparePackets("[1,831,12]", "[2,3,4]", true);
        System.out.println("success/ALL: " + success+"/"+(success+fail));
    }
    public static void comparePackets(String leftStr, String rightStr, boolean expected){
        Packet p1 = Packet.parsePacket(leftStr);
        Packet p2 = Packet.parsePacket(rightStr);
        boolean res = p1.isOrdered(p2);
        if(expected == res){
            success++;
        }
        else{
            fail++;
            System.out.println("Incorrect: " + leftStr + ":" + rightStr);
            System.out.println("Expected: " + expected);
        }
    }
    public static void compareWithFile(List<String> given, List<String> expected){
        int matches = 0;
        int minSize = Math.min(given.size(), expected.size());
        for (int i = 0; i < minSize; i++){
            String strGiven = given.get(i);
            String strExpected = expected.get(i);
            if(strGiven.equals(strExpected)){
                matches++;
            }else{
                System.out.println("Found diff at index: " + i);
                System.out.println("Given/Expected: " + strGiven + "/" + strExpected);
            }
        }
        System.out.println("matches/all: " + matches + "/" + minSize);
    }
}
