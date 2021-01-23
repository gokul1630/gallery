package com.gokul.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gokul.gallery.databinding.HeaderfooterBinding

class UnSplashHeaderFooterAdapter(private val retry:()->Unit):LoadStateAdapter<UnSplashHeaderFooterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val v=HeaderfooterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    inner class ViewHolder(private val bind:HeaderfooterBinding) :RecyclerView.ViewHolder(bind.root){
        init {
            bind.buttonRetry.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState){
            bind.apply {
                progressbar.isVisible= loadState is LoadState.Loading
                buttonRetry.isVisible=loadState!=LoadState.Loading
                textviewError.isVisible=loadState!=LoadState.Loading
            }
        }
    }
}