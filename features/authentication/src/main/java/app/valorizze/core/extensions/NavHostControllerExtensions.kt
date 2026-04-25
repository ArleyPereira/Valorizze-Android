package app.valorizze.core.extensions

import androidx.navigation.NavHostController

fun NavHostController.popBackStackSafely(): Boolean =
    runCatching { popBackStack() }.getOrDefault(false)

