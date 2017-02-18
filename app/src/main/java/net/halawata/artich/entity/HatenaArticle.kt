package net.halawata.artich.entity

data class HatenaArticle(
        override val id: Long,
        override val title: String,
        override val url: String,
        override val pubDate: String
): Article
