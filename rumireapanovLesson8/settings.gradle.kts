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
}

rootProject.name = "ru.mirea.panov.Lesson8"
include(":app")
include(":app:yandexmaps")
include(":app:yandexdriver")
include(":app:osmmaps")
