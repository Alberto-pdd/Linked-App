# Linked - Android App

## Project Overview
Linked is an Android app for saving, organizing, and accessing bookmarks/links.
- **Package:** `pdalbert.apps.linked`
- **Architecture:** MVVM with Jetpack Compose
- **Language:** Kotlin 2.0.21
- **UI:** Jetpack Compose + Material3
- **Min SDK:** 29 | **Target SDK:** 36 | **Compile SDK:** 36
- **JVM Target:** 11

## Build & Test Commands

```bash
# Build the app
./gradlew build

# Run all unit tests
./gradlew test

# Run a single unit test
./gradlew test --tests "pdalbert.apps.linked.ExampleUnitTest.addition_isCorrect"

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Lint check
./gradlew lint

# Debug build
./gradlew assembleDebug
```

## Project Structure

```
app/src/main/java/pdalbert/apps/
├── MainActivity.kt              # Entry point, sets up NavGraph
├── data/
│   ├── local/                   # SessionManager (DataStore persistence)
│   └── model/                   # Data classes (User, Link, Folder, TagColor)
├── di/                          # Dependency injection (future, .gitkeep)
├── ui/
│   ├── navigation/              # NavGraph, sealed class Screen
│   ├── screens/
│   │   ├── login/               # LoginScreen, LoginViewModel
│   │   └── splash/              # SplashScreen, SplashViewModel, SplashViewModelFactory
│   └── theme/                   # Color.kt, Type.kt, Theme.kt
└── viewmodel/                   # Shared ViewModels (future, .gitkeep)
```

Tests: `app/src/test/` (JUnit 4 unit), `app/src/androidTest/` (JUnit4 + Espresso instrumented)

## Code Style

### General
- Follow `kotlin.code.style=official` (set in `gradle.properties`)
- 4-space indentation
- No wildcard imports
- Import order: Android → Compose → Kotlin stdlib → Third-party → Project

### Naming Conventions
- **Files:** PascalCase matching primary class (`LoginViewModel.kt`, `SplashScreen.kt`)
- **Functions/Variables:** camelCase
- **Constants:** UPPER_SNAKE_CASE in `companion object`
- **Composable functions:** PascalCase (`LoginScreen`, `FloatingCard`, `PlaceholderScreen`)
- **Private composables:** marked with `private` modifier

### Compose Patterns
- Screen composables: `navController: NavHostController` + `viewModel: T` parameters
- Modifier as first optional parameter: `modifier: Modifier = Modifier`
- Local UI state: `remember { mutableStateOf(...) }` or `mutableFloatStateOf(...)`
- Navigation events: `StateFlow<String?>` from ViewModel, collected in `LaunchedEffect`
- Background color: always use `Background` from `ui/theme/Color.kt`, never inline hex
- Custom fonts: `Manrope` (primary), `DMSerifDisplay`/`DMSerifDisplayItalic` (headlines)

### ViewModels
- Extend `androidx.lifecycle.ViewModel`
- Expose state via `StateFlow`; backing field prefixed with underscore: `_destination`
- Use `viewModelScope.launch {}` for coroutines
- Factory pattern via `ViewModelProvider.Factory` subclass when constructor has params
- `@Suppress("UNCHECKED_CAST")` for ViewModelFactory casts

### Data Models
- `data class` with sensible defaults
- UUIDs: `val id: UUID = UUID.randomUUID()`
- Enums for fixed sets (`TagColor`)
- Computed properties via custom getters: `val initials: String get() = ...`

### Theming
- Colors in `ui/theme/Color.kt` (named palette: `Background`, `Surface`, `Ink`, `Accent`, etc.)
- Typography in `ui/theme/Type.kt`
- App wrapper: `LinkedTheme` → `MaterialTheme`
- Dark mode supported via `darkColorScheme`

### Dependencies
- Managed via Gradle version catalog (`gradle/libs.versions.toml`)
- Reference libs through `libs.*` aliases only
