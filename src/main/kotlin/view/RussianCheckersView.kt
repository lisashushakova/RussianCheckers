package view

import javafx.geometry.Pos
import javafx.geometry.Side
import tornadofx.*
import kotlin.system.exitProcess

class RussianCheckersView : View() {
    private val board = Board()
    override val root = borderpane {
        top = menubar {
            menu("Menu") {
                item("New Game") {
                    setOnAction { board.newBoard() }
                }
                item("Exit") {
                    setOnAction { exitProcess(0) }
                }
            }
        }
        center = board.root
    }

    init {
        title = "Russian Checkers"
        primaryStage.width = 800.0
        primaryStage.height = 860.0
    }
}

class GameOverWindow : Fragment() {
    val side: Side by param()
    val board: Board by param()

    override val root = vbox {
        alignment = Pos.CENTER

        label {
            text = "Game over! Press 'Restart' to continue"
        }

        hbox {
            alignment = Pos.CENTER

            button {
                text = "Restart"
                setOnAction {
                    board.newBoard()
                    close()
                }
            }

            button {
                text = "Exit"
                setOnAction { exitProcess(0) }
            }
        }
    }
}
