plugins {
    id("vocalize.android.feature")
    id("vocalize.android.test")
}

android {
    namespace = "com.nborba.vocalize.feature.recorder.impl"
}

dependencies {
    implementation(project(":feature:recorder-api"))
}
