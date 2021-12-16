package com.example.newsapp.db

import android.content.Context
import androidx.room.*
import com.example.newsapp.model.Article

@Database(

    entities = [Article::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class ArticleDatabaseInstance : RoomDatabase() {

    abstract fun articlesDao() : ArticleDao

    companion object{

        private var INSTANCE : ArticleDatabaseInstance? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(this){

            INSTANCE?. createDataBaseContext(context).also {
                INSTANCE = it
            }

        }



    }

    private fun createDataBaseContext(context: Context): ArticleDatabaseInstance {
        return Room.databaseBuilder(context,
            ArticleDatabaseInstance::class.java,"article_db.db")
            .build()

    }

}