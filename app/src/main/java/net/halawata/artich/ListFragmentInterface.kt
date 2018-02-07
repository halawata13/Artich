package net.halawata.artich

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import net.halawata.artich.model.AsyncNetworkTask
import net.halawata.artich.model.list.MediaList

interface ListFragmentInterface {

    val list: MediaList

    var selectedTitle: String

    var selectedUrlString: String

    fun reserve(urlString: String, title: String)

    fun update(urlString: String, title: String, useCache: Boolean = true)

    fun request(urlString: String, title: String, onResponse: ((responseCode: Int?, content: String?) -> Unit)? = null, useCache: Boolean = true) {
        val asyncNetWorkTask = AsyncNetworkTask()
        asyncNetWorkTask.request(urlString, AsyncNetworkTask.Method.GET, null, useCache)

        asyncNetWorkTask.onResponse = onResponse
    }

    fun applyFilter()

    fun reload(useCache: Boolean = true)

    fun launchUrl(activity: Activity, context: Context, urlString: String, colorId: Int) {
        val uri = Uri.parse(urlString) ?: return

        val intent = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, uri.toString())

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val icon = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_share_white_24dp)

        val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setToolbarColor(ContextCompat.getColor(activity, colorId))
                .setStartAnimations(activity, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
                .setExitAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .setCloseButtonIcon(BitmapFactory.decodeResource(activity.resources, R.drawable.ic_arrow_back_white_24dp))
                .setActionButton(icon, "共有", pendingIntent)
                .build()

        customTabsIntent.launchUrl(activity, uri)
    }
}
