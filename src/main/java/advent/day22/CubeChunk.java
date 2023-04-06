package advent.day22;

import advent.day22.enums.CubeSide;

//it really is just an extension of Chunk class
public class CubeChunk extends Chunk{
    public CubeSide side;

    //passed chunk will not be copied, passed instance
    public CubeChunk(Chunk copy){
        super(copy.map, copy.chunkTableRow, copy.chunkTableCol);
    }

    public CubeChunk(int chunkTableRow, int chunkTableCol){
        super(chunkTableRow, chunkTableCol);
    }
    public boolean equals(CubeChunk chunk){
        return this.isEmpty == chunk.isEmpty
                && this.chunkTableRow == chunk.chunkTableRow && this.chunkTableCol == chunk.chunkTableCol;
    }

}
