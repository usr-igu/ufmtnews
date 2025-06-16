package br.com.printk.ufmtnews.news.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UfmtNewsInfoDto(
    @SerialName("category") val category: Category,
    @SerialName("content") val content: String,
    @SerialName("description") val description: String?,
    @SerialName("featured_image") val featuredImage: String,
    @SerialName("featured_image_l") val featuredImageL: String,
    @SerialName("featured_image_m") val featuredImageM: String,
    @SerialName("featured_image_p") val featuredImageP: String,
    // @SerialName("gallery")
    // val gallery: Any?,
    @SerialName("hat") val hat: String?,
    @SerialName("id") val id: Int,
    @SerialName("image_description") val imageDescription: String?,
    @SerialName("language_code") val languageCode: String,
    // @SerialName("photographer")
    // val photographer: Any?,
    @SerialName("publication_date") val publicationDate: String,
    @SerialName("short_text") val shortText: String,
    @SerialName("signature") val signature: Signature,
    @SerialName("slug") val slug: String,
    @SerialName("tags") val tags: String,
    @SerialName("title") val title: String,
) {
    @Serializable
    data class Category(
        @SerialName("color") val color: String,
        @SerialName("id") val id: Int,
        @SerialName("language_code") val languageCode: String,
        @SerialName("name") val name: String,
        @SerialName("slug") val slug: String,
    )

    @Serializable
    data class Signature(
        @SerialName("branch_id") val branchId: Int,
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("role") val role: String,
    )
}
