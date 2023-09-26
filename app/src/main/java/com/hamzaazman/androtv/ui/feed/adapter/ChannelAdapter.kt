package com.hamzaazman.androtv.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hamzaazman.androtv.data.model.ChannelDto
import com.hamzaazman.androtv.databinding.ChannelRowItemBinding

class ChannelAdapter : ListAdapter<ChannelDto, ChannelAdapter.ViewHolder>(DiffCallback()) {

    var onChannelClickItem: (ChannelDto) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ChannelRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ChannelRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(channelDto: ChannelDto) = with(binding) {
            channelName.text = channelDto.name

            channelImage.load(channelDto.logo) {
                crossfade(true)
            }

            this.root.setOnClickListener {
                onChannelClickItem(channelDto)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChannelDto>() {
        override fun areItemsTheSame(oldItem: ChannelDto, newItem: ChannelDto) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChannelDto, newItem: ChannelDto) =
            oldItem == newItem
    }
}