package edu.pwr.checkers.server;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameJDBCTemplate {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    public void createGame(Integer numOfPlayers) {
        String SQL = "INSERT INTO game (numOfPlayers) VALUES (?)";
        // String SQL = "CALL createNewGame (?, @result)"; // do we want to use this procedure here?
        // We should return id of game we created here ig, so this might be the way
        try {
            jdbcTemplateObject.update( SQL, numOfPlayers);
        } catch (DataAccessException ex) {
            // ...
        }
    }

    public List<Game> listGames() {
        String SQL = "SELECT * FROM game";
        return jdbcTemplateObject.query(SQL, new GameMapper());
    }
}
