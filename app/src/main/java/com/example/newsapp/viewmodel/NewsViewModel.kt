package com.example.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsAppication
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val newsRepository: NewsRepository) : AndroidViewModel(app) {

    // Now write logic for each fragments data
    // for Breaking News and fo search news .

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber : Int = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPageNumber : Int = 1
    var searchNewsResponse : NewsResponse? = null

    // create function that could load or retrieve data from using the Repository
    // under coroutines

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String)=
        viewModelScope.launch {
            safeCallBreakingNews(countryCode)
        }

    private suspend fun safeCallBreakingNews(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.getBreakingNews(countryCode,breakingPageNumber)
            breakingNews.postValue(handleBreakingResponse(response))
        }catch (t: Throwable){
            when(t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun handleBreakingResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{

        if (response.isSuccessful) {
            searchPageNumber++
            response.body()?.let {
                if (searchNewsResponse == null) {
                    searchNewsResponse = it
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        safeCallSearchNews(searchQuery,)
    }

    private suspend fun safeCallSearchNews(searchQuery: String){
        searchNews.postValue(Resource.Loading())
       try {
           val response = newsRepository.getAllNews(searchQuery, searchPageNumber)
           searchNews.postValue(handelSearchNewsResponse(response))
       }catch (t : Throwable){
            when(t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
       }
    }

    private fun handelSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                if(breakingNewsResponse == null){
                    breakingNewsResponse = it
                }
                else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }


//    private fun hasInternetConnection() : Boolean{
//        val connectivityManager =
//    }



    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<NewsAppication>().getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run{
                when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}