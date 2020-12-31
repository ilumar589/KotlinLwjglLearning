package engine

class GameEngine(windowTitle: String?, width: Int, height: Int, vSync: Boolean, private val gameLogic: IGameLogic) : Runnable {
    private val window: Window = Window(windowTitle!!, width, height, vSync)
    private val gameLoopThread: Thread = Thread(this, "GAME_LOOP_THREAD")

    fun start() {
        val osName = System.getProperty("os.name")
        if (osName.contains("Mac")) {
            gameLoopThread.run()
        } else {
            gameLoopThread.start()
        }
    }

    override fun run() {
        try {
            init()
            gameLoop()
        } catch (excp: Exception) {
            excp.printStackTrace()
        } finally {
            cleanup()
        }
    }

    @Throws(Exception::class)
    fun init() {
        window.init()
        FrameTimer.init()
        gameLogic.init()
    }

    private fun gameLoop() {
        var elapsedTime: Float
        var accumulator = 0f
        val interval = 1f / TARGET_UPS
        val running = true
        while (running && !window.windowShouldClose()) {
            elapsedTime = FrameTimer.elapsedTime
            accumulator += elapsedTime
            input()
            while (accumulator >= interval) {
                update(interval)
                accumulator -= interval
            }
            render()
            if (!window.isvSync()) {
                sync()
            }
        }
    }

    fun cleanup() {
        gameLogic.cleanup()
    }

    private fun sync() {
        val loopSlot = 1f / TARGET_FPS
        val endTime = FrameTimer.lastLoopTime + loopSlot
        while (FrameTimer.time < endTime) {
            try {
                Thread.sleep(1)
            } catch (ie: InterruptedException) {
            }
        }
    }

    fun input() {
        gameLogic.input(window)
    }

    fun update(interval: Float) {
        gameLogic.update(interval)
    }

    fun render() {
        gameLogic.render(window)
        window.update()
    }

    companion object {
        const val TARGET_FPS = 75
        const val TARGET_UPS = 30
    }
}