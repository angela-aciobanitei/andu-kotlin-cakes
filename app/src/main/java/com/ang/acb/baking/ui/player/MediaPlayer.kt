package com.ang.acb.baking.ui.player

import android.content.Context
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.ang.acb.baking.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


interface MediaPlayer {

    fun getPlayer(context: Context): ExoPlayer
    fun play(mediaUri: Uri)
    fun releasePlayer()
    fun setMediaSessionState(isActive: Boolean)
}


class MediaPlayerImpl : MediaPlayer {

    companion object {
        private const val TAG = "MediaPlayerTag"
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder


    override fun getPlayer(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        initializeMediaSession()
        return exoPlayer
    }

    override fun play(mediaUri: Uri) {
        // Create a media source representing the media to be played.
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(context, userAgent)
        // A progressive media source is one which is obtained through progressive download.
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaUri)

        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = true
    }


    override fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }


    override fun setMediaSessionState(isActive: Boolean) {
        mediaSession.isActive = isActive
    }


    private fun initializePlayer() {
        // Create the player using the ExoPlayerFactory.
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context)
    }

    private fun initializeMediaSession() {
        mediaSession = MediaSessionCompat(context, TAG)
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        mediaSession.setMediaButtonReceiver(null)

        stateBuilder = PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                PlaybackStateCompat.ACTION_FAST_FORWARD or
                PlaybackStateCompat.ACTION_REWIND
        )

        mediaSession.setPlaybackState(stateBuilder.build())
        mediaSession.setCallback(SessionCallback())
        mediaSession.isActive = true
    }


    private inner class SessionCallback : MediaSessionCompat.Callback() {

        private val SEEK_WINDOW_MILLIS = 10000

        override fun onPlay() {
            exoPlayer.playWhenReady = true
        }

        override fun onPause() {
            exoPlayer.playWhenReady = false
        }

        override fun onRewind() {
            exoPlayer.seekTo(exoPlayer.currentPosition - SEEK_WINDOW_MILLIS)
        }

        override fun onFastForward() {
            exoPlayer.seekTo(exoPlayer.currentPosition + SEEK_WINDOW_MILLIS)
        }
    }
}