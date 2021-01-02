package edu.pwr.checkers.model;

import java.io.Serializable;

/**
 * Game object keeps track of the rules - asserts that all moves
 * are correct. This interface is quite abstract, and could match
 * any turn-based board game in which players move their pieces.
 * @version 1.0
 * @author Łukasz Pawlak
 * @author Wojciech Sęk
 */
public interface Game extends Serializable {
    /**
     * This method initializes tge game, should be
     * called before first use of Game object
     */
    void init();

    /**
     * This method moves a piece of given player to new position.
     * If it is not player's move, WrongPlayerException is thrown.
     * Tf the move is illegal, IllegalMoveException is thrown.
     * @param player player who wants to make the move.
     * @param piece piece to be moved.
     * @param newPosition position to which piece should be moved.
     * @throws IllegalMoveException when move does not follow the rules
     * @throws WrongPlayerException when it is not player's turn
     */
    void move(Player player, Piece piece, Coordinates newPosition)
            throws IllegalMoveException, WrongPlayerException;

    /**
     * This method cancels last move of the player, if he is active player.
     * Restores internal board to the state before any move was performed
     * by this player. If the player is not active (it is not his turn),
     * no action is performed.
     * After cancellation, active player remains unchanged.
     * @param player player who wants to cancel last move.
     */
    void cancelMove(Player player);

    /**
     * This method accepts move performed by player.
     * Afterwards, switches active player to the next.
     * Does nothing when player is not currently active.
     * @param player the player which wants to confirm his move.
     */
    void acceptMove(Player player);

    /**
     * This function returns current state of board.
     * @return current state of board.
     */
    Board getBoard();

    /**
     * This function returns active player, or player who is
     * currently ought to do his move.
     * @return active player.
     */
    Player getActivePlayer();
}
