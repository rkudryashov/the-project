rootProject.name = "revolut-task"

pluginManagement {
    val kotlinVersion: String by settings
    val shadowPluginVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "com.github.johnrengelman.shadow" -> useVersion(shadowPluginVersion)
            }
        }
    }
}