package components.piece;

import components.ChessBoard;
import components.ChessBox;

import java.util.ArrayList;

public class Rook extends Piece
{
    public Rook(String color, ChessBox setPosition)
    {
        super("Rook", color, setPosition);
    }

    public ArrayList<ChessBox> getPossibleMoves(ChessBoard chessBoard)
    {
        ArrayList<ChessBox> possibleMoves = new ArrayList<ChessBox>();
        int currentRank = getCurrentPosition().getRank();
        int currentFile = getCurrentPosition().getFile();



        //-----------------trying along the same rank - thus rankDifference is 0------------------

        //trying RIGHT
        for(int i = 1; (currentFile+i)<chessBoard.getNumberOfFiles();i++) {
            addToPossibleMoves(chessBoard, possibleMoves, 0, i);//right
            if(chessBoard.boxes[currentRank][currentFile+i].isOccupied())
                break;

        }

        //trying LEFT
        for(int i = 1; (currentFile-i)>=0;i++) {
            addToPossibleMoves(chessBoard, possibleMoves, 0, -i);//left
            if(chessBoard.boxes[currentRank][currentFile-i].isOccupied())
                break;

        }

        //-----------------trying along the same file - thus fileDifference is 0------------------

        //trying UP
        for(int i = 1; (currentRank+i)<chessBoard.getNumberOfRanks();i++) {
            addToPossibleMoves(chessBoard, possibleMoves, i, 0);//up
            if(chessBoard.boxes[currentRank+i][currentFile].isOccupied())
                break;

        }

        //trying DOWN
        for(int i = 1; (currentRank-i)>=0;i++) {
            addToPossibleMoves(chessBoard, possibleMoves, -i, 0);//left
            if(chessBoard.boxes[currentRank-i][currentFile].isOccupied())
                break;

        }


        return possibleMoves;
    }
}
