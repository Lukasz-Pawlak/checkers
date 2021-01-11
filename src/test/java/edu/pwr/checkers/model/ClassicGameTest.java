package edu.pwr.checkers.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ClassicGameTest {
  @Test
  public void generalTest() {
    ClassicGame game = new ClassicGame(2);
    game.setup();
    game.init();
    Piece piece = game.board.getField(10,4).getPiece();
    assertNotNull(piece);
    assertNotNull(game.getActivePlayer());
    assertNotNull(game.getBoard());

    if (game.getActivePlayer().getColors().contains(Color.RED)) {
      try {
        game.acceptMove(game.activePlayer);
      } catch (WrongPlayerException e) {
        e.printStackTrace();
      }
    }

    Coordinates cor = new Coordinates(10, 5);
    assertEquals(MoveType.ONESTEP, game.getType(piece, cor));
    cor = new Coordinates(10, 6);
    assertEquals(MoveType.JUMPSEQ, game.getType(piece, cor));
    cor = new Coordinates(0, 15);
    assertEquals(MoveType.UNKNOWN, game.getType(piece, cor));

    game.illegalMove(game.board.getField(9, 4).getPiece(), new Coordinates(5, 8));
    game.illegalMove(game.board.getField(10, 4).getPiece(), new Coordinates(6, 8));
    game.illegalMove(game.board.getField(10, 5).getPiece(), new Coordinates(6, 9));
    game.illegalMove(game.board.getField(11, 4).getPiece(), new Coordinates(7, 8));
    game.illegalMove(game.board.getField(11, 5).getPiece(), new Coordinates(7, 9));
    game.illegalMove(game.board.getField(11, 6).getPiece(), new Coordinates(7, 10));
    game.illegalMove(game.board.getField(12, 4).getPiece(), new Coordinates(8, 8));
    game.illegalMove(game.board.getField(12, 5).getPiece(), new Coordinates(8, 9));
    game.illegalMove(game.board.getField(12, 6).getPiece(), new Coordinates(8, 10));
    game.illegalMove(game.board.getField(12, 7).getPiece(), new Coordinates(8, 11));

    try {
      game.acceptMove(game.activePlayer);
    } catch (WrongPlayerException e) {
      e.printStackTrace();
    }
    assertEquals(0, game.ranking.size());

    game.illegalMove(game.board.getField(4, 9).getPiece(), new Coordinates(9, 4));
    game.illegalMove(game.board.getField(4, 10).getPiece(), new Coordinates(10, 4));
    game.illegalMove(game.board.getField(5, 10).getPiece(), new Coordinates(10, 5));
    game.illegalMove(game.board.getField(4, 11).getPiece(), new Coordinates(11, 4));
    game.illegalMove(game.board.getField(5, 11).getPiece(), new Coordinates(11, 5));
    game.illegalMove(game.board.getField(6, 11).getPiece(), new Coordinates(11, 6));
    game.illegalMove(game.board.getField(4, 12).getPiece(), new Coordinates(12, 4));
    game.illegalMove(game.board.getField(5, 12).getPiece(), new Coordinates(12, 5));
    game.illegalMove(game.board.getField(6, 12).getPiece(), new Coordinates(12, 6));
    game.illegalMove(game.board.getField(7, 12).getPiece(), new Coordinates(12, 7));

    try {
      game.acceptMove(game.activePlayer);
    } catch (WrongPlayerException e) {
      e.printStackTrace();
    }
    assertEquals(2, game.ranking.size());
  }

  @Test(expected = IllegalMoveException.class)
  public void testMove1() throws IllegalMoveException, WrongPlayerException {
    ClassicGame game = new ClassicGame(3);
    game.setup();
    game.init();
    Piece piece = game.board.getField(10,4).getPiece();
    Coordinates cor = new Coordinates(10, 8);
    assertNotNull(game.activePlayers);
    Player player = game.getActivePlayer();
    assertNotNull(player);
    game.move(player, piece, cor);
  }

  @Test(expected = IllegalMoveException.class)
  public void testMove2() throws IllegalMoveException, WrongPlayerException {
    ClassicGame game = new ClassicGame(4);
    game.setup();
    game.init();
    Piece piece = game.board.getField(10,4).getPiece();
    Coordinates cor = new Coordinates(10, 5);
    Player player = game.getActivePlayer();
    assertNotNull(game.activePlayers);
    assertNotNull(game.activePlayers.get(0));
    game.move(player, piece, cor);
    //game.acceptMove(player);
    //player = game.getActivePlayer();
    cor = new Coordinates(10, 6); // second ONESTEP
    game.move(player, piece, cor);
  }

  @Test
  public void testMove3() {
    ClassicGame game = new ClassicGame(6);
    game.setup();
    game.init();
    Piece piece = game.board.getField(10,4).getPiece();
    Coordinates cor = new Coordinates(10, 5);
    Player player = game.activePlayers.get(0);
    assertNotNull(game.activePlayers);
    assertNotNull(game.activePlayers.get(0));
    try {
      game.move(player, piece, cor);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    game.cancelMove(player);
    try {
      game.acceptMove(player);
    } catch (WrongPlayerException e) {
      e.printStackTrace();
    }

    assertEquals(4, piece.getField().getPosition().y);
  }
}
