package no.woldseth.pssolver;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private int boardSize;

    public List<List<BoardTile>> board;
    public List<BoardTile> fullTiles;
    public List<BoardTile> allTiles;



    @Override
    public int hashCode() {
        return Objects.hash(allTiles.stream().map(BoardTile::isFull));
    }

    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.board = new ArrayList<>();
        this.fullTiles = new ArrayList<>();
        this.allTiles = new ArrayList<>();

        var x_size = (boardSize * 2) + 1;
        var overlap = (boardSize-1)/2;

        for (int i = 0; i < x_size; i++) {
            List<BoardTile> row = new ArrayList<>();
            boolean shortRows = i <= overlap || i >= x_size - overlap-1;

            for (int n = 0; n < x_size; n++) {
                if (shortRows && (n <= overlap || n >= x_size - overlap-1)) {
                    row.add(null);
                }else {
                    row.add(new BoardTile(n, i));
                }
            }
            board.add(row);
        }
        for (int y = 0; y < x_size; y++) {
            for (int x = 0; x < x_size; x++) {
                var node = board.get(y).get(x);
                if (node != null){
                    fullTiles.add(node);
                    allTiles.add(node);
                    if (x > 0){
                        var west = board.get(y).get(x-1);
                        if (west != null){
                            node.west = west;
                        }
                    }
                    if (x < x_size - 1){
                        var east = board.get(y).get(x+1);
                        if (east != null){
                            node.east = east;
                        }
                    }
                    if (y > 0){
                        var north = board.get(y - 1).get(x);
                        if (north != null){
                            node.north = north;
                        }
                    }
                    if (y < x_size -1){
                        var south = board.get(y + 1).get(x);
                        if (south != null){
                            node.south = south;
                        }
                    }
                }

            }

        }


        var centerIdx = boardSize - overlap + 1;
        var centerNode = board.get(centerIdx).get(centerIdx);
        centerNode.setFull(false);
        fullTiles.remove(centerNode);
    }

    public void displayBoard(){
        var x_size = (boardSize * 2) + 1;
        for (int y = 0; y < x_size; y++) {
            for (int x = 0; x < x_size; x++) {
                var node = board.get(y).get(x);

                if (node == null){
                    System.out.print(" ");
                } else if (node.isFull()) {
                    System.out.print("X");
                }else{
                    System.out.print("O");
                }
            }
            System.out.print("\n");
        }
    }
    public List<Move> getAvailableMoves(BoardTile tile){
        List<Move> moveList = new ArrayList<>();
        if (tile.isFull()){
            for (DIRECTION dir: DIRECTION.values()){
                if (!tile.isDirOOB(dir)){
                    var step1 = tile.getDir(dir);
                    if (!step1.isDirOOB(dir)){
                        var step2 = step1.getDir(dir);
                        if (step1.isFull() && !step2.isFull()){
                            moveList.add(new Move(dir,tile));
                        }
                    }
                }
            }
        }
        return moveList;
    }

    public boolean isWon() {
        if (fullTiles.size() == 1){
            var finalTile = fullTiles.get(0);
            if (finalTile.x == boardSize+1 && finalTile.y == boardSize+1){
                return true;
            }
        }
        return false;
    }
    public Map<BoardTile, Boolean> getState(){
        return allTiles.stream().collect(Collectors.toMap(boardTile -> boardTile, BoardTile::isFull));
    }

    public void loadState(Map<BoardTile, Boolean> stateMap){
        fullTiles.clear();
        stateMap.forEach((boardTile, isFull) -> {
            if (isFull){
                fullTiles.add(boardTile);
            }
            boardTile.setFull(isFull);
        });
    }

    public void makeMove(Move move){
        var tile = move.boardTile;
        var dir = move.direction;
        var step_1 = tile.getDir(dir);
        var step_2 = step_1.getDir(dir);

        tile.setFull(false);
        step_1.setFull(false);
        step_2.setFull(true);

        fullTiles.remove(tile);
        fullTiles.remove(tile);
        fullTiles.add(step_2);
    }
}
