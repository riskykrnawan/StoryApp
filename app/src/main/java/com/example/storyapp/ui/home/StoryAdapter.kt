package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.StoryCardBinding
import com.example.storyapp.helper.Utils.withDateFormat
import com.example.storyapp.ui.detail.DetailActivity

class StoryAdapter(private val listStory: ArrayList<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private lateinit var binding: StoryCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StoryAdapter.ViewHolder, position: Int) {
        val story = listStory[position]

        Glide.with(holder.itemView.context).load(story.photoUrl).into(holder.ivItemPhoto)
        holder.tvItemName.text = story.name
        holder.tvItemCreatedAt.text = story.createdAt.withDateFormat()
        holder.tvItemDescription.text =
            if (story.description.count() > 20) "${story.description.substring(0, 20)} ...."
            else story.description

        holder.storyCard.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, story.id)
            ContextCompat.startActivity(holder.itemView.context, intent, Bundle.EMPTY)
        }
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemPhoto = binding.ivItemPhoto
        val tvItemName = binding.tvItemName
        val tvItemCreatedAt = binding.tvItemCreatedAt
        val tvItemDescription = binding.tvItemDescription
        val storyCard = binding.storyCard
    }
}