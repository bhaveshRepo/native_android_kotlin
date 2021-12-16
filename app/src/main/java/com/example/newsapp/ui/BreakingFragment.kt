package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.ArticleAdapter
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking.*
import okhttp3.internal.addHeaderLenient

class BreakingFragment : Fragment(R.layout.fragment_breaking) {

    private val TAG=  "Breaking Fragment"

    lateinit var newsViewModel : NewsViewModel
    lateinit var articleAdapter: ArticleAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel
        setupRecyclerView()


        articleAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
            putSerializable("article",it)
            }
        }


        newsViewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    paginationProgressBar.visibility = View.VISIBLE
                    it.data?.let {
                        articleAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    it.message?.let{
                        Log.e(TAG, "An error occured $it")
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){

        articleAdapter = ArticleAdapter()
        rvBreakingNews.apply{
        adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}