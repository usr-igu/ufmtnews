package br.com.printk.ufmtnews.news.newsinfo

import br.com.printk.ufmtnews.news.remote.UfmtNewsInfoDto
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class UfmtNewsInfoModel(
    val title: String,
    val shortText: String,
    val content: String,
    val featuredImage: String,
    val authorName: String,
    val publicationDate: String,
    val tags: ImmutableList<String>,
) {
    companion object {
        private val parseFormatter: DateTimeFormatter? =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        private val formatFormatter: DateTimeFormatter? =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

        fun fromRemoteDto(dto: UfmtNewsInfoDto): UfmtNewsInfoModel {
            val tags = dto.tags.split(",").map { it.trim() }.filter { it.isNotBlank() }.toImmutableList()

            val publicationDate =
                LocalDateTime.parse(
                    dto.publicationDate,
                    parseFormatter,
                )
                    .format(formatFormatter)

            return UfmtNewsInfoModel(
                title = dto.title,
                shortText = dto.shortText,
                content = dto.content,
                featuredImage = dto.featuredImageM,
                authorName = dto.signature.name,
                publicationDate = publicationDate,
                tags = tags,
            )
        }
    }
}
