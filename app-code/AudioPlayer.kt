package com.melodia.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var currentSongUrl: String = ""
    
    interface PlayerListener {
        fun onPlaybackStart()
        fun onPlaybackPause()
        fun onPlaybackEnd()
        fun onError(error: String)
    }
    
    private var listener: PlayerListener? = null
    
    fun setListener(listener: PlayerListener) {
        this.listener = listener
    }
    
    fun playSong(url: String) {
        try {
            if (currentSongUrl == url && mediaPlayer != null) {
                mediaPlayer?.start()
                listener?.onPlaybackStart()
                return
            }
            
            stop()
            currentSongUrl = url
            
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                
                setDataSource(url)
                prepareAsync()
                
                setOnPreparedListener {
                    Log.d("AudioPlayer", "Song prepared, starting playback")
                    start()
                    listener?.onPlaybackStart()
                }
                
                setOnCompletionListener {
                    Log.d("AudioPlayer", "Song completed")
                    listener?.onPlaybackEnd()
                }
                
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayer", "Error: $what, $extra")
                    listener?.onError("Playback error: $what")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Exception: ${e.message}")
            listener?.onError(e.message ?: "Unknown error")
        }
    }
    
    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                listener?.onPlaybackPause()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Pause error: ${e.message}")
        }
    }
    
    fun resume() {
        try {
            if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
                listener?.onPlaybackStart()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Resume error: ${e.message}")
        }
    }
    
    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            currentSongUrl = ""
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Stop error: ${e.message}")
        }
    }
    
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
    
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
    
    fun seekTo(position: Int) {
        try {
            mediaPlayer?.seekTo(position)
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Seek error: ${e.message}")
        }
    }
    
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
    
    fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Release error: ${e.message}")
        }
    }
}
