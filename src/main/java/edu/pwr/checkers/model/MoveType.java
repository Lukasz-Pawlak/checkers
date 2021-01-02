package edu.pwr.checkers.model;

/**
 * This enum represents possible move types in classic version
 * of Chinese Checkers, although it suits also regular checkers.
 * @version 1.0
 * @author Wojciech Sęk
 */
public enum MoveType {
  /** Move is one step. */
  ONESTEP,
  /** Move is sequence of jumps. */
  JUMPSEQ,
  /** Unknown type of move */
  UNKNOWN,
  /** Move has not been done nor started yet */
  NEWTURN
}
