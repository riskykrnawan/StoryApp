package com.example.storyapp.ui.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.helper.Utils.withDateFormat
import com.example.storyapp.helper.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("sessions")

class DetailActivity : AppCompatActivity() {

    private var _activityDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activityDetailBinding

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val pref = SessionPreferences.getInstance(dataStore)
        detailViewModel = obtainViewModel(this@DetailActivity, pref)

        detailViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }

        val storyId = intent.getStringExtra(EXTRA_ID)
        storyId?.let { detailViewModel.getStoryById(it) }

        detailViewModel.story.observe(this) { story ->
            setStoryData(story)
            playAnimation()
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(true)
                title = story.name
            }
        }

        detailViewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this@DetailActivity, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setStoryData(story: Story) {
        binding?.ivDetailPhoto?.let {
            Glide.with(applicationContext).load(story.photoUrl).into(it)
        }
        binding?.tvDetailName?.text = story.name
        binding?.tvDetailCreatedAt?.text = story.createdAt.withDateFormat()
        binding?.tvDetailDescription?.text = story.description
    }

    private fun showLoading(state: Boolean) {
        binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(
        activity: AppCompatActivity, pref: SessionPreferences
    ): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }


    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private fun playAnimation() {
        val ivDetailPhoto =
            ObjectAnimator.ofFloat(binding?.ivDetailPhoto, View.ALPHA, 1f).setDuration(300)
        val tvDetailName =
            ObjectAnimator.ofFloat(binding?.tvDetailName, View.ALPHA, 1f).setDuration(300)
        val tvDetailCreatedAt =
            ObjectAnimator.ofFloat(binding?.tvDetailCreatedAt, View.ALPHA, 1f).setDuration(300)
        val tvDetailDescription =
            ObjectAnimator.ofFloat(binding?.tvDetailDescription, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(ivDetailPhoto, tvDetailName, tvDetailCreatedAt, tvDetailDescription)
            start()
        }
    }
}