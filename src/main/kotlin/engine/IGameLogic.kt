package engine

import java.lang.Exception
import kotlin.Throws

interface IGameLogic {
    @Throws(Exception::class)
    fun init()
    fun input(window: Window?)
    fun update(interval: Float)
    fun render(window: Window?)
    fun cleanup()
}