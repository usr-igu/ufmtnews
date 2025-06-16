package br.com.printk.ufmtnews.news.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.com.printk.ufmtnews.news.remote.UfmtApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class UfmtNewsListViewModel(
    private val api: UfmtApi,
) : ViewModel() {
    private var _category: MutableStateFlow<Category?> = MutableStateFlow(null)
    val category = _category.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val news =
        _category
            .flatMapLatest { cat ->
                Pager(
                    config =
                    PagingConfig(
                        pageSize = UfmtApi.PAGE_SIZE,
                        initialLoadSize = UfmtApi.PAGE_SIZE * 2,
                        prefetchDistance = UfmtApi.PAGE_SIZE * 2,
                    ),
                    pagingSourceFactory = { UfmtNewsPagingSource(api, cat) },
                )
                    .flow
            }
            .cachedIn(viewModelScope)

    fun changeCategory(category: Category?) {
        _category.value = category
    }
}
