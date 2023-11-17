package com.webcheckers.util;

import com.webcheckers.model.*;

import java.util.ArrayList;

public class ValidationHelper {

    public int piecesOfColor(Board board, Piece.Color color){
        int pieces = 0;
        for (Row row : board){
            for (Space space: row){
                Piece piece = space.getPiece();
                Piece.Color pieceColor = null;
                if (piece != null){
                    pieceColor = piece.getColor();
                }
                if (pieceColor == color) pieces++;
            }
        }
        return pieces;
    }

    public void scanBoard(Board board, Piece.Color activeColor,
                          ArrayList<Move> possibleMoves, ArrayList<ArrayList<Move>> possibleJumps){
        for (Row row: board){
            int rowIndx = row.getIndex();
            for (Space space: row){
                int colIndx = space.getCellIdx();
                Space.SPACECOLOR spaceColor = space.getSpaceColor();
                Piece piece = space.getPiece();
                Piece.Color pieceColor = null;
                if (piece != null){
                    pieceColor = piece.getColor();
                }
                if (spaceColor == Space.SPACECOLOR.DARK && pieceColor == activeColor){
                    Position start = new Position(rowIndx, colIndx);
                    initMoves(start, board, activeColor, piece.getType(), possibleMoves, possibleJumps);
                }
            }
        }

        while (true){
            boolean jumpsLeft = addMoreJumps(board, activeColor, possibleJumps);
            if (!jumpsLeft){
                break;
            }
        }
    }


    private void initMoves(Position start, Board board, Piece.Color activeColor, Piece.Type pieceType,
                           ArrayList<Move> possibleMoves, ArrayList<ArrayList<Move>> possibleJumps){
        int startRow = start.getRow();
        int startCol = start.getCell();

        int positionMult = 1;
        if (activeColor == Piece.Color.WHITE){
            positionMult = -1;
        }

        Position N1E1 = new Position(startRow-(positionMult), startCol+1);
        Position N1W1 = new Position(startRow-(positionMult), startCol-1);
        Position N2E2 = new Position(startRow-(2*positionMult), startCol+2);
        Position N2W2 = new Position(startRow-(2*positionMult), startCol-2);
        Position S1E1 = new Position(startRow+(positionMult), startCol+1);
        Position S1W1 = new Position(startRow+(positionMult), startCol-1);
        Position S2E2 = new Position(startRow+(2*positionMult), startCol+2);
        Position S2W2 = new Position(startRow+(2*positionMult), startCol-2);

        if (isOnBoard(N1E1)) checkAndAddNormalMove(start, N1E1, board, possibleMoves);
        if (isOnBoard(N1W1)) checkAndAddNormalMove(start, N1W1, board, possibleMoves);
        if (isOnBoard(S1E1) && pieceType == Piece.Type.KING) checkAndAddNormalMove(start, S1E1, board, possibleMoves);
        if (isOnBoard(S1W1) && pieceType == Piece.Type.KING) checkAndAddNormalMove(start, S1W1, board, possibleMoves);


        if (isOnBoard(N1E1) && isOnBoard(N2E2)){
            Move jump1 = checkValidJump(start, N1E1, N2E2, board, activeColor);
            if (jump1 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump1);
                possibleJumps.add(tempList);
            }
        }

