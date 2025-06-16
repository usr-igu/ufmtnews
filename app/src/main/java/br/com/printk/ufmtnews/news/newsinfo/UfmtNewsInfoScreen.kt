package br.com.printk.ufmtnews.news.newsinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.printk.ufmtnews.R
import br.com.printk.ufmtnews.util.Resource
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UfmtNewsInfoScreen(
    slug: String,
    modifier: Modifier = Modifier,
    viewModel: UfmtNewsInfoViewModel = koinViewModel(),
) {
    val info = viewModel.info.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(slug) { viewModel.fetchNews(slug) }

    when (val resource = info.value) {
        is Resource.Loading -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            Column(
                modifier = modifier.padding(horizontal = 12.dp).verticalScroll(scrollState).fillMaxSize(),
            ) {
                val news = resource.data!!

                Box(
                    modifier =
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_efeito),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.TopStart),
                        contentScale = ContentScale.Fit,
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = "POR: ${news.authorName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(top = 8.dp),
                        )

                        Text(
                            text = "DATA: ${news.publicationDate}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }

                AsyncImage(
                    model = news.featuredImage,
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )

                if (news.shortText.isNotBlank()) {
                    Box(
                        modifier =
                        Modifier.width(50.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            .padding(vertical = 2.dp),
                    )

                    Text(
                        text = news.shortText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    )
                }

                CustomHtmlRenderer(html = news.content)

                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    for (tag in news.tags) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier =
                            Modifier.clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        }

        is Resource.Error -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(text = "Houve um erro ao carregar o conteúdo")
            }
        }
    }
}
