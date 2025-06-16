package br.com.printk.ufmtnews.news.newsinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.printk.ufmtnews.news.remote.UfmtApi
import br.com.printk.ufmtnews.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UfmtNewsInfoViewModel(
    private val api: UfmtApi,
) : ViewModel() {
    private var _info: MutableStateFlow<Resource<UfmtNewsInfoModel>> =
        MutableStateFlow(Resource.Loading())
    val info = _info.asStateFlow()

    fun fetchNews(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _info.value = Resource.Loading()

                val news = api.fetchNews(slug)

                val info = UfmtNewsInfoModel.fromRemoteDto(news)

                _info.value = Resource.Success(info)
            } catch (e: Exception) {
                _info.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}
