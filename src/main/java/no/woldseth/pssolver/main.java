package no.woldseth.pssolver;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class main {
    public static Random rng = new Random();
    public static void main(String[] args) {

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



        List<Thread> handles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            var handle = new Thread(() -> {

                Board board = new Board(3);
                var stateMap = board.getState();
                List<Move> bestMoves = null;
                boolean found = false;
                for (int j = 0; j < 20000000; j++) {
                    bestMoves = rolldown(board);
                    if (bestMoves != null){
                        found = true;
                        break;
                    }

                    board.loadState(stateMap);
                }

                if (found){
                    System.out.println(bestMoves);
                    board.displayBoard();
                    Collections.reverse(bestMoves);
                    bestMoves.forEach(move -> System.out.printf("x: %s, y: %s, DIR: %s\n", move.boardTile.x,move.boardTile.y,move.direction));
                }

            });
            handle.start();
            handles.add(handle);

        }
        handles.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
    public static List<Move> rolldown(Board board){
        tests += 1;
        if (tests % 10000000 == 0){
            System.out.println(tests);
        }

        ArrayList<Move> moves = board.fullTiles.stream().map(board::getAvailableMoves).flatMap(Collection::stream).collect(
                Collectors.toCollection(ArrayList::new));
        Collections.shuffle(moves);
        if (moves.size() == 0){
            return null;
        }

        var move = moves.get(0);
        board.makeMove(move);
        boolean isDone = board.isWon();
        if (isDone){
            List<Move> retMoves = new ArrayList<>();
            retMoves.add(move);
            return retMoves;
        }
        List<Move> bestMove = rolldown(board);

        if (bestMove != null){
            bestMove.add(move);
            return bestMove;
        }

        if (board.fullTiles.size() <= 1) {
            List<Move> retMoves = new ArrayList<>();
            retMoves.add(move);
            return retMoves;
//                board.displayBoard();
//                System.exit(0);
        }

//        if (moves.size() == 0){
//            return null;
//        }


        return null;


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
//
//        if (chekedStates.contains(stateHash)){
//            return null;
//        }
        ArrayList<Move> moves = board.fullTiles.stream().map(board::getAvailableMoves).flatMap(Collection::stream).collect(
                Collectors.toCollection(ArrayList::new));
        Collections.shuffle(moves);

        if (board.fullTiles.size() > board.allTiles.size() - 5) {
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
