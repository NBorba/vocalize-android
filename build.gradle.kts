// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
}

// Git Hooks Setup
tasks.register("installGitHooks") {
    description = "Installs the ktlint pre-commit git hook"
    val projectDir = layout.projectDirectory
    val sourceFile = projectDir.file("scripts/pre-commit-ktlint.sh").asFile
    val targetFile = providers.exec {
        commandLine("git", "rev-parse", "--git-path", "hooks/pre-commit")
    }.standardOutput.asText.map { projectDir.file(it.trim()).asFile }.get()

    val javaHome = System.getProperty("java.home")

    inputs.file(sourceFile)
    inputs.property("javaHome", javaHome)
    outputs.file(targetFile)

    doLast {
        val originalContent = sourceFile.readText()
        val newContent = originalContent.replaceFirst("#!/bin/sh", "#!/bin/sh\n\nexport JAVA_HOME=\"$javaHome\"")

        var shouldWrite = true

        if (targetFile.exists()) {
            val currentContent = targetFile.readText()
            if (currentContent == newContent) {
                shouldWrite = false
            }
        } else {
            targetFile.parentFile.mkdirs()
        }

        if (shouldWrite) {
            targetFile.writeText(newContent)
            targetFile.setExecutable(true)
            logger.lifecycle("ktlint pre-commit hook installed at: ${targetFile.absolutePath}")
        }
    }
}

// Ensure :app preBuild depends on installGitHooks
evaluationDependsOn(":app")
project(":app") {
    tasks.named("preBuild") {
        dependsOn(rootProject.tasks.named("installGitHooks"))
    }
}
