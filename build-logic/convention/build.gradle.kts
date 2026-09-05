plugins {
    `kotlin-dsl`
}

group = "com.nborba.vocalize.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "vocalize.android.library"
            implementationClass = "com.nborba.vocalize.conventions.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "vocalize.android.feature"
            implementationClass = "com.nborba.vocalize.conventions.AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "vocalize.android.hilt"
            implementationClass = "com.nborba.vocalize.conventions.AndroidHiltConventionPlugin"
        }
    }
}

