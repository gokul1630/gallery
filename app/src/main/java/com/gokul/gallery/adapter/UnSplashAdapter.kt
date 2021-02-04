package com.gokul.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gokul.gallery.R
import com.gokul.gallery.databinding.ItemUnsplashPhotoBinding
import com.gokul.gallery.model.UnSplashModel

class UnSplashAdapter(private val listener: onItemClickListener) : PagingDataAdapter<UnSplashModel, UnSplashAdapter.ViewHolder>(comparator) {
    inner class ViewHolder(private val binding: ItemUnsplashPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: UnSplashModel) {
            binding.apply {
                Glide.with(itemView)
                        .load(photo.urls.small)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .error(R.drawable.ic_error)
                        .into(imageView)
                user.text = photo.user.username
                btnDownload.setOnClickListener {
                    listener.onClickDownload(photo)
                }
                binding.root.setOnClickListener {
                    listener.onClickNavigate(photo)
                }
            }
        }
    }

    interface onItemClickListener {
        fun onClickNavigate(photo: UnSplashModel)
        fun onClickDownload(photo: UnSplashModel)
    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<UnSplashModel>() {
            override fun areItemsTheSame(oldItem: UnSplashModel, newItem: UnSplashModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UnSplashModel, newItem: UnSplashModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }
}