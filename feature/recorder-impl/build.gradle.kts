plugins {
    id("vocalize.android.feature")
}

android {
    namespace = "com.nborba.vocalize.feature.recorder.impl"
}

dependencies {
    implementation(project(":feature:recorder-api"))
}
