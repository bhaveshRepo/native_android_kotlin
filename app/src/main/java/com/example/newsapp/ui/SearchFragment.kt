package com.example.newsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import androidx.lifecycle.Observer
import com.example.newsapp.util.Resource
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.paginationProgressBar
import java.util.*

class SearchFragment : Fragment() {

    private val TAG = "Search Fragment"
    lateinit var  newsViewModel: NewsViewModel
    lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel
        setUpRecycleView()

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    paginationProgressBar.visibility  = View.INVISIBLE
                    it.data?.let {
                            articleAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error ->{
                    paginationProgressBar.visibility = View.INVISIBLE
                    it.message?.let {
                        Log.e(TAG, " Error occured $it")
                    }
                }
                is Resource.Loading ->{
                    paginationProgressBar.visibility = View.VISIBLE
                }
            }
        })


    }





    private fun setUpRecycleView(){
        articleAdapter = ArticleAdapter()
        rvSearchNews.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}