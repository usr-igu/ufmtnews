package br.com.printk.ufmtnews.news.newslist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.printk.ufmtnews.news.remote.UfmtApi
import br.com.printk.ufmtnews.news.remote.UfmtNewsAllDto
import br.com.printk.ufmtnews.news.remote.UfmtNewsAllFirstPageResponse
import java.util.concurrent.ConcurrentHashMap

class UfmtNewsPagingSource(
    private val api: UfmtApi,
    private val category: Category?,
) : PagingSource<Int, UfmtNewsListModel>() {
    companion object {
        // HACK: I getting duplicated news entries.
        val seen = ConcurrentHashMap<String, Int>()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UfmtNewsListModel> {
        try {
            val page = params.key ?: 1

            // Reset the seen map on first page load.
            if (page == 1) {
                seen.clear()
            }

            println("Fetching page $page, category: $category")

            val response = api.fetchNewsPage(page, category)

            var hasNextPage: Boolean

            val data =
                when (response) {
                    is UfmtNewsAllFirstPageResponse -> {
                        hasNextPage = response.nextPageUrl != null
                        response.data
                            .filter { entry ->
                                val count = seen.getOrDefault(entry.slug, 0)
                                val new = count + 1
                                seen[entry.slug] = new
                                new == 1
                            }
                            .map { entry -> UfmtNewsListModel.Companion.fromRemoteDto(entry) }
                    }

                    is UfmtNewsAllDto -> {
                        hasNextPage = response.nextPageUrl != null
                        response.data.values
                            .filter { entry ->
                                val count = seen.getOrDefault(entry.slug, 0)
                                val new = count + 1
                                seen[entry.slug] = new
                                new == 1
                            }
                            .map { entry -> UfmtNewsListModel.Companion.fromRemoteDto(entry) }
                    }
                }

            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (hasNextPage) page + 1 else null,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UfmtNewsListModel>): Int? = state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
}
