package com.javiermp.openbankmobiletest.common.widget

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundle
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorDialogFragment
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorUtils
import com.javiermp.openbankmobiletest.common.permissions.Permissions
import timber.log.Timber

/**
 * Base [Fragment] class for every fragment in this application.
 */
open class BaseFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // At this point, Kotlin extensions are available
        earlyInitializeViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeState(savedInstanceState)
        initializeViews(savedInstanceState)
        initializeContents(savedInstanceState)
    }

    /**
     * View initialization that does not depend on view models, like setting up a recycler view.
     *
     * Called before [initializeState].
     */
    @CallSuper
    protected open fun earlyInitializeViews() {
    }

    /**
     * Initializes fragment state with [androidx.lifecycle.ViewModel]s and parameters passed through [Bundle].
     *
     * Called before [initializeViews] and after [earlyInitializeViews].
     */
    @CallSuper
    protected open fun initializeState(savedInstanceState: Bundle?) {
    }

    /**
     * View initialization that depends on view models.
     *
     * Called before [initializeContents] and after [initializeState].
     */
    @CallSuper
    protected open fun initializeViews(savedInstanceState: Bundle?) {
    }

    /**
     * Initializes view contents.
     *
     * Called after [initializeViews].
     */
    @CallSuper
    protected open fun initializeContents(savedInstanceState: Bundle?) {
    }

    // region error views
    protected fun showErrorDialog(errorBundle: ErrorBundle) {
        val tag = ErrorDialogFragment.TAG
        context?.let { context ->
            val previousDialogFragment = childFragmentManager.findFragmentByTag(tag) as? DialogFragment

            // Check that error dialog is not already shown after a screen rotation
            val previousDialogFragmentDialog = previousDialogFragment?.dialog
            if (previousDialogFragment != null
                && !previousDialogFragment.isRemoving
                && previousDialogFragmentDialog != null
                && previousDialogFragmentDialog.isShowing
            ) {
                // Error dialog is shown
                Timber.w("Error dialog is already shown")
            } else {
                // Error dialog is not shown
                val errorDialogFragment = ErrorDialogFragment.newInstance(
                    ErrorUtils.buildErrorMessageForDialog(context, errorBundle),
                    true
                )
                if (!childFragmentManager.isDestroyed && !childFragmentManager.isStateSaved) {
                    // Fragment contains the dialog and dialog should be controlled from fragment interface.
                    // See: https://stackoverflow.com/a/8921129/5189200
                    errorDialogFragment.isCancelable = false
                    errorDialogFragment.show(childFragmentManager, tag)
                }
            }
        } ?: Timber.e("Context is null")
    }
    // endregion

    // region permissions
    private var onPermissionGrantedCallback: (() -> Unit)? = null
    private var onPermissionDeniedCallback: (() -> Unit)? = null
    private var onNeverShowAgainCallback: (() -> Unit)? = null
    private var permissionRequested: String = ""
    private var permissionRequestCode: Int = -1
    private var postRequestRationaleResId: Int = -1
    private var shouldShowPostRequestRationale: Boolean = false
    private var postRequestRationaleShown: Boolean = false
    fun checkAndHandlePermission(
        permission: String,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        onNeverShowAgainCallback: () -> Unit,
        @StringRes preRequestRationaleResId: Int = -1,
        shouldShowPreRequestRationale: Boolean = true,
        @StringRes postRequestRationaleResId: Int = -1,
        shouldShowPostRequestRationale: Boolean = false
    ) {
        onPermissionGrantedCallback = onPermissionGranted
        onPermissionDeniedCallback = onPermissionDenied
        permissionRequested = permission
        this.postRequestRationaleResId = postRequestRationaleResId
        this.shouldShowPostRequestRationale = shouldShowPostRequestRationale
        postRequestRationaleShown = false
        this.onNeverShowAgainCallback = onNeverShowAgainCallback
        activity?.let { activity ->
            if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permission) &&
                    shouldShowPreRequestRationale &&
                    preRequestRationaleResId != -1
                ) {
                    AlertDialog.Builder(activity)
                        .setMessage(getString(preRequestRationaleResId))
                        .setPositiveButton(R.string.permissions_rationale_possitive_button_text) { _, _ ->
                            showPermissionRequest(permission)
                        }.setNegativeButton(R.string.permissions_rationale_negative_button_text) { _, _ ->
                            onPermissionDenied()
                        }
                        .show()
                } else {
                    // No explanation needed, we can request the permission.
                    showPermissionRequest(permission)
                }
            } else {
                onPermissionGrantedCallback?.invoke()
            }
        }
    }

    fun hasPermission(permission: String) =
        activity?.let { activity ->
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        } ?: false

    private fun showPermissionRequest(permission: String) {
        Permissions.permissionsCode[permission]?.let { permissionCode ->
            permissionRequestCode = permissionCode
            this@BaseFragment.requestPermissions(
                arrayOf(permission),
                permissionCode
            )
        } ?: Timber.e("Could not request permission because its request code was not defined")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionRequestCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onPermissionGrantedCallback?.invoke()
                } else {
                    activity?.let { it ->
                        val pressedNeverShowAgain = !ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            permissionRequested
                        )
                        if (pressedNeverShowAgain) { //User pressed "Never show again". Fallback time!
                            onNeverShowAgainCallback?.invoke()
                        } else { //Last chance!
                            if (shouldShowPostRequestRationale && postRequestRationaleResId != -1 && !postRequestRationaleShown
                            ) {
                                postRequestRationaleShown = true
                                AlertDialog.Builder(activity)
                                    .setMessage(getString(postRequestRationaleResId))
                                    .setPositiveButton(R.string.permissions_rationale_possitive_button_text) { _, _ ->
                                        showPermissionRequest(permissionRequested)
                                    }.setNegativeButton(R.string.permissions_rationale_negative_button_text) { _, _ ->
                                        onPermissionDeniedCallback?.invoke()
                                    }
                                    .show()
                            }
                        }
                        onPermissionDeniedCallback?.invoke()
                    }
                    return
                }
            }
        }
    }

    // endregion

    // region animations

    //This is basically an ugly fix to this bug: https://issuetracker.google.com/issues/37036000
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim == R.anim.enter_from_right) {
            val nextAnimation = AnimationUtils.loadAnimation(context, nextAnim)
            nextAnimation.setAnimationListener(object : Animation.AnimationListener {
                private var startZ = 0f
                override fun onAnimationStart(animation: Animation) {
                    view?.apply {
                        startZ = ViewCompat.getTranslationZ(this)
                        ViewCompat.setTranslationZ(this, 1f)
                    }
                }

                override fun onAnimationEnd(animation: Animation) {
                    // Short delay required to prevent flicker since other Fragment wasn't removed just yet.
                    view?.apply {
                        this.postDelayed({ ViewCompat.setTranslationZ(this, startZ) }, 350)
                    }
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            return nextAnimation
        } else {
            return null
        }
    }

    // endregion animations
}
