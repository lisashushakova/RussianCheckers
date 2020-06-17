package model

import controller.Board
import controller.Board.Position
import controller.CheckerIsKing
import controller.MoveType
import controller.MoveType.CAPTURE
import controller.MoveType.MOVE
import controller.SideType
import controller.SideType.BLACK
import controller.SideType.WHITE

class RussianBoard : Board {
    override val boardSize = 8
    private val board: Array<Array<CheckerIsKing?>> = Array(boardSize) { arrayOfNulls<CheckerIsKing?>(boardSize) }
    private val whiteDirections = setOf(Position(1, -1), Position(-1, -1))
    private val blackDirections = setOf(Position(1, 1), Position(-1, 1))

    operator fun get(row: Int): Array<CheckerIsKing?> = board[row]

    init {
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                if (((row == 0 || row == 2) && col % 2 == 1) || row == 1 && col % 2 == 0)
                    board[row][col] = CheckerIsKing(BLACK)
                else if (((row == 5 || row == 7) && col % 2 == 0) || row == 6 && col % 2 == 1)
                    board[row][col] = CheckerIsKing(WHITE)
            }
        }
    }

    override fun checkChecker(position: Position): CheckerIsKing? =
            board[position.y - 1][position.x - 1]

    override fun possibleCaptures(position: Position): Set<Board.Move> {
        val checker: CheckerIsKing = checkChecker(position)
                ?: throw IllegalArgumentException("It must be checker on position")
        val result = mutableSetOf<Board.Move>()

        for (direction in whiteDirections + blackDirections) {
            var nextPosition = position

            while (true) {
                nextPosition += direction
                if (isPositionOnBoard(nextPosition)) {
                    val nextChecker = checkChecker(nextPosition)
                    if (nextChecker == null) {
                        if (checker.isKing) continue
                    } else if (nextChecker.side != checker.side) {
                        var nextCapture = nextPosition + direction
                        while (true) {
                            if (isPositionOnBoard(nextCapture) && checkChecker(nextCapture) == null)
                                result.add(Board.Move(nextCapture, CAPTURE, nextPosition))
                            else break
                            if (!checker.isKing) break
                            nextCapture += direction
                        }
                    }
                    break
                } else break
            }

        }

        return result
    }

    override fun isCaptureNeed(side: SideType): Boolean {
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                if (board[row][col]?.side == side)
                    if (possibleCaptures(Position(col + 1, row + 1)).isNotEmpty()) return true
            }
        }
        return false
    }

    override fun possibleMoves(position: Position): Set<Board.Move> {
        val checker = checkChecker(position) ?: throw IllegalArgumentException("It must be checker on position")
        val side = checker.side
        val result = mutableSetOf<Board.Move>()

        for (direction in when {
            checker.isKing -> whiteDirections + blackDirections
            side == WHITE -> whiteDirections
            else -> blackDirections
        }) {
            var nextPosition = position

            while (true) {
                nextPosition += direction
                if (isPositionOnBoard(nextPosition)) {
                    val nextChecker = checkChecker(nextPosition)
                    if (nextChecker == null) {
                        result.add(Board.Move(nextPosition, MOVE))
                        if (checker.isKing) continue
                    }
                    break
                } else break
            }
        }
        return (result + possibleCaptures(position)).toSet()
    }

    override fun moveChecker(position: Position, move: Board.Move): Position? {
        val checker = board[position.y - 1][position.x - 1]!!
        if (move.position.y == 1 && checker.side == WHITE || move.position.y == 8 && checker.side == BLACK)
            checker.isKing = true
        board[move.position.y - 1][move.position.x - 1] = checker

        if (move.moveType == CAPTURE) {
            board[move.capturedPosition!!.y - 1][move.capturedPosition.x - 1] = null
        }

        board[position.y - 1][position.x - 1] = null
        return move.capturedPosition
    }

    override fun randomMove(side: SideType): MoveType {
        TODO("Not yet implemented")
    }

    private fun isPositionOnBoard(position: Position): Boolean =
            position.x in 1..boardSize && position.y in 1..boardSize

    override fun toString(): String {
        val result = StringBuilder()
        for (row in 0 until boardSize) {
            result.appendln("-----------------")
            result.append("|")
            for (col in 0 until boardSize) {
                val currentChecker = board[row][col]
                result.append(if (currentChecker == null) " " else if (currentChecker.side == WHITE)
                    if (currentChecker.isKing) "̃W" else "W"
                else if (currentChecker.isKing) "̃B" else "B")
                result.append("|")
            }
            result.appendln()
        }
        result.append("-----------------")
        return result.toString()
    }
}