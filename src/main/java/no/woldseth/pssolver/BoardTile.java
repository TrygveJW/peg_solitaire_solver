package no.woldseth.pssolver;

public class BoardTile {
    public BoardTile north = null;
    public BoardTile east = null;
    public BoardTile south = null;
    public BoardTile west = null;

    private boolean isFull = true;

    public final int x;
    public final int y;

    public BoardTile(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public boolean isFull(){
        return this.isFull;
    }

    public void setFull(boolean full){
        this.isFull = full;
    }

    public boolean isDirOOB(DIRECTION direction){
        switch (direction){
            case NORTH -> {
                return this.north == null;
            }
            case EAST -> {
                return this.east == null;
            }
            case SOUTH -> {
                return this.south == null;
            }
            case WEST -> {
                return this.west == null;
            }
            default -> {
                throw new RuntimeException();
            }
        }
    }

    public boolean isDirFull(DIRECTION direction){
        switch (direction){
            case NORTH -> {
                return this.north.isFull;
            }
            case EAST -> {
                return this.east.isFull;
            }
            case SOUTH -> {
                return this.south.isFull;
            }
            case WEST -> {
                return this.west.isFull;
            }
            default -> {
                throw new RuntimeException();
            }
        }
    }

    public BoardTile getDir(DIRECTION direction){
        switch (direction){
            case NORTH -> {
                return this.north;
            }
            case EAST -> {
                return this.east;
            }
            case SOUTH -> {
                return this.south;
            }
            case WEST -> {
                return this.west;
            }
            default -> {
                throw new RuntimeException();
            }
        }
    }
}
