package edu.pwr.checkers.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class GameMapper implements RowMapper<Game> {
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setNumOfPlayers(rs.getInt("numOfPlayers"));

        return game;
    }
}