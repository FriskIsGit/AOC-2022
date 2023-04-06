package advent.day22.enums;


/**
 * For a cube having its sides defined as (regardless of POV):
 * F - front, B - back,
 * U - up, D - down,
 * R - right, L - left,
 */
public enum CubeSide{
    F, B, U, D, R, L;

    //methods apply only to a flat image of the cube's mesh
    public static CubeSide left(CubeSide side){
        switch (side){
            case F:
            case U:
            case D:
                return L;
            case B:
                return R;
            case L:
                return B;
            case R:
                return F;
            default: throw new Error();
        }
    }
    public static CubeSide right(CubeSide side){
        switch (side){
            case F:
            case U:
            case D:
                return R;
            case B:
                return L;
            case L:
                return F;
            case R:
                return B;
            default: throw new Error();
        }
    }
    public static CubeSide down(CubeSide side){
        switch (side){
            case F:
            case L:
            case R:
            case B:
                return D;
            case U:
                return F;
            case D:
                return B;
            default: throw new Error();
        }
    }
    public static CubeSide up(CubeSide side){
        switch (side){
            case F:
            case L:
            case R:
            case B:
                return U;
            case U:
                return B;
            case D:
                return F;
            default: throw new Error();
        }
    }
}
