package br.com.printk.ufmtnews.news.newsinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

@Composable
fun CustomHtmlRenderer(
    modifier: Modifier = Modifier,
    html: String,
) {
    val document = remember(html) { Jsoup.parse(html) }
    val elements = remember(document) { document.body().childNodes() }
    val uriHandler = LocalUriHandler.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        elements.forEach { node -> RenderNode(node, uriHandler) }
    }
}

@Composable
private fun RenderNode(node: Node, uriHandler: UriHandler) {
    when (node) {
        is TextNode -> {
            val text = node.text().trim()
            if (text.isNotEmpty()) {
                Text(text = text, style = MaterialTheme.typography.bodyLarge)
            }
        }

        is Element -> {
            when (node.tagName().lowercase()) {
                "p" -> {
                    val inlineNodes = mutableListOf<Node>()
                    node.childNodes().forEach { child ->
                        if (child is Element && child.tagName().lowercase() == "img") {
                            if (inlineNodes.isNotEmpty()) {
                                val annotatedString = buildAnnotatedString {
                                    inlineNodes.forEach { inlineNode ->
                                        buildSpannable(inlineNode, this, MaterialTheme.colorScheme)
                                    }
                                }
                                Text(text = annotatedString, style = MaterialTheme.typography.bodyLarge)
                                inlineNodes.clear()
                            }
                            RenderNode(child, uriHandler)
                        } else {
                            inlineNodes.add(child)
                        }
                    }
                    if (inlineNodes.isNotEmpty()) {
                        val annotatedString = buildAnnotatedString {
                            inlineNodes.forEach { inlineNode ->
                                buildSpannable(inlineNode, this, MaterialTheme.colorScheme)
                            }
                        }
                        if (annotatedString.isNotBlank()) {
                            Text(text = annotatedString, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6",
                -> {
                    val level = node.tagName().substring(1).toIntOrNull() ?: 1
                    val style =
                        when (level) {
                            1 -> MaterialTheme.typography.headlineLarge
                            2 -> MaterialTheme.typography.headlineMedium
                            3 -> MaterialTheme.typography.headlineSmall
                            4 -> MaterialTheme.typography.titleLarge
                            5 -> MaterialTheme.typography.titleMedium
                            else -> MaterialTheme.typography.titleSmall
                        }
                    val annotatedString = buildAnnotatedString {
                        buildSpannable(node, this, MaterialTheme.colorScheme)
                    }
                    Text(text = annotatedString, style = style)
                }

                "img" -> {
                    val src = node.attr("src")
                    if (src.isNotEmpty()) {
                        AsyncImage(
                            model = src,
                            contentDescription = node.attr("alt").ifEmpty { null },
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                }

                else -> {
                    node.childNodes().forEach { child -> RenderNode(child, uriHandler) }
                }
            }
        }
    }
}

private fun buildSpannable(
    node: Node,
    builder: androidx.compose.ui.text.AnnotatedString.Builder,
    colorScheme: ColorScheme,
) {
    when (node) {
        is TextNode -> {
            val txt = node.text()
            if (txt.isNotBlank()) {
                builder.append(txt)
            }
        }

        is Element -> {
            when (node.tagName().lowercase()) {
                "b",
                "strong",
                -> {
                    builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        node.childNodes().forEach { child -> buildSpannable(child, this, colorScheme) }
                    }
                }

                "i",
                "em",
                -> {
                    builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        node.childNodes().forEach { child -> buildSpannable(child, this, colorScheme) }
                    }
                }

                "u" -> {
                    builder.withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        node.childNodes().forEach { child -> buildSpannable(child, this, colorScheme) }
                    }
                }

                "a" -> {
                    val href = node.attr("href")
                    if (href.isNotEmpty()) {
                        val linkStyle =
                            SpanStyle(color = colorScheme.primary, textDecoration = TextDecoration.Underline)
                        builder.withStyle(linkStyle) {
                            builder.withLink(LinkAnnotation.Url(href)) {
                                node.childNodes().forEach { child -> buildSpannable(child, this, colorScheme) }
                            }
                        }
                    } else {
                        node.childNodes().forEach { child -> buildSpannable(child, builder, colorScheme) }
                    }
                }

                "br" -> builder.append("\n")
                else -> {
                    node.childNodes().forEach { child -> buildSpannable(child, builder, colorScheme) }
                }
            }
        }
    }
}
