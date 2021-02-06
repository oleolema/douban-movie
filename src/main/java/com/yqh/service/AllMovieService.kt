package com.yqh.service

import com.yqh.model.Movie
import com.yqh.model.SearchMovie
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

/**
 * @author yueqiuhong
 * @date 2021-02-05 13:15
 */
@Service
class AllMovieService(val baiduPanMovieService: BaiduPanMovieService, val okMovieService: OkMovieService) :
    MovieServiceBase() {

    override fun supportType() = "all"

    override fun search(s: String): List<Movie?> {
        return internalHandler { it.search(s) }
    }

    override fun searchMovie(s: String): List<SearchMovie> {
        return internalHandler { it.searchMovie(s) }
    }

    override fun getMovieDetail(url: String): List<String> {
        return internalHandler { it.getMovieDetail(url) }
    }

    private val movieServiceHandlers = listOf(
        baiduPanMovieService,
        okMovieService
    )

    private fun getHandler(type: String): MovieServiceBase {
        if (type == supportType()) {
            return this
        }
        return movieServiceHandlers.first { it.supportType() == type }
    }

    private fun <T> internalHandler(f: (it: MovieServiceBase) -> List<T>): List<T> {
        val completableFutureList = mutableListOf<CompletableFuture<Any>>()
        val list = MutableList<List<T>>(2) { listOf() }
        var index = 0
        movieServiceHandlers.map {
            val currentIndex = index++
            completableFutureList.add(CompletableFuture.supplyAsync {
                list[currentIndex] = f(it)
            })
        }
        CompletableFuture.allOf(*completableFutureList.toTypedArray()).join()
        return list.flatten()
    }


    fun searchByType(s: String, type: String): List<Movie?> {
        return getHandler(type).search(s)
    }

    fun searchMovieByType(s: String, type: String): List<SearchMovie> {
        return getHandler(type).searchMovie(s)
    }

    fun searchMovieDetailByType(url: String, type: String): List<String> {
        return getHandler(type).getMovieDetail(url)
    }

}