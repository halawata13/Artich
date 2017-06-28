package net.halawata.artich.entity

data class QiitaArticle(
        override val id: Long,
        override val title: String,
        override val url: String,
        override val pubDate: String,
        val user: String
): Article
