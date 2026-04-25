package app.valorizze.core.functions.browser

import android.content.Intent
import androidx.core.net.toUri
import app.valorizze.core.context.AppContextProvider

fun openUrlInBrowser(url: String) {
    val context = AppContextProvider.get() ?: return
    context.startActivity(
        Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

