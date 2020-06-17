import junit.framework.Assert.*
import controller.Board.Move
import controller.Board.Position
import controller.MoveType.CAPTURE
import controller.MoveType.MOVE
import controller.SideType
import model.RussianBoard
import model.RussianCheckers
import org.junit.Test

class RussianCheckersTests{
    private val board = RussianBoard()
    private val checkers = RussianCheckers()

    private fun printBoard1() {
        println(board.toString() + "\n")
    }

    private fun printBoard2() {
        println(checkers.board.toString() + "\n")
    }

    @Test
    fun createBoard() {
        printBoard1()
        assertEquals(setOf<Move>(), board.possibleCaptures(Position(2, 3)))

        board.moveChecker(Position(1, 6), Move(Position(2, 5), MOVE))
        printBoard1()
        assertEquals(setOf<Move>(), board.possibleCaptures(Position(2, 3)))

        board.moveChecker(Position(2, 5), Move(Position(3, 4), MOVE))
        printBoard1()
        assertEquals(setOf(Move(Position(4, 5), CAPTURE, Position(3, 4))), board.possibleCaptures(Position(2, 3)))
        assertEquals(setOf<Move>(), board.possibleCaptures(Position(3, 4)))

        board.moveChecker(Position(2, 3), board.possibleCaptures(Position(2, 3)).first())
        printBoard1()
        assertEquals(setOf(Move(Position(3, 4), CAPTURE, Position(4, 5))), board.possibleCaptures(Position(5, 6)))

        assertTrue(board.isCaptureNeed(SideType.WHITE))
        assertFalse(board.isCaptureNeed(SideType.BLACK))
    }

    @Test
    fun checkersTest() {
        printBoard2()
        assertEquals(emptySet<Move>(), checkers.selectChecker(2, 1))
        assertEquals(emptySet<Move>(), checkers.selectChecker(4, 3))
        assertEquals(setOf(Move(2, 5, MOVE), Move(4, 5, MOVE)), checkers.selectChecker(3, 6))

        checkers.moveChecker(4, 5, MOVE)
        checkers.selectChecker(2, 3)
        checkers.moveChecker(3, 4, MOVE)

        printBoard1()
        val moves = checkers.selectChecker(4, 5)
        assertEquals(setOf(Move(Position(2, 3), CAPTURE, Position(3, 4))), moves)

        checkers.moveChecker(moves.first())
        printBoard1()
    }

    @Test
    fun doubleCaptureTest() {
        printBoard2()
        checkers.selectChecker(7, 6)
        checkers.moveChecker(8, 5, MOVE)
        checkers.selectChecker(2, 3)
        checkers.moveChecker(3, 4, MOVE)
        checkers.selectChecker(5, 6)
        checkers.moveChecker(6, 5, MOVE)
        checkers.selectChecker(1, 2)
        checkers.moveChecker(2, 3, MOVE)
        checkers.selectChecker(4, 7)
        checkers.moveChecker(5, 6, MOVE)
        checkers.selectChecker(3, 4)
        checkers.moveChecker(4, 5, MOVE)
        printBoard1()
        val move1 = checkers.selectChecker(5, 6).first()
        checkers.moveChecker(move1)
        val move2 = checkers.selectChecker(3, 4).first()
        checkers.moveChecker(move2)
        printBoard1()
    }
}