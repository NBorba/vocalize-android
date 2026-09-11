# :core:permission

A lifecycle-aware permission management library built for Jetpack Compose. This module provides reactive permission state holders, Material 3 rationale and settings dialogs, multi-permission support, and test fixtures for Compose UI tests and `@Preview`s.

---

## 🚀 Features

* **Lifecycle-Aware Re-evaluation:** Uses `LifecycleResumeEffect` to automatically re-check permission status when a user returns to the app from Android System Settings.
* **4-State Permission Model:** Distinguishes between `Granted`, `Denied`, `ShowRationale`, and `DeniedPermanently`.
* **Single & Multiple Permission Support:** Handles individual permissions (`rememberPermissionState`) as well as multi-permission groups (`rememberMultiplePermissionsState`).
* **Material 3 Dialogs:** Pre-built `PermissionRationaleDialog` and `PermissionSettingsDialog`.
* **Test Fixtures:** Built-in `testFixtures` (`FakePermissionStateHolder` and `FakeMultiplePermissionsStateHolder`) for Compose `@Preview`s and UI tests.

---

## 📦 Gradle Dependency Setup

Add `:core:permission` to your feature module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":core:permission"))

    // Test fixtures for UI unit tests and Compose @Previews
    testImplementation(testFixtures(project(":core:permission")))
    androidTestImplementation(testFixtures(project(":core:permission")))
}
```

---

## 💻 Usage Examples

### 1. Single Permission with Rationale & Settings Dialogs

```kotlin
@Composable
fun MicrophoneScreen() {
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    when (permissionState.status) {
        PermissionState.Granted -> {
            // Permission is granted; show audio recorder UI
            RecordAudioContent()
        }
        PermissionState.ShowRationale -> {
            PermissionRationaleDialog(
                content = PermissionDialogContent(
                    title = stringResource(R.string.audio_permission_title),
                    description = stringResource(R.string.audio_permission_description),
                ),
                onConfirm = { permissionState.launchPermissionRequest() },
                onDismiss = { /* Handle cancellation */ },
            )
        }
        PermissionState.DeniedPermanently -> {
            PermissionSettingsDialog(
                content = PermissionDialogContent(
                    title = stringResource(R.string.audio_permission_settings_title),
                    description = stringResource(R.string.audio_permission_settings_description),
                ),
                onDismiss = { /* Handle cancellation */ },
            )
        }
        PermissionState.Denied -> {
            // First-time denied or initial state; offer action button
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text(text = "Enable Microphone")
            }
        }
    }
}
```

---

### 2. Feature Permission Groups (`PermissionFeature`)

For features requiring multiple permissions (e.g., `RECORD_AUDIO` + `POST_NOTIFICATIONS` on Android 13+), define a `PermissionFeature`:

```kotlin
sealed interface AudioRecorderFeature : PermissionFeature {
    object Recorder : PermissionFeature {
        override val permissions: List<String> = buildList {
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        override val rationaleContent: PermissionDialogContent
            @Composable get() = PermissionDialogContent(
                title = stringResource(R.string.recorder_rationale_title),
                description = stringResource(R.string.recorder_rationale_desc),
            )

        override val settingsContent: PermissionDialogContent
            @Composable get() = PermissionDialogContent(
                title = stringResource(R.string.recorder_settings_title),
                description = stringResource(R.string.recorder_settings_desc),
            )
    }
}

@Composable
fun FeatureRecorderScreen() {
    val feature = AudioRecorderFeature.Recorder
    val permissionState = rememberMultiplePermissionsState(feature.permissions)

    when {
        permissionState.allPermissionsGranted -> {
            RecorderContent()
        }
        permissionState.shouldShowRationale -> {
            PermissionRationaleDialog(
                content = feature.rationaleContent,
                onConfirm = { permissionState.launchMultiplePermissionRequest() },
                onDismiss = { /* Handle cancellation */ },
            )
        }
        else -> {
            PermissionSettingsDialog(
                content = feature.settingsContent,
                onDismiss = { /* Handle cancellation */ },
            )
        }
    }
}
```

---

### 3. Compose `@Preview`s Using Test Fixtures

Use `FakePermissionStateHolder` to preview feature screens in any permission state:

```kotlin
@Preview
@Composable
private fun RecorderScreenRationalePreview() {
    val fakePermissionState = FakePermissionStateHolder(
        permission = Manifest.permission.RECORD_AUDIO,
        status = PermissionState.ShowRationale,
    )

    VocalizeTheme {
        RecorderScreen(permissionState = fakePermissionState)
    }
}
```

---

### 4. UI Testing with Test Fixtures

Verify feature screen UI without launching real Android system permission popups:

```kotlin
@Test
fun recorderScreen_showsRationaleDialog_whenPermissionRequiresRationale() {
    val fakeState = FakePermissionStateHolder(
        status = PermissionState.ShowRationale,
    )

    composeTestRule.setContent {
        RecorderScreen(permissionState = fakeState)
    }

    composeTestRule.onNodeWithText("Microphone Permission Required")
        .assertIsDisplayed()
}
```
