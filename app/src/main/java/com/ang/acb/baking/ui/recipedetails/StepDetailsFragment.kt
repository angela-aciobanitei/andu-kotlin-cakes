package com.ang.acb.baking.ui.recipedetails


import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * See: https://exoplayer.dev/hello-world.html
 * See: https://github.com/googlecodelabs/exoplayer-intro/tree/master
 * See: https://codelabs.developers.google.com/codelabs/exoplayer-intro
 * See: https://www.raywenderlich.com/5573-media-playback-on-android-with-exoplayer-getting-started
 */

class StepDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: StepDetailsViewModel by viewModels { viewModelFactory }
    private val args: StepDetailsFragmentArgs by navArgs()
    private var binding: FragmentStepDetailsBinding by autoCleared()

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var playbackPosition: Long = 0
    private var currentWindow = 0
    private var currentStepIndex = -1


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

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentStepIndex = args.stepPosition
        restoreInstanceState(savedInstanceState)
        viewModel.init(args.recipeId, currentStepIndex)

        viewModel.step.observe(viewLifecycleOwner, Observer {stepResource ->
            // Make Step data available to data binding.
            binding.step = stepResource.data

            // Show step short description on action bar.
            (activity as AppCompatActivity).supportActionBar?.title =
                stepResource.data?.shortDescription

            // If step has a valid video URL, initialize player,
            // else display step thumbnail or a placeholder image.
            stepResource.data?.let { step -> handleStepUrl(step) }
            handleStepButtons()
        })
    }


    private fun handleStepUrl(step: Step) {
        // If step has a video, initialize player, else display an image.
        if (!TextUtils.isEmpty(step.videoURL)) {
            val videoUri = Uri.parse(step.videoURL)
            videoUri?.let { initializePlayer(it) }
        } else {
            if (!TextUtils.isEmpty(step.thumbnailURL)) {
                GlideApp
                    .with(binding.placeholderImage.context)
                    .load(step.thumbnailURL)
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
            currentStepIndex--
        }

        binding.nextStepButton.setOnClickListener {
            resetPlayer()
            viewModel.onNext()
            currentStepIndex++
        }

        // Necessary because Espresso cannot read data binding callbacks.
        binding.executePendingBindings()
    }


    private fun initializePlayer(mediaUri: Uri) {
        // https://codelabs.developers.google.com/codelabs/exoplayer-intro
        if (simpleExoPlayer == null) {
            // Create the player using the ExoPlayerFactory.
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
            // Attach the player to the view.
            binding.exoplayerView.player = simpleExoPlayer
        }

        // Create a media source representing the media to be played.
        val userAgent = Util.getUserAgent(requireContext(), getString(R.string.app_name))
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(requireContext(), userAgent)
        val mediaSource: MediaSource = ProgressiveMediaSource
            .Factory(dataSourceFactory)
            .createMediaSource(mediaUri)

        // Prepare the player with the source.
        simpleExoPlayer!!.prepare(mediaSource)

        // Control the player.
        simpleExoPlayer!!.seekTo(currentWindow, playbackPosition)
        simpleExoPlayer!!.playWhenReady = playWhenReady
    }


    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer!!.currentPosition
            currentWindow = simpleExoPlayer!!.currentWindowIndex
            playWhenReady = simpleExoPlayer!!.playWhenReady

            simpleExoPlayer!!.stop()
            simpleExoPlayer!!.release()
            simpleExoPlayer = null
        }
    }


    private fun resetPlayer() {
        playWhenReady = true
        playbackPosition = 0
        currentWindow = 0
        if (simpleExoPlayer != null) simpleExoPlayer!!.stop()
    }


    override fun onPause() {
        super.onPause()
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
        outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady)
        outState.putLong(PLAYBACK_POSITION_KEY, playbackPosition)
        outState.putInt(CURRENT_WINDOW, currentWindow)
        outState.putInt(CURRENT_STEP_INDEX, currentStepIndex)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PLAY_WHEN_READY_KEY)) {
                playWhenReady =  savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY)
            }
            if (savedInstanceState.containsKey(PLAYBACK_POSITION_KEY)) {
                playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY)
            }
            if (savedInstanceState.containsKey(CURRENT_WINDOW)) {
                currentWindow =  savedInstanceState.getInt(CURRENT_WINDOW)
            }
            if (savedInstanceState.containsKey(CURRENT_STEP_INDEX)) {
                currentStepIndex =  savedInstanceState.getInt(CURRENT_STEP_INDEX)
            }

        }
    }

    companion object {
        private const val PLAY_WHEN_READY_KEY = "SHOULD_PLAY_WHEN_READY_KEY";
        private const val PLAYBACK_POSITION_KEY = "CURRENT_PLAYBACK_POSITION_KEY";
        private const val CURRENT_WINDOW = "CURRENT_WINDOW"
        private const val CURRENT_STEP_INDEX = "CURRENT_STEP_INDEX"
    }
}
