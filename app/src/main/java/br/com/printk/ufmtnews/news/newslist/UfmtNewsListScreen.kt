package br.com.printk.ufmtnews.news.newslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UfmtNewsListScreen(
    modifier: Modifier = Modifier,
    onNewsEntryClick: (String) -> Unit,
    viewModel: UfmtNewsListViewModel = koinViewModel(),
) {
    val news = viewModel.news.collectAsLazyPagingItems()
    val category = viewModel.category.collectAsStateWithLifecycle()

    val lazyColumnState = rememberLazyListState()

    val categoriesScrollState = rememberScrollState()

    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        Row(modifier = Modifier.horizontalScroll(categoriesScrollState).padding(bottom = 8.dp)) {
            for (entry in Category.entries) {
                InputChip(
                    selected = entry.id == category.value?.id,
                    onClick = {
                        if (category.value != null && category.value == entry) {
                            viewModel.changeCategory(null)
                        } else {
                            viewModel.changeCategory(entry)
                        }
                    },
                    label = { Text(text = entry.getCategoryName(entry.id)) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }

        if (news.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                state = lazyColumnState,
            ) {
                items(
                    count = news.itemCount,
                    key = news.itemKey { it.slug },
                    contentType = { index -> "NewsListItem" },
                ) { index ->
                    val item = news[index]
                    if (item != null) {
                        Column(
                            modifier =
                            Modifier.padding(bottom = 24.dp).clickable {
                                onNewsEntryClick(item.slug)
                            },
                        ) {
                            CategoryBanner(item.categoryName, item.categoryColor)
                            PostBody(item.title, item.publicationDate, item.featuredImageUrl)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryBanner(categoryName: String, categoryColor: Color) {
    Row(
        modifier =
        Modifier.padding(bottom = 8.dp)
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = categoryName.uppercase(),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.fillMaxHeight().width(12.dp).background(categoryColor))
    }
}

@Composable
fun PostBody(
    title: String,
    publicationDate: String,
    featuredImageUrl: String = "",
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
        Modifier.height(IntrinsicSize.Max).background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(8.dp).weight(3f),
        ) {
            Text(
                text = publicationDate,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
            )
        }
        if (featuredImageUrl.isNotEmpty()) {
            AsyncImage(
                modifier = Modifier.weight(1f),
                alignment = Alignment.CenterStart,
                contentScale = ContentScale.FillHeight,
                model =
                ImageRequest.Builder(LocalContext.current)
                    .crossfade(true)
                    .data(featuredImageUrl)
                    .build(),
                contentDescription = null,
            )
        }
    }
}
