import java.io.File

const val SHADERS_BASE_PATH = "src/main/resources/shaders"
const val TEXTURES_BASE_PATH = "src/main/resources/textures"

fun readFileAsText(fileName: String) : String = File(fileName).readText(Charsets.UTF_8)