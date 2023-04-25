package com.example.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityHomeBinding
import com.example.storyapp.helper.SessionPreferences
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.ui.add_story.AddStoryActivity
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

        //delete this
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

        binding?.fabActionAdd?.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }

        showRecyclerItem()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                homeViewModel.deleteSession()
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showRecyclerItem() {
        val layoutManager = LinearLayoutManager(this)
        binding?.rvStories?.layoutManager = layoutManager
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