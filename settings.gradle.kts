pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.9.0")
            version("agp", "8.2.2")
            version("ksp", "1.9.0-1.0.13")
            version("composeBom", "2024.02.00")
            version("room", "2.6.1")
            version("hilt", "2.50")
            version("navigation", "2.7.7")
            version("workmanager", "2.9.0")
            version("coroutines", "1.7.3")
            version("lifecycle", "2.7.0")

            plugin("android-application", "com.android.application").versionRef("agp")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").versionRef("kotlin")
            plugin("ksp", "com.google.devtools.ksp").versionRef("ksp")
            plugin("hilt", "com.google.dagger.hilt.android").versionRef("hilt")

            library("compose-bom", "androidx.compose", "compose-bom").versionRef("composeBom")
            library("compose-ui", "androidx.compose.ui", "ui").withoutVersion()
            library("compose-ui-graphics", "androidx.compose.ui", "ui-graphics").withoutVersion()
            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
            library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
            library("compose-material3", "androidx.compose.material3", "material3").withoutVersion()

            library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("room-compiler", "androidx.room", "room-compiler").versionRef("room")
            library("room-ktx", "androidx.room", "room-ktx").versionRef("room")

            library("hilt-android", "com.google.dagger", "hilt-android").versionRef("hilt")
            library("hilt-compiler", "com.google.dagger", "hilt-android-compiler").versionRef("hilt")

            library("navigation-compose", "androidx.navigation", "navigation-compose").versionRef("navigation")
            library("workmanager", "androidx.work", "work-runtime-ktx").versionRef("workmanager")
            library("coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines")
            library("lifecycle-runtime", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle")
            library("lifecycle-viewmodel", "androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle")

            bundle("compose", listOf(
                "compose-ui",
                "compose-ui-graphics",
                "compose-ui-tooling",
                "compose-ui-tooling-preview",
                "compose-material3"
            ))
        }
    }
}

rootProject.name = "Challenges"
include(":app")
