package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabaseInstance
import com.example.newsapp.model.Article

class NewsRepository(private val db : ArticleDatabaseInstance) {

    suspend fun getBreakingNews(country: String, pageNumber: Int) = RetrofitInstance.
            api.getBreakingNews(country,pageNumber)

    suspend fun getAllNews(query: String, pageNumber: Int )= RetrofitInstance.
            api.getAllNews(query, pageNumber)

    suspend fun upsert(article: Article) = db.articlesDao().upsert(article)

    fun favourites() = db.articlesDao().getAllData()

    suspend fun delete(article: Article) = db.articlesDao().deleteArticles(article)

}