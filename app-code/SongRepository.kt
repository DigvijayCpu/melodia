package com.melodia.repository

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.tasks.await

data class Song(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val category: String = "",
    val url: String = "",
    val duration: String = "",
    val thumbnail: String = "",
    val releaseDate: String = ""
)

class SongRepository {
    private val database = FirebaseDatabase.getInstance().reference
    
    suspend fun fetchAllSongs(): List<Song> {
        return try {
            val snapshot = database.child("songs").get().await()
            val songs = mutableListOf<Song>()
            
            snapshot.children.forEach { child ->
                val song = child.getValue(Song::class.java)
                if (song != null) {
                    songs.add(song.copy(id = child.key ?: ""))
                }
            }
            songs
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun fetchSongsByCategory(category: String): List<Song> {
        return try {
            val snapshot = database.child("songs")
                .orderByChild("category")
                .equalTo(category)
                .get().await()
            
            val songs = mutableListOf<Song>()
            snapshot.children.forEach { child ->
                val song = child.getValue(Song::class.java)
                if (song != null) {
                    songs.add(song.copy(id = child.key ?: ""))
                }
            }
            songs
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun addSong(song: Song): Boolean {
        return try {
            val songId = database.child("songs").push().key ?: return false
            database.child("songs").child(songId).setValue(song).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
