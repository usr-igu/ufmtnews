package br.com.printk.ufmtnews.di

import br.com.printk.ufmtnews.news.newsinfo.UfmtNewsInfoViewModel
import br.com.printk.ufmtnews.news.newslist.UfmtNewsListViewModel
import br.com.printk.ufmtnews.news.remote.UfmtApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = false
                        prettyPrint = false
                    },
                )
            }
        }
    }

    singleOf(::UfmtApi)

    viewModelOf(::UfmtNewsInfoViewModel)
    viewModelOf(::UfmtNewsListViewModel)
}
