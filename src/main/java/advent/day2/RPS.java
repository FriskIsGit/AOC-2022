package advent.day2;

final class Result{
    public static final int LOSE = 0;
    public static final int TIE = 3;
    public static final int WIN = 6;
}
enum Move{
    ROCK, PAPER, SCISSORS;
    public static Move toMove(char c){
        switch (c){
            case 'A':
            case 'X':
                return ROCK;
            case 'B':
            case 'Y':
                return PAPER;
            case 'C':
            case 'Z':
                return SCISSORS;
        }
        throw new IllegalStateException();
    }
    public static int toResult(char c){
        switch (c){
            case 'X':
                return Result.LOSE;
            case 'Y':
                return Result.TIE;
            case 'Z':
                return Result.WIN;
        }
        throw new IllegalStateException();
    }
    public static int getYourScore(Move yourMove, Move elfMove){
        int score = shapeScore(yourMove);
        if(yourMove == elfMove){
            score += Result.TIE;
        }else if(yourMove == ROCK && elfMove == Move.SCISSORS){
            score += Result.WIN;
        }
        else if(yourMove == SCISSORS && elfMove == Move.PAPER){
            score += Result.WIN;
        }
        else if(yourMove == PAPER && elfMove == Move.ROCK){
            score += Result.WIN;
        }
        return score;
    }
    private static int shapeScore(Move move){
        switch (move){
            case ROCK:
                return 1;
            case PAPER:
                return 2;
            case SCISSORS:
                return 3;
        }
        throw new IllegalStateException();
    }
}
