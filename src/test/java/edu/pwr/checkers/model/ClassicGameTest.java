package edu.pwr.checkers.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassicGameTest {
  @Test
  public void Test() {
    ClassicGame game = new ClassicGame(3);
    game.init();
    Piece piece = game.board.getField(10,4).getPiece();
    assertNotNull(piece);
    Coordinates cor = new Coordinates(10, 5);
    assertEquals(MoveType.ONESTEP, game.getType(piece, cor));
    cor = new Coordinates(10, 6);
    assertEquals(MoveType.JUMPSEQ, game.getType(piece, cor));
    cor = new Coordinates(0, 15);
    assertEquals(MoveType.UNKNOWN, game.getType(piece, cor));
  }

  @Test(expected = IllegalMoveException.class)
  public void testMove1() throws IllegalMoveException, WrongPlayerException {
    ClassicGame game = new ClassicGame(3);
    game.init();
    Piece piece = game.board.getField(10,4).getPiece();
    Coordinates cor = new Coordinates(10, 8);
    Player player = game.activePlayers.get(0);
    assertNotNull(game.activePlayers);
    assertNotNull(game.activePlayers.get(0));
    game.move(player, piece, cor);
  }
}
