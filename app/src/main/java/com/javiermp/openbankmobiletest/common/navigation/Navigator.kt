package com.javiermp.openbankmobiletest.common.navigation

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import androidx.core.app.ActivityOptionsCompat
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.main.MainActivity
import com.javiermp.openbankmobiletest.webview.WebviewActivity

/**
 * Class used to navigate through activities.
 */
object Navigator {

    /**
     * Open the main screen
     *
     * @param activity An [Activity] needed to open the destiny activity.
     */
    fun navigateToMainActivity(@NonNull activity: Activity) {
        val intentToLaunch = MainActivity.getCallingIntent(activity)
        intentToLaunch.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intentToLaunch)
        activity.overridePendingTransition(R.anim.long_fade_in, R.anim.fade_out)
    }

    /**
     * Open the webview screen
     *
     * @param activity An [Activity] needed to open the destiny activity.
     */
    fun navigateToWebviewActivity(@NonNull activity: Activity, @NonNull url: String) {
        val intentToLaunch = WebviewActivity.getCallingIntent(activity, url)
        intentToLaunch.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intentToLaunch)
    }
}