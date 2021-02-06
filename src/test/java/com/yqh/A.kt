package com.yqh

import com.yqh.service.AllMovieService
import com.yqh.service.OkMovieService
import com.yqh.utils.HttpUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @author yueqiuhong
 * @date 2021-02-04 16:33
 */
//@SpringBootTest
//@ExtendWith
class A {

    @Autowired
    lateinit var allMovieService: AllMovieService

    @Autowired
    lateinit var okMovieService: OkMovieService

    @Test
    fun test1() {
        println(okMovieService.search("沐浴之王"))
    }

    @Test
    fun test2() {
        println(OkMovieService().search("沐浴之王"))
    }

    @Test
    fun test3() {
        val result = HttpUtils.postForm(
            """https://www.okzyw.com/index.php?m=vod-search""",
            """wd=${URLEncoder.encode("沐浴之王",StandardCharsets.UTF_8)}&submit=search"""
        )
        println(result)
    }

}