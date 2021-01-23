package com.gokul.gallery.view.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gokul.gallery.R
import com.gokul.gallery.databinding.FragmentDetailedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedFragment : Fragment(R.layout.fragment_detailed) {
    private lateinit var binding: FragmentDetailedBinding
    private val args by navArgs<DetailedFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailedBinding.bind(view)
        val photo = args.detailedFragment
        binding.apply {
            Glide.with(this@DetailedFragment)
                .load(photo.urls.regular)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        tvDescription.isVisible = photo.description != null
                        tvCreator.isVisible = true
                        progressBar.isVisible = false
                        return false
                    }
                })
                .into(binding.detailedImageView)
            tvCreator.apply {
                text = photo.user.name
                paint.isUnderlineText = true
                isClickable = true
                setOnClickListener {
                    val uri = Uri.parse(photo.user.attributionUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
            }
            tvDescription.text = photo.description
        }
    }
}