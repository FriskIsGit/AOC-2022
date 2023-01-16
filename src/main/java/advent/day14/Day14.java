package advent.day14;

import advent.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day14{
    enum State{
        ABYSS, PLACED, CLOGGED
    }
    public static ReservoirMap reservoirMap;
    //Regolith Reservoir
    public static void main(String[] args){
        part1();
        part2();
    }

    private static void part1(){
        List<String> lines = AdventOfCode.readDay(14);

        List<Structure> structures = new ArrayList<>();
        for(String line : lines){
            Structure struct = Structure.parseStructure(line);
            structures.add(struct);
        }
        System.out.println(structures);
        reservoirMap = mapStructsToCharArr(structures, false);
        System.out.println(reservoirMap);
        simulateSand();
    }
    private static void part2(){
        List<String> lines = AdventOfCode.readDay(14);

        List<Structure> structures = new ArrayList<>();
        for(String line : lines){
            Structure struct = Structure.parseStructure(line);
            structures.add(struct);
        }
        System.out.println(structures);
        reservoirMap = mapStructsToCharArr(structures, true);
        System.out.println(reservoirMap);
        simulateSand();
    }
    private static void simulateSand(){
        int[] sand = {0};
        State unitResult;
        do{
            unitResult = simulateUnit(ReservoirMap.SAND_SOURCE_ROW, ReservoirMap.SAND_SOURCE_COL, sand);
        }while (unitResult == State.PLACED);
        System.out.println(reservoirMap);
        System.out.println("Final sands: " + sand[0]);
    }
    private static State simulateUnit(int row, int col, int[] sand){
        for (;true; row++){
            if(!reservoirMap.isWithin(row, col)){
                return State.ABYSS;
            }
            char c = reservoirMap.get(row, col);
            //solid block
            if(c != ReservoirMap.AIR){
                if(row == ReservoirMap.SAND_SOURCE_ROW && col == ReservoirMap.SAND_SOURCE_COL){
                    //solid block is source
                    //if clogged -> return clogged
                    //else continue
                    char leftDown  = reservoirMap.get(row+1, col-1);
                    char rightDown = reservoirMap.get(row+1, col+1);
                    if(leftDown != ReservoirMap.AIR && rightDown != ReservoirMap.AIR){
                        System.out.println("Clogged");
                        reservoirMap.set(row, col, ReservoirMap.SAND);
                        sand[0]++;
                        return State.CLOGGED;
                    }else{
                        continue;
                    }

                }

                if(!reservoirMap.isWithin(row, col-1)){
                    return State.ABYSS;
                }
                if(!reservoirMap.isWithin(row, col+1)){
                    return State.ABYSS;
                }

                char left  = reservoirMap.get(row, col-1);
                char right  = reservoirMap.get(row, col+1);

                if(left != ReservoirMap.AIR && right != ReservoirMap.AIR){
                    reservoirMap.set(row-1, col, ReservoirMap.SAND);
                    sand[0]++;
                    return State.PLACED;
                }
                else if(left == ReservoirMap.AIR){
                    return simulateUnit(row+1, col-1, sand);
                }else {
                    return simulateUnit(row+1, col+1, sand);
                }
            }
        }
    }

    private static ReservoirMap mapStructsToCharArr(List<Structure> structs, boolean addFloor){
        int maxRow = Integer.MIN_VALUE, minRow = Integer.MAX_VALUE,
                maxCol = Integer.MIN_VALUE, minCol = Integer.MAX_VALUE;
        for(Structure struct: structs){
            for (Rock rock : struct.rockPath){
                maxRow = Math.max(rock.row, maxRow);
                minRow = Math.min(rock.row, minRow);

                maxCol = Math.max(rock.col, maxCol);
                minCol = Math.min(rock.col, minCol);
            }
        }
        if(addFloor){
            maxRow+=2;
            int y = maxRow-minRow + 2;
            //expand floor to the smallest required size
            minCol -= y;
            maxCol += y;
            Structure floor = new Structure(new Rock(maxRow, minCol), new Rock(maxRow, maxCol));
            structs.add(floor);
        }
        System.out.println("rows(from,to) cols(from,to) : " + minRow + "," + maxRow + "," + minCol + "," + maxCol);
        ReservoirMap reservoir = ReservoirMap.create(minRow, maxRow, minCol, maxCol);
        System.out.println(reservoir.rows());
        System.out.println(reservoir.columns());
        for(Structure struct: structs){
            List<Rock> rockPath = struct.rockPath;
            int rocks = rockPath.size();
            for (int i = 0; i < rocks-1; i++){
                Rock rock1 = rockPath.get(i);
                Rock rock2 = rockPath.get(i+1);
                if(rock1.row != rock2.row){
                    //row loop
                    int from = rock1.row, to = rock2.row;
                    if(from > to){
                        int temp = from;
                        from = to;
                        to = temp;
                    }
                    final int constCol = rock1.col;
                    for (int r = from; r < to; r++){
                        reservoir.set(r, constCol, ReservoirMap.ROCK);
                    }
                    reservoir.set(to, constCol, ReservoirMap.ROCK);
                }
                else{
                    //col loop
                    int from = rock1.col, to = rock2.col;
                    if(from > to){
                        int temp = from;
                        from = to;
                        to = temp;
                    }
                    final int constRow = rock1.row;
                    for (int c = from; c < to; c++){
                        reservoir.set(constRow, c, ReservoirMap.ROCK);
                    }
                    reservoir.set(constRow, to, ReservoirMap.ROCK);
                }
            }
        }
        return reservoir;
    }
}
