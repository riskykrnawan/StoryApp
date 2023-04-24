package com.example.storyapp.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.login.LoginActivity
import kotlin.math.absoluteValue

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("sessions")

class HomeActivity : AppCompatActivity() {

    private var _activityHomeBinding: ActivityHomeBinding? = null
    private val binding get() = _activityHomeBinding

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMain)
        supportActionBar?.title = resources.getString(R.string.app_name)

        val pref = SessionPreferences.getInstance(dataStore)
        homeViewModel = obtainViewModel(this@HomeActivity, pref)

        if (savedInstanceState == null) {
            homeViewModel.getStories()
        }

        homeViewModel.statusCode.observe(this) {
            if (it.absoluteValue == 401) {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        homeViewModel.stories.observe(this) { stories ->
            setStoriesData(stories)
        }

        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showRecyclerItem()
    }

    private fun showRecyclerItem() {
        val layoutManager = LinearLayoutManager(this)
        binding?.rvStories?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvStories?.addItemDecoration(itemDecoration)
    }

    private fun setStoriesData(stories: List<ListStoryItem?>?) {
        val listUsers = ArrayList<ListStoryItem>()
        if (stories != null) {
            for (story in stories) {
                if (story != null) {
                    listUsers.add(story)
                }
            }
        }
        val adapter = StoryAdapter(listUsers)
        binding?.rvStories?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityHomeBinding = null
    }

    private fun obtainViewModel(
        activity: AppCompatActivity, pref: SessionPreferences
    ): HomeViewModel {
        val factory = ViewModelFactory(activity.application, pref)
        return ViewModelProvider(activity, factory)[HomeViewModel::class.java]
    }
}