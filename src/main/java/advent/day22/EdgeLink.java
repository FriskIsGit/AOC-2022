package advent.day22;

import advent.day22.enums.Edge;

public class EdgeLink{
    //each cube should have exactly 12 edges
    //defines an edge connection
    public Edge edge1, edge2;
    public CubeChunk chunk1, chunk2;

    public EdgeLink(CubeChunk chunk1, Edge edge1, CubeChunk chunk2, Edge edge2){
        this.edge1 = edge1;
        this.chunk1 = chunk1;
        this.edge2 = edge2;
        this.chunk2 = chunk2;
    }
    public static EdgeLink of(CubeChunk side, Edge edge, CubeChunk side2, Edge edge2){
        return new EdgeLink(side, edge, side2, edge2);
    }
    public boolean hasMappingFor(CubeChunk ch, Edge anEdge){
        if(anEdge == edge1 && ch.equals(chunk1)){
            return true;
        }
        return anEdge == edge2 && ch.equals(chunk2);
    }

    @Override
    public String toString(){
        return  "(" +
                edge1 + " of " + chunk1.side +
                " <-> " +
                edge2 + " of " + chunk2.side +
                ")";
    }
}

