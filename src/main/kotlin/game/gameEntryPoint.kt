package game

import engine.GameEngine

fun main(args: Array<String>) {
    GameEngine("GAME", 800, 600, true, DummyGame()).start()
}