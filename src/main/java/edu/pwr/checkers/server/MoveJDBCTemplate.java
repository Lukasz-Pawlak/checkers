package edu.pwr.checkers.server;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class MoveJDBCTemplate {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    public void create(Integer game, Integer moveNumber, Integer oldX, Integer oldY, Integer newX, Integer newY) {
        String SQL = "INSERT INTO move (game, moveNumber, oldX, oldY, newX, newY) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplateObject.update( SQL, game, moveNumber, oldX, oldY, newX, newY);
        return;
    }

    public List<Move> listMoves(Integer game) {
        String SQL = "SELECT * FROM move WHERE game = " + game + " ORDER BY moveNumber";
        return jdbcTemplateObject.query(SQL, new MoveMapper());
    }
}
