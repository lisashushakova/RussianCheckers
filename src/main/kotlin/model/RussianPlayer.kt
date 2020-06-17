package model

import controller.Player
import controller.SideType

class RussianPlayer(override val side: SideType, override val name: String) : Player {
    override var lostCheckers: Int = 0
}