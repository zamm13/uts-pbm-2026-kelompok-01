# Repository Guidelines

## Project Structure & Module Organization

This is a single-module Android application. The Gradle `:app` module contains Kotlin sources under `app/src/main/java/com/example/seminarku`: `MainActivity.kt` hosts the Jetpack Compose screen, `Logika.kt` and `KartuKuota.kt` contain app logic/UI components, `model/` holds data models, and `ui/theme/` contains Compose theme definitions. Android resources and the manifest live in `app/src/main/res/` and `app/src/main/AndroidManifest.xml`. Local JVM tests belong in `app/src/test/`; device or emulator tests belong in `app/src/androidTest/`. Do not edit generated output under `build/` or `app/build/`.

## Build, Test, and Development Commands

Run commands from the repository root using the Gradle wrapper:

- `./gradlew.bat assembleDebug` builds the debug APK (PowerShell users can also run `gradlew.bat assembleDebug`).
- `./gradlew.bat testDebugUnitTest` runs local JVM tests.
- `./gradlew.bat connectedDebugAndroidTest` runs instrumented tests on a connected device or emulator.
- `./gradlew.bat lintDebug` runs Android static analysis.

Open the project in Android Studio to deploy the app to an emulator or device. The module currently targets SDK 37, with a minimum SDK of 24.

## Coding Style & Naming Conventions

Use Kotlin and Jetpack Compose for UI. Follow the existing four-space indentation and keep imports explicit. Use `UpperCamelCase` for classes and composable functions, `lowerCamelCase` for properties and functions, and `UPPER_SNAKE_CASE` for constants. Keep package names lowercase under `com.example.seminarku`; place models and reusable theme code in their existing subpackages. Prefer small composables and keep business rules separate from UI where practical. No formatter or linter configuration is currently checked in; maintain consistency with surrounding files and run `lintDebug` when making substantial changes.

## Testing Guidelines

Use JUnit tests in `app/src/test` for logic that does not require Android, and AndroidJUnit/Compose tests in `app/src/androidTest` for behavior that needs a device. Name test classes with a `Test` suffix and test methods to describe the behavior, such as `registrationRejectsFullClass`. Add focused tests for changed logic; the existing example tests are scaffolding rather than feature coverage.

## Commit & Pull Request Guidelines

Git metadata is not available in this checkout, so no established commit-message convention could be confirmed. Use short, imperative commit subjects (for example, `Add participant capacity validation`). Pull requests should summarize the user-visible change, note relevant tests and build results, link any related issue, and include screenshots for Compose UI changes. Highlight any configuration or migration steps reviewers need to know.