        if (isOnBoard(N1W1) && isOnBoard(N2W2)){
            Move jump2 = checkValidJump(start, N1W1, N2W2, board, activeColor);
            if (jump2 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump2);
                possibleJumps.add(tempList);
            }
        }
        // only if king type
        if (isOnBoard(S1E1) && isOnBoard(S2E2) && pieceType == Piece.Type.KING){
            Move jump3 = checkValidJump(start, S1E1, S2E2, board, activeColor);
            if (jump3 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump3);
                possibleJumps.add(tempList);
            }
        }

        if (isOnBoard(S1W1) && isOnBoard(S2W2) && pieceType == Piece.Type.KING){
            Move jump4 = checkValidJump(start, S1W1, S2W2, board, activeColor);
            if (jump4 != null){
                ArrayList<Move> tempList = new ArrayList<>();
                tempList.add(jump4);
                possibleJumps.add(tempList);
            }
        }
    }

    private boolean addMoreJumps(Board board, Piece.Color activeColor, ArrayList<ArrayList<Move>> possibleJumps){
        boolean flag = false;

        for (int i=0; i < possibleJumps.size(); i++){
            ArrayList<Move> sequence = possibleJumps.get(i);
            int sequenceSize = possibleJumps.get(i).size();
            Position startPosition = sequence.get(0).getStart();
            Piece.Type startingPieceType = board.getSpace(startPosition.getRow(), startPosition.getCell()).getPiece().getType();
            Move lastMoveInSequence = sequence.get(sequenceSize-1);
            System.out.println("Testing index: " + i + " at last move of: " + lastMoveInSequence);

            boolean newJumps = addAJump(lastMoveInSequence, board, activeColor, startingPieceType, possibleJumps, i);
            if (newJumps){
                flag = true;
            }
        }
        return flag;
    }


    private boolean addAJump(Move move, Board board, Piece.Color activeColor, Piece.Type pieceType, ArrayList<ArrayList<Move>> possibleJumps, int moveListIndex){
        int startRow = move.getEnd().getRow();
        int startCol = move.getEnd().getCell();

        int positionMult = 1;
        if (activeColor == Piece.Color.WHITE){
            positionMult = -1;
        }

        Position N1E1 = new Position(startRow-(positionMult), startCol+1);
        Position N1W1 = new Position(startRow-(positionMult), startCol-1);
        Position N2E2 = new Position(startRow-(2*positionMult), startCol+2);
        Position N2W2 = new Position(startRow-(2*positionMult), startCol-2);
        Position S1E1 = new Position(startRow+(positionMult), startCol+1);
        Position S1W1 = new Position(startRow+(positionMult), startCol-1);
        Position S2E2 = new Position(startRow+(2*positionMult), startCol+2);
        Position S2W2 = new Position(startRow+(2*positionMult), startCol-2);

        Move jump1 = null;
        Move jump2 = null;
        Move jump3 = null;
        Move jump4 = null;

        if (isOnBoard(N1E1) && isOnBoard(N2E2)) jump1 = checkValidJump(move.getEnd(), N1E1, N2E2, board, activeColor);
        if (isOnBoard(N1W1) && isOnBoard(N2W2)) jump2 = checkValidJump(move.getEnd(), N1W1, N2W2, board, activeColor);
        if (isOnBoard(S1E1) && isOnBoard(S2E2)
                && pieceType == Piece.Type.KING) jump3 = checkValidJump(move.getEnd(), S1E1, S2E2, board, activeColor);
        if (isOnBoard(S1W1) && isOnBoard(S2W2)
                && pieceType == Piece.Type.KING) jump4 = checkValidJump(move.getEnd(), S1W1, S2W2, board, activeColor);

        ArrayList<Move> notNullMoves = new ArrayList<>();

        if  (jump1 != null && !jump1.getEnd().equals(move.getStart())) notNullMoves.add(jump1);
        if  (jump2 != null && !jump2.getEnd().equals(move.getStart())) notNullMoves.add(jump2);
        if  (jump3 != null && !jump3.getEnd().equals(move.getStart())) notNullMoves.add(jump3);
        if  (jump4 != null && !jump4.getEnd().equals(move.getStart())) notNullMoves.add(jump4);

        if (notNullMoves.size() > 0){
            updateList(possibleJumps, moveListIndex, notNullMoves);
            return true;
        }else{
            return false;
        }
    }

    private void updateList(ArrayList<ArrayList<Move>> possibleJumps, int index, ArrayList<Move> notNullMoves){
        ArrayList<Move> originalList = possibleJumps.get(index);
        ArrayList<Move> temp_copy = new ArrayList<Move>(originalList);

        if (notNullMoves.size() == 3){
            temp_copy.add(notNullMoves.get(0));
            possibleJumps.add(temp_copy);
            temp_copy = new ArrayList<Move>(originalList);
            temp_copy.add(notNullMoves.get(1));
            possibleJumps.get(index).add(notNullMoves.get(2));
        }else if (notNullMoves.size() == 2){
            temp_copy.add(notNullMoves.get(0));
            possibleJumps.add(temp_copy);
            possibleJumps.get(index).add(notNullMoves.get(1));
        }else if(notNullMoves.size() == 1){
            possibleJumps.get(index).add(notNullMoves.get(0));
        }
    }


    private Piece attemptPiece(Board board, Position position){
        if (isOnBoard(position)){
            return board.getSpace(position.getRow(), position.getCell()).getPiece();
        }
        return null;
    }


    private boolean isOnBoard(Position position){
        int row = position.getRow();
        int cell = position.getCell();

        if (0 <= row && row <= 7){
            return 0 <= cell && cell <= 7;
        }
        return false;
    }

    public boolean checkAndAddNormalMove(Position start, Position end, Board board,
                                         ArrayList<Move> possibleForwardMoves){
        Piece endPiece = attemptPiece(board, end);
        if (endPiece == null){
            Move tempMove = new Move(start, end);
            possibleForwardMoves.add(tempMove);
            return true;
        }
        return false;
    }

    public Move checkValidJump(Position start, Position middle, Position end, Board board, Piece.Color activeColor){
        Piece middlePiece = attemptPiece(board, middle);
        Piece endPiece = attemptPiece(board, end);
        if (middlePiece != null && middlePiece.getColor() != activeColor & endPiece == null){
            return new Move(start, end);
        }
        return null;
    }
}
