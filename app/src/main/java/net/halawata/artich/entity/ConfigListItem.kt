package net.halawata.artich.entity

import net.halawata.artich.enum.Media

data class ConfigListItem(
        val id: Long,
        val mediaId: Media,
        val title: String
)
