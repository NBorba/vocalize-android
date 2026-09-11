# :core:permission

A suspending, Material 3-compatible permission management library for Jetpack Compose, designed to operate identically to Compose `SnackbarHostState` and `SnackbarHost`.

---

## 🚀 Key Features

* **`PermissionRequestHostState` & `PermissionRequestHost`:** A suspending `requestPermission` API operating with the same pattern as Material 3 `SnackbarHostState` / `SnackbarHost`.
* **Suspend Return Values:** `requestPermission` suspends until the user completes the flow and returns `PermissionResult.Granted`, `PermissionResult.Denied`, or `PermissionResult.DeniedPermanently`.
* **Automatic Rationale & Settings Dialogs:** Automatically hosts rationale and system settings dialogs when required.
* **ViewModel & Clean Architecture Support:** Injectable `PermissionChecker` interface for ViewModels, complete with `FakePermissionChecker` for unit tests.
* **Custom Dialog Slots:** Supports default Material 3 dialogs (`DefaultPermissionRationaleDialog`, `DefaultPermissionSettingsDialog`) or custom composable slots via `PermissionRequestHost`.

---

## 📦 Gradle Dependency Setup

Add `:core:permission` to your feature module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":core:permission"))

    // Test fixtures for ViewModel and UI unit tests
    testImplementation(testFixtures(project(":core:permission")))
    androidTestImplementation(testFixtures(project(":core:permission")))
}
```

---

## 💻 Usage Examples

### 1. Requesting Permissions in Composables

```kotlin
@Composable
fun MicrophoneScreen(viewModel: MicrophoneViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val permissionHostState = rememberPermissionRequestHostState()

    // Place PermissionRequestHost near your screen root layout
    PermissionRequestHost(hostState = permissionHostState)

    Button(
        onClick = {
            scope.launch {
                val result = permissionHostState.requestPermission(Manifest.permission.RECORD_AUDIO)
                if (result == PermissionResult.Granted) {
                    viewModel.startRecording()
                }
            }
        }
    ) {
        Text("Record Note")
    }
}
```

---

### 2. ViewModel Permission Checks (`PermissionChecker`)

Inject `PermissionChecker` into your ViewModels to check permission status without Android `Context` in ViewModel logic:

```kotlin
@HiltViewModel
class MicrophoneViewModel @Inject constructor(
    private val permissionChecker: PermissionChecker,
) : ViewModel() {

    fun isMicrophoneEnabled(): Boolean =
        permissionChecker.hasPermission(Manifest.permission.RECORD_AUDIO)
}
```

---

### 3. ViewModel Unit Testing with `FakePermissionChecker`

Use `FakePermissionChecker` in ViewModel unit tests:

```kotlin
@Test
fun `isMicrophoneEnabled returns true when permission is granted`() {
    val fakeChecker = FakePermissionChecker(defaultGranted = true)
    val viewModel = MicrophoneViewModel(permissionChecker = fakeChecker)

    assertTrue(viewModel.isMicrophoneEnabled())
}
```
