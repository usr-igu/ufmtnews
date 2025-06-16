package br.com.printk.ufmtnews.news.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface UfmtAkwardResponse

@Serializable
data class UfmtNewsAllDto(
    @SerialName("current_page") val currentPage: Int,
    @SerialName("data") val `data`: Map<Int, NewsEntry>,
    @SerialName("first_page_url") val firstPageUrl: String,
    @SerialName("from") val from: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("last_page_url") val lastPageUrl: String,
    @SerialName("links") val links: List<Link>,
    @SerialName("next_page_url") val nextPageUrl: String?,
    @SerialName("path") val path: String,
    @SerialName("per_page") val perPage: Int,
    @SerialName("prev_page_url") val prevPageUrl: String?,
    @SerialName("to") val to: Int,
    @SerialName("total") val total: Int,
) : UfmtAkwardResponse {
    @Serializable
    data class NewsEntry(
        @SerialName("category") val category: Category,
        @SerialName("featured_image") val featuredImage: String,
        @SerialName("featured_image_m") val featuredImageM: String,
        @SerialName("featured_image_p") val featuredImageP: String,
        // @SerialName("hat")
        // val hat: Any?,
        @SerialName("id") val id: Int,
        // @SerialName("image_description")
        // val imageDescription: Any?,
        // @SerialName("photographer")
        // val photographer: Any?,
        @SerialName("position") val position: Int,
        @SerialName("publication_date") val publicationDate: String,
        @SerialName("short_text") val shortText: String?,
        @SerialName("slug") val slug: String,
        @SerialName("status") val status: String,
        @SerialName("tags") val tags: String,
        @SerialName("title") val title: String,
    ) {
        @Serializable
        data class Category(
            @SerialName("color") val color: String,
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String,
            @SerialName("slug") val slug: String,
        )
    }

    @Serializable
    data class Link(
        @SerialName("active") val active: Boolean,
        @SerialName("label") val label: String,
        @SerialName("url") val url: String?,
    )
}

@Serializable
data class UfmtNewsAllFirstPageResponse(
    @SerialName("current_page") val currentPage: Int,
    @SerialName("data") val `data`: List<UfmtNewsAllDto.NewsEntry>,
    @SerialName("first_page_url") val firstPageUrl: String,
    @SerialName("from") val from: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("last_page_url") val lastPageUrl: String,
    @SerialName("links") val links: List<UfmtNewsAllDto.Link>,
    @SerialName("next_page_url") val nextPageUrl: String?,
    @SerialName("path") val path: String,
    @SerialName("per_page") val perPage: Int,
    @SerialName("prev_page_url") val prevPageUrl: String?,
    @SerialName("to") val to: Int,
    @SerialName("total") val total: Int,
) : UfmtAkwardResponse
