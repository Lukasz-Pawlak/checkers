package edu.pwr.checkers.server;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameJDBCTemplate {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    public void create(Integer id, Integer numOfPlayers) {
        String SQL = "INSERT INTO move (id, numOfPlayers) VALUES (?, ?)";
        jdbcTemplateObject.update( SQL, id, numOfPlayers);
        return;
    }

    public List<Game> listGames() {
        String SQL = "SELECT * FROM game";
        List<Game> games = jdbcTemplateObject.query(SQL, new GameMapper());
        return games;
    }
}
