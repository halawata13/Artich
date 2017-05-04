package net.halawata.artich.entity

data class GNewsArticle(
        override val id: Long,
        override val title: String,
        override val url: String,
        override val pubDate: String
): Article
