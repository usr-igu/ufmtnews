package br.com.printk.ufmtnews.news.remote

import br.com.printk.ufmtnews.news.newslist.Category
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class UfmtApi(
    private val httpClient: HttpClient,
) {
    suspend fun fetchNewsPage(
        page: Int,
        category: Category?,
    ): UfmtAkwardResponse {
        val url =
            "https://api-portal.ufmt.br/v1/news/all?branch=4&language=pt${
                if (category != null) "&category=${category.id}" else ""
            }"

        return if (page == 1) {
            httpClient
                .get(url) {
                    parameter("page", page)
                    parameter("perPage", PAGE_SIZE)
                }
                .body<UfmtNewsAllFirstPageResponse>()
        } else {
            httpClient
                .get(url) {
                    parameter("page", page)
                    parameter("perPage", PAGE_SIZE)
                }
                .body<UfmtNewsAllDto>()
        }
    }

    suspend fun fetchNews(slug: String): UfmtNewsInfoDto {
        val url = "https://api-portal.ufmt.br/v1/news/$slug?language=pt"

        return httpClient.get(url).body()
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
