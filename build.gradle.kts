// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
}

// Task that will install ktlint pre-commit hook
tasks.register("installGitHooks") {
    description = "Installs the ktlint pre-commit git hook"
    val projectDir = layout.projectDirectory
    // Path to the template script in the project
    val sourceFile = projectDir.file("scripts/pre-commit-ktlint.sh").asFile
    // Target path where Git looks for the pre-commit hook.
    // We resolve this via 'git rev-parse --git-path' to support worktrees and custom core.hooksPath.
    val targetFile = providers.exec {
        commandLine("git", "rev-parse", "--git-path", "hooks/pre-commit")
    }.standardOutput.asText.map { projectDir.file(it.trim()).asFile }.get()

    // Get the current Java home to ensure the hook uses the project's Java version
    val javaHome = System.getProperty("java.home")

    // Declare inputs and outputs for Gradle build optimization (up-to-date checks)
    inputs.file(sourceFile)
    inputs.property("javaHome", javaHome)
    outputs.file(targetFile)

    doLast {
        val originalContent = sourceFile.readText()
        // Inject the JAVA_HOME export into the script content
        val newContent = originalContent.replaceFirst("#!/bin/sh", "#!/bin/sh\n\nexport JAVA_HOME=\"$javaHome\"")
        
        var shouldWrite = true

        if (targetFile.exists()) {
            val currentContent = targetFile.readText()
            // Only overwrite if the content has changed
            if (currentContent == newContent) {
                shouldWrite = false
            } else {
                // If the file exists and is different, back it up before overwriting
                val backupFile = File(targetFile.parentFile, "${targetFile.name}.old")
                targetFile.copyTo(backupFile, overwrite = true)
                logger.lifecycle("Existing pre-commit hook backed up to: ${backupFile.absolutePath}")
            }
        } else {
            // Ensure the .git/hooks directory exists (e.g., in a fresh clone)
            targetFile.parentFile.mkdirs()
        }

        if (shouldWrite) {
            // Write the updated script to the Git hooks directory
            targetFile.writeText(newContent)
            // Make the hook file executable so Git can run it
            targetFile.setExecutable(true)
            logger.lifecycle("ktlint pre-commit hook installed at: ${targetFile.absolutePath}")
        }
    }
}

// Ensure :app:preBuild depends on installGitHooks
evaluationDependsOn(":app")
project(":app") {
    tasks.named("preBuild") {
        dependsOn(rootProject.tasks.named("installGitHooks"))
    }
}
