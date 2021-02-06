package com.yqh.service

import com.yqh.model.SearchMovie
import com.yqh.utils.HttpUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.util.UriEncoder
import java.net.http.HttpClient

/**
 * @author yueqiuhong
 * @date 2021-02-04 11:13
 */
@Service
class BaiduPanMovieService : MovieServiceBase() {

    private val httpClient: HttpClient = HttpClient.newHttpClient()
    override fun supportType() = "baidu"

    override fun searchMovie(s: String): List<SearchMovie> {
        val document = Jsoup.parse(HttpUtils.get("""http://b1.qianxun654321.com/?s=${UriEncoder.encode(s)}"""))
        val children = document.selectFirst(".list-grouped").children()
        return listOf(*children.map {
            val item = it.selectFirst(".list-body a")
            val name = item.text()
            val url = item.attr("href")
            SearchMovie(name, url)
        }.toTypedArray())
    }

    override fun getMovieDetail(url: String): List<String> {
        val detailDocument = Jsoup.parse(HttpUtils.get(url))
        val detailElements =
            detailDocument.selectFirst(".post-content").getElementsMatchingOwnText("""链接:.*提取码:""")
        return detailElements.map { i -> i.text() }
    }

}