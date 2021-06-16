package com.javiermp.openbankmobiletest.common.widget.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundle
import com.javiermp.openbankmobiletest.common.extensions.setOnSingleClickListener
import kotlinx.android.synthetic.main.view_error.view.*
import timber.log.Timber

/**
 * Widget used to display an error state to the user
 */
class ErrorView : LinearLayout {

    var errorListener: ErrorListener? = null
    var errorBundle: ErrorBundle? = null
        set(value) {
            field = value
            value?.let { setErrorMessage(context.getString(value.stringId)) }
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_error, this)
        bt_error_view_retry_button.setOnSingleClickListener {
            errorBundle?.let { errorBundle ->
                errorListener?.apply {
                    onRetry(errorBundle)
                } ?: Timber.e("Error listener is null")
            } ?: Timber.e("Error bundle is null")
        }
    }

    fun setErrorMessage(message: String): Boolean {
        var queuedTask = false

        // View are not nulls
        tv_error_view_message?.let {
            // Post a task to update the view
            tv_error_view_message.post {
                // When the task is executed, check that the view is still there
                tv_error_view_message?.let {
                    // Update the message
                    val stringId = errorBundle?.stringId ?: R.string.error_default_message
                    tv_error_view_message_description.text = "${context.getString(R.string.error_support_code)}: ${errorBundle?.appError}\n\n${context.getString(stringId)}"
                }
            }
            queuedTask = true
        } ?: Timber.d("There is no error view for setting message")

        return queuedTask
    }
}