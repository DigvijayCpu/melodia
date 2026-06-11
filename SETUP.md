# 🚀 Melodia Setup Guide

## Step 1: GitHub Setup ✅

### Upload Songs to GitHub

1. Go to: https://github.com/DigvijayCpu/melodia
2. Click **Add file** → **Upload files**
3. Create folders:
   - `songs/hindi/`
   - `songs/english/`
   - `songs/regional/`
4. Upload MP3 files

### Get Raw Song Links

1. Click on uploaded song file
2. Click **Raw** button
3. Copy the URL (example):
   ```
   https://raw.githubusercontent.com/DigvijayCpu/melodia/main/songs/hindi/song_name.mp3
   ```

---

## Step 2: Firebase Setup 🔥

### Create Firebase Project

1. Go to: https://console.firebase.google.com
2. Click **Create project**
3. Enter: `melodia`
4. Disable Google Analytics
5. Click **Create project**

### Setup Realtime Database

1. Left sidebar → **Build** → **Realtime Database**
2. Click **Create Database**
3. Select location: `asia-south1` (India)
4. Start in **Test mode** (for development)
5. Click **Enable**

### Firebase Database Structure

```json
{
  "songs": {
    "song_001": {
      "title": "Song Name",
      "artist": "Artist Name",
      "category": "hindi",
      "url": "https://raw.githubusercontent.com/DigvijayCpu/melodia/main/songs/hindi/song.mp3",
      "duration": "3:45",
      "thumbnail": "https://example.com/image.jpg"
    }
  }
}
```

---

## Step 3: Android Kotlin Setup 📱

### Add Firebase Dependencies

In `build.gradle.kts` (Project level):

```gradle
plugins {
    id 'com.google.gms.google-services' version '4.4.0' apply false
}
```

In `build.gradle.kts` (App level):

```gradle
plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'
}

dependencies {
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-database-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'
}
```

### Get google-services.json

1. Firebase Console → Project Settings
2. Download **google-services.json**
3. Place in: `app/`

---

## Step 4: Kotlin Code 💻

### Fetch Songs from Firebase

```kotlin
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot

data class Song(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val url: String = "",
    val duration: String = ""
)

class SongRepository {
    private val database = FirebaseDatabase.getInstance().reference
    
    fun fetchSongs(callback: (List<Song>) -> Unit) {
        database.child("songs").get().addOnSuccessListener { snapshot ->
            val songs = mutableListOf<Song>()
            snapshot.children.forEach { child ->
                val song = child.getValue(Song::class.java)
                if (song != null) {
                    songs.add(song.copy(id = child.key ?: ""))
                }
            }
            callback(songs)
        }
    }
}
```

### Play Audio from URL

```kotlin
import android.media.MediaPlayer

class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    
    fun playSong(url: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnErrorListener { _, what, extra ->
                println("Error: $what, $extra")
                false
            }
        }
    }
    
    fun pause() {
        mediaPlayer?.pause()
    }
    
    fun resume() {
        mediaPlayer?.start()
    }
    
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
```

---

## Step 5: Add Songs to Firebase 🎵

### Manual Entry (Firebase Console)

1. Firebase Console → Realtime Database
2. Click **+** to add data
3. Add song details with GitHub raw URL

### Upload Songs to GitHub

1. GitHub repo → `songs/` folder
2. Upload MP3 files
3. Get raw links
4. Add links to Firebase

---

## ✅ Complete Workflow

1. **Upload Song** → GitHub `songs/` folder
2. **Get Raw Link** → From GitHub
3. **Add to Firebase** → With song metadata
4. **Android App** → Fetches from Firebase & plays

---

## 🔗 Useful Links

- GitHub Repo: https://github.com/DigvijayCpu/melodia
- Firebase Console: https://console.firebase.google.com
- Android Documentation: https://developer.android.com

---

**Happy Coding! 🎵🚀**