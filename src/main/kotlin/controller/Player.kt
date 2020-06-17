package controller

interface Player {
    val side: SideType
    val name: String
    var lostCheckers: Int
}