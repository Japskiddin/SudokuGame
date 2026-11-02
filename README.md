# SudokuGame

Sudoku game written in Kotlin/Compose

All `core`, `features`, and `app` modules use Kotlin Multiplatform with Android and
JVM targets. Shared logic, ViewModels, and Compose screens live in `commonMain`.
Native drawing, database creation, and date formatting have platform
implementations. The threaded QQWing controller is shared by Android and JVM in
`jvmAndroidMain`.

`app` contains the shared application UI and Metro graph. `androidApp` is the
Android launcher (including splash screen, Tracer, manifest, and signing).
`desktopApp` is the desktop launcher; it stores data in `~/.sudoku`.

Use JDK 21 and an Android SDK with platform 37 installed.

```shell
./gradlew jvmTest
./gradlew :androidApp:assembleDebug
./gradlew :desktopApp:run
```

Debug builds use the configured signing key when available and Android's debug key
otherwise. Release signing uses the existing
`Keys.repo` properties or `SIGNING_KEYSTORE_PASSWORD`, `SIGNING_KEY_ALIAS`, and
`SIGNING_KEY_PASSWORD` environment variables together with the keystore file.
