package ldts.model;

public class Game {

    protected Board board;
    protected Piece piece;
    protected  Piece nextPiece;
    protected static final int gameScreenXoffset = 6;
    protected static final int gameScreenYoffset = 2;
    protected static final int gameScreenWidth = 26;
    protected static final int gameScreenLength = 26;
    protected int gameSpeed = 5;  //smaller is faster, ticks needed to force piece down
    protected int nTickCounter = 0;
    protected Score score;

    public Game(){
        board = new Board(gameScreenWidth, gameScreenLength);
        score = new Score();
    }

    public void nextTick(){
        score.addToScore(board.checkLineCompletition(new RemoveLine()));
        if (nTickCounter == gameSpeed) {
            if (board.hasHitBottom(piece))
                piece = null;
            else
                piece.forceDown();
            nTickCounter = 0;
        } else {
            nTickCounter++;
        }
    }
    public boolean isPieceNull(){
        if(nextPiece == null && piece == null){
            piece = new Piece(gameScreenWidth/4);
            nextPiece = new Piece(gameScreenWidth/4);
            return true;
        }
        if (piece == null) {
            piece = nextPiece;
            nextPiece = null;
            nextPiece = new Piece(gameScreenWidth/4);
            return true;
        }
        return false;
    }

    public void pressedLeft(){
         if (piece!=null && piece.getPos_x()>0 && board.canMove(piece.getPos_x()-1, piece))
            piece.moveLeft();
    }
    public void pressedRight(){
        if (piece!=null && piece.getRightPos()<gameScreenWidth/2-1 && board.canMove(piece.getPos_x()+1, piece))
            piece.moveRight();
    }
    public void pressedDown(){
        if(piece==null) return;

        if(board.hasHitBottom(piece))
            piece = null;
        else
            piece.forceDown();
        nTickCounter = 0;
    }
    public void pressedUp(){
        if(piece!=null && board.canRotate(piece)){
            piece.rotate();
        }
    }

    public Piece getPiece() {
        return piece;
    }
    public Piece getNextPiece(){return nextPiece;}
    public Board getBoard() {
        return board;
    }
    public Score getScore() { return score; }
    public int getGameSpeed() {
        return gameSpeed;
    }
    public int getTickCount() {
        return nTickCounter;
    }
    public boolean gameOver(){return board.gameOver();}
    public void increaseGameSpeed(){
        if(gameSpeed>1){
            gameSpeed--;
        }
    }
    public void decreaseGameSpeed(){
        if(gameSpeed<10){
            gameSpeed++;
        }
    }
}
