package com.example.storyapp.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.Story
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.helper.SessionPreferences
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

        val pref = SessionPreferences.getInstance(dataStore)
        detailViewModel = obtainViewModel(this@DetailActivity, pref)

        detailViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }

        val storyId = intent.getStringExtra(EXTRA_ID)
        storyId?.let { detailViewModel.getStoryById(it) }

        detailViewModel.story.observe(this) { story ->
            setStoryData(story)
        }
    }

    private fun setStoryData(story: Story) {
        binding?.ivStory?.let {
            Glide.with(applicationContext).load(story.photoUrl).into(it)
        }
        binding?.tvDescription?.text = story.description
        binding?.tvName?.text = story.name
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
}