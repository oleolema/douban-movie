package com.yqh.controller

import com.yqh.model.ApiResult
import com.yqh.model.Movie
import com.yqh.model.SearchMovie
import com.yqh.service.AllMovieService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author yueqiuhong
 * @date 2021-02-04 11:13
 */
@RestController
@RequestMapping("movie")
class MovieController(
    val allMovieService: AllMovieService,
) {

    @GetMapping("search")
    fun search(s: String, @RequestParam(defaultValue = "all") type: String): ApiResult<List<Movie?>> {
        return ApiResult.success(allMovieService.searchByType(s, type))
    }

    @GetMapping("searchMovie")
    fun searchMovie(s: String, @RequestParam(defaultValue = "all") type: String): ApiResult<List<SearchMovie>> {
        return ApiResult.success(allMovieService.searchMovieByType(s, type))
    }

    @GetMapping("getMovieDetail")
    fun getMovieDetail(url: String, @RequestParam(defaultValue = "all") type: String): ApiResult<List<String>> {
        return ApiResult.success(allMovieService.searchMovieDetailByType(url, type))
    }


}