package model

import controller.Board
import controller.Board.Position
import controller.Checkers
import controller.MoveType
import controller.MoveType.CAPTURE
import controller.Player
import controller.SideType.BLACK
import controller.SideType.WHITE

class RussianCheckers : Checkers {
    val board = RussianBoard()
    override val playerWhite = RussianPlayer(WHITE, "white_player")
    override val playerBlack = RussianPlayer(BLACK, "black_player")

    private var turn = WHITE
    private var selectedChecker: Position = Position(1, 1)

    override fun selectChecker(position: Position): Set<Board.Move> {
        selectedChecker = position
        val checker = board.checkChecker(position)
        return if (checker == null || checker.side != turn) emptySet()
        else if (board.isCaptureNeed(turn)) board.possibleMoves(position).filter { it.moveType == CAPTURE }.toSet()
        else board.possibleMoves(position)
    }

    fun selectChecker(x: Int, y: Int) = selectChecker(Position(x, y))

    override fun moveChecker(move: Board.Move): Position? {
        val capturedPosition = board.moveChecker(selectedChecker, move)
        if (move.moveType == CAPTURE) {
            if (turn == WHITE) playerBlack.lostCheckers++
            else playerWhite.lostCheckers++
            if (board.possibleCaptures(move.position).isEmpty()) changeTurn()
        } else changeTurn()
        return capturedPosition
    }

    fun moveChecker(x: Int, y: Int, moveType: MoveType) = moveChecker(Board.Move(x, y, moveType))

    override fun checkWinner(): Player? =
            when {
                playerBlack.lostCheckers == 12 -> playerWhite
                playerWhite.lostCheckers == 12 -> playerBlack
                else -> null
            }

    private fun changeTurn() {
        turn = if (turn == WHITE) BLACK
        else WHITE
    }
}