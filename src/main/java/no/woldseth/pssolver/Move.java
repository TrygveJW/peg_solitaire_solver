package no.woldseth.pssolver;

import java.util.List;

public class Move {
    public final DIRECTION direction;
    public final BoardTile boardTile;


    public Move(DIRECTION direction, BoardTile boardTile) {
        this.direction = direction;
        this.boardTile = boardTile;
    }
}
