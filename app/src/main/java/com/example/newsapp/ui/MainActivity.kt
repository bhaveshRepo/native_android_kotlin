package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.db.ArticleDatabaseInstance
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.viewmodel.NewsModelProviderFactory
import com.example.newsapp.viewmodel.NewsViewModel

class MainActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val repository = NewsRepository(ArticleDatabaseInstance(this) as ArticleDatabaseInstance)
        val providerFactory  = NewsModelProviderFactory(application, repository)
        newsViewModel = ViewModelProvider(this, providerFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

        println("Commit inside Experimental Branch")
        println("This is experimental commit")
        println("This is branch 3")
        println("Edited after adding repo to Github")
        println("Pushed using commandline origin 1")


    }
}