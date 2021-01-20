package edu.pwr.checkers.server;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import edu.pwr.checkers.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameJDBCTemplate {
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    public Integer createGame(Integer numOfPlayers) {
        String SQL = "INSERT INTO game (numOfPlayers) VALUES (?)";
        String SQL2 = "SELECT MAX(id) FROM game";

        try {
            jdbcTemplateObject.update( SQL, numOfPlayers);
            return jdbcTemplateObject.queryForObject(SQL2, Integer.class);
        } catch (DataAccessException ex) {
            Logger.err("Data processing gone wrong");
        }
        return null;
    }

    public List<Game> listGames() {
        String SQL = "SELECT * FROM game";
        return jdbcTemplateObject.query(SQL, new GameMapper());
    }
}
