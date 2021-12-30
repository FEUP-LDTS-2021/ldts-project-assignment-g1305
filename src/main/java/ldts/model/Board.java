package ldts.model;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;


public class Board {
    private String[][] matrix;

    private int width;
    private int length;

    public Board(int width, int length){
        width = width/2;
        this.width = width;
        this.length = length;

        matrix = new String[length][width];

        for (int y = 0; y < length; y++){
            for (int x = 0; x < width; x++){
                matrix[y][x] = "#000000";
            }
        }
        System.out.println("element added");
    }

    public void draw(TextGraphics screen) {
        for (int y = 0; y < length; y++){
            for (int x = 0; x < width*2; x+=2){
                screen.setBackgroundColor(TextColor.Factory.fromString(matrix[y][x/2]));
                //TODO REMOVE LINE (on to debug)
                screen.putString(new TerminalPosition(x + Game.getGameScreenXoffset(), y + Game.getGameScreenYoffset()), Integer.toString(x/2));
                //screen.putString(new TerminalPosition(x + ldts.model.Game.getGameScreenXoffset(), y + ldts.model.Game.getGameScreenYoffset()), ' ');
                screen.putString(new TerminalPosition(x+1 + Game.getGameScreenXoffset(), y + Game.getGameScreenYoffset()), " ");
            }
        }
    }

    public boolean canMove(int nextPosX, Piece piece) {
        for (int y = 0; y < piece.getMatrix().length; y++) {
            for (int x = 0; x < piece.getMatrix()[y].length; x++) {
                if (piece.getMatrix()[y][x] != "#000000") {
                    if (!matrix[piece.getPos_y()+y][x + nextPosX].equals("#000000")) {
                        System.out.println("Cant move there");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean hasHitBottom(Piece piece){
        for(int y =0; y<piece.getMatrix().length; y++){
            for (int x = 0; x <piece.getMatrix()[y].length; x++){
                if(piece.getMatrix()[y][x]!="#000000") {
                    if(y+piece.getPos_y()+1 == matrix.length){
                        System.out.println("Chegou ao Fundo");
                        addPiece(piece);
                        return true;
                    }
                    else if(matrix[y+piece.getPos_y()+1][x+ piece.getPos_x()]!="#000000"){
                        System.out.println("Colisão com peça");
                        addPiece(piece);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addPiece(Piece piece){
        for(int y =0; y<piece.getMatrix().length;y++){
            for(int x=0; x<piece.getMatrix()[y].length;x++){
                if(piece.getMatrix()[y][x] != "#000000"){
                    matrix[y+piece.getPos_y()][x+ piece.getPos_x()] = piece.getMatrix()[y][x];
                }
            }
        }

    }

    public int checkLineCompletition(){
        int counter = 0;
        for (int y = 0; y < length; y++){
            boolean fullprintedline = true;
            for (int x = 0; x < width; x++){
                if(matrix[y][x] == "#000000"){
                    fullprintedline = false;
                    break;
                }
            }
            if(fullprintedline) {
                counter++;
                removeLine(y);
                y--;
            }
        }
        return counter;
    }

    public void removeLine(int y){
        for(int line = y; line > 0; line--){
            matrix[line]= matrix[line-1];
        }
        for(int column=0; column<width;column++){
            matrix[0][column] = "#000000";
        }
    }

}