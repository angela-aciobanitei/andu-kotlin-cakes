package com.ang.acb.baking.ui.recipedetails


import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ang.acb.baking.R
import com.ang.acb.baking.data.database.Step
import com.ang.acb.baking.databinding.FragmentStepDetailsBinding
import com.ang.acb.baking.utils.GlideApp
import com.ang.acb.baking.utils.autoCleared
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class StepDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: StepDetailsViewModel by viewModels { viewModelFactory }
    private val args: StepDetailsFragmentArgs by navArgs()
    private var binding: FragmentStepDetailsBinding by autoCleared()

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var shouldPlayWhenReady = false
    private var playbackPosition: Long = 0
    private var currentStepCount = -1

    override fun onAttach(context: Context) {
        // When using Dagger with Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment.
        binding = FragmentStepDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Save the recipe ID and current step position sent via Navigation safe args
        viewModel.init(args.recipeId, args.stepPosition)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restoreInstanceState(savedInstanceState)

        viewModel.step.observe(viewLifecycleOwner, Observer {stepResource ->
            binding.step = stepResource.data
            stepResource.data?.let { step -> handleVideoUrl(step) }
            handleStepButtons()
        })
    }


    private fun handleVideoUrl(step: Step) {
        // If step has a video, initialize player, else display an image.
        val videoUrl: String? = step.videoURL
        if (!TextUtils.isEmpty(videoUrl)) {
            initializePlayer(Uri.parse(videoUrl))
        } else {
            val thumbnailUrl: String? = step.thumbnailURL
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                GlideApp
                    .with(binding.placeholderImage.context)
                    .load(thumbnailUrl)
                    // Display a placeholder until the image is loaded and processed.
                    .placeholder(R.drawable.loading_animation)
                    // Provide an error placeholder when Glide is unable to load the
                    // image. This will be shown for the non-existing-url.
                    .error(R.drawable.ic_broken_image)
                    // Use fallback image resource when the url can be null.
                    .fallback(R.drawable.baking)
                    .into(binding.placeholderImage)
            } else {
                binding.placeholderImage.setImageResource(R.drawable.baking)
            }
        }
    }


    private fun handleStepButtons() {
        binding.previousStepButton.setOnClickListener {
            resetPlayer()
            viewModel.onPrev()
        }

        binding.nextStepButton.setOnClickListener {
            resetPlayer()
            viewModel.onNext()
        }

        // Necessary because Espresso cannot read data binding callbacks.
        binding.executePendingBindings()
    }


    private fun initializePlayer(mediaUri: Uri) {
        // See: https://exoplayer.dev/hello-world
        if (simpleExoPlayer == null) {
            // Create the player using the ExoPlayerFactory.
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
            // Attach the payer to the view.
            binding.exoplayerView.player = simpleExoPlayer
        }
        // Create a media source representing the media to be played.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), getString(R.string.app_name))
            )
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaUri)
        // Prepare the player.
        simpleExoPlayer!!.prepare(mediaSource)
        // Control the player.
        simpleExoPlayer!!.seekTo(playbackPosition)
        simpleExoPlayer!!.playWhenReady = shouldPlayWhenReady
    }


    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            // Returns the playback position in the current content window
            // or ad, in milliseconds.
            playbackPosition = simpleExoPlayer!!.currentPosition
            // Returns whether playback will proceed when ready (i.e. when
            // Player.getPlaybackState() == Player.STATE_READY.)
            shouldPlayWhenReady = simpleExoPlayer!!.playWhenReady
            simpleExoPlayer!!.stop()
            simpleExoPlayer!!.release()
            simpleExoPlayer = null
        }
    }


    private fun resetPlayer() {
        shouldPlayWhenReady = true
        playbackPosition = 0
        if (simpleExoPlayer != null) simpleExoPlayer!!.stop()
    }


    override fun onPause() {
        super.onPause()
        // See: https://www.raywenderlich.com/5573-media-playback-on-android-with-exoplayer-getting-started
        // Release the player in onPause() if on Android Marshmallow and below.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        // Release the player in onStop() if on Android Nougat and above
        // because of the multi window support that was added in Android N.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(PLAYBACK_POSITION_KEY, playbackPosition)
        outState.putBoolean(PLAY_WHEN_READY_KEY, shouldPlayWhenReady)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PLAYBACK_POSITION_KEY)) {
                playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY)
            }
            if (savedInstanceState.containsKey(PLAY_WHEN_READY_KEY)) {
                shouldPlayWhenReady =  savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY)
            }
        }
    }

    companion object {
        private const val PLAYBACK_POSITION_KEY = "CURRENT_PLAYBACK_POSITION_KEY";
        private const val PLAY_WHEN_READY_KEY = "SHOULD_PLAY_WHEN_READY_KEY";
    }
}
