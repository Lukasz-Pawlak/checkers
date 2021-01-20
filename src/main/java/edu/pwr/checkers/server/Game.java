package edu.pwr.checkers.server;

import java.io.Serializable;

public class Game implements Serializable {
    private Integer id;
    private Integer numOfPlayers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(Integer numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public String toString() {
        return "Game: " + id + ", players: " + numOfPlayers;
    }
}
