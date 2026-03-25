plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.0"
}

val lwjglVersion = "3.4.1"
val lwjglNatives = "natives-windows"
val imguiVersion = "1.90.0"

group = "io.github.jwyoon1220"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.20")
    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-core
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.10.0")
    // Source: https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.32")
    // Source: https://mvnrepository.com/artifact/cz.advel.jbullet/jbullet
    implementation("cz.advel.jbullet:jbullet:20101010-1")
    // Source: https://mvnrepository.com/artifact/org.joml/joml
    implementation("org.joml:joml:1.10.8")

    implementation("io.github.spair:imgui-java-binding:${imguiVersion}")
    implementation("io.github.spair:imgui-java-lwjgl3:${imguiVersion}")
    implementation("io.github.spair:imgui-java-natives-windows:${imguiVersion}")

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-egl")
    implementation("org.lwjgl", "lwjgl-fmod")
    implementation("org.lwjgl", "lwjgl-freetype")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-harfbuzz")
    implementation("org.lwjgl", "lwjgl-hwloc")
    implementation("org.lwjgl", "lwjgl-jawt")
    implementation("org.lwjgl", "lwjgl-jemalloc")
    implementation("org.lwjgl", "lwjgl-ktx")
    implementation("org.lwjgl", "lwjgl-llvm")
    implementation("org.lwjgl", "lwjgl-lmdb")
    implementation("org.lwjgl", "lwjgl-lz4")
    implementation("org.lwjgl", "lwjgl-meshoptimizer")
    implementation("org.lwjgl", "lwjgl-msdfgen")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-odbc")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opencl")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-opengles")
    implementation("org.lwjgl", "lwjgl-opus")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-remotery")
    implementation("org.lwjgl", "lwjgl-renderdoc")
    implementation("org.lwjgl", "lwjgl-rpmalloc")
    implementation("org.lwjgl", "lwjgl-sdl")
    implementation("org.lwjgl", "lwjgl-shaderc")
    implementation("org.lwjgl", "lwjgl-spng")
    implementation("org.lwjgl", "lwjgl-spvc")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-tinyexr")
    implementation("org.lwjgl", "lwjgl-tinyfd")
    implementation("org.lwjgl", "lwjgl-vma")
    implementation("org.lwjgl", "lwjgl-vulkan")
    implementation("org.lwjgl", "lwjgl-xxhash")
    implementation("org.lwjgl", "lwjgl-yoga")
    implementation("org.lwjgl", "lwjgl-zstd")
    implementation ("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-bgfx", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-freetype", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-harfbuzz", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-hwloc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-jemalloc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-ktx", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-llvm", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-lmdb", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-lz4", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-meshoptimizer", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-msdfgen", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-opengles", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-opus", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-remotery", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-rpmalloc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-sdl", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-shaderc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-spng", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-spvc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-tinyexr", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-tinyfd", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-vma", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-xxhash", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-yoga", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-zstd", classifier = lwjglNatives)

}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}