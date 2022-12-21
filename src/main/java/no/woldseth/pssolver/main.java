package no.woldseth.pssolver;

import java.util.*;
import java.util.stream.Collectors;

public class main {
    public static Random rng = new Random();
    public static void main(String[] args) {
        Board board = new Board(3);

//        board.displayBoard();


//        System.out.println(board.getAvailableMoves());



//        for (int i = 0; i < 1000; i++) {
//            List<Move> moves = board.fullTiles.stream().map(board::getAvailableMoves).flatMap(Collection::stream).toList();
//            if (moves.size() == 0){
//                System.out.println(i);
//                break;
//            }
//            board.makeMove(moves.get(0));
//        }
        var bestMoves = recursiveGet(board);
        board.displayBoard();
        Collections.reverse(bestMoves);
        bestMoves.forEach(move -> System.out.printf("x: %s, y: %s, DIR: %s\n", move.boardTile.x,move.boardTile.y,move.direction));

    }
    public static int tests = 0;

    public static SortedSet chekedStates = new TreeSet();
    public static List<Move> recursiveGet(Board board){
        int stateHash = board.hashCode();
        tests += 1;
        if (tests % 100000 == 0){
            System.out.println(chekedStates.size());
            System.out.println(tests);
        }

        if (chekedStates.contains(stateHash)){
            return null;
        }
        ArrayList<Move> moves = board.fullTiles.stream().map(board::getAvailableMoves).flatMap(Collection::stream).collect(
                Collectors.toCollection(ArrayList::new));
        Collections.shuffle(moves);

            if (board.fullTiles.size() <= 3){
                List<Move> retMoves = new ArrayList<>();
                return retMoves;
//                board.displayBoard();
//                System.exit(0);
            }

        var stateMap = board.getState();
        if (moves.size() == 0){
            return null;
        }

        for (Move move :moves) {
            board.makeMove(move);
            boolean isDone = board.isWon();
            if (isDone){
                List<Move> retMoves = new ArrayList<>();
                retMoves.add(move);
                return retMoves;
            }
            List<Move> bestMove = recursiveGet(board);

            if (bestMove != null){
                bestMove.add(move);
                return bestMove;
            }

            board.loadState(stateMap);

        }

        chekedStates.add(stateHash);
        return null;


    }
}
