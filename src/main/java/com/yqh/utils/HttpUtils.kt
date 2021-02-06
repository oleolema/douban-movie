package com.yqh.utils

import org.apache.commons.io.IOUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * @author yueqiuhong
 * @date 2021-02-04 17:01
 */
object HttpUtils {

    private val httpClient: HttpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()
    private val closeableHttpClient: CloseableHttpClient = HttpClients.createDefault()
    private val commonRequest: HttpRequest.Builder
        get() {
            return HttpRequest.newBuilder().header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 10; Redmi K20 Pro Premium Edition Build/QKQ1.190825.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.92 Mobile Safari/537.36 MMWEBID/4761 MicroMessenger/7.0.9.1560(0x27000934) Process/tools NetType/4G Language/zh_CN ABI/arm64"
            )
        }

    fun httpGet(url: String): String {
        val httpRequest =
            commonRequest.GET().uri(URI.create(url)).build()
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body()
    }

    fun get(url: String): String {
        val httpPost = HttpPost().apply {
            uri = URI.create(url)
            addHeader("Content-Type", "application/x-www-form-urlencoded")
            addHeader(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 10; Redmi K20 Pro Premium Edition Build/QKQ1.190825.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.92 Mobile Safari/537.36 MMWEBID/4761 MicroMessenger/7.0.9.1560(0x27000934) Process/tools NetType/4G Language/zh_CN ABI/arm64"
            )
        }
        val execute = getHttpClient().execute(httpPost)
        return IOUtils.toString(execute.entity.content)
    }

    fun post(url: String, body: String): String {
        val httpRequest =
            commonRequest.POST(HttpRequest.BodyPublishers.ofString(body)).uri(URI.create(url)).build()
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body()
    }

    fun postForm(url: String, body: String): String {
        val httpPost = HttpPost(URI.create(url)).apply {
            entity = UrlEncodedFormEntity(URLEncodedUtils.parse(body, StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        }
        val execute = getHttpClient().execute(httpPost)
        return IOUtils.toString(execute.entity.content)
    }

//    fun postForm(url: String, body: String): String {
//        val httpRequest =
//            HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body))
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .uri(URI.create(url)).build()
//        return HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString()).body()
//    }

    private fun getHttpClient(): CloseableHttpClient {
        return try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(object : X509TrustManager {

                override fun checkClientTrusted(
                    x509Certificates: Array<X509Certificate>,
                    s: String
                ) {
                }

                override fun checkServerTrusted(
                    x509Certificates: Array<X509Certificate>,
                    s: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }), SecureRandom())
            val socketFactory =
                SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)
            HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build()
        } catch (e: Exception) {
            HttpClientBuilder.create().build()
        }
    }


}