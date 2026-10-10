# Project Fantasy — Android Pixel RPG

Prototype Android game in landscape mode with a pixel-fantasy lobby, profile, inventory, skills, quests, pet, settings, and simple offline battle loop.

## Build APK using GitHub Actions
1. Upload the **contents** of this folder into the root of a GitHub repository (not the enclosing `ProjectFantasy` folder).
2. Ensure `.github/workflows/android.yml` is included.
3. Open the repository's **Actions** tab and run **Build Project Fantasy APK**.
4. Download `project-fantasy-apk` from the successful run's Artifacts section, unzip it, then install `app-debug.apk`.

## Local build
Requires JDK 17, Android SDK platform 35/build-tools 35.0.0, and Gradle 8.7. Run `gradle assembleDebug`.

This is an offline playable prototype, not a networked MMORPG. Character stats and quest progress currently live in memory for the current app session.
