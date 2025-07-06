# Spaceship Game – Android App

## 🕹️ **Overview**  

Spaceship Game is an Android game where the player controls a spaceship at the bottom of the screen, trying to **avoid falling rocks**, **collect coins** and **hearts**, and **achieve the highest score possible**.  
You can control the spaceship either via **on-screen buttons** or by **tilting the device (using sensors)**.

---

## ⚙️ Main Features

### Control Options

- **Sensor Mode:**  
  - Tilt **right** – move spaceship right  
  - Tilt **left** – move spaceship left  
  - Tilt **forward** – increase falling speed of rocks  
  - Tilt **backward** – decrease falling speed of rocks  

- **Button Mode:**  
  - On-screen buttons for moving the spaceship **left** and **right**  

### Gameplay Elements

- **5 movement lanes** at the bottom of the screen  
- **Collision sound effect** when hitting a rock  
- **Collect coins** to increase **score** (with sound effects)  
- **Collect hearts** to restore **lives** (up to 3, also with sound effects)
- **Game over sound effect** when losing
- **High score saving** along with **geographic location** of the player 

---

## 🎮 Main Menu

- Choose control mode: **buttons** or **sensors**  
- Navigate to the **High Scores** screen  

---

## 🏆 High Scores Screen

- Consists of **two Fragments**:  
  1. **High Scores Table** – displays the **top 10 scores**  
  2. **Map** – shows the **location** of each **high score** using **Google Maps API**  
- Selecting a high score in the table updates the map view  

---

## 📂 Project Structure

- `app/src/main/java/com/example/task1_11345/` – game logic and UI  
- `app/src/main/java/com/example/task1_11345/utilities/TiltManager.kt` – sensor management  
- `app/src/main/java/com/example/task1_11345/utilities/SingleSoundPlayer.kt` – sound effect player  
- `res/drawable/` – game graphics (spaceship, rocks, hearts, etc.)  
- `res/layout/` – UI XML layout files
- Uses:
    - `RecyclerView` for score table
    - `SharedPreferences` for saving the score table
    - **Google Maps API** for location display

---

## 🛠️ Tech Stack

- **Language:** Kotlin  
- **Platform:** Android SDK (compileSdk 35, minSdk 26)  
- **Build System:** Gradle (Kotlin DSL)  
- **Java Compatibility:** Java 11 (JVM target 11)  
- **UI Components:** Material Design (com.google.android.material)
- **Sensors:** Accelerometer via SensorManager  
- **Audio:** MediaPlayer / SoundPool  

---

## 🚀 Getting Started

1. Clone the repository:  
   ```bash
   git clone https://github.com/roniltnt/SpaceshipGame-task-2.git
2. Open the project in Android Studio (Electric Eel or newer recommended).
3. Sync Gradle dependencies.
4. Connect an Android device or start an emulator (API 26+).
5. Run the app (▶) to build and launch the game!
