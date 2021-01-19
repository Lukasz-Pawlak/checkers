package edu.pwr.checkers.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MoveMapper implements RowMapper<Move> {
    public Move mapRow(ResultSet rs, int rowNum) throws SQLException {
        Move move = new Move();
        move.setGame(rs.getInt("game"));
        move.setMoveNumber(rs.getInt("moveNumber"));
        move.setNewX(rs.getInt("newX"));
        move.setNewY(rs.getInt("newY"));
        move.setOldX(rs.getInt("oldX"));
        move.setOldY(rs.getInt("oldY"));

        return move;
    }
}