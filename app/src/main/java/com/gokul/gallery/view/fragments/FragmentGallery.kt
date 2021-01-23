package com.gokul.gallery

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokul.gallery.adapter.UnSplashAdapter
import com.gokul.gallery.adapter.UnSplashHeaderFooterAdapter
import com.gokul.gallery.databinding.FragmentGalleryBinding
import com.gokul.gallery.model.UnSplashModel
import com.gokul.gallery.viewModel.UnSplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentGallery : Fragment(R.layout.fragment_gallery), UnSplashAdapter.onItemClickListener {
    private val viewModel by viewModels<UnSplashViewModel>()
    private var _binder: FragmentGalleryBinding? = null
    private val binding get() = _binder
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search, menu)
        val search = menu.findItem(R.id.search)
        val searchItem = search.actionView as? SearchView
        searchItem?.isSubmitButtonEnabled = true
        searchItem?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding?.apply {
                        recyclerView.scrollToPosition(0)
                        viewModel.searchPhotos(query)
                        searchItem.clearFocus()
                    }
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binder = FragmentGalleryBinding.bind(view)
        val adapter = UnSplashAdapter(this)
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding?.apply {
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                progressbar.isVisible = loadState.source.refresh is LoadState.Loading
                textviewError.isVisible = loadState.source.refresh is LoadState.Error
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                if (loadState.source.refresh is LoadState.NotLoading && adapter.itemCount < 1 && loadState.append.endOfPaginationReached) {
                    recyclerView.isVisible = false
                    textviewResultNotFound.isVisible = true
                    progressbar.isVisible = false
                } else {
                    textviewResultNotFound.isVisible = false
                }
            }
        }
        binding?.apply {
            buttonRetry.setOnClickListener {
                adapter.refresh()
            }
        }
        binding?.apply {
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                footer = UnSplashHeaderFooterAdapter { adapter.refresh() },
                header = UnSplashHeaderFooterAdapter { adapter.refresh() }
            )
            recyclerView.layoutManager = LinearLayoutManager(root.context)
            recyclerView.setHasFixedSize(true)
        }
        setHasOptionsMenu(true)
    }

    override fun onClickNavigate(photo: UnSplashModel) {
        val action = FragmentGalleryDirections.actionFragmentGalleryToDetailedFragment(photo)
        findNavController().navigate(action)
    }

    override fun onClickDownload(photo: UnSplashModel) {
        Toast.makeText(binding!!.root.context, "Downloading please wait...", Toast.LENGTH_SHORT).show()
        val manager = DownloadManager.Request(Uri.parse(photo.urls.full))
        manager.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        manager.setTitle("Gallery")
        manager.setDescription("Downloading Image please wait...")
        manager.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${photo.user.name}_${System.currentTimeMillis()}.jpg"
        )
        manager.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val request = binding!!.root.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        request.enqueue(manager)
    }
}
