# vocalize

Offline-first audio journal that automatically structures raw voice notes into actionable insights.

## 🏗️ Architecture & Module Structure

Vocalize follows a modularized, scalable Android architecture powered by **Gradle Convention Plugins (`:build-logic`)** and **Dependency Injection with Hilt & KSP**.

### Module Categories

* **`:app`**: Root application module that assembles feature implementations, configures Hilt (`@HiltAndroidApp`), and initializes root navigation/Activities.
* **`:core:<name>`** (e.g., `:core:designsystem`, `:core:data`, `:core:network`): Shared library modules providing UI design tokens, data repositories, network clients, or utility helpers. Configured with the `vocalize.android.library` convention plugin.
* **`:feature:<name>-api`**: Public interface module for a feature. Exposes navigation routes, interfaces, and data models to other modules without leaking private implementation details. Configured with `vocalize.android.library`.
* **`:feature:<name>-impl`**: Private implementation module containing ViewModels, Composables, and Hilt DI bindings (`@Module`). Depends on `:feature:<name>-api` and is configured with the `vocalize.android.feature` convention plugin.

### 🎨 Design System (`:core:designsystem`)

The `:core:designsystem` module acts as the single source of truth for the visual identity and UI primitives of Vocalize:

* **Theme & Material You (`VocalizeTheme`)**: Wraps `MaterialTheme` with support for Light/Dark modes and Android 12+ dynamic colors (Material You).
* **Design Tokens**:
  * **Colors (`Color.kt`)**: Defines raw color palettes and maps them to semantic Material 3 `ColorScheme` roles.
  * **Spacing (`Spacing.kt`)**: Custom spacing scale exposed via `CompositionLocal` (`MaterialTheme.spacing`).
  * **Typography (`Type.kt`)**: Tuned typography scale across Display, Headline, Title, Body, and Label roles.
  * **Shapes (`Shape.kt`)**: Corner radius scales for components (cards, dialogs, buttons, sheets).
* **Icon Registry (`VocalizeIcons`)**: Centralized icon registry object wrapping Material icons and custom vector assets so features don't couple directly to drawable IDs.
* **Feature Convention Plugin Integration**: Automatically included in all feature modules via the `vocalize.android.feature` convention plugin.

### 💉 Dependency Injection (Hilt & KSP)

* **Feature Isolation**: Feature modules depend *only* on another feature's `-api` module (`:feature:home-api`). They never depend directly on another feature's `-impl` module, preserving fast, parallel incremental build times.
* **Hilt Injected Interfaces**: Consuming modules rely on interface abstractions. Hilt `@Binds` the interface to its implementation (`@Inject constructor`) behind the scenes.

---

## 🛠️ Module Generator Scripts

Helper scripts are provided in `scripts/` to create new modules with proper directory structures, package names, and automatic `settings.gradle.kts` inclusion:

### Generate a Feature Module (API + IMPL pair)
```bash
./scripts/create-feature-module.sh onboarding
```
*Creates `:feature:onboarding-api` and `:feature:onboarding-impl`.*

### Generate a Core / Library Module
```bash
./scripts/create-library-module.sh network
```
*Creates `:core:network`.*

---

## Development

### Code Style
This project uses [ktlint](https://pinterest.github.io/ktlint/) to maintain consistent code formatting.

### Git Hooks
To ensure high code quality, a Git `pre-commit` hook is automatically installed when you first build the project (via the `:app:preBuild` task). This hook runs `ktlint` checks on your staged changes before every commit.

If you need to run lint checks manually, use:
```bash
./gradlew ktlintCheck
```

To automatically fix formatting issues:
```bash
./gradlew ktlintFormat
```
