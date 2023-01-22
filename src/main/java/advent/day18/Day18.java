package advent.day18;

import advent.AdventOfCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18{
    public static Cube[] cubes;
    private static Set<Integer> cubeSet;

    public static int MAX_COORDINATE;
    public static void main(String[] args){
        bothParts();
    }
    //4010 too high
    // the shape the cubes make could be sealed from the outside or half sealed
    // making the flow of water to the inside possible

    private static void bothParts(){
        List<String> lines = AdventOfCode.readDay(18);
        //List<String> lines = AdventOfCode.readCustom("kihau18.txt");
        //List<String> lines = AdventOfCode.readDummy(18);
        long st1 = System.currentTimeMillis();
        cubes = parseCoordinates(lines);

        int size = cubes.length;
        System.out.println("All cubes: " + size);
        for (int i = 0; i < size-1; i++){
            for (int j = i + 1; j < size; j++){
                Cube c1 = cubes[i];
                Cube c2 = cubes[j];
                if(c1.equals(c2)){
                    throw new IllegalStateException("Cubes are in the same spot");
                }
                if(c1.isConnected(c2)){
                    c1.connections++;
                    c2.connections++;
                }
            }
        }
        int allSides = size * 6;
        System.out.println("Total max area: " + allSides);
        for(Cube c : cubes){
            allSides -= c.connections;
        }
        System.out.println("Surface area: " + allSides);

        long en1 = System.currentTimeMillis();
        System.out.println("Part1 - time taken ms: " + (en1-st1));
        int lowX = Integer.MAX_VALUE, lowY = Integer.MAX_VALUE, lowZ = Integer.MAX_VALUE;
        int highX = -1, highY = -1, highZ = -1;

        long st2 = System.currentTimeMillis();
        //part2
        for(Cube c : cubes){
            lowX = Math.min(lowX, c.x);
            lowY = Math.min(lowY, c.y);
            lowZ = Math.min(lowZ, c.z);

            highX = Math.max(highX, c.x);
            highY = Math.max(highY, c.y);
            highZ = Math.max(highZ, c.z);
        }
        int dimX = highX - lowX;
        int dimY = highY - lowY;
        int dimZ = highZ - lowZ;
        MAX_COORDINATE = highX;
        //System.out.println("Min dimensions (x * y * z): " + dimX + " * " + dimY + " * " + dimZ);
        cubeSet = hashCubes(cubes);

        int trappedSides = 0;
        HashSet<Integer> trappedAirHashes = new HashSet<>();
        for(Cube c : cubes){
            int x = c.x, y = c.y, z = c.z;
            Cube cubeLeft = new Cube(x-1, y, z);
            Cube cubeRight = new Cube(x+1, y, z);
            Cube cubeAbove = new Cube(x, y, z+1);
            Cube cubeBelow = new Cube(x, y, z-1);
            Cube cubeFront = new Cube(x, y+1, z);
            Cube cubeBack = new Cube(x, y-1, z);
            Cube[] neighbors = new Cube[]{cubeLeft, cubeRight, cubeAbove, cubeBelow, cubeFront, cubeBack};
            for (Cube neighbor : neighbors){
                int neighborHash = neighbor.hashCode();

                if (cubeSet.contains(neighborHash)){
                    continue;
                }
                //if neighbor is not solid
                if(trappedAirHashes.contains(neighborHash)){
                    trappedSides++;
                    continue;
                }
                HashSet<Integer> airArea = new HashSet<>();
                boolean isAirTrapped = propagate(neighbor, airArea);
                if(isAirTrapped){
                    trappedAirHashes.addAll(airArea);
                    trappedSides++;
                }
            }
        }
        long en2 = System.currentTimeMillis();
        System.out.println("Part2 - time taken ms: " + (en2-st2));
        System.out.println("Water accessible area: " + (allSides - trappedSides));
    }

    private static void cubesFromBirdView(){
        for(int x = 0; x <= MAX_COORDINATE; x++){
            for (int y = 0; y <= MAX_COORDINATE; y++){
                boolean contained = false;
                for(Cube cube : cubes){
                    if(cube.x == x && cube.y == y){
                        contained = true;
                        break;
                    }
                }
                if(contained){
                    System.out.print('x');
                }else{
                    System.out.print(' ');
                }

            }
            System.out.println();
        }
    }

    /** @param   airCube, airArea
      * @return  true if the entirety of the air is sealed completely by solid blocks
      *          false when out of bounds coordinates are reached
      */

    private static boolean propagate(Cube airCube, HashSet<Integer> airArea){
        airArea.add(airCube.hashCode());
        int x = airCube.x, y = airCube.y, z = airCube.z;
        Cube cubeLeft  = new Cube(x-1, y, z);
        Cube cubeRight = new Cube(x+1, y, z);
        Cube cubeAbove = new Cube(x, y, z+1);
        Cube cubeBelow = new Cube(x, y, z-1);
        Cube cubeFront = new Cube(x, y+1, z);
        Cube cubeBack  = new Cube(x, y-1, z);
        Cube[] closeCubes = new Cube[]{
                cubeLeft, cubeRight, cubeAbove,
                cubeBelow, cubeFront, cubeBack};


        for (Cube closeCube : closeCubes){
            if (closeCube.isWithinBounds(0, MAX_COORDINATE)){
                // it's air, it's not already contained in the area,
                if(!cubeSet.contains(closeCube.hashCode()) && !airArea.contains(closeCube.hashCode())){
                    if(!propagate(closeCube, airArea)){
                        return false;
                    }
                }
            }else{
                // if not within bounds - outside has been reached
                return false;
            }
        }
        return true;
    }

    private static boolean areAllSolid(int... hashes){
        for (int hash : hashes){
            if (!cubeSet.contains(hash)){
                return false;
            }
        }
        return true;
    }

    private static Set<Integer> hashCubes(Cube[] cubes){
        HashSet<Integer> cubeSet = new HashSet<>();
        for (Cube cube : cubes){
            cubeSet.add(cube.hashCode());
        }
        return cubeSet;
    }

    public static Cube[] parseCoordinates(List<String> input){
        int size = input.size();
        Cube[] cubes = new Cube[size];
        for (int i = 0; i < size; i++){
            String line = input.get(i);
            String[] split = line.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);
            Cube c = new Cube(x, y, z);
            cubes[i] = c;
        }
        return cubes;
    }

}
