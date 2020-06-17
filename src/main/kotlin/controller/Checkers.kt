package controller

import controller.Board.Move
import controller.Board.Position

interface Checkers {
    val playerWhite: Player
    val playerBlack: Player

    fun selectChecker(position: Position): Set<Move>

    fun moveChecker(move: Move): Position?

    fun checkWinner(): Player?
}