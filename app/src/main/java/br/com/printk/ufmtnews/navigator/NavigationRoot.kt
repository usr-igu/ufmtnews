package br.com.printk.ufmtnews.navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import br.com.printk.ufmtnews.news.newsinfo.UfmtNewsInfoScreen
import br.com.printk.ufmtnews.news.newslist.UfmtNewsListScreen
import kotlinx.serialization.Serializable

@Serializable data object NewsListNav : NavKey

@Serializable
data class NewsInfoNav(
    val url: String,
) : NavKey

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(NewsListNav)

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(rememberSavedStateNavEntryDecorator()),
            entryProvider = { key ->
                when (key) {
                    is NewsListNav ->
                        NavEntry(key) {
                            UfmtNewsListScreen(
                                modifier = Modifier.padding(innerPadding),
                                onNewsEntryClick = { url: String -> backStack.add(NewsInfoNav(url)) },
                            )
                        }

                    is NewsInfoNav ->
                        NavEntry(key) {
                            UfmtNewsInfoScreen(modifier = Modifier.padding(innerPadding), slug = key.url)
                        }

                    else -> error("Unknown key: $key")
                }
            },
        )
    }
}
