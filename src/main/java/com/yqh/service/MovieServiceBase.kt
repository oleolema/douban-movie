package com.yqh.service

import com.yqh.model.Movie
import com.yqh.model.SearchMovie
import java.util.concurrent.CompletableFuture

/**
 * @author yueqiuhong
 * @date 2021-02-05 14:02
 */
abstract class MovieServiceBase {

    open fun search(s: String): List<Movie?> {
        val searchMovie = searchMovie(s)
        val completableFutureList = mutableListOf<CompletableFuture<Any>>()
        val list = MutableList<Movie?>(searchMovie.size) { null }
        var index = 0
        searchMovie.map { (name, url) ->
            val currentIndex = index++
            completableFutureList.add(CompletableFuture.supplyAsync {
                list[currentIndex] = Movie(name, getMovieDetail(url))
            })
        }
        CompletableFuture.allOf(*completableFutureList.toTypedArray()).join()
        return list
    }

    abstract fun supportType(): String

    abstract fun searchMovie(s: String): List<SearchMovie>

    abstract fun getMovieDetail(url: String): List<String>


}