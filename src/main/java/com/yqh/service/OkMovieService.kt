package com.yqh.service

import com.yqh.model.SearchMovie
import com.yqh.utils.HttpUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service
import org.yaml.snakeyaml.util.UriEncoder

/**
 * @author yueqiuhong
 * @date 2021-02-05 13:15
 */
@Service
class OkMovieService : MovieServiceBase() {
    override fun supportType() = "ok"
    private val MAX_TRY_COUNT = 5

    private fun searchPage(s: String): Document {
        var count = 0
        fun searchPage0(): Document {
            if (count++ > MAX_TRY_COUNT) {
                throw Exception("搜索失败, 请稍后重试")
            }
            var document =
                Jsoup.parse(
                    HttpUtils.postForm(
                        """https://www.okzyw.com/index.php?m=vod-search""",
                        """wd=${UriEncoder.encode(s)}&submit=search"""
                    )
                )
            if (document.selectFirst("a").text() == "continue") {
                document =
                    Jsoup.parse(HttpUtils.get("""https://www.okzyw.com${document.selectFirst("a").attr("href")}"""))
            }
            return document
        }

        var document = searchPage0()
        while (document.selectFirst(".xing_vb") == null) {
            document = searchPage0()
        }
        return document
    }

    override fun searchMovie(s: String): List<SearchMovie> {
        val children = searchPage(s).selectFirst(".xing_vb").children().filter { it.selectFirst("[class=tt]") != null }
        return listOf(*children.map {
            val item = it.selectFirst(".xing_vb4 a")
            val name = """${item.text()} (${it.selectFirst(".xing_vb6").text()})"""
            val url = """https://www.okzyw.com${item.attr("href")}"""
            SearchMovie(name, url)
        }.toTypedArray())
    }

    override fun getMovieDetail(url: String): List<String> {
        val detailDocument = Jsoup.parse(HttpUtils.get(url))
        val detailElements =
            detailDocument.select(".vodplayinfo").filter { it.text().contains("\$http") }

        return detailElements.map { i ->
            val li = i.selectFirst("ul").children()
            return@map li.joinToString("<br>") { j ->
                val text = j.text()
                val index = text.indexOf("$") + 1
                val content = text.substring(0, index) + UriEncoder.encode(text.substring(index))
                """[${i.selectFirst(".suf")?.text()}] $content"""
            }
        }
    }


}