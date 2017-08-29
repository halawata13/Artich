package net.halawata.artich.model.list

import net.halawata.artich.model.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

interface MediaList {

    val dateFormat: String

    fun parse(content: String): ArrayList<*>?

    fun formatDate(dateString: String): String? {
        return try {
            val inFormat = SimpleDateFormat(dateFormat, Locale.US)
            val outFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US)

            outFormat.format(inFormat.parse(dateString))

        } catch (ex: ParseException) {
            Log.e(ex.message)
            null
        }
    }
}
