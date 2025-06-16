package br.com.printk.ufmtnews.news.newslist

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import br.com.printk.ufmtnews.news.remote.UfmtNewsAllDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class Category(
    val id: Int,
) {
    Institucional(6),
    Ciencias(3),
    Comunidade(1),
    Academico(5),
    Cultura(2),
    UfmtCiencia(7),
    ;

    fun getCategoryName(id: Int): String = when (id) {
        //      Todos.id -> "Todos"
        Comunidade.id -> "Comunidade"
        Cultura.id -> "Cultura"
        Ciencias.id -> "Ciências"
        Academico.id -> "Acadêmico"
        Institucional.id -> "Institucional"
        UfmtCiencia.id -> "Ufmt Ciência"
        else -> "Desconhecida"
    }
}

data class UfmtNewsListModel(
    val slug: String,
    val title: String,
    val categoryName: String,
    val categoryColor: Color,
    val publicationDate: String,
    val featuredImageUrl: String,
) {
    companion object {
        private val parseFormatter: DateTimeFormatter? =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        private val formatFormatter: DateTimeFormatter? =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        fun fromRemoteDto(model: UfmtNewsAllDto.NewsEntry): UfmtNewsListModel = UfmtNewsListModel(
            slug = model.slug,
            title = model.title,
            categoryName = model.category.name,
            categoryColor =
            Color(
                model.category.color.toColorInt().toLong(),
            ),
            publicationDate =
            LocalDateTime.parse(
                model.publicationDate,
                parseFormatter,
            )
                .format(formatFormatter),
            featuredImageUrl = model.featuredImageM,
        )
    }
}
