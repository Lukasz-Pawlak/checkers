package edu.pwr.checkers.server;

public class Move {
    private Integer game;
    private Integer moveNumber;
    private Integer oldX;
    private Integer oldY;
    private Integer newX;
    private Integer newY;

    public void setGame(Integer game) {
        this.game = game;
    }
    public Integer getGame() {
        return game;
    }
    public void setMoveNumber(Integer moveNumber) {
        this.moveNumber = moveNumber;
    }
    public Integer getMoveNumber() {
        return moveNumber;
    }
    public void setNewX(Integer newX) {
        this.newX = newX;
    }
    public Integer getNewX() {
        return newX;
    }
    public void setNewY(Integer newY) {
        this.newY = newY;
    }
    public Integer getNewY() {
        return newY;
    }
    public void setOldX(Integer oldX) {
        this.oldX = oldX;
    }
    public Integer getOldX() {
        return oldX;
    }
    public void setOldY(Integer oldY) {
        this.oldY = oldY;
    }
    public Integer getOldY() {
        return oldY;
    }
}
